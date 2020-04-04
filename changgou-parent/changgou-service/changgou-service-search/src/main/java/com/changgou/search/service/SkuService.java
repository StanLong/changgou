package com.changgou.search.service;

import java.util.Map;

/**
 * @author 矢量
 * @date 2020/3/29-17:42
 */
public interface SkuService {
    /**
     * 导入数据到es索引库中
     */
    void importData();

    /**
     * 条件搜索
     * @param searchMap
     * @return
     */
    Map<String, Object> search(Map<String, String> searchMap) throws Exception;
}




