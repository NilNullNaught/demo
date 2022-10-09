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
 * 培训参与人员表
 * </p>
 *
 * @author 汪阳东
 * @since 2022-10-09
 */
@Getter
@Setter
@TableName("training_record_participant")
@ApiModel(value = "TrainingRecordParticipant对象", description = "培训参与人员表")
public class TrainingRecordParticipant implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty("id")
    @TableId(type = IdType.ASSIGN_UUID)
    private String id;

    @ApiModelProperty("培训记录 id")
    private String trainingRecord;

    @ApiModelProperty("参与人员 id")
    private String trainingParticipant;

    @ApiModelProperty("逻辑删除 1（true）已删除， 0（false）未删除")
    private Boolean isDeleted;

    @ApiModelProperty("创建时间")
    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime gmtCreate;

    @ApiModelProperty("更新时间")
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime gmtModified;


}
