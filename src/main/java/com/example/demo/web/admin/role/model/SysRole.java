package com.example.demo.web.admin.role.model;

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
 * 系统角色信息表
 * </p>
 *
 * @author zrk
 * @since 2023-02-15
 */
@Getter
@Setter
@TableName("SYS_ROLE")
@ApiModel(value = "SysRole对象", description = "系统角色信息表")
public class SysRole implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty("角色ID")
    private BigDecimal fId;

    @ApiModelProperty("角色编码，系统管理员、层级管理员等指定编码")
    private String vcRoleCode;

    @ApiModelProperty("角色名称")
    private String vcRoleName;

    @ApiModelProperty("所属部门")
    private BigDecimal fDeptid;

    @ApiModelProperty("状态  1有效    0无效")
    private String vcStatus;

    @ApiModelProperty("备注")
    private String vcMemo;

    @ApiModelProperty("排序")
    private BigDecimal fSort;

    @ApiModelProperty("更新时间")
    private Date dUpdatetime;

    @ApiModelProperty("更新人")
    private String vcUpdatauser;


}
