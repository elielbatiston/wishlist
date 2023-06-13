package com.elielbatiston.wishlist.configs;

import com.elielbatiston.wishlist.configs.InternationalizationConfig;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import java.util.Arrays;
import java.util.List;
import java.util.Locale;

@Component
public class InterceptorHandle implements HandlerInterceptor {

    @Autowired
    private InternationalizationConfig config;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
        final String language = request.getHeader("Accept-Language");
        if (language != null) {
            final List<String> languages = Arrays.asList(language.split("_"));
            final Locale locale = new Locale(languages.get(0), languages.get(1));
            config.setLocale(locale);
        }
        return true;
    }
}
