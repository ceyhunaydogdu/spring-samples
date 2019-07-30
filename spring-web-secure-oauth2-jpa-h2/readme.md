# Spring Boot Secure Web Application With OAuth2 and JPA Authentication Using H2 Database

This example illustrates how to build Spring Boot Secure Web Application through a login page with [OAuth2.0 Authorization Framework](https://tools.ietf.org/html/rfc6749#section-4.1)  and custom `UserDetailsService` configured to use JPA authentication based on H2 database. In order to use OAuth2, JPA and H2 DB, we need to add following dependencies to `pom.xml` file.

```maven
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-oauth2-client</artifactId>
</dependency>
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-data-jpa</artifactId>
</dependency>
<dependency>
    <groupId>com.h2database</groupId>
    <artifactId>h2</artifactId>
    <scope>runtime</scope>
</dependency>
```
