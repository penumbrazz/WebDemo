package com.example.demo.web.system.model;

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
 * 部门信息表
 * </p>
 *
 * @author zrk
 * @since 2023-02-07
 */
@Getter
@Setter
@TableName("SYS_DEPT")
@ApiModel(value = "SysDept对象", description = "部门信息表")
public class SysDept implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty("部门ID")
    private BigDecimal fDeptid;

    @ApiModelProperty("父级ID，0为根节点")
    private BigDecimal fDeptpid;

    @ApiModelProperty("部门名称")
    private String vcDeptname;

    @ApiModelProperty("创建日期")
    private Date dCreatedate;

    @ApiModelProperty("部门编号")
    private String vcDeptCode;

    @ApiModelProperty("备注")
    private String vcMemo;

    @ApiModelProperty("排序")
    private BigDecimal fSort;

    @ApiModelProperty("状态 1有效      0无效")
    private String vcStatus;

    @ApiModelProperty("更新人")
    private String vcUpdatauser;

    @ApiModelProperty("更新时间")
    private Date dUpdatetime;


}
