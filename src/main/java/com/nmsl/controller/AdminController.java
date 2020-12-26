package com.nmsl.controller;

import com.alibaba.fastjson.JSONObject;
import com.nmsl.service.AdminService;
import com.nmsl.utils.Consts;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.annotations.Param;
import org.springframework.web.bind.annotation.*;
import javax.annotation.Resource;
import javax.servlet.http.HttpSession;


/**
 * @author Paracosm
 * 管理员后台控制类
 */
@RestController
@Slf4j
@RequestMapping("/admin")
public class AdminController {

    @Resource
    private AdminService adminService;

    /**
     * 判断是否登陆成功
     */
   @PostMapping(value = "/login/status")
    public Object loginStatus(@Param("username") String name, String password, HttpSession session) {
        JSONObject json = new JSONObject();

            boolean flag = adminService.verifyPassword(name, password);
            //成功
                if (flag) {
                    json.put(Consts.CODE, 1);
                    //json.put(Consts.MSG, name+" 登陆成功！");

                    json.put(Consts.MSG, Consts.TIMETHREE() + name);
                    session.setAttribute(Consts.NAME, name);
                    return json;
                }
            //失败
            json.put(Consts.CODE,0);
            json.put(Consts.MSG,"登陆失败，用户名或密码不正确！");
        return json;
    }


}
