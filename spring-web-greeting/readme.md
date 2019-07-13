# Spring Boot Web Greeting Application

This sample project illustrates how to serve web content with Spring Boot Web Application. Greeting application greets user with customized message leveraging request param within url.

## Testing Greeting Application

Spring Boot eases to test components with ***MockMvc***. *`GreetingControllerTest`* class allows us to test the application with default and custom users.

### Testing Controllers With Default User

When testing with http://localhost:8080/greeting url in the  [*GreetingControllerTest#greetWithDefaultUser()*](src\test\java\com\ca\samples\springwebgreeting\GreetingControllerTest.java) method, the application greets with default user which is set as "Spring Booter" in the *`GreetingController`* class.

### Testing Controllers With Custom User

When testing with [*GreetingControllerTest#greetWithCustomUser()*](src\test\java\com\ca\samples\springwebgreeting\GreetingControllerTest.java) method, the application greets with custom user which is set as request param within test method by building http://localhost:8080/greeting?name=Ceyhun url.
