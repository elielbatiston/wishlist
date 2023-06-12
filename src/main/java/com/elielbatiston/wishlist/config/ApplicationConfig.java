package com.elielbatiston.wishlist.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class ApplicationConfig implements WebMvcConfigurer {

    @Autowired
    private InterceptorHandle handle;

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(handle);
    }
}
