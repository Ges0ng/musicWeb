package com.nmsl.service.impl;

import com.nmsl.dao.SongListDAO;
import com.nmsl.domain.SongList;
import com.nmsl.service.SongListService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * 歌单service实现类
 * @author Paracosm
 */
@Service
public class SongListServiceImpl implements SongListService {

    @Resource
    private SongListDAO songListDAO;

    @Override
    public boolean insert(SongList songList) {
        return songListDAO.insert(songList)>0;
    }

    @Override
    public boolean update(SongList songList) {
        return songListDAO.update(songList)>0;
    }

    @Override
    public boolean delete(Integer id) {
        return songListDAO.delete(id)>0;
    }

    @Override
    public SongList selectById(Integer id) {
        return songListDAO.selectById(id);
    }

    @Override
    public List<SongList> selectAllSongList() {
        return songListDAO.selectAllSongList();
    }

    @Override
    public List<SongList> songOfTitle(String title) {
        return songListDAO.songOfTitle(title);
    }

    @Override
    public List<SongList> likeTitle(String title) {
        return songListDAO.likeTitle(title);
    }

    @Override
    public List<SongList> likeStyle(String style) {
        return songListDAO.likeStyle(style);
    }
}
