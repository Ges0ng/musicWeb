package com.nmsl.controller;

import com.alibaba.fastjson.JSONObject;
import com.nmsl.domain.ListSong;
import com.nmsl.domain.Song;
import com.nmsl.service.ListSongService;
import com.nmsl.utils.Consts;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import javax.annotation.Resource;
import java.io.File;
import java.io.IOException;

/**
 * 歌单的歌曲管理
 */
@RestController
@RequestMapping("/listSong")
public class ListSongController {

    @Resource
    private ListSongService listSongService;
    /**
     * 添加歌曲 (传统方式)
     *
     * @return
     */
    @PostMapping("/add")
    public Object addListSong(String songId,String songListId) {
        JSONObject json = new JSONObject();

        ListSong listSong = new ListSong();

        listSong.setSongId(Integer.parseInt(songId));
        listSong.setSongListId(Integer.parseInt(songListId));

        boolean flag = listSongService.insert(listSong);
        if (flag) {
            json.put(Consts.CODE, 1);
            json.put(Consts.MSG, "添加成功!");
        } else {
            json.put(Consts.CODE, 0);
            json.put(Consts.MSG, "添加失败!");
        }
        return json;
    }

    /**
     * 根据歌单的id查询歌曲
     */
    @GetMapping("/detail")
    public Object listSongOfSongListId(String songListId){
        return  listSongService.listSongOfSongListId(Integer.parseInt(songListId));
    }


    /**
     * 删除歌单歌曲
     */
    @GetMapping("/delete")
    public Object deleteListSong(String songId,String songListId) {
        JSONObject json = new JSONObject();
        boolean flag = listSongService.deleteBySongIdAndSongListId(Integer.parseInt(songId),Integer.parseInt(songListId));
        if (flag){
            json.put(Consts.CODE,1);
            json.put(Consts.MSG,"删除成功");
        }else {
            json.put(Consts.CODE,0);
            json.put(Consts.MSG,"删除失败");
        }
        return json;
    }
}
