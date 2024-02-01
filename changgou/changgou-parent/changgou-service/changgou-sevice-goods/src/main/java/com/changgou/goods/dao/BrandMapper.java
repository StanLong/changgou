package com.changgou.goods.dao;

import com.changgou.goods.pojo.Brand;
import org.springframework.stereotype.Component;
import tk.mybatis.mapper.common.Mapper;

/**
 * DAO 使用通用 mapper，需要继承接口 tk.mybatis.mapper.common.Mapper， 这个接口里把mapper的增删改查都写好了
 */
@Component
public interface BrandMapper extends Mapper<Brand> {
}
