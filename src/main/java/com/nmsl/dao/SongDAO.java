package com.nmsl.dao;

import com.nmsl.domain.Song;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface SongDAO {

    //增
    int insert(Song song);
    //改
    int update(Song song);
    //删
    int delete(Integer id);


    Song selectById(Integer id);  //根据id查歌曲
    List<Song> selectAllSong(); //查询所有歌曲
    List<Song> songOfName(String name);   //根据歌名精确查询
    List<Song> likeSongOfName(String name);   //根据歌名模糊查询

    List<Song> songOfSingerId(Integer singerId);//根据歌手id查询

}
