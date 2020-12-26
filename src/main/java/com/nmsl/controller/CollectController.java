package com.nmsl.controller;

import com.alibaba.fastjson.JSONObject;
import com.nmsl.domain.Collect;
import com.nmsl.service.CollectService;
import com.nmsl.utils.Consts;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;


/**
 * @author Paracosm
 *  收藏Controller
 */
@RestController
@Slf4j
@RequestMapping("/collect")
@CrossOrigin
public class CollectController {

    @Resource
    private CollectService collectService;

    /**
     * 收藏
     * @param userId
     * @param type
     * @param songId
     * @return
     */
    @PostMapping("/add")
    public Object addCollect(String userId,String type,String songId){
        JSONObject json = new JSONObject();

        if (songId == null || "".equals(songId)) {
            json.put(Consts.CODE,0);
            json.put(Consts.MSG,"你还没有收藏歌曲喔");
            return json;
        }
        if (collectService.existSongId(Integer.parseInt(userId),Integer.parseInt(songId))){
            json.put(Consts.CODE,2);
            json.put(Consts.MSG,"已经收藏了这个歌曲");
            return json;
        }

        Collect collect = new Collect();
        collect.setUserId(Integer.parseInt(userId));
        collect.setType(new Byte(type));
        collect.setSongId(Integer.parseInt(songId));

        boolean flag = collectService.insert(collect);
        if (flag){
            json.put(Consts.CODE,1);
            json.put(Consts.MSG,"收藏成功!");
        }else{
            json.put(Consts.CODE,0);
            json.put(Consts.MSG,"收藏失败");
        }
        return json;
    }

    /**
     * 取消收藏
     * @param id
     * @return
     */
    @GetMapping("/delete")
    public Object deleteCollect(String id) {
        boolean flag = collectService.delete(Integer.parseInt(id));
        return flag;
    }


    @GetMapping("/deleteCollect")
    public Object deleteCollect(String userId, String songId) {
        boolean flag = collectService.deleteByUserIdSongId(Integer.parseInt(userId),Integer.parseInt(songId));
        return flag;
    }

    /**
     * 查询所有收藏
     * @return
     */
    @GetMapping(value = "/allCollect")
    public Object selectAllCollect(){
        return collectService.selectAllCollect();
    }

    /**
     * 查询某个用户的收藏列表
     * @param userId
     * @return
     */
    @GetMapping("/collectOfUserId")
    public Object collectOfUserId (String userId){
        return collectService.collectOfUserId(Integer.parseInt(userId));
    }

}
