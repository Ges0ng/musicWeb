package com.nmsl.service;

import com.nmsl.domain.SongList;
import org.springframework.stereotype.Service;

import java.util.List;


/**
 * 歌单service接口
 * @author Paracosm
 */
public interface SongListService {
    //增
    boolean insert(SongList song);
    //改
    boolean update(SongList song);
    //删
    boolean delete(Integer id);


    SongList selectById(Integer id);  //根据id查歌曲
    List<SongList> selectAllSongList(); //查询所有歌单
    List<SongList> songOfTitle(String title);//根据标题精确查询
    List<SongList> likeTitle(String title);   //根据标题模糊查询歌单列表
    List<SongList> likeStyle(String style);   //根据风格模糊查询歌单列表
}
