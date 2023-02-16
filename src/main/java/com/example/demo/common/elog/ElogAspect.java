package com.example.demo.common.elog;

import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.IdUtil;
import com.example.demo.common.utils.SqlUtils;
import com.google.gson.GsonBuilder;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.session.SqlSessionFactory;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.UUID;

@Slf4j
@Aspect
@Component
public class ElogAspect {
    private ThreadLocal<String> interfaceUuid=new ThreadLocal<>();
    private ThreadLocal<String> sqlUuid=new ThreadLocal<>();

    @Autowired
    private SqlSessionFactory sqlSessionFactory;

    /** 操作方法名 */
    private static final String SELECT = "select";
    private static final String DELETE = "delete";
    private static final String UPDATE = "update";
    private static final String GET = "get";
    private static final String SEARCH = "search";
    private static final String EXPORT = "export";
    private static final String SAVE = "save";

    private static final String X_FORWARDED_FOR = "x-forwarded-for";
    private static final String SQL_UUID = "sqlUuid";
    /**
     * controller层
     */
    @Pointcut("(execution(* com.example.demo.web.*.controller.*.*(..)))" +
            "&&(@annotation(org.springframework.web.bind.annotation.RequestMapping)" +
            "|| @annotation(org.springframework.web.bind.annotation.PostMapping)) ")
    private  void pointCutController(){
    }

    @Pointcut("execution(* com.example.demo.web.*.mapper.*.*(..)))&&@annotation(com.example.demo.common.elog.Elog)")
    private void pointCutMapper(){

    }

    @Around("pointCutController()")
    public Object doAroundController(ProceedingJoinPoint joinPoint) throws Throwable{
        interfaceUuid.set(IdUtil.simpleUUID());
        MethodSignature joinPointObject = (MethodSignature) joinPoint.getSignature();
        //获取类上上请求路径
        String classUri = AnnotationUtils.findAnnotation(joinPointObject.getMethod().getDeclaringClass(), RequestMapping.class).value()[0];
        //获取方法上请求路径
        String methodUri = "";
        RequestMapping requestMapping = ((MethodSignature) joinPoint.getSignature()).getMethod().getAnnotation(RequestMapping.class);
        if (null == requestMapping) {
            PostMapping postMapping = ((MethodSignature) joinPoint.getSignature()).getMethod().getAnnotation(PostMapping.class);
            methodUri = postMapping.value()[0];
        } else {
            methodUri = requestMapping.value()[0];
        }
        RequestAttributes ra = RequestContextHolder.getRequestAttributes();
        ServletRequestAttributes sra = (ServletRequestAttributes) ra;
        HttpServletRequest request = sra.getRequest();
        String clientIp;
        if (request.getHeader("") == null) {
            clientIp = request.getRemoteAddr();
        } else {
            clientIp = request.getHeader(X_FORWARDED_FOR);
        }
        String browserDetails = request.getHeader("User-Agent");

        log.info("测试controller"+classUri+clientIp+browserDetails);
        Object proceed;
        try{
            proceed = joinPoint.proceed();
        }catch (Exception e)
        {
            log.error(methodUri,e);
            throw e;
        }
        interfaceUuid.remove();
        return proceed;
    }

    //@Around("pointCutMapper()")
    public Object doAroundMapper(ProceedingJoinPoint joinPoint) throws Throwable
    {
        RequestAttributes ra=RequestContextHolder.getRequestAttributes();
        ServletRequestAttributes sra=(ServletRequestAttributes) ra;
        if(sra == null){
            return joinPoint.proceed();
        }
        sqlUuid.set(IdUtil.simpleUUID());
        HttpServletRequest request=sra.getRequest();
        request.setAttribute(SQL_UUID,sqlUuid.get());
        //参数
        Object[] args=joinPoint.getArgs();
        Object params = "";
        if (args.length > 0) {
            params = args[0];
        }
        //方法名
        String methodName = ((MethodSignature) joinPoint.getSignature()).getMethod().getName();
        String curdType = SELECT;
        String resultBeforeUpdate = "非修改删除类SQL";
        if (methodName.startsWith(DELETE)) {
            curdType = DELETE;
            resultBeforeUpdate = request.getAttribute("resultBeforeUpdate") == null ? null : request.getAttribute("resultBeforeUpdate").toString();
        } else if (methodName.startsWith(UPDATE)) {
            curdType = UPDATE;
            resultBeforeUpdate = request.getAttribute("resultBeforeUpdate") == null ? null : request.getAttribute("resultBeforeUpdate").toString();
        } else if (methodName.startsWith(SELECT) || methodName.startsWith(EXPORT)
                || methodName.startsWith(GET)
                || methodName.startsWith(SEARCH)) {
            curdType = SELECT;
        } else if (methodName.startsWith(SAVE)) {
            curdType = SAVE;
        }

        String sql = SqlUtils.getMybatisSql(joinPoint, sqlSessionFactory);
        logSqlExecute(sqlUuid.get(), sql);
        Object proceed = joinPoint.proceed();
        if (SELECT.equals(curdType)) {
            request.setAttribute("resultBeforeUpdate", new GsonBuilder().create().toJson(proceed));
        }
        logSqlSlog(sqlUuid.get(), interfaceUuid.get() == null ?
                        UUID.randomUUID().toString().replace("-", "") : interfaceUuid.get(),
                curdType, "admin", "demo", params, proceed, resultBeforeUpdate);
        return proceed;

    }

    public void logSqlExecute(String sqlUuid, String sql) {
        HashMap<String, Object> logMap = new HashMap<>(7);
        logMap.put("logType", "3");
        long nowTime = System.currentTimeMillis();
        logMap.put("dateTime", DateUtil.now());
        logMap.put("timestamp", nowTime);
        logMap.put("sqlUUID", sqlUuid);
        logMap.put("sqlExecute", sql);
        log.info(new GsonBuilder().disableHtmlEscaping().create().toJson(logMap));
    }

    public void logSqlSlog(String onlySqlUuid, String onlyInterfaceUuid, String curdType, String personId, String systemType,
                           Object params, Object result, String resultBeforeUpdate) {
        HashMap<String, Object> logMap = new HashMap<>(15);
        logMap.put("logType", "2");
        long nowTime = System.currentTimeMillis();
        logMap.put("dateTime", DateUtil.now());
        logMap.put("timestamp", nowTime);
        logMap.put("sqlUUID", onlySqlUuid);
        logMap.put("interfaceUUID", onlyInterfaceUuid);
        logMap.put("curdType", curdType);
        logMap.put("personId", personId);
        logMap.put("systemType", systemType);
        logMap.put("params", new GsonBuilder().create().toJson(params));
        logMap.put("result", new GsonBuilder().create().toJson(result));
        logMap.put("resultBeforeUpdate", resultBeforeUpdate);
        log.info(new GsonBuilder().disableHtmlEscaping().create().toJson(logMap));
    }
}
