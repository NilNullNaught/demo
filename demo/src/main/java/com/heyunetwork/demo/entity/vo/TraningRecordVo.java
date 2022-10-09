package com.heyunetwork.demo.entity.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class TraningRecordVo {

    @ApiModelProperty("培训日期")
    private LocalDateTime trainingDate;

    @ApiModelProperty("培训老师名字")
    private String trainingTeacher;

    @ApiModelProperty("培训内容")
    private String trainingContent;

}
