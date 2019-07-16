package com.ca.samples.springwebdocker;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.hamcrest.Matchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;

@RunWith(SpringRunner.class)
@WebMvcTest
public class GreetingControllerTest {

	@Autowired
	MockMvc mvc;

	@Test
	public void greetWithDefaultUser() throws Exception {
		mvc
			.perform(get("/greeting"))
			.andExpect(status().isOk())
			.andExpect(content().string(containsString("Hi, Spring Booter!, welcome to web application hosted on docker")));
	}

	@Test
	public void greetWithCustomUser() throws Exception {
		mvc
			.perform(get("/greeting").param("name", "Ceyhun"))
			.andExpect(status().isOk())
			.andExpect(content().string(containsString("Hi, Ceyhun!, welcome to web application hosted on docker")));
	}
}
