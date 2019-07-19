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

## Configuring JDBC Authentication

While configuring db for authentication, we need to create `users` and `authorities` tables
before running the app. `schema.sql` as below is used to create tables in db automatically.

```sql
create table users (
  username varchar(256),
  password varchar(256),
  enabled boolean
);

create table authorities (
  username varchar(256),
  authority varchar(256)
);
```

Likewise, `data.sql` as below is used to populate the db with default users.

```sql
insert into users (username, password, enabled) values ('user', '{noop}password', true);
insert into users (username, password, enabled) values ('ceyhun', '{noop}ceyhun', true);

insert into authorities (username, authority) values ('ceyhun', 'ROLE_ADMIN');
insert into authorities (username, authority) values ('user', 'ROLE_USER')
```

Note that, since we use H2 in memory db, we need to create and populate the db on ever startup.

### First alternative for using jdbc authentication

When `JdbcUserDetailsManager` is used, `schema.sql` and `data.sql` should also be included
in resources folder in order to create users and authorities tables and populate the db. 

```java
@Bean
public JdbcUserDetailsManager jdbcUserDetailsManager(DataSource dataSource) {
  JdbcUserDetailsManager detailsManager=new JdbcUserDetailsManager();
    detailsManager.setDataSource(dataSource);
  return detailsManager; 
}
```

### Second alternative for using jdbc authentication

When `AuthenticationManagerBuilder` is used, we can either include the sql file or not. If
we use the code below, then we should include `schema.sql` and `data.sql` files in resource folder this specific setup.

```java
@Override
protected void configure(AuthenticationManagerBuilder auth) throws Exception {
   auth
    .jdbcAuthentication()
      .dataSource(datasource);
}
```
On the other hand, as variant of `AuthenticationManagerBuilder`, we can also use the code below.

```java
@Override
protected void configure(AuthenticationManagerBuilder auth) throws Exception {
      UserBuilder users = User.withDefaultPasswordEncoder();
        auth
            .jdbcAuthentication()
                .dataSource(datasource)
                .withDefaultSchema()
                .withUser(users.username("user").password("user").roles("USER"))
                .withUser(users.username("ceyhun").password("ceyhun").roles("USER", "ADMIN"));
}
```

Since we are using default schema and populate db with users within this method, we do not need to include sql setup files in resources folder.
