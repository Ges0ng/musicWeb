package com.nmsl.service;

import com.nmsl.domain.ListSong;

import java.util.List;

/**
 * 歌单中歌曲service
 * @author Paracosm
 */
public interface ListSongService {
    //增
    boolean insert(ListSong listSong);
    //改
    boolean update(ListSong listSong);
    /**
     * 删除
     */
    boolean delete(Integer id);
    /**
     * 根据歌曲id和歌单id删除
     */
    boolean deleteBySongIdAndSongListId(Integer songId,Integer songListId);



    ListSong selectById(Integer id);  //根据id查歌曲
    List<ListSong> selectAllListSong(); //查询所有歌单中歌曲
    List<ListSong> listSongOfSongListId(Integer songListId);//根s据歌单id查询所有歌曲
}
