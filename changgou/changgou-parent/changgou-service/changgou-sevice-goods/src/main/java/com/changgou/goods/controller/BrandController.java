package com.changgou.goods.controller;

import com.changgou.goods.pojo.Brand;
import com.changgou.goods.service.BrandService;
import entity.Result;
import entity.StatusCode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(value="/brand")
@CrossOrigin // 跨域, 域名访问B域名的数据，当域名、请求端口或者协议不一致时就存在跨域
public class BrandController {

    @Autowired
    private BrandService brandService;

    /**
     * 查询所有品牌
     */
    @GetMapping
    public Result<List<Brand>> findAll(){
        List<Brand> brandList = brandService.findAll();
        return new Result<>(true, StatusCode.OK, "查询品牌集合成功", brandList);
    }

    /**
     * 根据id查询品牌
     */
    @GetMapping(value = "/{id}")
    public Result<Brand> findById(@PathVariable(value = "id") Integer id){
        Brand brand = brandService.findById(id);
        return new Result<Brand>(true, StatusCode.OK, "根据id查询商品成功", brand);
    }

    /**
     * 新增商品
     * @param brand
     * @return
     */
    @PostMapping
    public Result add(@RequestBody Brand brand){
        brandService.add(brand);
        return new Result(true, StatusCode.OK, "增加品牌成功");
    }



}












