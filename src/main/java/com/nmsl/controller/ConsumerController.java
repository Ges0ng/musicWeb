package com.nmsl.controller;

import com.alibaba.fastjson.JSONObject;
import com.nmsl.domain.Consumer;
import com.nmsl.service.ConsumerService;
import com.nmsl.utils.Consts;
import com.nmsl.utils.VerifyCodeUtils;
import com.sun.xml.messaging.saaj.util.ByteOutputStream;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FilenameUtils;
import org.springframework.util.Base64Utils;
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
import org.springframework.beans.factory.annotation.Value;
/**
 * @author Paracosm
 *  歌手控制类
 */
@RestController
@Slf4j
@RequestMapping("/consumer")
@CrossOrigin
public class ConsumerController {

    @Resource
    private ConsumerService consumerService;

    @Value("${AvatorImages.dir}")
    private String AvatorImagesPath;





    /**
     * 登录
     */

    @PostMapping("/login")
    public Object login(String username, String password) {

        JSONObject json = new JSONObject();
        if (username==null||username.equals("")){
            json.put(Consts.CODE, 0);
            json.put(Consts.MSG, "用户名不能为空");
            return json;
        }
        if (password==null||password.equals("")){
            json.put(Consts.CODE, 0);
            json.put(Consts.MSG, "密码不能为空");
            return json;
        }

        boolean flag = consumerService.verifyPassword(username, password);

        if (flag) {
            json.put(Consts.CODE, 1);
            json.put(Consts.MSG, "登录成功");
            json.put("userMsg", consumerService.selectByUsername(username));
        } else {
            json.put(Consts.CODE, 0);
            json.put(Consts.MSG, "登陆失败,用户名或密码错误");
        }
        return json;


    }
    
    
    /**
     * 添加用户
     */

    @PostMapping("/add")
    public Object addConsumer(String username, String password, String sex,
                            String phoneNum, String email, String birth,
                            String introduction, String location, String avator) {

        JSONObject json = new JSONObject();
        if (username==null||username.equals("")){
            json.put(Consts.CODE, 0);
            json.put(Consts.MSG, "用户名不能为空");
            return json;
        }

        Consumer consumers = consumerService.selectByUsername(username);
        if (consumers!=null){
            json.put(Consts.CODE, 0);
            json.put(Consts.MSG, "注册失败,用户已存在");
            return json;
        }

        if (password==null||password.equals("")){
            json.put(Consts.CODE, 0);
            json.put(Consts.MSG, "密码不能为空");
            return json;
        }

        //把生日转换成Date格式
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        Date birthDate = new Date();
        try {
            birthDate = dateFormat.parse(birth);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        Consumer consumer = new Consumer();
        consumer.setUsername(username);
        consumer.setPassword(password);
        consumer.setSex(new Byte(sex));
        consumer.setPhoneNum(phoneNum);
        consumer.setEmail(email);
        consumer.setBirth(birthDate);
        consumer.setIntroduction(introduction);
        consumer.setLocation(location);
        consumer.setAvator("/img/AvatorImages/"+avator);

        boolean flag = consumerService.insert(consumer);
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
     * 修改前端用户
     */


    @PostMapping("/update")
    public Object updateConsumer(String id,String username, String password, String sex,
                                 String phoneNum, String email, String birth,
                                 String introduction, String location, String avator) {

        JSONObject json = new JSONObject();
        //判断
        if (username==null||username.equals("")){
            json.put(Consts.CODE, 0);
            json.put(Consts.MSG, "用户名不能为空");
            return json;
        }else if (password==null||password.equals("")){
            json.put(Consts.CODE, 0);
            json.put(Consts.MSG, "密码不能为空");
            return json;
        }

        //生日转换成Date格式
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        Date birthDate = new Date();
        try {
            birthDate = dateFormat.parse(birth);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        //保存对象
        Consumer consumer = new Consumer();
        consumer.setId(Integer.parseInt(id));
        consumer.setUsername(username);
        consumer.setPassword(password);
        consumer.setSex(new Byte(sex));
        consumer.setPhoneNum(phoneNum);
        consumer.setEmail(email);
        consumer.setBirth(birthDate);
        consumer.setIntroduction(introduction);
        consumer.setLocation(location);
        //判断是否添加
        boolean flag = consumerService.update(consumer);
        if (flag) {
            json.put(Consts.CODE, 1);
            json.put(Consts.MSG, "修改成功");
        } else {
            json.put(Consts.CODE, 0);
            json.put(Consts.MSG, "修改失败");
        }
        return json;
    }


    /**
     *
     * @param id
     * @return
     */
    @GetMapping("/delete")
    public Object deleteConsumer(String id) {
        JSONObject json = new JSONObject();
        //先保存之前的路径拿来删除
        String avatorOldPath = consumerService.selectById(Integer.parseInt(id)).getAvator().substring(18);
        boolean flag = consumerService.delete(Integer.parseInt(id));

        if (flag) {
            //在本地删除该用户的旧信息
            File file = new File(AvatorImagesPath,avatorOldPath);
            if (file.exists() && !"user.png".equals(avatorOldPath)) {
                file.delete();  //删除头像
            }
            json.put(Consts.CODE, 1);
            json.put(Consts.MSG, "删除成功！");
        }else{
            json.put(Consts.CODE, 0);
            json.put(Consts.MSG, "删除失败~");
        }
       return json;
    }

    /**
     * 查询单个用户
     * @param id
     * @return
     */
    @GetMapping("/selectById")
    public Consumer selectById(String id){
        return consumerService.selectById(Integer.parseInt(id));
    }

    /**
     * 查询所有用户信息
     * @return
     */
    @GetMapping(value = "/allConsumer")
    public List<Consumer> allConsumer(){
        return consumerService.selectAllConsumer();
    }


    @PostMapping("/updateConsumerAvator")
    public Object updateConsumerAvator(@RequestParam("file") MultipartFile avatorFile, @RequestParam("id") int id) {
        JSONObject json = new JSONObject();

        if (avatorFile.isEmpty()){
            json.put(Consts.CODE,0);
            json.put(Consts.MSG,"更新失败!");
            return json;
        }


        try {
            //先保存之前的路径拿来删除
            String avatorOldPath = consumerService.selectById(id).getAvator().substring(18);
            //头像生成
            String newFileName = UUID.randomUUID().toString()
                    + "." + FilenameUtils.getExtension(avatorFile.getOriginalFilename());

            avatorFile.transferTo(new File(AvatorImagesPath,newFileName));

            Consumer consumer = new Consumer();
            consumer.setId(id);
            consumer.setAvator("/img/AvatorImages/"+newFileName);
            boolean flag = consumerService.update(consumer);
            if (flag) {
                json.put(Consts.CODE,1);
                json.put(Consts.MSG,"更新成功!");
                json.put("pic",newFileName);

                //删除头像  暂不删除，前端有bug
                //avator(avatorOldPath);
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


    /**
     * 生成验证码图片
     */
    @GetMapping("/getImage")
    public String getImageCode(HttpServletRequest request) throws IOException {
        //1.使用工具类生成验证码. 4位数的验证码
        String code = VerifyCodeUtils.generateVerifyCode(4);
        //2.拿去和用户输入的验证码做比对,所以要存入到servletContext(最大的作用域).日后可以存入到redis中
        request.getServletContext().setAttribute("code",code);

        //3.将图片转为base64处理.

        //创建一个内存中的输出流
        ByteOutputStream byteOutputStream = new ByteOutputStream();
        VerifyCodeUtils.outputImage(100,30,byteOutputStream,code);
        String base64 = "data:image/png;base64," + Base64Utils.encodeToString(byteOutputStream.toByteArray());
        return base64;
    }

    public void avator(String avatorOldPath){
        //如果更新成功，删除旧头像
        if (avatorOldPath != null) {
            //在本地删除该用户的旧头像
            File file = new File(AvatorImagesPath,avatorOldPath);
            if (file.exists() && !"user.png".equals(avatorOldPath)) {
                //延迟删除，前端修改头像有bug。Header的缓存不能及时更新会导致图片无法读取
                    file.delete();  //删除头像
            }
        }
    }

}
