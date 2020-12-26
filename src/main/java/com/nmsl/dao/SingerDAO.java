package com.nmsl.dao;

import com.nmsl.domain.Singer;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * @author Paracosm
 */
@Mapper
public interface SingerDAO {
    //增
    int insert(Singer singer);
    //改
    int update(Singer singer);
    //删
    int delete(Integer id);


    Singer selectById(Integer id);  //根据id查歌手
    List<Singer> selectAllSinger(); //查询所有歌手
    List<Singer> singerOfName(String name);   //根据歌手名模糊查询
    List<Singer> singerOfSex(Integer sex);//根据性别查询

}
