package com.changgou.goods.controller;

import com.changgou.entity.Result;
import com.changgou.entity.StatusCode;
import com.changgou.goods.pojo.Brand;
import com.changgou.goods.service.BrandService;
import com.github.pagehelper.PageInfo;
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
    public Result<Brand> findBrandById(@PathVariable(value="id") Long id){
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
    public Result deleteBrandById(@PathVariable(value="id") Long id){
        brandService.delete(id);
        return new Result<>(true, StatusCode.OK, "品牌删除成功");
    }

    @PostMapping(value="/search")
    public Result<List<Brand>> findList(@RequestBody Brand brand){
        List<Brand> brandList = brandService.findList(brand);
        return new Result<>(true, StatusCode.OK, "品牌条件查询", brandList);
    }

    @GetMapping(value="/search/{page}/{size}")
    public Result<PageInfo<Brand>> findPage(@PathVariable(value = "page") Integer page, @PathVariable(value = "size") Integer size){
        PageInfo<Brand> pageInfo = brandService.findPage(page, size);
        return new Result<>(true, StatusCode.OK, "品牌分页查询", pageInfo);
    }

    @PostMapping(value="/search/{page}/{size}")
    public Result<PageInfo<Brand>> findPage(@RequestBody Brand brand,
                                            @PathVariable(value = "page") Integer page,
                                            @PathVariable(value = "size") Integer size){
        PageInfo<Brand> pageInfo = brandService.findPage(brand, page, size);
        return new Result<>(true, StatusCode.OK, "品牌条件+分页查询", pageInfo);
    }

}
