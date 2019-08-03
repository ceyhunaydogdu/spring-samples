package com.ca.samples.springrestjpah2;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

/*
@author Ceyhun Aydoğdu <aydogdu.ceyhun@gmail.com>
*/

@Entity
public class Message {
    @Id
    @GeneratedValue
    private Long id;
    private String message;

    public Message() {
    }

    /**
     * @param message
     */

    public Message(String message) {
        this.message = message;
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
     * @return the message
     */
    public String getMessage() {
        return message;
    }

    /**
     * @param message the message to set
     */
    public void setMessage(String message) {
        this.message = message;
    }

    
}