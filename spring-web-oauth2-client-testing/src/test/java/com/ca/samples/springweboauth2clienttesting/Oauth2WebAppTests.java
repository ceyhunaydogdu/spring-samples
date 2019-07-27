package com.ca.samples.springweboauth2clienttesting;

import static org.assertj.core.api.Assertions.assertThat;

import java.io.IOException;
import java.net.MalformedURLException;
import java.util.Optional;

import com.gargoylesoftware.htmlunit.FailingHttpStatusCodeException;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.html.HtmlAnchor;
import com.gargoylesoftware.htmlunit.html.HtmlPage;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.oauth2.client.registration.ClientRegistration;
import org.springframework.security.oauth2.client.registration.ClientRegistrationRepository;
import org.springframework.test.context.junit4.SpringRunner;

// import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
public class Oauth2WebAppTests {

	private static final String AUTHORIZATION_BASE_URI = "/oauth2/authorization";
	private static final String REDIRECTION_BASE_URL = "http://localhost:8080/login/oauth2/code";

	// @Autowired
	// private WebApplicationContext context;

	// @Autowired
	// private MockMvc mvc;

	@Autowired
	private WebClient webClient;

	@Autowired
	private ClientRegistrationRepository clientRepo;

	@Before
	public void setup() {
		// //Alternative 1st
		// mvc =
		// MockMvcBuilders.webAppContextSetup(context).apply(springSecurity()).build();
		// webClient = MockMvcWebClientBuilder
		// // .webAppContextSetup(context, springSecurity())
		// .mockMvcSetup(mvc)
		// .contextPath("")
		// .build();

		webClient.getCookieManager().clearCookies();
	}

	@Test
	public void whenRequestedIndexPageUnauthenticated_thenRedirectToLoginPage()
			throws FailingHttpStatusCodeException, MalformedURLException, IOException {
		// HtmlPage expectedLoginPage = webClient.getPage("http://localhost:8080");
		HtmlPage expectedLoginPage = webClient.getPage("/");
		this.assertRedirectedToLoginPage(expectedLoginPage);
	}

	@Test
	public void whenRequestedPageNotExistingUnauthenticated_thenRedirectToLoginPage()
			throws FailingHttpStatusCodeException, MalformedURLException, IOException {
		HtmlPage expectedLoginPage = webClient.getPage("/pageNotExisting");
		this.assertRedirectedToLoginPage(expectedLoginPage);
	}

	public void assertRedirectedToLoginPage(HtmlPage expectedLoginPage) {
		// Assert google link is on login page
		assertThat(expectedLoginPage
					 .getWebResponse()
					 .getContentAsString()
					 .contains(AUTHORIZATION_BASE_URI + "/google")).isTrue();
		// Assert github link is on login page
		assertThat(expectedLoginPage
					.getWebResponse()
					.getContentAsString()
					.contains(AUTHORIZATION_BASE_URI + "/github")).isTrue();

				System.out.println(expectedLoginPage.getWebResponse().getContentAsString());

	}

	@Test
	public void whenAuthorizeGithubLinkClicked_atLoginPage_thenRedirectForAuthorization()
			throws FailingHttpStatusCodeException, MalformedURLException, IOException {
		HtmlPage loginPage = this.webClient.getPage("/");

		ClientRegistration clientRegistration = this.clientRepo.findByRegistrationId("github");
		HtmlAnchor clientAnchorElement = this.getClientAnchorElement(loginPage, clientRegistration);
		assertThat(clientAnchorElement).isNotNull();
	}

	public HtmlAnchor getClientAnchorElement(HtmlPage page, ClientRegistration clientRegistration) {
		Optional<HtmlAnchor> clientAnchorElement = page.getAnchors().stream()
				.filter(e -> e.asText().equals(clientRegistration.getClientName())).findFirst();
		return (clientAnchorElement.orElse(null));
	}

	
}
