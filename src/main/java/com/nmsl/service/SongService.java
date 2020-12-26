package com.nmsl.service;


import com.nmsl.domain.Singer;
import com.nmsl.domain.Song;

import java.util.List;

/**
 * 歌曲service接口
 * @author Paracosm
 */
public interface SongService {

    //增
    boolean insert(Song song);
    //改
    boolean update(Song song);
    //删
    boolean delete(Integer id);


    Song selectById(Integer id);  //根据id查歌曲
    List<Song> selectAllSong(); //查询所有歌曲
    List<Song> songOfName(String name);   //根据歌名精确查询
    List<Song> likeSongOfName(String name);   //根据歌名模糊查询
    List<Song> songOfSingerId(Integer singerId);//根据歌手id查询
}
