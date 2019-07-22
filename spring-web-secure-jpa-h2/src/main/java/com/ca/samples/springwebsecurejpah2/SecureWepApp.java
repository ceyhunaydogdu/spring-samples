package com.ca.samples.springwebsecurejpah2;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class SecureWepApp implements ApplicationRunner {

	@Autowired
	private UserRepository userRepository;

	public static void main(String[] args) {
		SpringApplication.run(SecureWepApp.class, args);
	}

	@Override
	public void run(ApplicationArguments args) throws Exception {

		// Add users to db for testing
		userRepository.save(new User("user", "{noop}password"));
		userRepository.save(new User("ceyhun", "{noop}ceyhun"));

	}

}
