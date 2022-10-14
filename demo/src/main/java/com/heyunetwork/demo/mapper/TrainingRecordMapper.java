package com.heyunetwork.demo.mapper;

import com.heyunetwork.demo.entity.TrainingRecord;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.heyunetwork.demo.entity.vo.TrainingRecordVo;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * <p>
 * 培训记录表 Mapper 接口
 * </p>
 *
 * @author 汪阳东
 * @since 2022-10-09
 */
@Mapper
public interface TrainingRecordMapper extends BaseMapper<TrainingRecord> {
    TrainingRecordVo trainingRecordQueryById(String id);

    List<TrainingRecordVo> trainingRecordComplexQuery(@Param("field") String field,
                                                      @Param("keyword") String keyword,
                                                      @Param("sortBy")String sortBy,
                                                      @Param("isAsc") Boolean isAsc,
                                                      @Param("limit") Long limit,
                                                      @Param("offset") Long offset);
}
