package com.lianshang.rmq.admin;

import org.glassfish.jersey.jackson.JacksonFeature;
import org.glassfish.jersey.media.multipart.MultiPartFeature;
import org.glassfish.jersey.server.ResourceConfig;
import org.glassfish.jersey.server.mvc.beanvalidation.MvcBeanValidationFeature;
import org.glassfish.jersey.server.mvc.freemarker.FreemarkerMvcFeature;
import org.springframework.web.filter.RequestContextFilter;

/**
 * 注册器
 * 
 * @author yujie.yao
 */
public class Application extends ResourceConfig {

    /**
     * 注册JAX-RS组件
     */
    public Application() {
        super(RequestContextFilter.class, JacksonFeature.class, MvcBeanValidationFeature.class);


        property(FreemarkerMvcFeature.TEMPLATES_BASE_PATH, "/ftl");
        property(FreemarkerMvcFeature.TEMPLATE_OBJECT_FACTORY, FreemarkerConfig.class);
        property(FreemarkerMvcFeature.CACHE_TEMPLATES, false);
        register(FreemarkerMvcFeature.class);
        register(MultiPartFeature.class);
    }



}
