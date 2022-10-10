package com.heyunetwork.demo.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.IdWorker;
import com.heyunetwork.demo.entity.TrainingRecord;
import com.heyunetwork.demo.entity.TrainingRecordParticipant;
import com.heyunetwork.demo.entity.vo.StaffInfoVo;
import com.heyunetwork.demo.entity.vo.TrainingRecordVo;
import com.heyunetwork.demo.mapper.TrainingRecordMapper;
import com.heyunetwork.demo.service.TrainingRecordParticipantService;
import com.heyunetwork.demo.service.TrainingRecordService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.util.*;
import java.util.stream.Collectors;

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

    // region 《=== service ===》

    @Autowired
    private TrainingRecordParticipantService trainingRecordParticipantService;

    // endregion


    // region 《=== 查询 ===》

    /**
     * 根据 ID 查询单条培训记录及其参与人员
     *
     * @param id
     * @return
     */
    @Override
    public TrainingRecordVo trainingRecordQueryById(String id) {
        return baseMapper.trainingRecordQueryById(id);
    }

    /**
     * 复杂查询，查询培训记录及其参与人员
     *
     * @param current
     * @param size
     * @param field
     * @param keyword
     * @param isAsc
     * @return
     */
    @Override
    public Map<String, Object> trainingRecordComplexQuery(long current, long size, String field, String keyword, Boolean isAsc) {

        // 1.执行复杂查询
        List<TrainingRecordVo> result = baseMapper.trainingRecordComplexQuery(field, keyword, isAsc, size, size * (current - 1));

        // region <- 获取符合查询条件的记录数 ->
        // endregion
        QueryWrapper qw = new QueryWrapper<TrainingRecord>();
        qw.like(field, keyword);
        Long total = baseMapper.selectCount(qw);

        // region <- 3.封装查询结果并返回 ->
        Map<String, Object> map = new HashMap<>();

        map.put("items", result);
        map.put("current", current);
        map.put("size", size);
        map.put("total", total);
        return map;
        // endregion
    }

    // endregion


    // region 《=== 新增 ===》

    /**
     * 新增一条培训记录
     *
     * @param trainingRecordVo
     */
    @Override
    @Transactional
    public void addTrainingRecord(TrainingRecordVo trainingRecordVo) {

        // region <- 1.插入培训记录数据 ->

        // 1-1.封装数据
        TrainingRecord trainingRecord = new TrainingRecord();
        BeanUtils.copyProperties(trainingRecordVo, trainingRecord);

        // 1-2.手动生成培训记录 id
        trainingRecord.setId(IdWorker.get32UUID());

        // 1-3.向 training_record 表中插入数据
        baseMapper.insert(trainingRecord);
        // endregion


        // region <- 2.插入参与人员数据 ->

        // 2-1.判断插入人员数据是否为空，为空直接返回
        List<StaffInfoVo> trpRow = trainingRecordVo.getList();
        if (CollectionUtils.isEmpty(trpRow)) return;

        // 2-2.封装数据
        // TODO 去重实现需要改进
        List<TrainingRecordParticipant> trpList = trpRow.stream().map(StaffInfoVo::getId).collect(Collectors.toSet())
                .stream().map(item -> {
                    TrainingRecordParticipant trp = new TrainingRecordParticipant();
                    trp.setTrainingParticipant(item);
                    trp.setTrainingRecord(trainingRecord.getId());
                    return trp;
                }).collect(Collectors.toList());

        // 2-3.进行插入
        trainingRecordParticipantService.saveBatch(trpList);
        // endregion
    }

    // endregion


    // region 《=== 修改 ===》

    /**
     * 修改培训记录及参与人员
     * @param trainingRecordVo
     */
    @Override
    @Transactional
    public void updateTrainingRecord(TrainingRecordVo trainingRecordVo) {

        // region <- 1.培训记录数据修改 ->
        // 1-1.updateTrainingRecord 数据重新封装
        TrainingRecord trainingRecord = new TrainingRecord();
        BeanUtils.copyProperties(trainingRecordVo, trainingRecord);
        // 1-2.更新
        baseMapper.updateById(trainingRecord);
        // endregion

        // region <- 2.参与人员数据修改 ->

        // 2-1.封装当前的参与人员数据
        Set<String> trpNewSet = trainingRecordVo.getList().stream().map(StaffInfoVo::getId).collect(Collectors.toSet());

        // 2-2.查询修改前的参与人员数据
        QueryWrapper qw = new QueryWrapper<TrainingRecordParticipant>();
        qw.eq("training_record",trainingRecord.getId());
        qw.select("training_participant");
        List<TrainingRecordParticipant> trpOld = trainingRecordParticipantService.list(qw);
        Set<String> trpOldSet= trpOld.stream().map(TrainingRecordParticipant::getTrainingParticipant).collect(Collectors.toSet());

        // 2-3.新增人员，修改该前新增部分
        HashSet<String> addSet = new HashSet<>();
        addSet.addAll(trpNewSet);
        addSet.removeAll(trpOldSet);
        if (!CollectionUtils.isEmpty(addSet)){
            List<TrainingRecordParticipant> addTRP = addSet.stream().map(item -> {
                TrainingRecordParticipant trp = new TrainingRecordParticipant();
                trp.setTrainingParticipant(item);
                trp.setTrainingRecord(trainingRecord.getId());
                return trp;}).collect(Collectors.toList());
            trainingRecordParticipantService.saveBatch(addTRP);
        }

        // 2-4.删除人员
        HashSet<String> deleteSet = new HashSet<>();
        deleteSet.addAll(trpOldSet);
        deleteSet.removeAll(trpNewSet);
        if (!CollectionUtils.isEmpty(deleteSet)){
            QueryWrapper qwRemove =  new QueryWrapper<TrainingRecordParticipant>();
            qwRemove.eq("training_record",trainingRecordVo.getId());
            qwRemove.in("training_participant",deleteSet);
            trainingRecordParticipantService.remove(qwRemove);
        }
        // endregion

    }

    // endregion


    // region 《=== 删除 ===》

    /**
     * 删除一条培训记录及所有参与人员
     *
     * @param id
     */
    @Override
    @Transactional
    public void deleteTrainingRecordById(String id) {
        baseMapper.deleteById(id);
        QueryWrapper qw = new QueryWrapper<TrainingRecordParticipant>();
        qw.eq("training_record", id);
        trainingRecordParticipantService.remove(qw);
    }

    // endregion


    // region 《=== 其他 ===》

    // endregion
}
