package com.ca.samples.springrestjpah2;

import static org.assertj.core.api.Assertions.assertThat;

import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;

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
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringRunner;
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
	private TestRestTemplate restTemplate;

	@Before
	public void setUp() throws URISyntaxException {
		this.base=new URI("http://localhost:"+port+"/messages");
	}

	@Test
	public void whenRequestAllMessages_thenOkAndMessageSize5() {
		ParameterizedTypeReference<Resources<Message>> resourceOfMessages=new ParameterizedTypeReference<Resources<Message>>() {};

		ResponseEntity<Resources<Message>> response = restTemplate.exchange(base, HttpMethod.GET, null,	resourceOfMessages);
		assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
		assertThat(response.getBody().getContent().size()).isEqualTo(5);
	}
	
	@Test
	public void whenPostNewMessage_thenCreated() {
		ParameterizedTypeReference<Resource<Message>> messageResource=new ParameterizedTypeReference<Resource<Message>>() {};
		
		HttpEntity<Message> requestEntity= new HttpEntity<Message>(new Message("Hot rest, not nest !!!"));
		ResponseEntity<Resource<Message>> response = restTemplate.exchange(base, HttpMethod.POST, requestEntity ,messageResource);
		assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CREATED);
		assertThat(response.getBody().getContent().getMessage()).isEqualTo("Hot rest, not nest !!!");
	}
	
	
	@Test
	public void whenFindMessageByValidId_thenSuccessAndFound() {
		ParameterizedTypeReference<Resource<Message>> messageResource=new ParameterizedTypeReference<Resource<Message>>() {};
		//Valid id=2 message="Rest in Peace"
		ResponseEntity<Resource<Message>> response = restTemplate.exchange(base+"/2", HttpMethod.GET, null ,messageResource);
		assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
		assertThat(response.getBody().getContent().getMessage()).isEqualTo("Rest in Peace");
		assertThat(response.getBody().getLink("self").getHref()).isEqualTo("http://localhost:"+port+"/messages/2");

	}
	
	@Test
	public void whenSearchCustomByMessage_thenSuccessAndFound() {
		ParameterizedTypeReference<Resources<Message>> messageResource=new ParameterizedTypeReference<Resources<Message>>() {};
		//Search for "nest" keyword, result should be with id=5 and message="Who is nest by the way?"
		ResponseEntity<Resources<Message>> response = restTemplate.exchange(base+"/search/by-message?m=nest", HttpMethod.GET, null ,messageResource);
		assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
		assertThat(response.getBody().getContent().stream().findFirst().get().getMessage()).isEqualTo("Who is nest by the way?");
	}

	@Test
	public void whenFindMessageByInvalidId_thenNotFound() {
		ParameterizedTypeReference<Resource<Message>> messageResource=new ParameterizedTypeReference<Resource<Message>>() {};
		//Invalid id=100
		ResponseEntity<Resource<Message>> response = restTemplate.exchange(base+"/100", HttpMethod.GET, null ,messageResource);
		assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
	}


	

}
