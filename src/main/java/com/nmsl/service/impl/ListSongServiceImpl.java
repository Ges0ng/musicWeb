package com.nmsl.service.impl;

import com.nmsl.dao.ListSongDAO;
import com.nmsl.domain.ListSong;
import com.nmsl.service.ListSongService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * 歌单中的歌曲service实现类
 * @author Paracosm
 */
@Service
public class ListSongServiceImpl implements ListSongService {

    @Resource
    private ListSongDAO listSongDAO;

    @Override
    public boolean insert(ListSong listSong) {
        return listSongDAO.insert(listSong) > 0;
    }

    @Override
    public boolean update(ListSong listSong) {
        return listSongDAO.update(listSong) > 0;
    }

    @Override
    public boolean delete(Integer id) {
        return listSongDAO.delete(id) > 0;
    }

    @Override
    public boolean deleteBySongIdAndSongListId(Integer songId,Integer songListId){
        return listSongDAO.deleteBySongIdAndSongListId(songId, songListId) > 0;
    }

    @Override
    public ListSong selectById(Integer id) {
        return listSongDAO.selectById(id);
    }

    @Override
    public List<ListSong> selectAllListSong() {
        return listSongDAO.selectAllListSong();
    }

    @Override
    public List<ListSong> listSongOfSongListId(Integer songListId) {
        return listSongDAO.listSongOfSongListId(songListId);
    }
}
