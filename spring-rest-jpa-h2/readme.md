# Spring Boot Rest Application with RestRepository using JPA and H2

This example illustrates how to build Spring Boot Rest Application with RestRepository using Java Persistence API and H2 inMemory database. In order to build the application, we need to add following dependencies to `pom.xml` file.

```maven
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-data-rest</artifactId>
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

## Setting the REST

First of all, we created `Message` entity class as shown below to persist messages into database.

```java
@Entity
public class Message {
    @Id
    @GeneratedValue
    private Long id;
    private String message;

    public Message() {
    }

    public Message(String message) {
        this.message = message;
    }

   //and Getters and Setters 
}
```

After that, we created `MessageRepo` repository as below to interact with database. Within repository, custom `findByMessageContaining` method is added for search conveniency. Note that we used, `@RepositoryRestResource` annotatition to indicate the `MessageRepo` is a source for rest api. Besides, `@RestResource` annotation is used to change path name to "by-message" of the method which is "findByMessageContaining" by default for searching.

```java
@RepositoryRestResource
interface MessageRepo extends JpaRepository<Message, Long>{
    @RestResource(path = "by-message")
    Collection<Message> findByMessageContaining(@Param("mes") String m);
}
```

Lastly, we need some dummy data to test our application. To do that, we used `CommandLineRunner` bean depicted as below to insert dummy data to database.

```java
@Bean
CommandLineRunner cLineRunner(MessageRepo messageRepo){
    return args -> {
        Stream.of("Hello resters!","Rest in Peace", "Never underestimate", "Rest of the world","Who is nest?")
            .forEach(m -> messageRepo.save(new Message(m)));

    };
}
```

## In Action

Thats all, we just need to run application and see if it's working.
If we go to [http://localhost:8080/messages](http://localhost:8080/messages) rest endpoint, all messages would be returned as below.

```json
{
  "_embedded" : {
    "messages" : [ {
      "message" : "Hello resters!",
      "_links" : {
        "self" : {
          "href" : "http://localhost:8080/messages/1"
        },
        "message" : {
          "href" : "http://localhost:8080/messages/1"
        }
      }
    }, {
      "message" : "Rest in Peace",
      "_links" : {
        "self" : {
          "href" : "http://localhost:8080/messages/2"
        },
        "message" : {
          "href" : "http://localhost:8080/messages/2"
        }
      }
    }, {
      "message" : "Never underestimate",
      "_links" : {
        "self" : {
          "href" : "http://localhost:8080/messages/3"
        },
        "message" : {
          "href" : "http://localhost:8080/messages/3"
        }
      }
    }, {
      "message" : "Rest of the world",
      "_links" : {
        "self" : {
          "href" : "http://localhost:8080/messages/4"
        },
        "message" : {
          "href" : "http://localhost:8080/messages/4"
        }
      }
    }, {
      "message" : "Who is nest?",
      "_links" : {
        "self" : {
          "href" : "http://localhost:8080/messages/5"
        },
        "message" : {
          "href" : "http://localhost:8080/messages/5"
        }
      }
    } ]
  },
  "_links" : {
    "self" : {
      "href" : "http://localhost:8080/messages{?page,size,sort}",
      "templated" : true
    },
    "profile" : {
      "href" : "http://localhost:8080/profile/messages"
    },
    "search" : {
      "href" : "http://localhost:8080/messages/search"
    }
  },
  "page" : {
    "size" : 20,
    "totalElements" : 5,
    "totalPages" : 1,
    "number" : 0
  }
}

```

When we enter [http://localhost:8080/messages/search/by-message?m=nest](http://localhost:8080/messages/search/by-message?mes=nest) for custom search (messages containing "nest" keyword), output would be as below.

```json
{
  "_embedded" : {
    "messages" : [ {
      "message" : "Who is nest?",
      "_links" : {
        "self" : {
          "href" : "http://localhost:8080/messages/5"
        },
        "message" : {
          "href" : "http://localhost:8080/messages/5"
        }
      }
    } ]
  },
  "_links" : {
    "self" : {
      "href" : "http://localhost:8080/messages/search/by-message?mes=nest"
    }
  }
}

```

## Testing

We can test the REST Application with http GET, POST, PUT and DELETE request using `TestRestTemplate`. However, Spring `TestRestTemplate` does not support PATCH operation out of box, so we need to add following dependency to `pom.xml` file to enable PATCH operations.

```maven
<dependency>
  <groupId>org.apache.httpcomponents</groupId>
  <artifactId>httpclient</artifactId>
  <version>4.5.9</version>
  <scope>test</scope>
</dependency>
```

After adding library, we need to tune `TestRestTemplate` as follows to enable `TestRestTemplate` to send PATCH requests accordingly.

```java
//Tuning testRestTemplate to send PATCH request.
patchRestTemplate=testRestTemplate.getRestTemplate();
HttpClient hClient = HttpClientBuilder.create().build();
patchRestTemplate.setRequestFactory(new HttpComponentsClientHttpRequestFactory(hClient));
```
