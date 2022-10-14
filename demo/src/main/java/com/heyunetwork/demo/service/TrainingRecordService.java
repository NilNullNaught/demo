package com.heyunetwork.demo.service;

import com.heyunetwork.demo.entity.TrainingRecord;
import com.baomidou.mybatisplus.extension.service.IService;
import com.heyunetwork.demo.entity.vo.TrainingRecordVo;

import java.util.Map;

/**
 * <p>
 * 培训记录表 服务类
 * </p>
 *
 * @author 汪阳东
 * @since 2022-10-09
 */
public interface TrainingRecordService extends IService<TrainingRecord> {

    // region 《=== 查询 ===》

    TrainingRecordVo trainingRecordQueryById(String id);

    Map<String, Object> trainingRecordComplexQuery(long current, long size, String field, String keyword,String sortBy, Boolean isAsc);

    // endregion

    // region 《=== 新增 ===》

    void addTrainingRecord(TrainingRecordVo trainingRecordVo);


    // endregion

    // region 《=== 修改 ===》

    // endregion

    // region 《=== 删除 ===》

    void deleteTrainingRecordById(String id);

    void updateTrainingRecord(TrainingRecordVo trainingRecordVo);

    // endregion
}
