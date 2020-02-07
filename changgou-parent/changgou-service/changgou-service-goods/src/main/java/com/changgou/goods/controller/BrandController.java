package com.changgou.goods.controller;

import com.changgou.goods.entity.Result;
import com.changgou.goods.entity.StatusCode;
import com.changgou.goods.goods.pojo.Brand;
import com.changgou.goods.service.BrandService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @author 矢量
 * @date 2020/2/7-12:37
 */
@RestController
@RequestMapping(value="/brand")
@CrossOrigin
public class BrandController {

    @Autowired
    private BrandService brandService;

    @GetMapping
    public Result<List<Brand>> findAll(){
        List<Brand> brandList = brandService.findAll();
        return new Result<>(true, StatusCode.OK, "查询品牌集合成功", brandList);
    }

    @GetMapping(value="/{id}")
    public Result<Brand> findBrandById(@PathVariable(value="id") Integer id){
        Brand brand = brandService.findById(id);
        return new Result<>(true, StatusCode.OK, "根据id查询成功", brand);
    }

    @PostMapping
    public Result addBrand(@RequestBody Brand brand){
        brandService.add(brand);
        return new Result<>(true, StatusCode.OK, "品牌新增成功");
    }

    @PutMapping(value="/{id}")
    public Result updateBrand(@PathVariable(value="id") Integer id, @RequestBody Brand brand){
        brand.setId(id);
        brandService.update(brand);
        return new Result<>(true, StatusCode.OK, "品牌修改成功");
    }

    @DeleteMapping(value="/{id}")
    public Result deleteBrandById(@PathVariable(value="id") Integer id){
        brandService.delete(id);
        return new Result<>(true, StatusCode.OK, "品牌删除成功");
    }
}
