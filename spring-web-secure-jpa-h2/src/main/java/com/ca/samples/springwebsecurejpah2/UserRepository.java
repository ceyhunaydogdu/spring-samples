package com.ca.samples.springwebsecurejpah2;

import org.springframework.data.jpa.repository.JpaRepository;

/*
@author Ceyhun Aydoğdu <aydogdu.ceyhun@gmail.com>
*/
public interface UserRepository extends JpaRepository<User, Long> {
    User findByUsername(String username);
}