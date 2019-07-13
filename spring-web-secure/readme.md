# Spring Boot Secure Web Application

This sample project illustrates how to deploy basic security with spring boot web application. It also contains integrated security testing components.

## Testing with Security

Integration of Spring Boot with Spring Security eases to test components that interact with a security tier.

### Testing Controllers with `@WebMvcTest`

When the *`@WebMvcTest`* annotation approach is used with Spring Security, ***MockMvc* is automatically configured with the necessary filter chain** required to test our security configuration.

Since ***MockMvc*** is configured for us, we’re able to use *`@WithMockUser`* for tests without any additional configuration.

### Testing Controllers With *`@SpringBootTest`*

If the *`@SpringBootTest`* annotation is used to test controllers with Spring Security, it’s necessary **to explicitly configure the filter chain** when setting up ***MockMvc***.
