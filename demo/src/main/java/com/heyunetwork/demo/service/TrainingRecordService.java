package com.heyunetwork.demo.service;

import com.heyunetwork.demo.entity.TrainingRecord;
import com.baomidou.mybatisplus.extension.service.IService;
import com.heyunetwork.demo.entity.vo.TrainingRecordVo;

/**
 * <p>
 * 培训记录表 服务类
 * </p>
 *
 * @author 汪阳东
 * @since 2022-10-09
 */
public interface TrainingRecordService extends IService<TrainingRecord> {

    TrainingRecordVo getTrainingRecordAndParticipantById(String id);
}
