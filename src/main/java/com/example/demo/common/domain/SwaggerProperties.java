package com.example.demo.common.domain;

import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = false)
@Builder
public class SwaggerProperties {
    /**
     * API文档生成基础路径
     */
    private String apiBasePackage;
    /**
     * 是否启用登录认证
     */
    private boolean enableSecurity;
    /**
     *标题
     */
    private String title;
    /**
     *描述
     */
    private String description;
    /**
     *版本
     */
    private String version;
    /**
     *联系人
     */
    private String contactName;
    /**
     *网址
     */
    private String contactUrl;
    /**
     *邮箱
     */
    private String contactEmail;

}
