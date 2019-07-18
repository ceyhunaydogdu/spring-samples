# Spring Boot Secure Web Application With JDBC Authentication Using H2 Database

This example illustrates how to build Spring Boot Secure Web Application configured to use JDBC authentication based on H2 database. In order to use JDBC and H2 DB, we need to add following dependencies to `pom.xml` file.

```maven
<dependency>
      <groupId>org.springframework.boot</groupId>
      <artifactId>spring-boot-starter-jdbc</artifactId>
      <scope>compile</scope>
</dependency>

<dependency>
      <groupId>com.h2database</groupId>
      <artifactId>h2</artifactId>
      <scope>runtime</scope>
</dependency>
```
