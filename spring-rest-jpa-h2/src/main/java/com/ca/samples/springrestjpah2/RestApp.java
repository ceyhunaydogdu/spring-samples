package com.ca.samples.springrestjpah2;

import java.util.stream.Stream;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

/*
@author Ceyhun Aydoğdu <aydogdu.ceyhun@gmail.com>
*/

@SpringBootApplication
public class RestApp {

	@Bean
	CommandLineRunner cLineRunner(MessageRepo messageRepo){
		return args -> {
			Stream.of("Hello resters!","Rest in Peace", "Never underestimate", "Rest of the world","Who is nest?")
				.forEach(m -> messageRepo.save(new Message(m)));

		};
	}

	public static void main(String[] args) {
		SpringApplication.run(RestApp.class, args);
	}

}
