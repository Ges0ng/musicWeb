package com.nmsl.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

/**
 * 解决跨域问题
 * @author Paracosm
 */

@Configuration
public class WebMvcConfig implements WebMvcConfigurer {
    @Override
    public void addCorsMappings(CorsRegistry registry) {

        //对哪些目录可以跨域访问
        registry.addMapping("/**")
                //允许哪些网站可以跨域访问
                //.allowedOrigins("http://localhost:8080")  //springboot2.4.0会遇到跨域问题,不允许使用通配符 *
                .allowedOrigins("*")
                //允许哪些方法
                .allowedMethods("*")
                .maxAge(3600)
                //访问的时候是否需要验证
                .allowCredentials(true);

    }

}
