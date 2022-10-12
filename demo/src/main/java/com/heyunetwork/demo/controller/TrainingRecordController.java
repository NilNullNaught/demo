package com.heyunetwork.demo.controller;


import com.heyunetwork.demo.entity.TrainingRecord;
import com.heyunetwork.demo.entity.vo.TrainingRecordVo;
import com.heyunetwork.demo.service.TrainingRecordService;
import com.heyunetwork.demo.util.ResponseResult.R;
import com.heyunetwork.demo.util.constant.StaffConstant;
import com.heyunetwork.demo.util.exceptionhandler.MyException;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

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
@CrossOrigin
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
        TrainingRecordVo trainingRecordVo = trainingRecordService.trainingRecordQueryById(id);
        return R.ok().data("data", trainingRecordVo);
    }

    @ApiOperation(value = "复杂查询，查询培训记录及其参与人员",
            notes = "1.模糊查询；<br>" +
                    "2.分页查询，可以指定页码（current）、页长（size）；<br>" +
                    "3.条件查询，查询条件（field）可以为培训老师（training_teacher）、培训内容（training_content）、培训日期（training_date）<br>" +
                    "4.根据查询条件（field）正序或逆序排序")
    @GetMapping("trainingRecordComplexQuery")
    public R trainingRecordComplexQuery(@RequestParam("current") long current,
                                        @RequestParam("size") long size,
                                        @RequestParam(value = "field", required = false) String field,
                                        @RequestParam(value = "keyword", required = false) String keyword,
                                        @RequestParam(value = "sortAsc", required = false, defaultValue = "true") Boolean isAsc) {

        // region <- 数据校验 ->
        // 是否进行条件查询
        if (isEmptyString(keyword)) {
            keyword = null;
        }
        // 校验 field 是否可用
        if (!StaffConstant.trainingRecordFields.contains(field)) {
            throw new MyException(20001, "不符合规范的查询条件");
        }
        // endregion

        Map<String, Object> map = trainingRecordService.trainingRecordComplexQuery(current, size, field, keyword, isAsc);
        return R.ok().data(map);
    }

    // endregion

    // region 《=== 新增 ===》

    @ApiOperation(value = "新增一条培训记录", notes = "1.所有数据都不能为空。")
    @PostMapping
    public R add(@RequestBody TrainingRecordVo trainingRecordVo) {

        // region <- 数据校验 ->
        if (trainingRecordVo.getTrainingDate() == null ||
                isEmptyString(trainingRecordVo.getTrainingContent()) ||
                isEmptyString(trainingRecordVo.getTrainingTeacher())
        ) {
            throw new MyException(20001, "数据不能为空");
        }
        // 新增时不能 VO 中不能有 id
        trainingRecordVo.setId(null);
        // endregion

        trainingRecordService.addTrainingRecord(trainingRecordVo);
        return R.ok();
    }

    // endregion

    // region 《=== 修改 ===》

    @ApiOperation(value = "修改培训记录及参与人员")
    @PutMapping
    public R update(@RequestBody TrainingRecordVo trainingRecordVo) {
        trainingRecordService.updateTrainingRecord(trainingRecordVo);
        return R.ok();
    }

    // endregion

    // region 《=== 删除 ===》

    @ApiOperation(value = "删除一条培训记录及所有参与人员")
    @DeleteMapping
    public R delete(@RequestParam("id") String id) {

        trainingRecordService.deleteTrainingRecordById(id);

        return R.ok();
    }

    // endregion

    // region 《=== 其他 ===》
    // endregion

}
