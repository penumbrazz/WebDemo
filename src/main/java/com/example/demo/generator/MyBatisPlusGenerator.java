package com.example.demo.generator;

import cn.hutool.core.util.StrUtil;
import cn.hutool.setting.dialect.Props;
import com.baomidou.mybatisplus.core.exceptions.MybatisPlusException;
import com.baomidou.mybatisplus.generator.AutoGenerator;
import com.baomidou.mybatisplus.generator.config.*;
import com.baomidou.mybatisplus.generator.config.po.LikeTable;
import com.baomidou.mybatisplus.generator.config.querys.MySqlQuery;
import com.baomidou.mybatisplus.generator.config.querys.OracleQuery;
import com.baomidou.mybatisplus.generator.config.rules.DateType;
import com.baomidou.mybatisplus.generator.config.rules.NamingStrategy;
import com.baomidou.mybatisplus.generator.engine.VelocityTemplateEngine;

import java.util.Collections;
import java.util.Scanner;

/**
 *
 */
public class MyBatisPlusGenerator {

    public static  void main(String[] args){
        String projectPath=System.getProperty("user.dir");
        String moduleName=scanner("模块名");
        String[] tableNames=scanner("表名，多个英文逗号分割").split(",");
        AutoGenerator autoGenerator=new AutoGenerator(initDataSourceConfig());
        autoGenerator.global(initGlobalConfig(projectPath));
        autoGenerator.packageInfo(initPackageConfig(projectPath,moduleName));
        autoGenerator.injection(initInjectionConfig(projectPath,moduleName));
        autoGenerator.template(initTemplateConfig());
        autoGenerator.strategy(initStrategyConfig(tableNames));
        autoGenerator.execute(new VelocityTemplateEngine());

    }

    /**
     * 读取控制台内容信息
     * @param tip
     * @return
     */
    private static String scanner(String tip){
        Scanner scanner=new Scanner(System.in);
        System.out.println(("请输入")+tip+":");
        if(scanner.hasNext()){
            String next=scanner.next();
            if(StrUtil.isNotEmpty(next))
            {
                return next;
            }
        }
        throw new MybatisPlusException("请输入正确的"+tip+"!");
    }

    /**
     * 初始化全局配置
     * @param projectPath
     * @return
     */
    private static GlobalConfig initGlobalConfig(String projectPath){
        return new GlobalConfig.Builder()
                .outputDir(projectPath+"/src/main/java")
                .author("zrk")
                .disableOpenDir()
                .enableSwagger()
                .fileOverride()
                .dateType(DateType.ONLY_DATE)
                .build();
    }

    /**
     * 初始化数据库连接
     * @return
     */
    private static DataSourceConfig initDataSourceConfig(){
        Props props=new Props("generator.properties");
        String url=props.getStr("dataSource.url");
        String username=props.getStr("dataSource.username");
        String password= props.getStr("dataSource.password");
        return new DataSourceConfig.Builder(url,username,password)
                .dbQuery(new OracleQuery())//oracle数据库
                .build();
    }

    /**
     * 初始化包配置
     * @param projectPath
     * @param moduleName
     * @return
     */
    private static PackageConfig initPackageConfig(String projectPath,String moduleName){
        Props props=new Props("generator.properties");
        return new PackageConfig.Builder()
                .moduleName(moduleName)
                .parent(props.getStr("package.base"))
                .entity("model")
                .pathInfo(Collections.singletonMap(OutputFile.mapperXml,projectPath+"/src/main/resources/mapper/"+moduleName))
                .build();
    }

    /**
     * 初始化模板配置
     * @return
     */
    private static TemplateConfig initTemplateConfig(){
        return new TemplateConfig.Builder().build();
    }

    /**
     * 初始化策略配置
     * @param tableNames
     * @return
     */
    private static StrategyConfig initStrategyConfig(String[] tableNames){
        StrategyConfig.Builder builder=new StrategyConfig.Builder();
        builder.entityBuilder()
                .naming(NamingStrategy.underline_to_camel)
                .columnNaming(NamingStrategy.underline_to_camel)
                .enableLombok()
                .formatFileName("%s")
                .mapperBuilder()
                .enableBaseResultMap()
                .formatMapperFileName("%sMapper")
                .formatXmlFileName("%sMapper")
                .serviceBuilder()
                .formatServiceFileName("%sService")
                .formatServiceImplFileName("%sServiceImpl")
                .controllerBuilder()
                .enableRestStyle()
                .formatFileName("%sController");
        if(tableNames.length==1&&tableNames[0].contains("*")){
            String[] likeStr=tableNames[0].split("_");
            String likePrefix=likeStr[0]+"_";
            builder.likeTable(new LikeTable(likePrefix));
        }else{
            builder.addInclude(tableNames);
        }
        return builder.build();
    }

    /**
     * 初始化自定义配置
     * @param projectPath
     * @param moudleName
     * @return
     */
    private static InjectionConfig initInjectionConfig(String projectPath,String moudleName)
    {
        return new InjectionConfig.Builder().build();

    }


}
