package com.heyunetwork.demo.controller;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.heyunetwork.demo.entity.StaffInfo;
import com.heyunetwork.demo.entity.vo.StaffInfoVo;
import com.heyunetwork.demo.service.StaffService;
import com.heyunetwork.demo.util.ResponseResult.R;
import com.heyunetwork.demo.util.constant.StaffConstant;
import com.heyunetwork.demo.util.exceptionhandler.MyException;
import com.heyunetwork.demo.util.verification.MyVerifyUtil;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

import static com.sun.xml.internal.fastinfoset.stax.events.Util.isEmptyString;

/**
 * <p>
 * 员工资料表 前端控制器
 * </p>
 *
 * @author 汪阳东
 * @since 2022-10-09
 */
@RestController
@RequestMapping("/demo/staff-info")
public class StaffController {

    // region 《=== Service ===》

    @Autowired
    private StaffService staffService;

    // endregion

    // region 《=== 查询 ===》

    @ApiOperation(value = "根据 id 查询一条员工数据")
    @GetMapping
    public R query(@RequestParam("id") String id) {

        StaffInfo staffInfo = staffService.getById(id);
        StaffInfoVo staffInfoVo = new StaffInfoVo();
        BeanUtils.copyProperties(staffInfo, staffInfoVo);

        return R.ok().data("data", staffInfoVo);
    }

    @ApiOperation(value = "员工复杂查询",
            notes = "1.模糊查询；<br>" +
                    "2.分页查询，可以指定页码、页长；<br>" +
                    "3.条件查询，查询条件可以为姓名（name）、性别（sex）、部门（department）、学历（formal_schooling）；<br>" +
                    "4.可以正序或逆序排序")
    @GetMapping("staffComplexQuery")
    public R staffComplexQuery(@RequestParam("current") long current,
                    @RequestParam("size") long size,
                    @RequestParam("field") String field,
                    @RequestParam("keyword")String keyword,
                    @RequestParam(value = "sortAsc",required = false,defaultValue = "true")Boolean sortAsc) {

        // region <- 数据校验 ->
        // 校验查询条件是否可用
        if (StaffConstant.staffQueryFields.contains(field));
        // endregion

        Map<String, Object> map = staffService.staffComplexQuery(current,size,field,keyword,sortAsc);
        return R.ok().data(map);
    }

    // endregion

    // region 《=== 新增 ===》

    @ApiOperation(value = "新增一条员工信息",
            notes = "1.姓名和身份证号是必填项；<br>" +
                    "2.身份证号不可重复，需要校验唯一性；<br>" +
                    "3.身份证号和邮箱地址需要校验格式")
    @PostMapping
    public R add(@RequestBody StaffInfoVo staffInfoVo) {

        // region <- 数据校验 ->
        // 需要校验的字段
        String name = staffInfoVo.getName();
        String idcardNumber = staffInfoVo.getIdcardNumber();
        String email = staffInfoVo.getEmail();

        // 必填字段
        if (isEmptyString(name) ||
                isEmptyString(idcardNumber)
        ) {
            throw new MyException(20001, "姓名和身份证号是必填项");
        }
        // 身份证号格式校验
        if (!MyVerifyUtil.idcardNumberVerify(idcardNumber)) {
            throw new MyException(20001, "身份证号格式错误");
        }
        // 是否设置了邮箱
        if (!isEmptyString(email)){
            // 邮箱格式校验
            if (!MyVerifyUtil.emailVerify(email)) {
                throw new MyException(20001, "邮箱格式错误");
            }
        }
        // 身份证号唯一性校验
        QueryWrapper qw = new QueryWrapper<StaffInfo>();
        qw.eq("idcard_number", idcardNumber);
        if (staffService.count(qw) != 0) {
            throw new MyException(20001, "身份证已被注册");
        }
        // endregion

        // region <- 数据重新封装 ->
        StaffInfo staffInfo = new StaffInfo();
        BeanUtils.copyProperties(staffInfoVo, staffInfo);
        // endregion

        staffService.save(staffInfo);

        return R.ok();
    }

    // endregion

    // region 《=== 修改 ===》

    @ApiOperation(value = "修改一条员工数据",
            notes = "1.身份证号不可重复，需要校验唯一性；<br>" +
                    "2.身份证号和邮箱地址需要校验格式")
    @PutMapping
    public R update(@RequestBody StaffInfoVo staffInfoVo, @RequestParam("id") String id) {

        // region <- 数据校验 ->
        // 需要校验的字段
        String idcardNumber = staffInfoVo.getIdcardNumber();
        String email = staffInfoVo.getEmail();

        // 是否修改了邮箱
        if (!isEmptyString(email)) {
            // 邮箱格式校验
            if (!MyVerifyUtil.emailVerify(email)) throw new MyException(20001, "邮箱格式错误");
        }

        // 是否修改了身份证号
        if (!isEmptyString(idcardNumber)) {
            // 格式校验
            if (!MyVerifyUtil.idcardNumberVerify(idcardNumber)) throw new MyException(20001, "身份证号格式错误");
            // 身份证号唯一性校验
            QueryWrapper qw = new QueryWrapper<StaffInfo>();
            qw.eq("idcard_number", idcardNumber);
            if (staffService.count(qw) != 0) throw new MyException(20001, "身份证已被注册");
        }
        // endregion

        // region <- 数据重新封装 ->
        StaffInfo staffInfo = new StaffInfo();
        BeanUtils.copyProperties(staffInfoVo, staffInfo);
        // endregion

        UpdateWrapper uw = new UpdateWrapper<StaffInfo>();
        uw.eq("id", id);
        staffService.update(staffInfo, uw);

        return R.ok();
    }

    // endregion

    // region 《=== 删除 ===》

    @ApiOperation(value = "删除一条员工数据")
    @DeleteMapping
    public R delete(@RequestParam("id") String id) {

        staffService.removeById(id);

        return R.ok();
    }

    // endregion

    // region 《=== 其他 ===》
    // endregion
}
