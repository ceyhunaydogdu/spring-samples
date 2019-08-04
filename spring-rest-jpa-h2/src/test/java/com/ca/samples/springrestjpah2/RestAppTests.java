package com.ca.samples.springrestjpah2;

import static org.assertj.core.api.Assertions.assertThat;

import java.net.URI;
import java.net.URISyntaxException;

import org.apache.http.client.HttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.hateoas.Resource;
import org.springframework.hateoas.Resources;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.web.client.RestTemplate;

/*
@author Ceyhun Aydoğdu <aydogdu.ceyhun@gmail.com>
*/
@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
public class RestAppTests {

	@LocalServerPort
	private int port;

	private URI base;

	@Autowired
	private TestRestTemplate testRestTemplate;

	private RestTemplate patchRestTemplate;

	@Before
	public void setUp() throws URISyntaxException {
		this.base = new URI("http://localhost:" + port + "/messages");
		// Tuning testRestTemplate to send PATCH request.
		patchRestTemplate = testRestTemplate.getRestTemplate();
		HttpClient hClient = HttpClientBuilder.create().build();
		patchRestTemplate.setRequestFactory(new HttpComponentsClientHttpRequestFactory(hClient));
	}

	@Test
	public void whenRequestAllMessages_thenOkAndMessageSize5() {
		ParameterizedTypeReference<Resources<Message>> resourceOfMessages = new ParameterizedTypeReference<Resources<Message>>() {
		};

		ResponseEntity<Resources<Message>> response = testRestTemplate.exchange(base, HttpMethod.GET, null,
				resourceOfMessages);
		assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
		assertThat(response.getBody().getContent().size()).isEqualTo(5);
	}

	@Test
	public void whenPostNewMessage_thenCreated() {
		ParameterizedTypeReference<Resource<Message>> messageResource = new ParameterizedTypeReference<Resource<Message>>() {
		};

		HttpEntity<Message> requestEntity = new HttpEntity<Message>(new Message("Hot rest, not nest !!!"));
		ResponseEntity<Resource<Message>> response = testRestTemplate.exchange(base, HttpMethod.POST, requestEntity,
				messageResource);
		assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CREATED);
		assertThat(response.getBody().getContent().getMessage()).isEqualTo("Hot rest, not nest !!!");
	}

	@Test
	public void whenFindMessageByValidId_thenSuccessAndFound() {
		ParameterizedTypeReference<Resource<Message>> messageResource = new ParameterizedTypeReference<Resource<Message>>() {
		};
		// Valid id=2 message="Rest in Peace"
		ResponseEntity<Resource<Message>> response = testRestTemplate.exchange(base + "/2", HttpMethod.GET, null,
				messageResource);
		assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
		assertThat(response.getBody().getContent().getMessage()).isEqualTo("Rest in Peace");
		assertThat(response.getBody().getLink("self").getHref()).isEqualTo("http://localhost:" + port + "/messages/2");

	}

	@Test
	public void whenFindMessageByInvalidId_thenNotFound() {
		ParameterizedTypeReference<Resource<Message>> messageResource = new ParameterizedTypeReference<Resource<Message>>() {
		};
		// Invalid id=100
		ResponseEntity<Resource<Message>> response = testRestTemplate.exchange(base + "/100", HttpMethod.GET, null,
				messageResource);
		assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
	}

	@Test
	public void whenUpdateMessage_withPut_thenSuccessAndGetUpdatedMessage() {
		ParameterizedTypeReference<Resource<Message>> messageResource = new ParameterizedTypeReference<Resource<Message>>() {
		};
		// Update id=1 message="Hello Resters!" to "Hello Testers!"
		ResponseEntity<Resource<Message>> response = testRestTemplate.exchange(base + "/1", HttpMethod.GET, null,
				messageResource);
		assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
		Message message = response.getBody().getContent();
		assertThat(message.getMessage()).isEqualTo("Hello Resters!");
		message.setMessage("Hello Testers!");
		HttpEntity<Message> requestEntity = new HttpEntity<Message>(message);
		response = testRestTemplate.exchange(base + "/1", HttpMethod.PUT, requestEntity, messageResource);
		assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
		message = response.getBody().getContent();
		assertThat(message.getMessage()).isEqualTo("Hello Testers!");
	}

	@Test
	public void whenUpdateMessage_withPatch_thenSuccessAndGetUpdatedMessage() {
		ParameterizedTypeReference<Resource<Message>> messageResource = new ParameterizedTypeReference<Resource<Message>>() {
		};
		// get id=2 message="Rest in Peace"
		ResponseEntity<Resource<Message>> response = testRestTemplate.exchange(base + "/2", HttpMethod.GET, null,
				messageResource);
		assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
		Message message = response.getBody().getContent();
		assertThat(message.getMessage()).isEqualTo("Rest in Peace");
		// Update id=2 message="Rest in Peace" to "pest in peace"
		// String patchInJson = "{\"message\":\"pest in peace\"}";
		HttpHeaders headers = new HttpHeaders();
		MediaType mediaType = new MediaType("application", "merge-patch+json");
		headers.setContentType(mediaType);
		message.setMessage("pest in peace");
		HttpEntity<Message> requestEntity = new HttpEntity<Message>(message, headers);

		response = patchRestTemplate.exchange(base + "/2", HttpMethod.PATCH, requestEntity, messageResource);
		assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
		message = response.getBody().getContent();
		assertThat(message.getMessage()).isEqualTo("pest in peace");
	}

	@Test
	public void whenDeleteMessage_thenNoContent() {
		// Delete id=3 message="Never undeRestimate"
		ResponseEntity<Void> response = testRestTemplate.exchange(base + "/3", HttpMethod.DELETE, null,
				Void.class);
		assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NO_CONTENT);
	}

	@Test
	public void whenSearchCustomByMessage_thenSuccessAndFound() {
		ParameterizedTypeReference<Resources<Message>> messageResource = new ParameterizedTypeReference<Resources<Message>>() {
		};
		// Search for "nest" keyword, result should be with id=5 and message="Who is
		// nest by the way?"
		ResponseEntity<Resources<Message>> response = testRestTemplate.exchange(base + "/search/by-message?m=nest",
				HttpMethod.GET, null, messageResource);
		assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
		assertThat(response.getBody().getContent().stream().findFirst().get().getMessage())
				.isEqualTo("Who is nest by the way?");
	}

}
