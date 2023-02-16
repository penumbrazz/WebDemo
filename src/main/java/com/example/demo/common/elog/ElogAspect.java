package com.example.demo.common.elog;

import cn.hutool.core.util.IdUtil;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Slf4j
@Aspect
@Component
public class ElogAspect {
    private ThreadLocal<String> interfaceUuid=new ThreadLocal<>();
    private ThreadLocal<String> sqlUuid=new ThreadLocal<>();

    /**
     * controller层
     */
    @Pointcut("(execution(* com.example.demo.web.*.controller.*.*(..)))" +
            "&&(@annotation(org.springframework.web.bind.annotation.RequestMapping)" +
            "|| @annotation(org.springframework.web.bind.annotation.PostMapping)) ")
    private  void pointCutController(){
    }

    @Pointcut("execution(* com.example.demo.web.*.mapper.*.*(..)))&&@@annotation(com.example.demo.common.elog.Elog)")
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

        log.info("测试controller"+classUri);
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
}
