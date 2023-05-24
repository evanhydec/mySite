package com.juity.blog.INTERCEPTOR;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
@Component
public class webMVCConfigurer implements WebMvcConfigurer {
    @Autowired
    private baseInterceptor base;

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(base);
    }
}
