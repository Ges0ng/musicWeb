package com.nmsl.dao;

import com.nmsl.domain.ListSong;
import org.apache.ibatis.annotations.Mapper;
import java.util.List;

/**
 * 歌单中歌曲
 * @author Paracosm
 */
@Mapper
public interface ListSongDAO {

    //增
    int insert(ListSong listSong);
    //改
    int update(ListSong listSong);
    /**
     * 删除
     */
     int delete(Integer id);

    /**
     * 根据歌曲id和歌单id删除
     */
     int deleteBySongIdAndSongListId(Integer songId,Integer songListId);



    ListSong selectById(Integer id);  //根据id查歌曲
    List<ListSong> selectAllListSong(); //查询所有歌单中歌曲
    List<ListSong> listSongOfSongListId(Integer songListId);//根据歌单id查询所有歌曲

}
