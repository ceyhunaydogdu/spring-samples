package com.ca.samples.springrestjpah2;

import java.util.Collection;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.data.rest.core.annotation.RestResource;

/*
@author Ceyhun Aydoğdu <aydogdu.ceyhun@gmail.com>
*/
@RepositoryRestResource
interface MessageRepo extends JpaRepository<Message, Long>{
    @RestResource(path = "by-message")
    Collection<Message> findByMessageContainingIgnoringCase(@Param("m") String m);
}