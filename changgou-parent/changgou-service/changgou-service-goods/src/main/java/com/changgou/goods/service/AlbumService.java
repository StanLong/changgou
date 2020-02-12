package com.changgou.goods.service;

import com.changgou.goods.pojo.Album;
import com.github.pagehelper.PageInfo;

import java.util.List;

/**
 * @author 矢量
 * @date 2020/2/7-18:29
 */
public interface AlbumService {
    /**
     * 查询所有品牌
     * @return
     */
    List<Album> findAll();

    Album findById(Long id);

    void add(Album album);

    void update(Album album);

    void delete(Long id);

    List<Album> findList(Album album);

    PageInfo<Album> findPage(Integer page, Integer size);

    /**
     * 条件加分页搜索
     * @param album 封装查询条件
     * @param page 当前页
     * @param size 每页显示的记录数
     * @return
     */
    PageInfo<Album> findPage(Album album, Integer page, Integer size);
}
