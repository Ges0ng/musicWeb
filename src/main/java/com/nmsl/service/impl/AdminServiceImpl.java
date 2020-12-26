package com.nmsl.service.impl;

import com.nmsl.dao.AdminDAO;
import com.nmsl.domain.Admin;
import com.nmsl.service.AdminService;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

import javax.annotation.Resource;

/**
 * @author Paracosm
 */
@Service

public class AdminServiceImpl implements AdminService {

    @Resource
    private AdminDAO adminDAO;

    @Override
    public boolean verifyPassword(String name, String password) {
            //如果返回值大于0,则说明有用户密码正确
        return adminDAO.verifyPassword(name, password) > 0;
    }
}
