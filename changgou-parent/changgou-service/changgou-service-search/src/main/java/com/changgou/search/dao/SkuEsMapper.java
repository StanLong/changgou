package com.changgou.search.dao;

import com.changgou.search.pojo.SkuInfo;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * @author 矢量
 * @date 2020/3/29-17:46
 */
public interface SkuEsMapper extends ElasticsearchRepository<SkuInfo, Long> {
}
