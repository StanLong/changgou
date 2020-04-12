package com.changgou.search.service.impl;

import com.alibaba.fastjson.JSON;
import com.changgou.entity.Result;
import com.changgou.goods.feign.SkuFeign;
import com.changgou.goods.pojo.Sku;
import com.changgou.search.dao.SkuEsMapper;
import com.changgou.search.pojo.SkuInfo;
import com.changgou.search.service.SkuService;
import org.apache.commons.lang.StringUtils;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.common.text.Text;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.aggregations.AggregationBuilders;
import org.elasticsearch.search.aggregations.bucket.terms.StringTerms;
import org.elasticsearch.search.fetch.subphase.highlight.HighlightBuilder;
import org.elasticsearch.search.fetch.subphase.highlight.HighlightField;
import org.elasticsearch.search.sort.FieldSortBuilder;
import org.elasticsearch.search.sort.SortOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.elasticsearch.core.ElasticsearchTemplate;
import org.springframework.data.elasticsearch.core.SearchResultMapper;
import org.springframework.data.elasticsearch.core.aggregation.AggregatedPage;
import org.springframework.data.elasticsearch.core.aggregation.impl.AggregatedPageImpl;
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
            Map<String, Object> specMap
                    = JSON.parseObject(skuInfo.getSpec(), Map.class);
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

        // 筛选过滤
        BoolQueryBuilder queryBuilder = QueryBuilders.boolQuery();


        if(searchMap != null && searchMap.size() >0){
            // 根据关键词搜索
            String keywords = searchMap.get("keywords");
            if(StringUtils.isNotEmpty(keywords)){
                //nativeSearchQueryBuilder.withQuery(QueryBuilders.queryStringQuery(keywords).field("name"));
                queryBuilder.must(QueryBuilders.queryStringQuery(keywords).field("name"));
            }

            // 输入了分类
            String category = searchMap.get("category");
            if(StringUtils.isNotEmpty(category)){
                //nativeSearchQueryBuilder.withQuery(QueryBuilders.queryStringQuery(keywords).field("name"));
                queryBuilder.must(QueryBuilders.termQuery("categoryName", category));
            }

            // 输入了品牌
            String brand = searchMap.get("brand");
            if(StringUtils.isNotEmpty(brand)){
                queryBuilder.must(QueryBuilders.termQuery("brandName", brand));
            }

            // 规格过滤实现：spec_网络=联通&spec_颜色=红
            for (Map.Entry<String, String> entry : searchMap.entrySet()) {
                String key = entry.getKey();
                // 如何 key 以 spec_ 开头，表示规格筛选查询
                if(key.startsWith("spec_")){
                    String value = entry.getValue();
                    queryBuilder.must(QueryBuilders.termQuery("specMap." + key.substring(5) + ".keyword", value));
                }
            }

            //  价格筛选 0-500元 500-1000元
            // 去掉“”“中文"元"和"以上"
            // 获取价格区间值
            String price = searchMap.get("price");
            if(StringUtils.isNotEmpty(price)){
                price = price.replace("元", "").replace("以上", "");
                String[] prices = price.split("-");
                if(prices != null && prices.length>0){
                    queryBuilder.must(QueryBuilders.rangeQuery("price").gt(Integer.parseInt(prices[0])));
                    if(prices.length == 2){
                        queryBuilder.must(QueryBuilders.rangeQuery("price").lte(Integer.parseInt(prices[1])));
                    }
                }
            }

            // 排序实现
            String sortField = searchMap.get("sortField"); // 指定排序的域
            String sortRule = searchMap.get("sortRule"); // 指定排序的规则

            if(StringUtils.isNotEmpty(sortField) && StringUtils.isNotEmpty(sortRule)){
                nativeSearchQueryBuilder.withSort(new FieldSortBuilder(sortField).order(SortOrder.valueOf(sortRule)));
            }

        }

        // 分页,用户不传分页参数，默认第一页
        Integer pageNum = convertPage(searchMap); // 默认第一页
        Integer size = 3; // 默认查询的条数

        nativeSearchQueryBuilder.withPageable(PageRequest.of(pageNum-1, size));

        nativeSearchQueryBuilder.withQuery(queryBuilder);

        // 集合搜索
        Map<String, Object> resultMap = searchList(nativeSearchQueryBuilder);

        // 当用户选择了分类，将分类作为搜索条件，则不需要对分类进行分组搜索，因为分组搜索的数据是用于显示分类搜索条件的
        if(searchMap == null || StringUtils.isEmpty(searchMap.get("category"))){
            // 分类分组查询
            List<String> categoryList = searchCategoryList(nativeSearchQueryBuilder);
            resultMap.put("categoryList",categoryList);
        }

        // 当用户选择了品牌，将品牌作为搜索条件，则不需要对品牌进行分组搜索，因为分组搜索的数据是用于显示品牌搜索条件的
        // 品牌分组查询
        if(searchMap == null || StringUtils.isEmpty(searchMap.get("brand"))){
            // 分类分组查询
            List<String> brandList = searchBrandList(nativeSearchQueryBuilder);
            resultMap.put("brandList", brandList);
        }


        // 规格分组查询
        Map<String, Set<String>> specList = searchSpecList(nativeSearchQueryBuilder);

        resultMap.put("specList", specList);
        return resultMap;
    }

    /**
     * 接收前端传入的分页参数
     * @param searchMap
     * @return
     */
    public Integer convertPage(Map<String, String> searchMap){
        if(searchMap != null && searchMap.get("pageNum") != null){
            String pageName = searchMap.get("pageNum");
            try {
                return Integer.parseInt(pageName);
            }catch (NumberFormatException e){
                e.printStackTrace();
            }
        }
        return 1;
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
        // 高亮配置
        HighlightBuilder.Field field = new HighlightBuilder.Field("name"); // 指定高亮域
        // 前缀
        field.preTags("<em style=\"color:red;\">");
        // 后缀
        field.postTags("</em>");
        // 碎片长度 关键词数据的长度
        field.fragmentSize(100);
        // 添加高亮
        nativeSearchQueryBuilder.withHighlightFields(field);



        /**
         * 执行搜索，响应结果
         * queryForPage需要两个参数
         * 1)搜索条件封装对象
         * 2)搜索的结果集（集合数据）需要转换的类型
         */
        //AggregatedPage<SkuInfo> page = elasticsearchTemplate.queryForPage(nativeSearchQueryBuilder.build(), SkuInfo.class);
        AggregatedPage<SkuInfo> page = elasticsearchTemplate.queryForPage(
                nativeSearchQueryBuilder.build(), //搜索条件封装
                SkuInfo.class, // 数据集合要转换的类型的字节码
                new SearchResultMapper() { // 执行搜索后，将数据结果集封装到该对象中
                    @Override
                    public <T> AggregatedPage<T> mapResults(SearchResponse searchResponse, Class<T> aClass, Pageable pageable) {
                        // 存储所有转换后的高亮数据对象
                        List<T> list = new ArrayList<T>();

                        // 执行查询获取所有数据（包含高亮数据和非高亮数据）
                        for (SearchHit hit : searchResponse.getHits()) {
                            // 分析结果集数据，获取非高亮数据
                            SkuInfo skuInfo = JSON.parseObject(hit.getSourceAsString(), SkuInfo.class);
                            // 获取某个高亮域的数据
                            HighlightField highlightField = hit.getHighlightFields().get("name");
                            if(null != highlightField && highlightField.getFragments() != null){
                                // 高亮数据读取出来
                                Text[] fragments = highlightField.getFragments();
                                StringBuffer buffer = new StringBuffer();
                                for (Text fragment : fragments) {
                                    buffer.append(fragment.toString());
                                }
                                // 非高亮数据中指定的域替换成高亮数据
                                skuInfo.setName(buffer.toString());

                            }
                            // 将高亮数据添加到集合中
                            list.add((T)skuInfo);
                            // 将数据返回
                        }
                        /**
                         * AggregatedPageImpl 需要传三个参数
                         * 1) 搜索的集合数据
                         * 2) 分页对象信息
                         * 3) 搜索记录的总条数
                         */
                        return new AggregatedPageImpl<T>(list, pageable, searchResponse.getHits().getTotalHits());
                    }

                }
        );

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
