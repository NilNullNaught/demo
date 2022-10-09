package com.heyunetwork.demo.service.impl;

import com.heyunetwork.demo.entity.TrainingRecord;
import com.heyunetwork.demo.entity.vo.TrainingRecordVo;
import com.heyunetwork.demo.mapper.TrainingRecordMapper;
import com.heyunetwork.demo.service.TrainingRecordService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 培训记录表 服务实现类
 * </p>
 *
 * @author 汪阳东
 * @since 2022-10-09
 */
@Service
public class TrainingRecordServiceImpl extends ServiceImpl<TrainingRecordMapper, TrainingRecord> implements TrainingRecordService {

    // region 《=== {{service、mapper、client、template}} ===》

    // endregion

    // region 《=== 查询 ===》

    /** 根据 ID 查询单条培训记录及其参与人员
     * @param id
     * @return
     */
    @Override
    public TrainingRecordVo getTrainingRecordAndParticipantById(String id) {
        return baseMapper.queryTrainingRecord(id);
    }

    // endregion

    // region 《=== 新增 ===》

    // endregion

    // region 《=== 修改 ===》

    // endregion

    // region 《=== 删除 ===》

    // endregion

    // region 《=== 其他 ===》

    // endregion
}
