/*
package com.nmsl.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

*/
/**
 * 定位歌手头像地址
 * @author Paracosm
 *//*

@Configuration
public class FileConfig implements WebMvcConfigurer {



    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
            //相对路径映射 因为不重新部署，这个就会显示404
        registry.addResourceHandler("/img/singerPic/**").addResourceLocations(
                "file:" + System.getProperty("user.dir") + System.getProperty("file.separator")
                        + "img" + System.getProperty("file.separator") + "singerPic" + System.getProperty("file.separator")
        );

        registry.addResourceHandler("/img/songPic/**").addResourceLocations(
                "file:" + System.getProperty("user.dir") + System.getProperty("file.separator")
                        + "img" + System.getProperty("file.separator") + "songPic" + System.getProperty("file.separator")
        );

        registry.addResourceHandler("/img/songListPic/**").addResourceLocations(
                "file:" + System.getProperty("user.dir") + System.getProperty("file.separator")
                        + "img" + System.getProperty("file.separator") + "songListPic" + System.getProperty("file.separator")
        );

        registry.addResourceHandler("/song/**").addResourceLocations(
                "file:" + System.getProperty("user.dir") + System.getProperty("file.separator")
                        + "song" + System.getProperty("file.separator")
        );

        //绝对路径映射 ，这个不重新部署就可以回显，因为映射你的本地
        registry.addResourceHandler("/img/singerPic/**").addResourceLocations("file:C:\\Users\\2020\\Project\\music\\src\\main\\resources\\static\\img\\singerPic");
        registry.addResourceHandler("/img/songPic/**").addResourceLocations("file:C:\\Users\\2020\\Project\\music\\src\\main\\resources\\static\\img\\songPic");
        registry.addResourceHandler("/img/songListPic/**").addResourceLocations("file:C:\\Users\\2020\\Project\\music\\src\\main\\resources\\static\\img\\songListPic");
        registry.addResourceHandler("/song/**").addResourceLocations("file:C:\\Users\\2020\\Project\\music\\src\\main\\resources\\static\\song");

    }
}
*/
