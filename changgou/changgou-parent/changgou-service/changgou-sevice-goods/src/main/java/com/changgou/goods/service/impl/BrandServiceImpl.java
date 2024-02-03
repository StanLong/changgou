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
import tk.mybatis.mapper.util.StringUtil;

import java.util.List;

@Service
public class BrandServiceImpl implements BrandService {

    @Autowired
    private BrandMapper brandMapper;

    /**
     * 查询所有
     * @return
     */
    @Override
    public List<Brand> findAll() {
        return brandMapper.selectAll();
    }

    @Override
    public Brand findById(Integer id) {
        return brandMapper.selectByPrimaryKey(id);
    }

    @Override
    public void add(Brand brand) {
        /**
         * 通用mapper的方法中但凡有 Selective 就表示 通用mapper拼装的sql语句会忽略空值
         * 比如这个对象 brand , 只传入了两个属性 name 、 letter
         * Mapper.insertSelective(brand) -> 拼装Sql语句 -> insert into tb_brand(name, letter) values(?,?)
         *
         * 而不带 Selective 的方法就不会忽略空值
         * Mapper.insert（brand) -> 拼装sql语句 -> insert into tb_brand(id, name, image, letter, seq) values(?,?,?,?,?)
         */
        brandMapper.insertSelective(brand);
    }

    /**
     * 根据id修改品牌
     * @param brand
     */
    @Override
    public void update(Brand brand) {
        brandMapper.updateByPrimaryKeySelective(brand);
    }

    @Override
    public void delete(Integer id) {
        brandMapper.deleteByPrimaryKey(id);
    }


    public Example createExample(Brand brand){
        // 自定义条件查询对象
        Example example = new Example(Brand.class);
        Example.Criteria criteria = example.createCriteria(); // 条件构造器
        if (brand != null){
            if(!StringUtils.isEmpty(brand.getName())){
                /**
                 * andLike 参数说明
                 * 第一个参数：搜索对象的属性名
                 * 第二个参数：占位符参数，搜索的条件
                 *
                 */
                criteria.andLike("name", "%" + brand.getName() + "%"); // 这段代码会拼接 like 的sql语句
            }
            if(!StringUtils.isEmpty(brand.getLetter())){
                criteria.andEqualTo("letter", brand.getLetter());
            }
        }
        return example;
    }
    @Override
    public List<Brand> findList(Brand brand) {
        Example example = createExample(brand);
        return brandMapper.selectByExample(example);
    }

    @Override
    public PageInfo<Brand> findPage(Integer page, Integer size) {
        PageHelper.startPage(page, size);
        List<Brand> brandList = brandMapper.selectAll();
        return new PageInfo<Brand>(brandList);
    }


}
