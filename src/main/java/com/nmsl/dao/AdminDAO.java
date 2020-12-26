package com.nmsl.dao;

import com.nmsl.domain.Admin;
import org.apache.ibatis.annotations.Mapper;

/**
 * @author Paracosm
 */
@Mapper
public interface AdminDAO {
    /**
     * 验证密码
     * */

    int verifyPassword(String name,String password);
}
