package com.ca.samples.springwebsecurejpah2;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.security.test.context.support.WithAnonymousUser;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;

@RunWith(SpringRunner.class)
@WebMvcTest
public class WebMvcConfigTest {

	@Autowired
	MockMvc mvc;

	@Test
	@WithMockUser
	public void whenGetRootPathWithValidUser_thenOk() throws Exception {
		mvc
			.perform(get("/"))
			.andExpect(status().isOk());
	}

	@Test
	@WithAnonymousUser
	public void whenGetRootPathWithInvalidUser_thenUnauthorized() throws Exception {
		mvc
			.perform(get("/"))
			.andExpect(status().isUnauthorized());
	}
}
