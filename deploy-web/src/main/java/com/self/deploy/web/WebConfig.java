package com.self.deploy.web;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.CacheControl;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

/**
 * Created by shaojieyue
 * Created time 2017-02-12 14:17
 */
@Configuration
@EnableWebMvc
@ComponentScan("com.self.deploy.web")
public class WebConfig extends WebMvcConfigurerAdapter {
    private static final Logger logger = LoggerFactory.getLogger(WebConfig.class);

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("/app/**")
                .addResourceLocations("file:/home/shaojieyue/tools/workspace/deploy-framework/deploy-web/src/main/resources/static/app/")
                .setCachePeriod(0).setCacheControl(CacheControl.noStore());
        registry.addResourceHandler("/vendor/**")
                .addResourceLocations("file:/home/shaojieyue/tools/workspace/deploy-framework/deploy-web/src/main/resources/static/vendor/");
        registry.addResourceHandler("/")
                .addResourceLocations("file:/home/shaojieyue/tools/workspace/deploy-framework/deploy-web/src/main/resources/static/index.html");

        registry.addResourceHandler("/dist/**")
                .addResourceLocations("file:/data/deploy/dist-source/");
        super.addResourceHandlers(registry);
    }


}
