package com.changgou.search.service.impl;

import com.alibaba.fastjson.JSON;
import com.changgou.entity.Result;
import com.changgou.goods.feign.SkuFeign;
import com.changgou.goods.pojo.Sku;
import com.changgou.search.dao.SkuEsMapper;
import com.changgou.search.pojo.SkuInfo;
import com.changgou.search.service.SkuService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

/**
 * @author 矢量
 * @date 2020/3/29-17:43
 */
@Service
public class SkuServiceImpl implements SkuService {

    @Autowired
    private SkuFeign skuFeign;

    @Autowired
    private SkuEsMapper skuEsMapper;
    /**
     * 导入索引库
     */
    @Override
    public void importData() {
        // Feign 调用， 查询List<Sku>
        Result<List<Sku>> skuResult = skuFeign.findAll();

        // 将List<Sku> 转换成<skuInfo>
        List<SkuInfo> skuInfoList = JSON.parseArray(JSON.toJSONString(skuResult.getData()), SkuInfo.class);

        for(SkuInfo skuInfo : skuInfoList){
            Map<String, Object> specMap = JSON.parseObject(skuInfo.getSpec(), Map.class);
            skuInfo.setSpecMap(specMap);
        }
        // 调用dao实现数据批量导入
        skuEsMapper.saveAll(skuInfoList);


    }
}
