package com.ca.samples.springwebsecurejdbcmysql;

import org.springframework.data.annotation.Id;

/*
@author Ceyhun Aydoğdu <aydogdu.ceyhun@gmail.com>
*/
public class User{

    public User() {
    }

    @Id
    private Long id;
    private String username;
    private String password;

    /**
     * @param username
     * @param password
     */

    public User(String username, String password) {
        this.username = username;
        this.password = password;
    }

    /**
     * @return the id
     */
    public Long getId() {
        return id;
    }

    /**
     * @param id the id to set
     */
    public void setId(Long id) {
        this.id = id;
    }

    /**
     * @return the username
     */
    public String getUsername() {
        return username;
    }

    /**
     * @param username the username to set
     */
    public void setUsername(String username) {
        this.username = username;
    }

    /**
     * @return the password
     */
    public String getPassword() {
        return password;
    }

    /**
     * @param password the password to set
     */
    public void setPassword(String password) {
        this.password = password;
    }

    
    

}