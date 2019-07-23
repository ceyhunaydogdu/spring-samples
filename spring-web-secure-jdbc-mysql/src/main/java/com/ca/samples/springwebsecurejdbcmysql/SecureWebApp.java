package com.ca.samples.springwebsecurejdbcmysql;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/*
@author Ceyhun Aydoğdu <aydogdu.ceyhun@gmail.com>
*/
@SpringBootApplication
public class SecureWebApp implements WebMvcConfigurer {

	public static void main(String[] args) {
		SpringApplication.run(SecureWebApp.class, args);
	}
	
	@Override
	public void addViewControllers(ViewControllerRegistry registry) {
		registry.addViewController("/").setViewName("home");
	}

}
