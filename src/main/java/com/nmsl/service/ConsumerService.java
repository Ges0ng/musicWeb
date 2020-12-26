package com.nmsl.service;

import com.nmsl.domain.Consumer;

import java.util.List;

/**
 * 前端用户service
 * @author Paracosm
 */
public interface ConsumerService {
    //增
    boolean insert(Consumer consumer);
    //改
    boolean update(Consumer consumer);
    //删
    boolean delete(Integer id);

    /*根据id查询用户*/
    Consumer selectById(Integer id);

    /*查询所有用户*/
    List<Consumer> selectAllConsumer();

    /*验证密码*/
    boolean verifyPassword(String username, String password);

    /*根据账号查询*/
    Consumer selectByUsername(String username);
}
