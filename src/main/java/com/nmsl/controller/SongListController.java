package com.nmsl.controller;

import com.alibaba.fastjson.JSONObject;
import com.nmsl.domain.SongList;
import com.nmsl.service.SongListService;
import com.nmsl.utils.Consts;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FilenameUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.UUID;

/**
 * @author Paracosm
 *  歌单控制类
 */
@RestController
@Slf4j
@RequestMapping("/songList")
@CrossOrigin
public class SongListController {

    @Resource
    private SongListService songListService;

    @Value("${songListPic.dir}")
    private String songListPicPath;


    /**
     * 添加歌单
     */
    @PostMapping("/add")
    public Object addSongList(String title , String pic, String introduction,String style) {

        JSONObject json = new JSONObject();
        SongList songList = new SongList();
        songList.setTitle(title.trim());
        songList.setPic("/img/songListPic/"+pic.trim());
        songList.setIntroduction(introduction.trim());
        songList.setStyle(style.trim());

        //判断是否添加成功
        boolean flag = songListService.insert(songList);
        if (flag) {
            json.put(Consts.CODE, 1);
            json.put(Consts.MSG, "添加成功");
        } else {
            json.put(Consts.CODE, 0);
            json.put(Consts.MSG, "添加失败");
        }
        return json;
    }


    /**
     * 修改歌单信息
     */
    @PostMapping("/update")
    public Object updateSongList(String id,String title,String introduction,String style) {
        JSONObject json = new JSONObject();
        //设置属性
        SongList songList = new SongList();
        songList.setId(Integer.parseInt(id));
        songList.setTitle(title.trim());
        songList.setIntroduction(introduction.trim());
        songList.setStyle(style.trim());
        //判断是否添加
        boolean flag = songListService.update(songList);
        if (flag){
            json.put(Consts.CODE,1);
            json.put(Consts.MSG,"修改成功");
        }else{
            json.put(Consts.CODE,0);
            json.put(Consts.MSG,"修改失败");
        }
        return json;
    }


    /**
     * 删除歌单
     * @param id
     * @return
     */
    @GetMapping("/delete")
    public Object deleteSongList(String id) {
        String songListOldPic = songListService.selectById(Integer.parseInt(id)).getPic().substring(17);

        boolean flag = songListService.delete(Integer.parseInt(id));
        if (flag){
            if (songListOldPic != null) {
                //在本地删除旧的头像
                File file = new File(songListPicPath,songListOldPic);
                if (file.exists() && !"1.jpg".equals(songListOldPic)) {
                    file.delete();  //删除头像
                }
            }
        }

       return flag;
    }

    /**
     * 查询单个歌单
     * @param id
     * @return
     */
    @GetMapping("/selectById")
    public SongList selectById(String id){
        return songListService.selectById(Integer.parseInt(id));
    }

    /**
     * 查询所有歌单信息
     * @return
     */
    @GetMapping(value = "/allSongList")
    public Object selectAllSongList(){
        return songListService.selectAllSongList();
    }

    /**
     * 根据歌单标题
     * @param title
     * @return
     */
    @GetMapping("/songOfTitle")
    public List<SongList> songOfTitle(String title){
        return songListService.songOfTitle(title);
    }

    /**
     * 根据歌单标题模糊查询
     * @param title
     * @return
     */
    @GetMapping("/likeTitle")
    public List<SongList> likeTitle(String title){
        return songListService.likeTitle("%"+title.trim()+"%");
    }

    /**
     * 根据风格模糊查询
     * @param style
     * @return
     */
    @GetMapping("/likeStyle")
    public List<SongList> likeStyle(String style){
        return songListService.likeStyle("%"+style.trim()+"%");
    }



    @PostMapping("/updateSongListPic")
    public Object updateSongListPic(@RequestParam("file") MultipartFile avatorFile, @RequestParam("id") int id) {
        JSONObject json = new JSONObject();

        if (avatorFile.isEmpty()){
            json.put(Consts.CODE,0);
            json.put(Consts.MSG,"更新失败!");
            return json;
        }

        //先保存之前的路径拿来删除
        String songListOldPic = songListService.selectById(id).getPic().substring(17);
        System.out.println(songListPicPath+"=================="+songListOldPic);
        try {
            //创建文件
            String newFileName = UUID.randomUUID().toString()
                    + "." + FilenameUtils.getExtension(avatorFile.getOriginalFilename());
            avatorFile.transferTo(new File(songListPicPath,newFileName));

            SongList songList = new SongList();
            songList.setId(id);
            songList.setPic("/img/songListPic/"+newFileName);
            boolean flag = songListService.update(songList);
            if (flag) {

                json.put(Consts.CODE,1);
                json.put(Consts.MSG,"更新成功!");
                json.put("pic",newFileName);
            }else{
                json.put(Consts.CODE,0);
                json.put(Consts.MSG,"更新失败!");
                json.put("pic",newFileName);
            }
        } catch (IOException e) {
            json.put(Consts.CODE,0);
            json.put(Consts.MSG,"更新失败,原因为:"+e.getMessage());
        }finally {
            if (songListOldPic != null) {
                //在本地删除旧的头像
                File file = new File(songListPicPath,songListOldPic);
                if (file.exists() && !"1.jpg".equals(songListOldPic)) {
                    file.delete();  //删除头像
                }
            }
            return json;
        }

    }


}
