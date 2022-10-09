package com.heyunetwork.demo.entity.vo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class StaffInfoVo {

    @ApiModelProperty("员工资料 id")
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
}
