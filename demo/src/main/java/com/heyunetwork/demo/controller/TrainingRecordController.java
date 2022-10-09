package com.heyunetwork.demo.controller;


import com.heyunetwork.demo.entity.TrainingRecord;
import com.heyunetwork.demo.entity.vo.TrainingRecordVo;
import com.heyunetwork.demo.service.TrainingRecordService;
import com.heyunetwork.demo.util.ResponseResult.R;
import com.heyunetwork.demo.util.exceptionhandler.MyException;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import static com.sun.xml.internal.fastinfoset.stax.events.Util.isEmptyString;

/**
 * <p>
 * 培训记录表 前端控制器
 * </p>
 *
 * @author 汪阳东
 * @since 2022-10-09
 */
@RestController
@RequestMapping("/demo/training-record")
public class TrainingRecordController {

    // region 《=== service ===》

    @Autowired
    private TrainingRecordService trainingRecordService;

    // endregion

    // region 《=== 查询 ===》

    @ApiOperation("根据 ID 查询单条培训记录及其参与人员")
    @GetMapping
    public R query(@RequestParam("id") String id) {

        TrainingRecordVo trainingRecordVo = trainingRecordService.getTrainingRecordAndParticipantById(id);
        return R.ok().data("data", trainingRecordVo);
    }

    // endregion

    // region 《=== 新增 ===》

    @ApiOperation(value = "新增一条培训记录",notes = "1.所有数据都不能为空。")
    @PostMapping
    public R add(@RequestBody TrainingRecordVo trainingRecordVo) {

        // region <- 数据校验 ->
        if (trainingRecordVo.getTrainingDate() == null ||
                isEmptyString(trainingRecordVo.getTrainingContent()) ||
                isEmptyString(trainingRecordVo.getTrainingTeacher())
        ) {
            throw new MyException(20001,"数据不能为空");
        }
        // 新增时不能 VO 中不能有 id
        trainingRecordVo.setId(null);
        // endregion

        // region <- 数据重新封装 ->
        TrainingRecord trainingRecord = new TrainingRecord();
        BeanUtils.copyProperties(trainingRecordVo, trainingRecord);
        // endregion

        trainingRecordService.save(trainingRecord);

        return R.ok();
    }

    // endregion

    // region 《=== 修改 ===》

    @ApiOperation(value = "修改一条培训记录")
    @PutMapping
    public R update(@RequestBody TrainingRecordVo trainingRecordVo) {

        // region <- 数据重新封装 ->
        TrainingRecord trainingRecord = new TrainingRecord();
        BeanUtils.copyProperties(trainingRecordVo, trainingRecord);
        // endregion

        trainingRecordService.updateById(trainingRecord);
        return R.ok();
    }

    // endregion

    // region 《=== 删除 ===》

    @ApiOperation(value = "删除一条培训记录")
    @DeleteMapping
    public R delete(@RequestParam("id") String id) {

        trainingRecordService.removeById(id);

        return R.ok();
    }


    // endregion

    // region 《=== 其他 ===》
    // endregion

}
