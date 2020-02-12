package com.changgou.goods.service.impl;

import com.changgou.goods.dao.BrandMapper;
import com.changgou.goods.pojo.Brand;
import com.changgou.goods.service.BrandService;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tk.mybatis.mapper.entity.Example;

import java.util.List;

/**
 * @author 矢量
 * @date 2020/2/7-12:34
 */
@Service
public class BrandServiceImpl implements BrandService {

    @Autowired
    private BrandMapper brandMapper;

    @Override
    public List<Brand> findAll() {
        return brandMapper.selectAll();
    }

    @Override
    public Brand findById(Long id) {
        return brandMapper.selectByPrimaryKey(id);
    }

    @Override
    public void add(Brand brand) {
        // 凡是方法中带有selective会忽略空值
        brandMapper.insertSelective(brand);
    }

    @Override
    public void update(Brand brand) {
        brandMapper.updateByPrimaryKeySelective(brand);
    }

    @Override
    public void delete(Long id) {
        brandMapper.deleteByPrimaryKey(id);
    }

    @Override
    public List<Brand> findList(Brand brand) {
        Example example = createExample(brand);
        return brandMapper.selectByExample(example);
    }

    @Override
    public PageInfo<Brand> findPage(Integer page, Integer size) {
        /**
         * 分页实现，PageHelper.startPage(page, size);分页实现，后面的查询紧跟集合查询
         *  page：当前页
         *  size：每页显示的记录数
         */
        PageHelper.startPage(page, size);
        List<Brand> brandList = brandMapper.selectAll();
        // 封闭PageInfo
        return new PageInfo<>(brandList);
    }

    @Override
    public PageInfo<Brand> findPage(Brand brand, Integer page, Integer size) {
        //分页
        PageHelper.startPage(page, size);
        //搜索数据
        Example example = createExample(brand);
        List<Brand> brandLit = brandMapper.selectByExample(example);
        // 封装PageInfo<Brand>
        return new PageInfo<>(brandLit);
    }

    /**
     * 构建查询条件
     * @param brand
     * @return
     */
    public Example createExample(Brand brand){
        Example example = new Example(Brand.class);
        Example.Criteria criteria = example.createCriteria(); // 条件构造器
        if(null != brand){
            if(!StringUtils.isEmpty(brand.getName())){
                /**
                 * 1: brand的属性名
                 * 2：占位符参数，搜索的条件
                 */
                criteria.andLike("name", "%" + brand.getName() + "%");
            }
            if(!StringUtils.isEmpty(brand.getLetter())){
                criteria.andEqualTo("letter", brand.getLetter());
            }
        }
        return example;
    }
}
