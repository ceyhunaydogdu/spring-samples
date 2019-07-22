package com.ca.samples.springwebsecurejpah2;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

/*
@author Ceyhun Aydoğdu <aydogdu.ceyhun@gmail.com>
*/

@RunWith(SpringRunner.class)
@SpringBootTest
@Transactional
public class RepositoryTest {
    @Autowired
    UserRepository userRepository;

    @Test
    public void whenFindAllUsers_thenHasSize2() {
        int sizeOfUsers = userRepository.findAll().size();
        assertThat(sizeOfUsers).isEqualTo(2);
        
    }
}