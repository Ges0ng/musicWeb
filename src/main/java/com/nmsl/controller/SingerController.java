package com.nmsl.controller;

import com.alibaba.fastjson.JSONObject;
import com.nmsl.domain.Singer;
import com.nmsl.service.SingerService;
import com.nmsl.utils.Consts;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FilenameUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import sun.net.www.content.image.png;

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
 * @author Paracosm
 *  歌手控制类
 */
@RestController
@Slf4j
@RequestMapping("/singer")
@CrossOrigin
public class SingerController {

    @Resource
    private SingerService singerService;

    @Value("${singerPic.dir}")
    private String realPath;    //到singerPic的路径地址



    /**
     * 添加歌手
     */

    @PostMapping("/add")
    public Object addSinger(String name, String sex, String pic, String birth,
                            String location, String introduction) {
        Singer singer = new Singer();
        JSONObject json = new JSONObject();

        //生日转换成Date格式
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        Date birthDate = new Date();
        try {
            birthDate = dateFormat.parse(birth);


            if (!"".equals(name.trim()) && name.length() > 0 && name != null && !"".equals(sex)
                    && !"".equals(pic) && !"".equals(birth)) {

                singer.setName(name);
                singer.setSex(new Byte(sex.trim()));
                singer.setPic(pic.trim());
                singer.setBirth(birthDate);
                singer.setLocation(location.trim());
                singer.setIntroduction(introduction.trim());

                //判断是否添加
                boolean flag = singerService.insert(singer);

                if (flag) {
                    json.put(Consts.CODE, 1);
                    json.put(Consts.MSG, "添加成功");
                } else {
                    json.put(Consts.CODE, 0);
                    json.put(Consts.MSG, "添加失败");
                }
            }

        } catch (ParseException e) {
            e.printStackTrace();
        }finally{
            return json;
        }

    }

    /**
     * 修改歌手信息
     */


    @PostMapping("/update")
    public Object updateSinger(String id,String name, String sex, String pic, String birth,
                            String location, String introduction) {

        JSONObject json = new JSONObject();
        //生日转换成Date格式
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        Date birthDate = new Date();
        try {
            birthDate = dateFormat.parse(birth);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        //设置属性
        Singer singer = new Singer();
        singer.setId(Integer.parseInt(id));
        singer.setName(name.trim());
        singer.setSex(new Byte(sex.trim()));
        singer.setBirth(birthDate);
        singer.setLocation(location.trim());
        singer.setIntroduction(introduction.trim());
        //判断是否添加
        boolean flag = singerService.update(singer);
        if (flag){
            json.put(Consts.CODE,1);
            json.put(Consts.MSG,"修改成功");
            return json;
        }
        json.put(Consts.CODE,0);
        json.put(Consts.MSG,"修改失败");
        return json;
    }


    /**
     *
     * @param id
     * @return
     */
    @GetMapping("/delete")
    public Object deleteSinger(String id) {

        //删除旧头像
        //先保存之前的路径拿来删除,单纯的文件名
        String singerOldName = singerService.selectById(Integer.parseInt(id)).getPic().substring(14);

        boolean flag = singerService.delete(Integer.parseInt(id));

        if (flag) {
            //在本地删除学生的旧的头像
            File file = new File(realPath,singerOldName);
            if (file.exists() && !"user.png".equals(singerOldName)) {
                file.delete();  //删除头像
            }
        }

       return flag;
    }

    /**
     * 查询单个歌手
     * @param id
     * @return
     */
    @GetMapping("/selectById")
    public Singer selectById(String id){
        return singerService.selectById(Integer.parseInt(id));
    }

    /**
     * 查询所有歌手信息
     * @return
     */
    @GetMapping(value = "/allSinger")
    public Object selectAllSinger(){
        return singerService.selectAllSinger();
    }

    /**
     * 根据歌手名字模糊查询
     * @param name
     * @return
     */
    @GetMapping("/singerOfName")
    public List<Singer> singerOfName(String name){
        return singerService.singerOfName("%"+name.trim()+"%");
    }

    /**
     * 根据歌手名字模糊查询
     * @param sex
     * @return
     */
    @GetMapping("/singerOfSex")
    public List<Singer> singerOfSex(String sex){
        return singerService.singerOfSex(Integer.parseInt(sex));
    }


    /**
     * 更新歌手图片
     */
    /*@PostMapping("/updateSingerpic")
    public Object updateSingerpic(@RequestParam("file") MultipartFile avatorFile, @RequestParam("id") int id) {
        JSONObject json = new JSONObject();
        if (avatorFile.isEmpty()){
            json.put(Consts.CODE,0);
            json.put(Consts.MSG,"更新失败!");
            return json;
        }
        //文件名=时间戳+文件名
        String fileName = System.currentTimeMillis()+avatorFile.getOriginalFilename();
        //文件路径
        String filePath = System.getProperty("user.dir") + System.getProperty("file.separator")
                + "img" + System.getProperty("file.separator") + "singerPic";

        File file = new File(filePath);
        //文件不存在,则创建
        if (!file.exists()){
            file.mkdir();
        }
        //实际的文件地址
        File dest = new File(filePath+System.getProperty("file.separator")+fileName);
        //存储到数据库里的相对文件地址
        String storeAvatorPath = "img/singerPic/" +fileName;

        try {
            //真正的上传过程,有可能发生IO异常.
            avatorFile.transferTo(dest);
            Singer singer = new Singer();
            singer.setId(id);
            singer.setPic(storeAvatorPath);
            boolean flag = singerService.update(singer);
            if (flag) {
                json.put(Consts.CODE,1);
                json.put(Consts.MSG,"更新成功!");
                json.put("pic",storeAvatorPath);
            }
            json.put(Consts.CODE,0);
            json.put(Consts.MSG,"更新失败!");
            json.put("pic",storeAvatorPath);
            return json;
        } catch (IOException e) {
            json.put(Consts.CODE,0);
            json.put(Consts.MSG,"更新失败,原因为:"+e.getMessage());
        }finally {
            return json;
        }
    }*/



    @PostMapping("/updateSingerPic")
    public Object updateSingerpic(@RequestParam("file") MultipartFile avatorFile, @RequestParam("id") int id) {
        JSONObject json = new JSONObject();

        if (avatorFile.isEmpty()){
            json.put(Consts.CODE,0);
            json.put(Consts.MSG,"更新失败!");
            return json;
        }
        //先保存之前的路径拿来删除
        String singerOldName = singerService.selectById(id).getPic().substring(14);

        try {

            //头像如何保存?
            String newFileName = UUID.randomUUID().toString()
                    + "." + FilenameUtils.getExtension(avatorFile.getOriginalFilename());

            avatorFile.transferTo(new File(realPath,newFileName));

            Singer singer = new Singer();
            singer.setId(id);
            singer.setPic("/img/singerPic/"+newFileName);
            boolean flag = singerService.update(singer);
            if (flag) {
                json.put(Consts.CODE,1);
                json.put(Consts.MSG,"更新成功!");
                json.put("pic",newFileName);


                //删除旧头像

                if (singerOldName != null) {
                    //在本地删除学生的旧的头像

                    File file = new File(realPath,singerOldName);
                    if (file.exists() && !"user.png".equals(singerOldName)) {
                        file.delete();  //删除头像
                    }
                }

            }else{
                json.put(Consts.CODE,0);
                json.put(Consts.MSG,"更新失败!");
                json.put("pic",newFileName);
            }
        } catch (IOException e) {
            json.put(Consts.CODE,0);
            json.put(Consts.MSG,"更新失败,原因为:"+e.getMessage());
        }finally {
            return json;
        }

    }


}
