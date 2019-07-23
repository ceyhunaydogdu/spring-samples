# Spring Boot Secure Web Application With JPA Authentication Using H2 Database

This example illustrates how to build Spring Boot Secure Web Application with custom `UserDetailsService` configured to use JPA authentication based on H2 database. In order to use JPA and H2 DB, we need to add following dependencies to `pom.xml` file.

```maven
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

## Configuring Custom UserDetailsService

In order to create **`CustomUserDetailsService`**, we implemented `UserDetailsService` interface which has one method named `loadUserByUsername()` to be customized as below for process of finding the user.

```java
@Override
public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
    User user=userRepository.findByUsername(username);
    if (user==null) {
        throw new UsernameNotFoundException("Username: "+username+" not found on repository ");
    }
    return new CustomPrincipal(user);
}
```

Since we configured **`CustomUserDetailsService`** class with the ***`@Service`*** annotation, the spring application will automatically detect and create it during component-scan. Thus we do not need to add any further configuration.

As you can see from the code above, we also need a `CustomPrincipal` class which implements `UserDetails` interface to convert user data to a format which enables security measures to be conducted.

## Interacting with Database

Besides, we also created `User` entity class and `UserRepository` interface to store and retrieve user data from database. `UserRepository` has just one custom method to enable finding users with name as depicted below.

```java
public interface UserRepository extends JpaRepository<User, Long> {
    User findByUsername(String username);
}

```
