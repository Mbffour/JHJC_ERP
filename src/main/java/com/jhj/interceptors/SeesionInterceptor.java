package com.jhj.interceptors;

import com.jhj.filter.AccessControlAllowOriginFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

@Configuration
public class SeesionInterceptor extends WebMvcConfigurerAdapter {

    @Autowired
    AuthInterceptors authInterceptors;


    @Bean
    public FilterRegistrationBean FilterRegistration() {
        FilterRegistrationBean registration = new FilterRegistrationBean(new AccessControlAllowOriginFilter());
        registration.addUrlPatterns("/*"); //
        registration.setOrder(1);
        return registration;
    }



    @Bean
    public WebMvcConfigurer corsConfigurer() {
        return new WebMvcConfigurerAdapter() {
            @Override
            public void addCorsMappings(CorsRegistry registry) {
                registry.addMapping("/**");
            }
        };
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(authInterceptors)
                .addPathPatterns("/**")
                .excludePathPatterns("/user/login");
        super.addInterceptors(registry);
    }
}
