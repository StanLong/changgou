package com.changgou.goods.feign;

import com.changgou.entity.Result;
import com.changgou.goods.pojo.Sku;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

/**
 * @author 矢量
 * @date 2020/3/29-17:38
 */
@FeignClient(value = "goods")
@RequestMapping("/sku")
public interface SkuFeign {

    /**
     * 查询sku全部数据
     * @return
     */
    @GetMapping
    Result<List<Sku>> findAll();
}
