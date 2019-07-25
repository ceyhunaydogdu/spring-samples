# Spring Boot Secure Web Application with OAuth2 Login

This example illustrates how to build Spring Boot Secure Web Application with [OAuth2.0 Authorization Framework](https://tools.ietf.org/html/rfc6749#section-4.1) using the existing account at an **OAuth 2.0 Provider** *(e.g. Google)*. In order to use **OAuth2** authentication, we need to add following dependencies to `pom.xml` file.

```maven
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-oauth2-client</artifactId>
</dependency>
```

## Setting up OAuth2 Client Registration with Google

First step for meeting **OAuth** framework is to go through client registration process and get a valid `client-id` and `client-secret` from `the oauth-provider`. Since we are using Google as a`the oauth-provider`, we need to go [Google API Console](https://console.developers.google.com/) to do so.

If you are new starter, you can follow [Google's OpendID Connect Guide](https://developers.google.com/identity/protocols/OpenIDConnect) to get through OAuth2 client registration process and obtain `client-id` and `client-secret` for the application.

Upon successfully completion of client registration, we can add the following properties to the `application.yml` to tell the web app that we are going to use Google as an `the oauth-provider` and rest is upon Spring.

```yaml
spring:
  security:
    oauth2:
      client:
        registration:
          google:
            client-id: your-client-id-obtained-from-google-api-console
            client-secret: your-client-secret-obtained-from-google-api-console
```
