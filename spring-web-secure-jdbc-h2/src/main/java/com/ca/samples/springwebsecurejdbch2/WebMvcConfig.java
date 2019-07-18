package com.ca.samples.springwebsecurejdbch2;

import java.util.Date;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;



/*
@author Ceyhun Aydoğdu <aydogdu.ceyhun@gmail.com>
*/
@Controller
public class WebMvcConfig implements WebMvcConfigurer {
    @Override
    public void addViewControllers(ViewControllerRegistry registry) {
        registry.addViewController("/login").setViewName("login");
        registry.addViewController("/").setViewName("home");
    }

    @GetMapping("/")
    public String getHome(Model model) {
        model.addAttribute("title", "Home of Spring Booters");
        model.addAttribute("message", "Wellcome to secure home of spring booters");
        model.addAttribute("date", new Date());
        return "home";
    }
    
    @RequestMapping("/no")
    public String no() {
        throw new RuntimeException("Exception occurred in WebMvcConfig Controller");
    }
    
}