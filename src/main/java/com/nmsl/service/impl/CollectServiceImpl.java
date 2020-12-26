package com.nmsl.service.impl;

import com.nmsl.dao.CollectDAO;
import com.nmsl.domain.Collect;
import com.nmsl.service.CollectService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

@Service
public class CollectServiceImpl implements CollectService {

    @Resource
    private CollectDAO collectDAO;

    @Override
    public boolean insert(Collect collect) {
        return collectDAO.insert(collect) > 0;
    }

    @Override
    public boolean delete(Integer id) {
        return collectDAO.delete(id) > 0;
    }

    @Override
    public List<Collect> selectAllCollect() {
        return collectDAO.selectAllCollect();
    }

    @Override
    public List<Collect> collectOfUserId(Integer userId) {
        return collectDAO.collectOfUserId(userId);
    }

    @Override
    public boolean existSongId(Integer userId,Integer songId) {
        return collectDAO.existSongId(userId, songId) > 0;
    }

    @Override
    public boolean deleteByUserIdSongId(Integer userId, Integer songId) {
        return collectDAO.deleteByUserIdSongId(userId, songId) > 0;
    }
}
