package com.course.file.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class SpringMvcConfig implements WebMvcConfigurer {
    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("/f/**").addResourceLocations("file:/Users/zhanghaifeng/zhf/study/imooc/416-spring-cloud-vue/course/files/");
    }
    // http://127.0.0.1:9003/file/f/teacher/bk3zunMu-IMG_0306.jpeg
}
