package com.changgou.goods.service;

import com.changgou.goods.goods.pojo.Brand;
import com.github.pagehelper.PageInfo;

import java.util.List;

/**
 * @author 矢量
 * @date 2020/2/7-12:33
 */
public interface BrandService {
    /**
     * 查询所有品牌
     * @return
     */
    List<Brand> findAll();

    Brand findById(Long id);

    void add(Brand brand);

    void update(Brand brand);

    void delete(Long id);

    List<Brand> findList(Brand brand);

    PageInfo<Brand> findPage(Integer page, Integer size);

    /**
     * 条件加分页搜索
     * @param brand 封装查询条件
     * @param page 当前页
     * @param size 每页显示的记录数
     * @return
     */
    PageInfo<Brand> findPage(Brand brand, Integer page, Integer size);
}
