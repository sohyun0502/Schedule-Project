package kr.spartaclub.scheduleproject.config;

import kr.spartaclub.scheduleproject.interceptor.LoginCheckInterceptor;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(new LoginCheckInterceptor())
                .addPathPatterns(
                        "/users/**",
                        "/schedules/**",
                        "/comments/**"
                )
                .excludePathPatterns(
                        "/signup",
                        "/login",
                        "/logout"
                );
    }
}

