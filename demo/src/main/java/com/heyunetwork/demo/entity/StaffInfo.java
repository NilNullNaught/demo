package com.heyunetwork.demo.entity;

import com.baomidou.mybatisplus.annotation.*;

import java.io.Serializable;
import java.time.LocalDateTime;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

/**
 * <p>
 * 员工资料表
 * </p>
 *
 * @author 汪阳东
 * @since 2022-10-09
 */
@Getter
@Setter
@TableName("staff_info")
@ApiModel(value = "StaffInfo对象", description = "员工资料表")
public class StaffInfo implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty("员工资料 id")
    @TableId(type = IdType.ASSIGN_UUID)
    private String id;

    @ApiModelProperty("员工姓名")
    private String name;

    @ApiModelProperty("性别 0 男，1 女")
    private Integer sex;

    @ApiModelProperty("身份证号")
    private String idcardNumber;

    @ApiModelProperty("部门：1-业务部；2-采购部；3-行政部")
    private Integer department;

    @ApiModelProperty("学历：0-未设置；1-初中；2-高中；3-大专、4-本科")
    private Integer formalSchooling;

    @ApiModelProperty("邮箱地址")
    private String email;

    @ApiModelProperty("逻辑删除 1（true）已删除， 0（false）未删除")
    private Boolean isDeleted;

    @ApiModelProperty("创建时间")
    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime gmtCreate;

    @ApiModelProperty("更新时间")
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime gmtModified;


}
