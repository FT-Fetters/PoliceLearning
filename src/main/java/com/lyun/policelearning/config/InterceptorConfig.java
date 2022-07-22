package com.lyun.policelearning.config;

import com.lyun.policelearning.utils.PathTools;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class InterceptorConfig implements WebMvcConfigurer {

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        //访问路径
        registry.addResourceHandler("/api/**") //不能直接/**
                //映射真实路径
                .addResourceLocations("file:" + PathTools.getRunPath() + "/");//必须加"/"，不然映射不到
    }
}