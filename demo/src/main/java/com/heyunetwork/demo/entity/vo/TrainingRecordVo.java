package com.heyunetwork.demo.entity.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
public class TrainingRecordVo {

    @ApiModelProperty("培训记录 id")
    private String id;

    @ApiModelProperty("培训日期")
    private LocalDateTime trainingDate;

    @ApiModelProperty("培训老师名字")
    private String trainingTeacher;

    @ApiModelProperty("培训内容")
    private String trainingContent;

    @ApiModelProperty("培训参与人员")
    private List<StaffInfoVo> list;
}
