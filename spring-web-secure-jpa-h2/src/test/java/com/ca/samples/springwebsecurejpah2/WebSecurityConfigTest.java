package com.ca.samples.springwebsecurejpah2;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestBuilders.formLogin;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.anonymous;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.security.test.web.servlet.response.SecurityMockMvcResultMatchers.authenticated;
import static org.springframework.security.test.web.servlet.response.SecurityMockMvcResultMatchers.unauthenticated;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.security.test.context.support.WithAnonymousUser;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.context.WebApplicationContext;

/*
@author Ceyhun Aydoğdu <aydogdu.ceyhun@gmail.com>
*/
@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
@Transactional
public class WebSecurityConfigTest {

    @Autowired
    private WebApplicationContext context;

    // @Autowired
    // private static UserRepository userRepository;

    private MockMvc mvc;

    // @BeforeClass
    // public static void setupForAll() {

    //     // Add users to db for testing
    //     userRepository.save(new User("user", "{noop}password"));
    //     userRepository.save(new User("ceyhun", "{noop}ceyhun"));

    // }

    @Before
    public void setup() {
        mvc = MockMvcBuilders.webAppContextSetup(context).apply(springSecurity()).build();

    }

    // @AfterClass
    // public static void destroyForAll() {
    //     // Delete test users from db
    //     userRepository.delete(userRepository.findByUsername("user"));
    //     userRepository.delete(userRepository.findByUsername("ceyhun"));
    // }

    @Test
    @WithMockUser
    public void whenGetRootPathWithValidUser_thenOk() throws Exception {
        mvc
                // principal does not need to exist with default values:
                // u:"user"/p:"password"/r:"ROLE_USER"
                .perform(get("/")).andExpect(status().isOk());
    }

    @Test
    @WithAnonymousUser
    public void whenGetRootPathWithInvalidUser_thenUnauthorized() throws Exception {
        mvc.perform(get("/")).andExpect(status().isUnauthorized());
    }

    @Test
    @WithUserDetails
    public void whenGetRootPathWithValidUserDetails_thenOk() throws Exception {
        mvc
                // principal needs to exist with default values: u:"user"/p:"password"
                .perform(get("/")).andExpect(status().isOk()).andExpect(authenticated().withUsername("user"));
    }

    @Test
    @WithUserDetails(value = "ceyhun", userDetailsServiceBeanName = "customUserDetailsService")
    public void whenGetRootPathWithValidCustomUserDetails_thenOk() throws Exception {
        mvc
                // principal needs to exist with custom values: u:"ceyhun"/p:"ceyhun"
                .perform(get("/")).andExpect(status().isOk()).andExpect(authenticated());
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