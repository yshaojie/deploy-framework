//package com.self.deploy.web;
//
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//import org.springframework.context.annotation.ComponentScan;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.web.servlet.config.annotation.EnableWebMvc;
//import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
//import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;
//
///**
// * Created by shaojieyue
// * Created time 2017-02-12 14:17
// */
//@Configuration
//@EnableWebMvc
//@ComponentScan("com.self.deploy.web")
//public class WebConfig extends WebMvcConfigurerAdapter {
//    private static final Logger logger = LoggerFactory.getLogger(WebConfig.class);
//
//    @Override
//    public void addResourceHandlers(ResourceHandlerRegistry registry) {
//        registry.addResourceHandler("/resources/**").setCachePeriod(0);
//    }
//}
