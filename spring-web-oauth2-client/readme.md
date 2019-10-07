# Spring Boot Secure Web Application with OAuth2 Login

This example illustrates how to build Spring Boot Secure Web Application with [OAuth2.0 Authorization Frameworks](https://tools.ietf.org/html/rfc6749#section-4.1) using the existing account at an **OAuth 2.0 Provider** *(e.g. Google, GitHub and Okta)*. In order to use **OAuth2** authentication, we need to add following dependencies to `pom.xml` file.

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

## Setting up OAuth2 Client Registration with Google, GitHub and Okta

First step for meeting **OAuth2** framework is to go through client registration process and get a valid `client-id` and `client-secret` from `the oauth-provider`. During registration, you need to specify redirect URI by using `{baseUrl}/login/oauth2/code/{registrationId}` template. The `baseURL` would be the address of the web server, where `registrationId` is the name of the `the oauth-provider`.

For Google as `the oauth-provider`, we need to go to [Google API Console](https://console.developers.google.com/) to register our app as client. If you are new starter, you can follow [Google's OpendID Connect Guide](https://developers.google.com/identity/protocols/OpenIDConnect) to get through OAuth2 client registration process and obtain `client-id` and `client-secret` for the application. Redirect-URI for Google would be `http://localhost:8080/login/oauth2/code/google`.

For GitHub as `the oauth-provider`, we need to go to [GitHub Application](https://github.com/settings/applications/new) page to register our app as client. You can follow [GitHub's Authorizing OAuth Apps Guide](https://developer.github.com/apps/building-oauth-apps/authorizing-oauth-apps/#web-application-flow) to get through OAuth2 client registration process and obtain `client-id` and `client-secret` for the application.Redirect-URI for GitHub would be `http://localhost:8080/login/oauth2/code/github`.

For Okta as `the oauth-provider`, we need to go to ***Okta Application*** page to register our app as client. You can follow [Okta's Sign users in to your Web Application Guide](https://developer.okta.com/docs/guides/sign-into-web-app/springboot/create-okta-application/) to get through OAuth2 client registration process and obtain `client-id` and `client-secret` for the application. Redirect-URI for Okta would be `http://localhost:8080/login/oauth2/code/okta`. Since Okta gives subdomains called tennants, we need additional properties to define our subdomain as auth-provider in `application.yml` as shown below.

Upon successfully completion of client registrations, we can add the following properties to the `application.yml` to tell the web app that we are going to use Google, GitHub and Okta as an `the oauth-provider` and rest is upon Spring.

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
          okta:
            client-id: your-client-id-obtained-from-okta
            client-secret: your-client-secret-obtained-from-okta
        provider:
          okta:
            authorization-uri: https://[your-domain].okta.com/oauth2/v1/authorize
            token-uri: https://[your-domain].okta.com/oauth2/v1/token
            user-info-uri: https://[your-domain].okta.com/oauth2/v1/userinfo
            user-name-attribute: sub
            jwk-set-uri: https://[your-domain].okta.com/oauth2/v1/keys
```
