# Spring Boot Secure Web Application With JDBC Authentication Using MySQL Database

This example illustrates how to build Spring Boot Secure Web Application with custom `UserDetailsService` and password encryiption configured to use JDBC authentication based on MySQL database. In order to use JDBC and MySQl DB, we need to add following dependencies to `pom.xml` file

```maven
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-data-jdbc</artifactId>
</dependency>

<dependency>
      <groupId>mysql</groupId>
      <artifactId>mysql-connector-java</artifactId>
      <scope>runtime</scope>
    </dependency>
```

## Interacting with MySQL Database

 We created `User` entity class and `UserRepository` interface to store and retrieve user data from database. `UserRepository` has just one custom method to enable finding users with name as depicted below. Here, we used native query definition and passed a parameter to the query via name using the **`@Param`** annotation.

 ```java
 public interface UserRepository extends CrudRepository<User, Long> {
    @Query("select * from user where username=:name")
    User findByUsername(@Param("name") String username);
}
 ```

To establish a connection to MySQL db, first we need to start MySQL server locally. Then we create user account (account name:test, password:test in this case) and grant permission so that the application can access db seamlessly.

```mysql
CREATE USER 'test'@'localhost' IDENTIFIED BY 'test';

GRANT All ON Test.* TO test@localhost
```

Since, we are not using JPA library, we need to ensure that the necessarry tables (User table) are  created and populated with data.

```mysql
CREATE TABLE IF NOT EXISTS `user` (
  `id` INT UNSIGNED NOT NULL AUTO_INCREMENT,
  `username` VARCHAR(255),
  `password` varchar(255),
  PRIMARY KEY `pk_id`(`id`),  
  UNIQUE KEY `unique_username`(`username`));
```

For the purpose of testing, we inserted two users namely `user` and `ceyhun` in to the user table with below code.

```mysql
insert into user (id,username, password) values (null,'user', '{noop}user');
insert into user (id,username, password) values (null,'ceyhun', '{noop}ceyhun');
```

Note that passwords are prefixed with `{noop}` phrase meaning that passwords are in plain text mode.

## Testing 

While testing the db connection, we used the `UserRepository` and tested db with following methods against the two records entered during db-setup.

```java
@Autowired
private UserRepository userRepository;

@Test
public void whenFindAllUserFromRepository_thenFind2() {
  assertThat(this.userRepository.findAll()).hasSize(2);
}

@Test
public void whenFindByNameValid_thenMatch() {
  assertThat(this.userRepository.findByUsername("ceyhun").getUsername()).isEqualTo("ceyhun");
}

@Test
public void whenFindByNameInvalid_thenNoMatch() {
  assertThat(this.userRepository.findByUsername("invalid")).isNull();
}
```
