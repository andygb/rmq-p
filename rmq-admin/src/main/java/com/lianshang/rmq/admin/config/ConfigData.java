package com.lianshang.rmq.admin.config;

import com.lianshang.rmq.admin.security.SecurityInterceptor;
import org.springframework.boot.context.embedded.MultipartConfigFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.ImportResource;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

import javax.servlet.MultipartConfigElement;

/**
 *
 * @author    fly.wu    Mar 6, 2016
 */
@Configuration
@ImportResource("classpath*:config/spring/*.xml")
public class ConfigData extends WebMvcConfigurerAdapter {


    @Bean
    public MultipartConfigElement multipartConfigElement() {
        MultipartConfigFactory factory = new MultipartConfigFactory();
        factory.setMaxFileSize("10MB");
        factory.setMaxRequestSize("10MB");
        return factory.createMultipartConfig();
    }
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(new SecurityInterceptor()).addPathPatterns("/record/**","/topic-mgmt/**", "/rmq-admin/record/**", "/rmq-admin/topic-mgmt");
    }
}
