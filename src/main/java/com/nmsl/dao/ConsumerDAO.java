package com.nmsl.dao;

import com.nmsl.domain.Consumer;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * 前端用户管理
 * @author Paracosm
 */
@Mapper
public interface ConsumerDAO {
    //增
    int insert(Consumer consumer);
    //改
    int update(Consumer consumer);
    //删
    int delete(Integer id);

    /*根据id查询用户*/
    Consumer selectById(Integer id);

    /*查询所有用户*/
    List<Consumer> selectAllConsumer();

    /*修改密码*/
    int verifyPassword(String username,String password);

    /*根据账号查询*/
    Consumer selectByUsername(String username);


}
