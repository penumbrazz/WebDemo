package com.example.demo.web.admin.user.model;

import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

/**
 * <p>
 * 用户信息表
 * </p>
 *
 * @author zrk
 * @since 2023-02-07
 */
@Getter
@Setter
@TableName("SYS_USER")
@ApiModel(value = "SysUser对象", description = "用户信息表")
public class SysUser implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty("ID")
    private BigDecimal fUserid;

    @ApiModelProperty("员工编号")
    private String vcUserno;

    @ApiModelProperty("登陆账户")
    private String vcAccount;

    @ApiModelProperty("登陆密码")
    private String vcPassword;

    @ApiModelProperty("姓名")
    private String vcUsername;

    @ApiModelProperty("昵称")
    private String vcNickname;

    @ApiModelProperty("性别")
    private String vcSex;

    @ApiModelProperty("年龄")
    private BigDecimal fAge;

    @ApiModelProperty("手机")
    private String vcCellphone;

    @ApiModelProperty("座机")
    private String vcTelephone;

    @ApiModelProperty("邮箱")
    private String vcEmail;

    @ApiModelProperty("传真")
    private String vcFax;

    @ApiModelProperty("工作状况 0：在职（默认）1：离职")
    private String vcCondition;

    @ApiModelProperty("状态 0：可用（默认）1：禁用")
    private String vcStatus;

    @ApiModelProperty("机构ID")
    private BigDecimal fCompanyid;

    @ApiModelProperty("创建日期")
    private Date dCreatedate;

    @ApiModelProperty("更新时间")
    private Date dUpdatetime;

    @ApiModelProperty("备注")
    private String vcRemark;

    @ApiModelProperty("修改人")
    private String vcUpdatauser;

    @ApiModelProperty("权限-按组合类型")
    private String vcFundType;


}
