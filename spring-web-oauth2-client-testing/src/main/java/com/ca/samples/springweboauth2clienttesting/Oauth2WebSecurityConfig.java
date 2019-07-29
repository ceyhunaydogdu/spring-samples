package com.ca.samples.springweboauth2clienttesting;

import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

/*
@author Ceyhun Aydoğdu <aydogdu.ceyhun@gmail.com>
*/
@EnableWebSecurity
public class Oauth2WebSecurityConfig extends WebSecurityConfigurerAdapter{
    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.authorizeRequests()
            .anyRequest().authenticated()
            .and().oauth2Login();
    }
}