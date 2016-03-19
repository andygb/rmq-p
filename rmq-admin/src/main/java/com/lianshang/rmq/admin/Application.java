package com.lianshang.rmq.admin;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.context.web.SpringBootServletInitializer;

/**
 * Created by yuan.zhong on 2016-03-19.
 *
 * @author yuan.zhong
 */
@SpringBootApplication
public class Application extends SpringBootServletInitializer {

    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }

    @Override
    protected SpringApplicationBuilder configure(SpringApplicationBuilder builder) {
        return builder.sources(Application.class);
    }

//    public void addInterceptors(InterceptorRegistry registry){
//        registry.addInterceptor(new SecurityControlInterceptor()).addPathPatterns("/**");
//    }


}
