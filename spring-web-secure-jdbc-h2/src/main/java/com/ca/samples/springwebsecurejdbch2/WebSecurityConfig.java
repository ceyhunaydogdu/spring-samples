package com.ca.samples.springwebsecurejdbch2;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
// import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
// import org.springframework.security.core.userdetails.User;
// import org.springframework.security.provisioning.JdbcUserDetailsManager;
// import org.springframework.security.core.userdetails.User.UserBuilder;

/*
@author Ceyhun Aydoğdu <aydogdu.ceyhun@gmail.com>
*/
@Configuration
@EnableWebSecurity
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

    @Autowired
    DataSource datasource;

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.authorizeRequests()
            .anyRequest().authenticated()
            .and().formLogin().loginPage("/login").permitAll()
            .and().logout().permitAll();
    }

    /*
    This first alternative to use jdbc authentication 
     When this method is used, schema.sql and data.sql should also be included
     in resources folder in order to create users and authority tables in db 
     and populate with users' data.
     */
    // @Bean
    // public JdbcUserDetailsManager jdbcUserDetailsManager(DataSource dataSource) {
    //     JdbcUserDetailsManager detailsManager=new JdbcUserDetailsManager();
    //     detailsManager.setDataSource(dataSource);
    //     return detailsManager; 
    // }

    /* 
     This second alternative to use jdbc authentication 
     When this method is used, schema.sql and data.sql should also be included
     in resources folder in order to create users and authority tables in db 
     and populate with users' data.
     */
    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth
            .jdbcAuthentication()
                .dataSource(datasource);
    }

    /*
     This is variant of second alternative to use jdbc authentication 
     When this method is used, since we are using default schema and populate db 
     with users within this method, we do not need to include sql setup files
     in resources folder.
     */
    // @Override
    // protected void configure(AuthenticationManagerBuilder auth) throws Exception {
    //     UserBuilder users = User.withDefaultPasswordEncoder();
    //     auth
    //         .jdbcAuthentication()
    //             .dataSource(datasource)
    //             .withDefaultSchema()
    //             .withUser(users.username("user").password("user").roles("USER"))
    //             .withUser(users.username("ceyhun").password("ceyhun").roles("USER", "ADMIN"));
    // }
}
