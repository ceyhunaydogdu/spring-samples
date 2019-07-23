package com.ca.samples.springwebsecurejdbcmysql;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

@RunWith(SpringRunner.class)
@SpringBootTest
@Transactional
public class SecureWebAppTests {

	@Autowired
	private UserRepository userRepository;

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

}
