package com.changgou.search.service.impl;

import com.alibaba.fastjson.JSON;
import com.changgou.entity.Result;
import com.changgou.goods.feign.SkuFeign;
import com.changgou.goods.pojo.Sku;
import com.changgou.search.dao.SkuEsMapper;
import com.changgou.search.pojo.SkuInfo;
import com.changgou.search.service.SkuService;
import org.apache.commons.lang.StringUtils;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.aggregations.AggregationBuilders;
import org.elasticsearch.search.aggregations.bucket.terms.StringTerms;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.elasticsearch.core.ElasticsearchTemplate;
import org.springframework.data.elasticsearch.core.aggregation.AggregatedPage;
import org.springframework.data.elasticsearch.core.query.NativeSearchQueryBuilder;
import org.springframework.stereotype.Service;

import java.util.*;

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
     * ElasticsearchTemplate: 可以实现索引库的增删改查
     */
    @Autowired
    private ElasticsearchTemplate elasticsearchTemplate;



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

    /**
     * 多条件搜索
     * @param searchMap
     * @return
     */
    @Override
    public Map<String, Object> search(Map<String, String> searchMap) throws Exception{


        //NativeSearchQueryBuilder 搜索条件构建对象，用于封装各种搜索条件
        NativeSearchQueryBuilder nativeSearchQueryBuilder = new NativeSearchQueryBuilder();

        if(searchMap != null && searchMap.size() >0){
            // 根据关键词搜索
            String keywords = searchMap.get("keywords");
            if(StringUtils.isNotEmpty(keywords)){
                nativeSearchQueryBuilder.withQuery(QueryBuilders.queryStringQuery(keywords).field("name"));
            }

        }

        // 集合搜索
        Map<String, Object> resultMap = searchList(nativeSearchQueryBuilder);


        // 分类分组查询
        List<String> categoryList = searchCategoryList(nativeSearchQueryBuilder);

        // 品牌分组查询
        List<String> brandList = searchBrandList(nativeSearchQueryBuilder);

        // 规格分组查询
        Map<String, Set<String>> specList = searchSpecList(nativeSearchQueryBuilder);

        resultMap.put("categoryList",categoryList);
        resultMap.put("brandList", brandList);
        resultMap.put("specList", specList);
        return resultMap;
    }

    /**
     *  分类分组查询
     * @param nativeSearchQueryBuilder
     * @return
     */
    public List<String> searchCategoryList(NativeSearchQueryBuilder nativeSearchQueryBuilder){
        /**
         * 分组查询分类集合
         * addAggregation() 添加一个聚合操作
         * 参数1 取别名
         * 参数2 表示根据哪个域进行分组查询
         */
        nativeSearchQueryBuilder.addAggregation(AggregationBuilders.terms("skuCategory").field("categoryName"));
        AggregatedPage<SkuInfo> aggregatedPage = elasticsearchTemplate.queryForPage(nativeSearchQueryBuilder.build(), SkuInfo.class);

        /**
         * 获取分组数据
         * aggregatedPage.getAggregations() 获取的是集合，可以根据多个域进行分组
         * get("skuCategory") 获取指定域的集合数
         */
        StringTerms stringTerms = aggregatedPage.getAggregations().get("skuCategory");
        List<String> categoryList = new ArrayList<String>();
        for (StringTerms.Bucket bucket : stringTerms.getBuckets()) {
            String categoryName = bucket.getKeyAsString(); // 其中的一个分类名字
            categoryList.add(categoryName);
        }
        return categoryList;
    }

    /**
     *  品牌分组查询
     * @param nativeSearchQueryBuilder
     * @return
     */
    public List<String> searchBrandList(NativeSearchQueryBuilder nativeSearchQueryBuilder){
        /**
         * 分组查询分类集合
         * addAggregation() 添加一个聚合操作
         * 参数1 取别名
         * 参数2 表示根据哪个域进行分组查询
         */
        nativeSearchQueryBuilder.addAggregation(AggregationBuilders.terms("skuBrand").field("brandName"));
        AggregatedPage<SkuInfo> aggregatedPage = elasticsearchTemplate.queryForPage(nativeSearchQueryBuilder.build(), SkuInfo.class);

        /**
         * 获取分组数据
         * aggregatedPage.getAggregations() 获取的是集合，可以根据多个域进行分组
         * get("skuBrand") 获取指定域的集合数
         */
        StringTerms stringTerms = aggregatedPage.getAggregations().get("skuBrand");
        List<String> brandList = new ArrayList<String>();
        for (StringTerms.Bucket bucket : stringTerms.getBuckets()) {
            String brandName = bucket.getKeyAsString(); // 其中的一个品牌名字
            brandList.add(brandName);
        }
        return brandList;
    }

    /**
     *  规格分组查询
     * @param nativeSearchQueryBuilder
     * @return
     */
    public Map<String, Set<String>> searchSpecList(NativeSearchQueryBuilder nativeSearchQueryBuilder){
        /**
         * 分组查询规格集合
         * addAggregation() 添加一个聚合操作
         * 参数1 取别名
         * 参数2 表示根据哪个域进行分组查询
         */
        nativeSearchQueryBuilder.addAggregation(AggregationBuilders.terms("skuSpec").field("spec.keyword").size(100));
        AggregatedPage<SkuInfo> aggregatedPage = elasticsearchTemplate.queryForPage(nativeSearchQueryBuilder.build(), SkuInfo.class);

        /**
         * 获取分组数据
         * aggregatedPage.getAggregations() 获取的是集合，可以根据多个域进行分组
         * get("skuBrand") 获取指定域的集合数
         */
        StringTerms stringTerms = aggregatedPage.getAggregations().get("skuSpec");
        List<String> specList = new ArrayList<String>();
        for (StringTerms.Bucket bucket : stringTerms.getBuckets()) {
            String specName = bucket.getKeyAsString(); // 其中的一个规格名字
            specList.add(specName);
        }

        // 规格汇总合并
        Map<String, Set<String>> allSpec = putAllSpec(specList);

        return allSpec;
    }

    /**
     * 规格汇总合并
     * @param specList
     * @return
     */
    private Map<String, Set<String>> putAllSpec(List<String> specList) {
        //合并后的map对象
        Map<String, Set<String>> allSpec = new HashMap<String, Set<String>>();

        // 1 循环specList对象
        for (String spec : specList) {
            // 2 将每个JSON字符串转成Map
            Map<String, String> specMap = JSON.parseObject(spec, Map.class);

            // 4 合并流程
            for (Map.Entry<String, String> entry : specMap.entrySet()) {
                // 4.1 循环所有Map
                String key = entry.getKey(); // 规格名字
                String value = entry.getValue(); // 规格值

                // 获取当前规格对应的Set集合数据
                Set<String> specSet = allSpec.get(key);
                if(null == specSet){
                    specSet = new HashSet<>();
                }
                specSet.add(value);
                allSpec.put(key, specSet);
            }
        }
        return allSpec;
    }

    /**
     * 集合搜索
     * @param nativeSearchQueryBuilder
     * @return
     */
    public Map<String, Object> searchList(NativeSearchQueryBuilder nativeSearchQueryBuilder){
        /**
         * 执行搜索，响应结果
         * queryForPage需要两个参数
         * 1)搜索条件封装对象
         * 2)搜索的结果集（集合数据）需要转换的类型
         */
        AggregatedPage<SkuInfo> page = elasticsearchTemplate.queryForPage(nativeSearchQueryBuilder.build(), SkuInfo.class);


        // 分析数据
        // 获取数据结果集
        List<SkuInfo> contents = page.getContent();

        // 总页数
        int totalPages = page.getTotalPages();

        // 总记录数
        long totalElements = page.getTotalElements();
        // 封装一个map存储所有数据并返回
        Map<String, Object> resultMap = new HashMap<String ,Object>();
        resultMap.put("rows", contents);
        resultMap.put("total", totalElements);
        resultMap.put("totalPage", totalPages);
        return resultMap;
    }
}
