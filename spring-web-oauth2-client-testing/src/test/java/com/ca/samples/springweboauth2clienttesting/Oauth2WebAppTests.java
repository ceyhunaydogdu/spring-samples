package com.ca.samples.springweboauth2clienttesting;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.springframework.http.HttpStatus.MOVED_PERMANENTLY;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URL;
import java.net.URLDecoder;
import java.time.Instant;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import com.gargoylesoftware.htmlunit.FailingHttpStatusCodeException;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.WebResponse;
import com.gargoylesoftware.htmlunit.html.HtmlAnchor;
import com.gargoylesoftware.htmlunit.html.HtmlPage;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.stubbing.Answer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringBootConfiguration;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.oauth2.client.InMemoryOAuth2AuthorizedClientService;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClientService;
import org.springframework.security.oauth2.client.authentication.OAuth2LoginAuthenticationToken;
import org.springframework.security.oauth2.client.endpoint.OAuth2AccessTokenResponseClient;
import org.springframework.security.oauth2.client.endpoint.OAuth2AuthorizationCodeGrantRequest;
import org.springframework.security.oauth2.client.oidc.authentication.OidcAuthorizationCodeAuthenticationProvider;
import org.springframework.security.oauth2.client.registration.ClientRegistration;
import org.springframework.security.oauth2.client.registration.ClientRegistrationRepository;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserService;
import org.springframework.security.oauth2.client.web.AuthenticatedPrincipalOAuth2AuthorizedClientRepository;
import org.springframework.security.oauth2.client.web.OAuth2AuthorizedClientRepository;
import org.springframework.security.oauth2.core.OAuth2AccessToken;
import org.springframework.security.oauth2.core.endpoint.OAuth2AccessTokenResponse;
import org.springframework.security.oauth2.core.endpoint.OAuth2AuthorizationResponseType;
import org.springframework.security.oauth2.core.endpoint.OAuth2ParameterNames;
import org.springframework.security.oauth2.core.oidc.IdTokenClaimNames;
import org.springframework.security.oauth2.core.oidc.OidcIdToken;
import org.springframework.security.oauth2.core.oidc.endpoint.OidcParameterNames;
import org.springframework.security.oauth2.core.oidc.user.DefaultOidcUser;
import org.springframework.security.oauth2.core.oidc.user.OidcUser;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.oauth2.core.user.OAuth2UserAuthority;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.web.util.UriComponents;
import org.springframework.web.util.UriComponentsBuilder;

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

	@MockBean
	private OidcAuthorizationCodeAuthenticationProvider authProvider;

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
		assertThat(expectedLoginPage.getWebResponse().getContentAsString().contains(AUTHORIZATION_BASE_URI + "/google"))
				.isTrue();
		// Assert github link is on login page
		assertThat(expectedLoginPage.getWebResponse().getContentAsString().contains(AUTHORIZATION_BASE_URI + "/github"))
				.isTrue();

	}

	@Test
	public void whenGoogleLinkClicked_atLoginPage_thenRedirectForAuthorization()
			throws FailingHttpStatusCodeException, MalformedURLException, IOException {
		HtmlPage loginPage = this.webClient.getPage("/");

		// get the google link for authorization which is "/oauth2/authorization/google"
		// and assert link is not null
		ClientRegistration clientRegistration = this.clientRepo.findByRegistrationId("google");
		HtmlAnchor clientAnchorElement = this.getClientAnchorElement(loginPage, clientRegistration);
		assertThat(clientAnchorElement).isNotNull();

		// follow the google link for authorization and assert that user is redirected
		// to authorize the client (this web app)
		WebResponse response = followLinkAndGetResponseBeforeRedirected(clientAnchorElement);
		assertThat(response.getStatusCode()).isEqualTo(MOVED_PERMANENTLY.value());
		String authorizationRedirectionURI = response.getResponseHeaderValue("location");
		assertThat(authorizationRedirectionURI).isNotNull();

		UriComponents uriComponents = UriComponentsBuilder.fromUri(URI.create(authorizationRedirectionURI)).build();
		String authorizationRequestUri = uriComponents.getScheme() + "://" + uriComponents.getHost()
				+ uriComponents.getPath();
		assertThat(authorizationRequestUri).isEqualTo(clientRegistration.getProviderDetails().getAuthorizationUri());

		// assert that query params are correct
		Map<String, String> params = uriComponents.getQueryParams().toSingleValueMap();
		assertThat(params.get(OAuth2ParameterNames.RESPONSE_TYPE))
				.isEqualTo(OAuth2AuthorizationResponseType.CODE.getValue());
		assertThat(params.get(OAuth2ParameterNames.CLIENT_ID)).isEqualTo(clientRegistration.getClientId());
		assertThat(params.get(OAuth2ParameterNames.REDIRECT_URI))
				.isEqualTo(REDIRECTION_BASE_URL + "/" + clientRegistration.getRegistrationId());
		assertThat(URLDecoder.decode(params.get(OAuth2ParameterNames.SCOPE), "UTF-8"))
				.isEqualTo(clientRegistration.getScopes().stream().collect(Collectors.joining(" ")));
		assertThat(params.get(OAuth2ParameterNames.STATE)).isNotNull();
	}

	public WebResponse followLinkAndGetResponseBeforeRedirected(HtmlAnchor clientAnchorElement) throws IOException {
		WebResponse response = null;
		try {
			// Disable redirect so that we can catch the redirect url
			this.webClient.getOptions().setRedirectEnabled(false);
			clientAnchorElement.click();

		} catch (FailingHttpStatusCodeException e) {
			// Get response and enable redirect back again.
			response = e.getResponse();
			this.webClient.getOptions().setRedirectEnabled(true);
		}
		return response;
	}

	public HtmlAnchor getClientAnchorElement(HtmlPage page, ClientRegistration clientRegistration) {
		Optional<HtmlAnchor> clientAnchorElement = page.getAnchors().stream()
				.filter(e -> e.asText().equals(clientRegistration.getClientName())).findFirst();
		return (clientAnchorElement.orElse(null));
	}

	@Test
	public void whenValidAuthorizationResponse_thenDisplayHomePage()
			throws FailingHttpStatusCodeException, MalformedURLException, IOException {

		HtmlPage page = this.webClient.getPage("/");
		ClientRegistration clientReg = this.clientRepo.findByRegistrationId("github");
		HtmlAnchor clientAnchorElement = this.getClientAnchorElement(page, clientReg);
		assertThat(clientAnchorElement).isNotNull();

		WebResponse response = this.followLinkAndGetResponseBeforeRedirected(clientAnchorElement);
		UriComponents uriComponents = UriComponentsBuilder
				.fromUri(URI.create(response.getResponseHeaderValue("Location"))).build();
		Map<String, String> params = uriComponents.getQueryParams().toSingleValueMap();
		String code = "fake-auth-code";
		String state = URLDecoder.decode(params.get(OAuth2ParameterNames.STATE), "UTF-8");
		String redirectUri = URLDecoder.decode(params.get(OAuth2ParameterNames.REDIRECT_URI), "UTF-8");

		String validAuthorizationResponseUri = UriComponentsBuilder.fromHttpUrl(redirectUri)
				.queryParam(OAuth2ParameterNames.CODE, code).queryParam(OAuth2ParameterNames.STATE, state).build()
				.encode().toUriString();

		HtmlPage homePage = this.webClient.getPage(new URL(validAuthorizationResponseUri));
		assertThat(homePage.getTitleText()).isEqualToIgnoringWhitespace("Home");
		assertThat(homePage.getBody().asText()).contains("OAuth 2.0 Login with Spring Security");
		assertThat(homePage.getBody().asText()).contains("email: casample@gmail.com");

	}

	@Test
	public void whenValidAuthorizationResponseFromGoogle_thenDisplayHomePage()
			throws FailingHttpStatusCodeException, MalformedURLException, IOException {

		HtmlPage page = this.webClient.getPage("/");
		ClientRegistration clientReg = this.clientRepo.findByRegistrationId("google");
		HtmlAnchor clientAnchorElement = this.getClientAnchorElement(page, clientReg);
		assertThat(clientAnchorElement).isNotNull();

		WebResponse response = this.followLinkAndGetResponseBeforeRedirected(clientAnchorElement);
		UriComponents uriComponents = UriComponentsBuilder
				.fromUri(URI.create(response.getResponseHeaderValue("Location"))).build();
		Map<String, String> params = uriComponents.getQueryParams().toSingleValueMap();
		String code = "fake-auth-code";
		String state = URLDecoder.decode(params.get(OAuth2ParameterNames.STATE), "UTF-8");
		String redirectUri = URLDecoder.decode(params.get(OAuth2ParameterNames.REDIRECT_URI), "UTF-8");

		String validAuthorizationResponseUri = UriComponentsBuilder.fromHttpUrl(redirectUri)
				.queryParam(OAuth2ParameterNames.CODE, code).queryParam(OAuth2ParameterNames.STATE, state).build()
				.encode().toUriString();

		Map<String, Object> attributes = new HashMap<>();
		attributes.put("id", "ca");
		attributes.put("first-name", "Ceyhun");
		attributes.put("last-name", "Aydoğdu");
		attributes.put("email", "casample@gmail.com");

		OAuth2UserAuthority oAuth2UserAuthority = new OAuth2UserAuthority(attributes);
		Collection<GrantedAuthority> authorities = new HashSet<>();
		authorities.add(oAuth2UserAuthority);

		Map<String, Object> claims = new HashMap<>();
		claims.put(IdTokenClaimNames.ISS, "https://mock.provider.com");
		claims.put(IdTokenClaimNames.SUB, "ceyhun");
		claims.put(IdTokenClaimNames.AUD, Arrays.asList("client1", "client2"));
		claims.put(IdTokenClaimNames.AZP, "client1");
		Instant issuedAt = Instant.now();
		Instant expiresAt = Instant.from(issuedAt).plusSeconds(3600);
		Map<String, Object> headers = new HashMap<>();
		headers.put("alg", "RS256");
		Jwt jwt = new Jwt("id-token", issuedAt, expiresAt, headers, claims);
		OidcIdToken oidcToken=new OidcIdToken(jwt.getTokenValue(), jwt.getIssuedAt(), jwt.getExpiresAt(), jwt.getClaims());;
		OidcUser user = new DefaultOidcUser(authorities, oidcToken);
				
		when(authProvider.authenticate(any()))
		.thenAnswer((Answer<OAuth2LoginAuthenticationToken>) invocation -> {
			OAuth2LoginAuthenticationToken authenticationToken=invocation.getArgument(0);
			System.out.println("bul: "+authenticationToken.getClientRegistration().getRegistrationId());
			return new OAuth2LoginAuthenticationToken(
				authenticationToken.getClientRegistration(),
				authenticationToken.getAuthorizationExchange(),
				user,
				authorities,
				null,
				null);
			});

		HtmlPage homePage = this.webClient.getPage(new URL(validAuthorizationResponseUri));
		assertThat(homePage.getTitleText()).isEqualToIgnoringWhitespace("Home");
		assertThat(homePage.getBody().asText()).contains("OAuth 2.0 Login with Spring Security");
		assertThat(homePage.getBody().asText()).contains("email: casample@gmail.com");

	}

	@Test
	public void whenInvalidAuthorizationResponse_thenDisplayLoginPageWithError()
			throws FailingHttpStatusCodeException, MalformedURLException, IOException {
		HtmlPage page = this.webClient.getPage("/");
		URL loginPageUrl = page.getBaseURL();
		URL loginErrorPageUrl = new URL(loginPageUrl.toString() + "?error");

		ClientRegistration clientReg = this.clientRepo.findByRegistrationId("google");
		String code = "fake-auth-code";
		String state = "wrong-state";
		String redirectUri = REDIRECTION_BASE_URL + "/" + clientReg.getRegistrationId();

		String invalidAuthorizationResponseUri = UriComponentsBuilder.fromHttpUrl(redirectUri)
				.queryParam(OAuth2ParameterNames.CODE, code).queryParam(OAuth2ParameterNames.STATE, state).build()
				.encode().toUriString();

		this.webClient.getCookieManager().clearCookies();
		page = this.webClient.getPage(new URL(invalidAuthorizationResponseUri));
		assertThat(page.getBaseURL()).isEqualTo(loginErrorPageUrl);
		assertThat(page.getBody().asText()).contains("authorization_request_not_found");
	}

	@EnableWebSecurity
	@Order(Ordered.HIGHEST_PRECEDENCE)
	public static class WebSecurityTestConfig extends WebSecurityConfigurerAdapter {
		@Override
		protected void configure(HttpSecurity http) throws Exception {
			http.authorizeRequests().anyRequest().authenticated().and().oauth2Login().tokenEndpoint()
					.accessTokenResponseClient(this.mockAccessTokenResponseClient()).and().userInfoEndpoint()
					// .oidcUserService(this.mockOidcUserService())
					.userService(this.mockUserService());

		}

		// @Override
		// protected void configure(AuthenticationManagerBuilder auth) throws Exception {
		// 	auth.authenticationProvider(this.mockAuthenticationProvider());
		// }

		
		
		// private OidcAuthorizationCodeAuthenticationProvider mockAuthenticationProvider()
		// throws UnsupportedEncodingException {
			
		// 	OidcAuthorizationCodeAuthenticationProvider authenticationProvider = mock(OidcAuthorizationCodeAuthenticationProvider.class);
		// 	System.out.println("bul: mockAutonton");
		// 	OidcUser user = this.mockOidcUserService().loadUser(null);
		// 	Collection<? extends GrantedAuthority> authorities = user.getAuthorities();
			
		// 	when(authenticationProvider.authenticate(any()))
		// 	.thenAnswer((Answer<OAuth2LoginAuthenticationToken>) invocation -> {
		// 		OAuth2LoginAuthenticationToken authenticationToken=invocation.getArgument(0);
		// 		System.out.println("bul: "+authenticationToken.getClientRegistration().getRegistrationId());
		// 		return new OAuth2LoginAuthenticationToken(
		// 			authenticationToken.getClientRegistration(),
		// 			authenticationToken.getAuthorizationExchange(),
		// 			user,
		// 			authorities,
		// 			this.mockAccessTokenResponseClient().getTokenResponse(null).getAccessToken(),
		// 			this.mockAccessTokenResponseClient().getTokenResponse(null).getRefreshToken());
		// 		});
				
				
		// 		return authenticationProvider;
		// 	}
			
			private OAuth2UserService<OAuth2UserRequest, OAuth2User> mockUserService() {
				Map<String, Object> attributes = new HashMap<>();
			attributes.put("id", "ca");
			attributes.put("first-name", "Ceyhun");
			attributes.put("last-name", "Aydoğdu");
			attributes.put("email", "casample@gmail.com");

			OAuth2UserAuthority oAuth2UserAuthority = new OAuth2UserAuthority(attributes);
			Collection<GrantedAuthority> authorities = new HashSet<>();
			authorities.add(oAuth2UserAuthority);

			OAuth2User user=new DefaultOAuth2User(authorities, attributes, "email");

			OAuth2UserService<OAuth2UserRequest, OAuth2User> userService = mock(OAuth2UserService.class);
			
			when(userService.loadUser(any())).thenReturn(user);

			return userService;
			}
			
		// 	private OAuth2UserService<OidcUserRequest, OidcUser> mockOidcUserService() {
		// 		Map<String, Object> attributes = new HashMap<>();
		// 		attributes.put("id", "ca");
		// 		attributes.put("first-name", "Ceyhun");
		// 	attributes.put("last-name", "Aydoğdu");
		// 	attributes.put("email", "casample@gmail.com");

		// 	OAuth2UserAuthority oAuth2UserAuthority = new OAuth2UserAuthority(attributes);
		// 	Collection<GrantedAuthority> authorities = new HashSet<>();
		// 	authorities.add(oAuth2UserAuthority);

		// 	Map<String, Object> claims = new HashMap<>();
		// 	claims.put(IdTokenClaimNames.ISS, "https://mock.provider.com");
		// 	claims.put(IdTokenClaimNames.SUB, "ceyhun");
		// 	claims.put(IdTokenClaimNames.AUD, Arrays.asList("client1", "client2"));
		// 	claims.put(IdTokenClaimNames.AZP, "client1");
		// 	Instant issuedAt = Instant.now();
		// 	Instant expiresAt = Instant.from(issuedAt).plusSeconds(3600);
		// 	Map<String, Object> headers = new HashMap<>();
		// 	headers.put("alg", "RS256");
		// 	Jwt jwt = new Jwt("id-token", issuedAt, expiresAt, headers, claims);
		// 	OidcIdToken oidcToken=new OidcIdToken(jwt.getTokenValue(), jwt.getIssuedAt(), jwt.getExpiresAt(), jwt.getClaims());;
		// 	OidcUser user = new DefaultOidcUser(authorities, oidcToken);

		// 	OAuth2UserService<OidcUserRequest, OidcUser> userService = mock(OAuth2UserService.class);
		// 	when(userService.loadUser(any())).thenReturn(user);

		// 	return userService;
		// }

		private OAuth2AccessTokenResponseClient<OAuth2AuthorizationCodeGrantRequest> mockAccessTokenResponseClient()
				throws UnsupportedEncodingException {
			Instant expiresAt = Instant.now().plusSeconds(5);
			Set<String> scopes = new LinkedHashSet<>(Arrays.asList("openid", "profile", "email"));
			Map<String, Object> additionalParameters = new HashMap<>();
			additionalParameters.put(OidcParameterNames.ID_TOKEN, "fake.jwt.token");
			additionalParameters.put("other_param", "other-param-value");
			additionalParameters.put("yet_another_param", "yet-another-param-value");
			OAuth2AccessTokenResponse accessTokenResponse = OAuth2AccessTokenResponse.withToken("fake-access-token-123")
					.tokenType(OAuth2AccessToken.TokenType.BEARER).additionalParameters(additionalParameters)
					.expiresIn(expiresAt.getEpochSecond()).scopes(scopes).refreshToken("fake-refresh-token-123")
					.additionalParameters(additionalParameters).build();
			OAuth2AccessTokenResponseClient accessTokenResponseClient = mock(OAuth2AccessTokenResponseClient.class);
			when(accessTokenResponseClient.getTokenResponse(any())).thenReturn(accessTokenResponse);

			return accessTokenResponseClient;
		}
	}

	@SpringBootConfiguration
	@EnableAutoConfiguration
	@ComponentScan("com.ca.samples.springweboauth2clienttesting")
	public static class TestConfig {

		@Bean
		public OAuth2AuthorizedClientService authorizedClientService(
				ClientRegistrationRepository clientRegistrationRepository) {
			return new InMemoryOAuth2AuthorizedClientService(clientRegistrationRepository);
		}

		@Bean
		public OAuth2AuthorizedClientRepository auth2AuthorizedClientRepository(
				OAuth2AuthorizedClientService oauth2AuthorizedClientService) {
			return new AuthenticatedPrincipalOAuth2AuthorizedClientRepository(oauth2AuthorizedClientService);
		}
	}

}
