package com.ca.samples.springwebsecurejdbcmysql;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestBuilders.formLogin;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.*;
import static org.springframework.security.test.web.servlet.response.SecurityMockMvcResultMatchers.*;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.context.WebApplicationContext;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
@Transactional
public class SecureWebAppTests {

	@Autowired
	private WebApplicationContext context;

	private MockMvc mvc;

	@Autowired
	private UserRepository userRepository;

	@Before
	public void setup() {
		mvc = MockMvcBuilders.webAppContextSetup(context).apply(springSecurity()).build();
	}

	@Test
	public void whenFindAllUserFromRepository_thenFind2() {
		assertThat(this.userRepository.findAll()).hasSize(2);
	}

	@Test
	public void whenFindByNameValid_thenMatch() {
		assertThat(this.userRepository.findByUsername("ceyhun").getUsername()).isEqualTo("ceyhun");
	}

	@Test
	public void whenFindByNameInvalid_thenNoMatch() {
		assertThat(this.userRepository.findByUsername("invalid")).isNull();
	}

	@Test
	public void whenGetPathWithValidDefaultUser_thenOk() throws Exception {
		mvc
				// principal does not need to exist with default values:
				// u:"user"/p:"password"/r:"ROLE_USER"
				.perform(get("/").with(user("user"))).andExpect(status().isOk()).andExpect(authenticated());
	}

	@Test
	public void whenGetPathWithValidCustomUser_thenOk() throws Exception {
		mvc
				// principal does not need to exist with values: u:"usera"/p:"pass"
				.perform(get("/").with(user("usera").password("pass"))).andExpect(status().isOk())
				.andExpect(authenticated().withUsername("usera"));
	}

	@Test
	public void whenGetPathWithInvalidUser_thenUnAuthorized() throws Exception {
		mvc.perform(get("/").with(anonymous())).andExpect(status().isUnauthorized()).andExpect(unauthenticated());
	}

	@Test
	public void whenLoginWithValid_thenAuthenticated() throws Exception {
		mvc
				// principal needs to exist with default values: "user"/"password"
				.perform(formLogin()).andExpect(authenticated());
	}

	@Test
	public void whenLoginWithCustomValid_thenAuthenticated() throws Exception {
		mvc
				// principal needs to exist with custom values: u:"ceyhun"/p:"ceyhun"
				.perform(formLogin().user("ceyhun").password("ceyhun")).andExpect(authenticated());
	}

	@Test
	public void whenLoginWithInvalidUser_thenUnauthenticated() throws Exception {
		mvc.perform(formLogin().user("invalid").password("invalid")).andExpect(unauthenticated());
	}

}
