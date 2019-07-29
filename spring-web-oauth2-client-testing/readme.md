# Spring Boot Secure Web Application with OAuth2 Login and Testing

This example illustrates how to build Spring Boot Secure Web Application with [OAuth2.0 Authorization Framework](https://tools.ietf.org/html/rfc6749#section-4.1) using an **OAuth 2.0 Provider** *(e.g. Google and GitHub)* and how to build OAuth2 testing tools. In order to use **OAuth2** authentication, we need to add following dependencies to `pom.xml` file.

```maven
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-oauth2-client</artifactId>
</dependency>
<dependency>
    <groupId>net.sourceforge.htmlunit</groupId>
    <artifactId>htmlunit</artifactId>
</dependency>
```

In order to test this web application, we need to add custom test configuration classes. We added `WebSecurityTestConfig` and `TestConfig` classes embedded in `Oauth2WebAppTests` class. Then we configured mock `tokenEndPoint` and `userInfoEndPoint` for login as below.

```java
@Override
protected void configure(HttpSecurity http) throws Exception {
    http.authorizeRequests()
            .anyRequest().authenticated()
            .and()
        .oauth2Login()
            .tokenEndpoint()
                .accessTokenResponseClient(this.mockAccessTokenResponseClient())
                .and()
            .userInfoEndpoint().userService(this.mockUserService());
}
```
With this setup, we can test the web app with OAuth2 framework.
