package com.changgou.goods.service;

import com.changgou.goods.goods.pojo.Brand;

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

    Brand findById(Integer id);

    void add(Brand brand);

    void update(Brand brand);

    void delete(Integer id);
}
