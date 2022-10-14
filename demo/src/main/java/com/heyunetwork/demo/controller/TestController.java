package com.heyunetwork.demo.controller;

import com.heyunetwork.demo.entity.StaffInfo;
import com.heyunetwork.demo.entity.TrainingRecord;
import com.heyunetwork.demo.entity.TrainingRecordParticipant;
import com.heyunetwork.demo.mapper.TrainingRecordParticipantMapper;
import com.heyunetwork.demo.service.StaffService;
import com.heyunetwork.demo.service.TrainingRecordService;
import com.heyunetwork.demo.util.ResponseResult.R;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

@RestController
@RequestMapping("test")
public class TestController {

    @Autowired
    private StaffService staffService;

    @Autowired
    private TrainingRecordService trainingRecordService;

    @Autowired
    private TrainingRecordParticipantMapper trainingRecordParticipantMapper;

    @GetMapping("helloworld")
    public String helloworld() {
        return "helloworld" + LocalDateTime.now();
    }

    @PostMapping("trainingRecordParticipantData")
    public R generateTrainingRecordParticipantData() {

        List<StaffInfo> staffInfos = staffService.list();
        List<TrainingRecord> trainingRecords = trainingRecordService.list();

        Random rd=new Random();

        for (TrainingRecord tr : trainingRecords
        ) {
            for (StaffInfo si : staffInfos
            ) {
                if (rd.nextInt(100)+1 > 50){
                    continue;
                }
                if (tr.getTrainingTeacher() == si.getName()){
                    continue;
                }
                TrainingRecordParticipant trp = new TrainingRecordParticipant();
                trp.setTrainingRecord(tr.getId());
                trp.setTrainingParticipant(si.getId());
                trainingRecordParticipantMapper.insert(trp);
            }
        }

        return R.ok();
    }



}
