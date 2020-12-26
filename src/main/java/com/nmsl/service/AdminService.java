package com.nmsl.service;


import com.nmsl.domain.Admin;
import org.springframework.stereotype.Service;

/**
 * @author Paracosm
 */

public interface AdminService {

    /**
     * 验证密码
     */
    boolean verifyPassword(String name, String password);

}
