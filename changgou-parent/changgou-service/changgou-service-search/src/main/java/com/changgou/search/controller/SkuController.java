package com.changgou.search.controller;

import com.changgou.entity.Result;
import com.changgou.entity.StatusCode;
import com.changgou.search.service.SkuService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author 矢量
 * @date 2020/3/29-17:51
 */
@RestController
@RequestMapping(value = "/search")
@CrossOrigin
public class SkuController {

    @Autowired
    private SkuService skuService;
    /**
     * 数据导入
     * @return
     */
    @GetMapping(value = "/import")
    public Result importData(){
        skuService.importData();
        return new Result(true, StatusCode.OK, "执行成功");
    }
}
