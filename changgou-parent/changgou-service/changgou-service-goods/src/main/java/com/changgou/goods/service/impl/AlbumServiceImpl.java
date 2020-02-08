package com.changgou.goods.service.impl;

import com.changgou.goods.dao.AlbumMapper;
import com.changgou.goods.goods.pojo.Album;
import com.changgou.goods.service.AlbumService;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tk.mybatis.mapper.entity.Example;

import java.util.List;

/**
 * @author 矢量
 * @date 2020/2/7-18:29
 */
@Service
public class AlbumServiceImpl implements AlbumService {

    @Autowired
    private AlbumMapper albumMapper;

    @Override
    public List<Album> findAll() {
        return albumMapper.selectAll();
    }

    @Override
    public Album findById(Long id) {
        return albumMapper.selectByPrimaryKey(id);
    }

    @Override
    public void add(Album album) {
        albumMapper.insert(album);
    }

    @Override
    public void update(Album album) {
        albumMapper.updateByPrimaryKeySelective(album);
    }

    @Override
    public void delete(Long id) {
        albumMapper.deleteByPrimaryKey(id);
    }

    @Override
    public List<Album> findList(Album album) {
        Example example = createExample(album);
        return albumMapper.selectByExample(example);
    }

    @Override
    public PageInfo<Album> findPage(Integer page, Integer size) {
        PageHelper.startPage(page, size);
        List<Album> albumList = albumMapper.selectAll();
        return new PageInfo<>(albumList);
    }

    @Override
    public PageInfo<Album> findPage(Album album, Integer page, Integer size) {
        PageHelper.startPage(page, size);
        Example example = createExample(album);
        List<Album> albumList = albumMapper.selectByExample(example);
        return new PageInfo<>(albumList);
    }

    /**
     * 构建查询条件
     * @param brand
     * @return
     */
    public Example createExample(Album album){
        Example example = new Example(Album.class);
        Example.Criteria criteria = example.createCriteria(); // 条件构造器
        if(null != album){
            if(!StringUtils.isEmpty(album.getTitle())){
                /**
                 * 1: album 的属性名
                 * 2：占位符参数，搜索的条件
                 */
                criteria.andLike("title", "%" + album.getTitle() + "%");
            }
        }
        return example;
    }
}
