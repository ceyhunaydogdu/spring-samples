# Spring Boot Secure Web Application with OAuth2 Login

This example illustrates how to build Spring Boot Secure Web Application with [OAuth2.0 Authorization Framework](https://tools.ietf.org/html/rfc6749#section-4.1) using the existing account at an **OAuth 2.0 Provider** *(e.g. Google and GitHub)*. In order to use **OAuth2** authentication, we need to add following dependencies to `pom.xml` file.

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

## Setting up OAuth2 Client Registration with Google and GitHub

First step for meeting **OAuth2** framework is to go through client registration process and get a valid `client-id` and `client-secret` from `the oauth-provider`. During registration, you need to specify redirect URI as following `{baseUrl}/login/oauth2/code/{registrationId}` template.

For Google as `the oauth-provider`, we need to go [Google API Console](https://console.developers.google.com/) to register our app as client. If you are new starter, you can follow [Google's OpendID Connect Guide](https://developers.google.com/identity/protocols/OpenIDConnect) to get through OAuth2 client registration process and obtain `client-id` and `client-secret` for the application. Redirect-URI for Google would be `http://localhost:8080/login/oauth2/code/google`.

For GitHub as `the oauth-provider`, we need to go [GitHub Application](https://github.com/settings/applications/new) page to register our app as client. You can follow [GitHub's Authorizing OAuth Apps Guide](https://developer.github.com/apps/building-oauth-apps/authorizing-oauth-apps/#web-application-flow) to get through OAuth2 client registration process and obtain `client-id` and `client-secret` for the application.Redirect-URI for GitHub would be `http://localhost:8080/login/oauth2/code/github`.

Upon successfully completion of client registrations, we can add the following properties to the `application.yml` to tell the web app that we are going to use Google and GitHub as an `the oauth-provider` and rest is upon Spring.

```yaml
spring:
  security:
    oauth2:
      client:
        registration:
          google:
            client-id: your-client-id-obtained-from-google-api-console
            client-secret: your-client-secret-obtained-from-google-api-console
          github:
            client-id: your-client-id-obtained-from-github
            client-secret: your-client-secret-obtained-from-github
```
