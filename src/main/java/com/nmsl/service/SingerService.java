package com.nmsl.service;


import com.nmsl.domain.Singer;

import java.util.List;

/**
 * @author Paracosm
 */

public interface SingerService {

    //增
    boolean insert(Singer singer);
    //改
    boolean update(Singer singer);
    //删
    boolean delete(Integer id);


    Singer selectById(Integer id);  //根据id查歌手
    List<Singer> selectAllSinger(); //查询所有歌手
    List<Singer> singerOfName(String name);   //根据歌手名模糊查询
    List<Singer> singerOfSex(Integer sex);//根据性别查询

}
