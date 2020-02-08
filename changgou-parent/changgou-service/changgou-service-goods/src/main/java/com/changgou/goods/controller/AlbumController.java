package com.changgou.goods.controller;

import com.changgou.entity.Result;
import com.changgou.entity.StatusCode;
import com.changgou.goods.goods.pojo.Album;
import com.changgou.goods.service.AlbumService;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @author 矢量
 * @date 2020/2/7-18:30
 */
@RestController
@RequestMapping(value="/album")
@CrossOrigin
public class AlbumController {

    @Autowired
    private AlbumService albumService;

    @GetMapping
    public Result<List<Album>> findAll(){
        List<Album> albumList = albumService.findAll();
        return new Result<>(true, StatusCode.OK, "查询相册集合成功", albumList);
    }

    @GetMapping(value="/{id}")
    public Result<Album> findAlbumById(@PathVariable(value="id") Long id){
        Album album = albumService.findById(id);
        return new Result<>(true, StatusCode.OK, "根据id查询成功", album);
    }

    @PostMapping
    public Result addAlbum(@RequestBody Album album){
        albumService.add(album);
        return new Result<>(true, StatusCode.OK, "相册新增成功");
    }

    @PutMapping(value="/{id}")
    public Result updateAlbum(@PathVariable(value="id") Long id, @RequestBody Album album){
        album.setId(id);
        albumService.update(album);
        return new Result<>(true, StatusCode.OK, "相册修改成功");
    }

    @DeleteMapping(value="/{id}")
    public Result deleteAlbumById(@PathVariable(value="id") Long id){
        albumService.delete(id);
        return new Result<>(true, StatusCode.OK, "相册删除成功");
    }

    @PostMapping(value="/search")
    public Result<List<Album>> findList(@RequestBody Album album){
        List<Album> albumList = albumService.findList(album);
        return new Result<>(true, StatusCode.OK, "相册条件查询", albumList);
    }

    @GetMapping(value="/search/{page}/{size}")
    public Result<PageInfo<Album>> findPage(@PathVariable(value = "page") Integer page,
                                                                      @PathVariable(value = "size") Integer size){
        PageInfo<Album> pageInfo = albumService.findPage(page, size);
        return new Result<>(true, StatusCode.OK, "相册分页查询", pageInfo);
    }

    @PostMapping(value="/search/{page}/{size}")
    public Result<PageInfo<Album>> findPage(@RequestBody Album album,
                                                                      @PathVariable(value = "page") Integer page,
                                                                      @PathVariable(value = "size") Integer size){
        PageInfo<Album> pageInfo = albumService.findPage(album, page, size);
        return new Result<>(true, StatusCode.OK, "相册条件+分页查询", pageInfo);
    }
}
