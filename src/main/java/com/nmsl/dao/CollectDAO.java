package com.nmsl.dao;

import com.nmsl.domain.Collect;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 收藏功能 Dao
 * @author Paracosm
 */
@Mapper
public interface CollectDAO {
    //增
    int insert(Collect collect);

    //删
    int delete(Integer id);

    List<Collect> selectAllCollect(); //查询所有收藏
    List<Collect> collectOfUserId(Integer userId);   //查询某个用户的收藏列表
    int existSongId(@Param("userId")Integer userId, @Param("songId")Integer songId);//查看用户是否收藏了某歌曲

    /**
     * 根据用户id和歌曲id删除
     */
    int deleteByUserIdSongId(@Param("userId")Integer userId, @Param("songId")Integer songId);

}
