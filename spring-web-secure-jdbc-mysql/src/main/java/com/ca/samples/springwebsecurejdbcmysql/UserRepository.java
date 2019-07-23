package com.ca.samples.springwebsecurejdbcmysql;

import org.springframework.data.jdbc.repository.query.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

/*
@author Ceyhun Aydoğdu <aydogdu.ceyhun@gmail.com>
*/
public interface UserRepository extends CrudRepository<User, Long> {
    @Query("select * from user where username=:name")
    User findByUsername(@Param("name") String username);
}