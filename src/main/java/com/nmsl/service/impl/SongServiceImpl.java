package com.nmsl.service.impl;

import com.nmsl.dao.SongDAO;
import com.nmsl.domain.Singer;
import com.nmsl.domain.Song;
import com.nmsl.service.SongService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * @author Paracosm
 */
@Service
public class SongServiceImpl implements SongService {

    @Resource
    private SongDAO songDAO;

    @Override
    public boolean insert(Song song) {
        return songDAO.insert(song) > 0;
    }

    @Override
    public boolean update(Song song) {
        return songDAO.update(song) > 0;
    }

    @Override
    public boolean delete(Integer id) {
        return songDAO.delete(id) > 0;
    }

    @Override
    public Song selectById(Integer id) {
        return songDAO.selectById(id);
    }

    @Override
    public List<Song> selectAllSong() {
        return songDAO.selectAllSong();
    }

    @Override
    public List<Song> songOfName(String name) {
        return songDAO.songOfName(name);
    }

    @Override
    public List<Song> likeSongOfName(String name) { //模糊查询
        return songDAO.likeSongOfName(name);
    }

    @Override
    public List<Song> songOfSingerId(Integer singerId) {
        return songDAO.songOfSingerId(singerId);
    }
}
