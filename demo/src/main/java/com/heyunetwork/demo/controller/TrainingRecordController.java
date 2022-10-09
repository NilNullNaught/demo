package com.heyunetwork.demo.controller;


import com.heyunetwork.demo.entity.TrainingRecord;
import com.heyunetwork.demo.service.TrainingRecordService;
import com.heyunetwork.demo.util.ResponseResult.R;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

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

    @ApiOperation("根据 ID 查询单条培训记录")
    @GetMapping
    public R query(@RequestParam("id") String id){

        TrainingRecord trainingRecord =  trainingRecordService.getById(id);
        return R.ok().data("data",trainingRecord);
    }

    // endregion

    // region 《=== 新增 ===》

    @ApiOperation("新增一条培训记录")
    @PostMapping
    public R add(){

        return R.ok();
    }

    // endregion

    // region 《=== 修改 ===》
    // endregion

    // region 《=== 删除 ===》
    // endregion

    // region 《=== 其他 ===》
    // endregion

}
