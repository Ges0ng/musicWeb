package com.nmsl.service.impl;

import com.nmsl.dao.SingerDAO;
import com.nmsl.domain.Singer;
import com.nmsl.service.SingerService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

@Service
public class SingerServiceImpl implements SingerService {

    @Resource
    private SingerDAO singerDAO;

    @Override
    public boolean insert(Singer singer) {
        //如果返回数值大于0说明添加成功
        return singerDAO.insert(singer) > 0;
    }

    @Override
    public boolean update(Singer singer) {
        return singerDAO.update(singer) > 0;
    }

    @Override
    public boolean delete(Integer id) {
        return singerDAO.delete(id) > 0;
    }

    @Override
    public Singer selectById(Integer id) {
        return singerDAO.selectById(id);
    }

    @Override
    public List<Singer> selectAllSinger() {
        return singerDAO.selectAllSinger();
    }

    @Override
    public List<Singer> singerOfName(String name) {
        return singerDAO.singerOfName(name);
    }

    @Override
    public List<Singer> singerOfSex(Integer sex) {
        return singerDAO.singerOfSex(sex);
    }
}
