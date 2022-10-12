package com.heyunetwork.demo.service;

import com.heyunetwork.demo.entity.StaffInfo;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.Map;

/**
 * <p>
 * 员工资料表 服务类
 * </p>
 *
 * @author 汪阳东
 * @since 2022-10-09
 */
public interface StaffService extends IService<StaffInfo> {

    // region 《=== 查询 ===》

    Map<String, Object> staffComplexQuery(long current, long size, String field, String keyword,String sortBy, Boolean sortAsc);

    // endregion

    // region 《=== 新增 ===》
    // endregion

    // region 《=== 修改 ===》
    // endregion

    // region 《=== 删除 ===》
    // endregion

}
