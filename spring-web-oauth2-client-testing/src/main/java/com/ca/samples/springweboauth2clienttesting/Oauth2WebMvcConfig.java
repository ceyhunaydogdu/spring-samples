package com.ca.samples.springweboauth2clienttesting;

import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/*
@author Ceyhun Aydoğdu <aydogdu.ceyhun@gmail.com>
*/


public class Oauth2WebMvcConfig implements WebMvcConfigurer {
    @Override
    public void addViewControllers(ViewControllerRegistry registry) {
        registry.addViewController("/").setViewName("home");
    }
}
