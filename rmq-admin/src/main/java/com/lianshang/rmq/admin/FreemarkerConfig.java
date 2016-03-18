package com.lianshang.rmq.admin;

import freemarker.cache.ClassTemplateLoader;
import freemarker.cache.MultiTemplateLoader;
import freemarker.cache.TemplateLoader;
import freemarker.template.Configuration;
import freemarker.template.TemplateExceptionHandler;

import java.util.List;

import static com.google.common.collect.Lists.newArrayList;

/**
 * Created by Bao on 2016-03-02.
 */
public class FreemarkerConfig extends Configuration {

    public FreemarkerConfig() {
        super(Configuration.VERSION_2_3_23);
        this.setAPIBuiltinEnabled(true);
        this.setDefaultEncoding("UTF-8");
        this.setNumberFormat("#");
        this.setDateFormat("yyyy-MM-dd HH:mm:ss");
//        this.setTemplateUpdateDelayMilliseconds(600000000);
        this.setTemplateExceptionHandler(TemplateExceptionHandler.RETHROW_HANDLER);
        addLoaders();
    }

    private void addLoaders() {
        final List<TemplateLoader> loaders = newArrayList();
        loaders.add(new ClassTemplateLoader(getClass(), "/"));
        this.setTemplateLoader(new MultiTemplateLoader(loaders.toArray(new TemplateLoader[loaders.size()])));
    }

}
