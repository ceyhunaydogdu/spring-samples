# Spring Boot Secure Web Application With JPA enabled JDBC Authentication Using MySQL Database

This example illustrates how to build Spring Boot Secure Web Application with custom `UserDetailsService` and password encryiption configured to use JDBC authentication based on MySQL database. In order to use JPA enabled JDBC and MySQl DB, we need to add following dependencies to `pom.xml` file

```maven
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-data-jdbc</artifactId>
</dependency>


```

