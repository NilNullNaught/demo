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
 * 培训记录表
 * </p>
 *
 * @author 汪阳东
 * @since 2022-10-09
 */
@Getter
@Setter
@TableName("training_record")
@ApiModel(value = "TrainingRecord对象", description = "培训记录表")
public class TrainingRecord implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty("培训记录 id")
    @TableId(type = IdType.ASSIGN_UUID)
    private String id;

    @ApiModelProperty("培训日期")
    private LocalDateTime trainingDate;

    @ApiModelProperty("培训老师名字")
    private String trainingTeacher;

    @ApiModelProperty("培训内容")
    private String trainingContent;

    @ApiModelProperty("逻辑删除 1（true）已删除， 0（false）未删除")
    private Boolean isDeleted;

    @ApiModelProperty("创建时间")
    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime gmtCreate;

    @ApiModelProperty("更新时间")
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime gmtModified;


}
