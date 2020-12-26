package com.nmsl.controller;

import com.alibaba.fastjson.JSONObject;
import com.nmsl.domain.Singer;
import com.nmsl.domain.Song;
import com.nmsl.service.SongService;
import com.nmsl.utils.Consts;
import org.apache.commons.io.FilenameUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.io.IOException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.UUID;

/**
 * 歌曲管理
 */
@RestController
@RequestMapping("/song")
public class SongController {

    @Resource
    private SongService songService;

    @Value("${song.dir}")
    private String realPath;

    @Value("${songPic.dir}")
    private String songPicPath;

    /**
     * 添加歌曲 (传统方式)
     *
     * @return
     */
    @PostMapping("/add")
    public Object addSong(HttpServletRequest request, @RequestParam("file") MultipartFile mpFile) {
        Song song = new Song();
        JSONObject json = new JSONObject();
        String singerId = request.getParameter("singerId").trim();  //所属歌手id
        String name = request.getParameter("name").trim();          //歌名
        String introduction = request.getParameter("introduction").trim();          //简介
        String pic = "songLogo.jpg";                    //默认图片
        String lyric = request.getParameter("lyric").trim();     //歌词

        //上传歌曲文件
        if (mpFile.isEmpty()) {
            json.put(Consts.CODE, 0);
            json.put(Consts.MSG, "歌曲上传失败!");
            return json;
        }

        try {
            //文件完整名称
            String songFullPath = singerId +"-"+ mpFile.getOriginalFilename();
                    //+ "." + FilenameUtils.getExtension(mpFile.getOriginalFilename());
            System.out.println(songFullPath);
            //保存文件！
            mpFile.transferTo(new File(realPath, songFullPath));


            song.setSingerId(Integer.parseInt(singerId));
            song.setName(name);
            song.setIntroduction(introduction);
            song.setPic("/img/songPic/"+pic);
            song.setLyric(lyric);
            song.setUrl(songFullPath);

            boolean flag = songService.insert(song);
            if (flag) {
                json.put(Consts.CODE, 1);
                json.put(Consts.MSG, "上传成功!");
                json.put("avator", songFullPath);
            } else {
                json.put(Consts.CODE, 0);
                json.put(Consts.MSG, "上传失败!");
                json.put("avator", songFullPath);
            }
        } catch (IOException e) {
            json.put(Consts.CODE, 0);
            json.put(Consts.MSG, "上传失败,原因为:" + e.getMessage());
        } finally {
            return json;
        }
    }

    /**
     * 根据歌手id查询歌曲
     */
    @GetMapping("/singer/detail")
    public Object songerOfSingId(String singerId){
        return  songService.songOfSingerId(Integer.parseInt(singerId));
    }

    /**
     * 修改歌曲信息
     */
    @PostMapping("/update")
    public Object updateSinger(String id, String name, String introduction, String lyric) {
        JSONObject json = new JSONObject();
        Song song = new Song();
        //设置属性
        song.setId(Integer.parseInt(id));
        song.setName(name.trim());
        song.setIntroduction(introduction);
        song.setLyric(lyric);
        //判断是否添加
        boolean flag = songService.update(song);
        if (flag){
            json.put(Consts.CODE,1);
            json.put(Consts.MSG,"修改成功");
        }else {
            json.put(Consts.CODE,0);
            json.put(Consts.MSG,"修改失败");
        }
        return json;
    }


    /**
     * 删除歌曲
     */
    @GetMapping("/delete")
    public Object deleteSinger(String id) {
        JSONObject json = new JSONObject();
        boolean flag = songService.delete(Integer.parseInt(id));
        if (flag){
            json.put(Consts.CODE,1);
            json.put(Consts.MSG,"删除成功");
        }else {
            json.put(Consts.CODE,0);
            json.put(Consts.MSG,"删除失败");
        }
        return json;
    }


    /**
     * 更新歌曲图片
     * @param avatorFile
     * @param id
     * @return
     */
    @PostMapping("/updateSongPic")
    public Object updateSongpic(@RequestParam("file") MultipartFile avatorFile, @RequestParam("id") int id) {
        JSONObject json = new JSONObject();

        if (avatorFile.isEmpty()){
            json.put(Consts.CODE,0);
            json.put(Consts.MSG,"更新失败!");
            return json;
        }
        //先保存之前的路径拿来删除
        String songOldName = songService.selectById(id).getPic().substring(13);
        String oldSingerId = songService.selectById(id).getName();
        try {


            //文件完整名称
            String songFullPath = oldSingerId +"-"+ avatorFile.getOriginalFilename();

            System.out.println("完整音乐文件路径"+songFullPath);
            //保存文件(地址，文件名)
            avatorFile.transferTo(new File(songPicPath, songFullPath));

            Song song = new Song();
            song.setId(id);
            song.setPic("/img/songPic/"+songFullPath);
            boolean flag = songService.update(song);
            if (flag) {
                json.put(Consts.CODE,1);
                json.put(Consts.MSG,"更新成功!");
                json.put("pic",songFullPath);


                //删除旧头像

                if (songOldName != null) {
                    //在本地删除歌曲的旧的头像
                    File file = new File(songPicPath,songOldName);
                    if (file.exists()) {
                        file.delete();  //删除头像
                    }
                }

            }else{
                json.put(Consts.CODE,0);
                json.put(Consts.MSG,"更新失败!");
                json.put("pic",songFullPath);
            }
        } catch (IOException e) {
            json.put(Consts.CODE,0);
            json.put(Consts.MSG,"更新失败,原因为:"+e.getMessage());
        }finally {
            return json;
        }

    }


    /**
     * 更新歌曲
     * @param avatorFile
     * @param id
     * @return
     */
    @PostMapping("/updateSongUrl")
    public Object updateSongUrl(@RequestParam("file") MultipartFile avatorFile, @RequestParam("id") int id) {
        JSONObject json = new JSONObject();

        if (avatorFile.isEmpty()){
            json.put(Consts.CODE,0);
            json.put(Consts.MSG,"更新失败!");
            return json;
        }
        //先保存之前的路径拿来删除
        String songOldUrl = songService.selectById(id).getUrl();
        try {
            //文件完整名称
            String songFullPath = avatorFile.getOriginalFilename();
            System.out.println("完整音乐文件路径"+songFullPath);
            //保存文件(地址，文件名)
            avatorFile.transferTo(new File(realPath, songFullPath));

            Song song = new Song();
            song.setId(id);
            song.setUrl(songFullPath);
            boolean flag = songService.update(song);
            if (flag) {
                json.put(Consts.CODE,1);
                json.put(Consts.MSG,"更新成功!");
                json.put("pic",songFullPath);


                //删除旧歌曲
                if (songOldUrl != null) {

                    File file = new File(realPath,songOldUrl);
                    if (file.exists()) {
                        file.delete();
                    }
                }

            }else{
                json.put(Consts.CODE,0);
                json.put(Consts.MSG,"更新失败!");
                json.put("pic",songFullPath);
            }
        } catch (IOException e) {
            json.put(Consts.CODE,0);
            json.put(Consts.MSG,"更新失败,原因为:"+e.getMessage());
        }finally {
            return json;
        }

    }


    /**
     * 根据歌曲id查询歌曲
     */
    @GetMapping("/songOfSongId")
    public Object songOfSongId(String id){
        return  songService.selectById(Integer.parseInt(id));
    }


    /**
     * 根据歌曲名字精确查询歌曲
     */
    @GetMapping("/songOfSongName")
    public Object songOfSongName(String songName){
        return  songService.songOfName(songName);
    }

    /**
     * 根据歌曲名字模糊查询歌曲
     */
    @GetMapping("/likeSongOfName")
    public Object likeSongOfName(String songName){
        return  songService.likeSongOfName(songName);
    }

    //查询所有歌曲
    @GetMapping("/allSong")
    public Object allSong(){
        return  songService.selectAllSong();
    }

}
