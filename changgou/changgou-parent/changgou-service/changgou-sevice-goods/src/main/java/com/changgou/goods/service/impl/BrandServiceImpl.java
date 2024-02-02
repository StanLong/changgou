package com.changgou.goods.service.impl;

import com.changgou.goods.dao.BrandMapper;
import com.changgou.goods.pojo.Brand;
import com.changgou.goods.service.BrandService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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


}
