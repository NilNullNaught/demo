package com.heyunetwork.demo.util;


import org.springframework.beans.BeanUtils;
import org.springframework.util.CollectionUtils;

import java.util.List;


public class MyBeanUtil {

    // TODO 该工具类的实现不优雅，效率低下，需要修改
    public static <T1, T2> List<T2> listCopyProperties(List<T1> source, List<T2> target, Class<T2> targetClass) throws InstantiationException, IllegalAccessException {

        if (!CollectionUtils.isEmpty(source)) {
                for (T1 s : source) {
                    T2 t = targetClass.newInstance();
                    BeanUtils.copyProperties(s, t);
                    target.add(t);
                }

            return target;
        } else {
            throw new RuntimeException("source 为空");
        }
    }
}
