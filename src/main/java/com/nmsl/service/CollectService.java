package com.nmsl.service;

import com.nmsl.domain.Collect;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 收藏功能service
 * @author Paracosm
 */
public interface CollectService {
    //增
    boolean insert(Collect collect);

    //删
    boolean delete(Integer id);


    /**
     * 根据用户id和歌曲id删除
     */
    boolean deleteByUserIdSongId(Integer userId,Integer songId);

    List<Collect> selectAllCollect(); //查询所有收藏
    List<Collect> collectOfUserId(Integer userId);   //查询某个用户的收藏列表
    boolean existSongId(@Param("userId")Integer userId, @Param("songId")Integer songId);//查看用户是否收藏了某歌曲

}
