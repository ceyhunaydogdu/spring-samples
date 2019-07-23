package com.ca.samples.springwebsecurejdbcmysql;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/*
@author Ceyhun Aydoğdu <aydogdu.ceyhun@gmail.com>
*/
@Service
@Transactional
public class CustomUserDetailsService implements UserDetailsService {

    @Autowired
    private UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user=userRepository.findByUsername(username);
        if (user==null) {
            throw new UsernameNotFoundException("Username: "+username+" not found on repository ");
        }        
		return new CustomPrincipal(user);
	}

}