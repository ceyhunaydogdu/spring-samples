# Spring Boot Secure Web Application with OAuth2 Login

This example illustrates how to build Spring Boot Secure Web Application with [OAuth2.0 Authorization Framework](https://tools.ietf.org/html/rfc6749#section-4.1) using the existing account at an **OAuth 2.0 Provider** *(e.g. Google)*. In order to use **OAuth2** authentication, we need to add following dependencies to `pom.xml` file.

```maven
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-oauth2-client</artifactId>
</dependency>
```

## Setting up OAuth2 Client Registration with Google

First step for setup, [Google API Console](https://console.developers.google.com/).