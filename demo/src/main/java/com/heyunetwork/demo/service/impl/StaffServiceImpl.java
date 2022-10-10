package com.heyunetwork.demo.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.heyunetwork.demo.entity.StaffInfo;
import com.heyunetwork.demo.entity.vo.StaffInfoVo;
import com.heyunetwork.demo.mapper.StaffInfoMapper;
import com.heyunetwork.demo.service.StaffService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.heyunetwork.demo.util.myutil.MyBeanUtil;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * 员工资料表 服务实现类
 * </p>
 *
 * @author 汪阳东
 * @since 2022-10-09
 */
@Service
public class StaffServiceImpl extends ServiceImpl<StaffInfoMapper, StaffInfo> implements StaffService {


    // region 《=== 查询 ===》

    /**
     * 员工复杂查询
     *
     * @param current
     * @param size
     * @param field
     * @param keyword
     * @param sortAsc
     * @return
     */
    @Override
    public Map<String, Object> staffComplexQuery(long current, long size, String field, String keyword, Boolean sortAsc) {

        // region <- 1.封装查询条件 ->
        QueryWrapper qw = new QueryWrapper<StaffInfo>();
        if (sortAsc)
            qw.orderByAsc(field);
        else
            qw.orderByDesc(field);
        if (keyword != null) {
            qw.like(field, keyword);
        }
        Page<StaffInfo> page = new Page<>(current, size);
        // endregion

        // 2.执行查询
        baseMapper.selectPage(page, qw);

        // region <- 3.封装查询结果并返回 ->
        Map<String, Object> map = new HashMap<>();

        List items = new ArrayList<StaffInfoVo>();
        try {
            MyBeanUtil.listCopyProperties(page.getRecords(), items, StaffInfoVo.class);
        } catch (Exception e) {
            e.printStackTrace();
        }

        map.put("items", items);
        map.put("current", page.getCurrent());
        map.put("pages", page.getPages());
        map.put("size", page.getSize());
        map.put("total", page.getTotal());
        map.put("hasNext", page.hasNext());
        map.put("hasPrevious", page.hasPrevious());
        return map;
        // endregion
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
