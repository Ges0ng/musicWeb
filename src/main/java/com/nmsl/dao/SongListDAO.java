package com.nmsl.dao;

import com.nmsl.domain.SongList;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface SongListDAO {


    //增
    int insert(SongList songList);
    //改
    int update(SongList songList);
    //删
    int delete(Integer id);


    SongList selectById(Integer id);  //根据id查歌曲
    List<SongList> selectAllSongList(); //查询所有歌单
    List<SongList> songOfTitle(String title);//根据标题精确查询
    List<SongList> likeTitle(String title);   //根据标题模糊查询歌单列表
    List<SongList> likeStyle(String style);   //根据风格模糊查询歌单列表
}
