package com.nmsl.service.impl;

import com.nmsl.dao.ConsumerDAO;
import com.nmsl.domain.Consumer;
import com.nmsl.service.ConsumerService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

@Service
public class ConsumerServiceImpl implements ConsumerService {

    @Resource
    private ConsumerDAO consumerDAO;

    @Override
    public boolean insert(Consumer consumer) {
        return consumerDAO.insert(consumer) > 0;
    }

    @Override
    public boolean update(Consumer consumer) {
        return consumerDAO.update(consumer) > 0;
    }

    @Override
    public boolean delete(Integer id) {
        return consumerDAO.delete(id) > 0;
    }

    @Override
    public Consumer selectById(Integer id) {
        return consumerDAO.selectById(id);
    }

    @Override
    public List<Consumer> selectAllConsumer() {
        return consumerDAO.selectAllConsumer();
    }

    @Override
    public boolean verifyPassword(String username, String password) {   //验证密码是否正确
        return consumerDAO.verifyPassword(username, password) > 0;
    }

    @Override
    public Consumer selectByUsername(String username) {
        return consumerDAO.selectByUsername(username);
    }
}
