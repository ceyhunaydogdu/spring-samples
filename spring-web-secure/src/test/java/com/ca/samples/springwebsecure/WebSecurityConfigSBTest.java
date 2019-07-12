package com.ca.samples.springwebsecure;


import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestBuilders.formLogin;
import static org.springframework.security.test.web.servlet.response.SecurityMockMvcResultMatchers.authenticated;
import static org.springframework.security.test.web.servlet.response.SecurityMockMvcResultMatchers.unauthenticated;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.security.test.context.support.WithAnonymousUser;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

/*
@author Ceyhun Aydoğdu <aydogdu.ceyhun@gmail.com>
*/
@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
public class WebSecurityConfigSBTest {

	@Autowired
	private WebApplicationContext context;

	private MockMvc mvc;

	@Before
	public void setup() {
		mvc=MockMvcBuilders.webAppContextSetup(context)
				.apply(springSecurity())
				.build();
	}

	@Test
	@WithMockUser
	public void whenMapSecuredViewsWithValid_thenOk() throws Exception {
		mvc
			.perform(get("/hello"))
			.andExpect(status().isOk())
			.andExpect(authenticated());		

	}

	

	@Test
	// @DisabledIf("true")
	@WithAnonymousUser
	public void whenMapSecuredViewWithInvalid_thenFail() throws Exception {
		mvc.perform(get("/hello"))
			.andExpect(status().is3xxRedirection())
			.andExpect(unauthenticated());
	}

	@Test
	public void whenLoginWithValid_thenAuthenticated() throws Exception {
		mvc
			//user needs to exist with default values: "user"/"password"
			.perform(formLogin()) 
			.andExpect(authenticated());
	}

	@Test
	public void whenLoginWithValidCustom_thenAuthenticated() throws Exception {
		mvc
			//user needs to exist with custom values: "usera"/"pass"
			.perform(formLogin().user("usera").password("pass"))
			.andExpect(authenticated());
	}

	@Test
	public void whenLoginWithInvalid_thenUnauthenticated() throws Exception {
		mvc
			.perform(formLogin().user("invalid").password("invalid"))
			.andExpect(unauthenticated());
	}


}
