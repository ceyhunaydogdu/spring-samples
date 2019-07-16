# Spring Boot Web Greeting Application on Docker

This sample project illustrates how to serve Spring Boot Web Application on Docker platform. Greeting application is simple app which greets user with customized message leveraging request param within url. We can test and run our application using either maven or gradle. After running the app, go to http://localhost:8080 to see our greeting app.

## Deploying Application on Docker

Using maven wrapper `./mvnw package` command we created jar file which is needed while building docker image. Our web appliction jar path would be something like `./target/spring-web-docker-0.0.1-SNAPSHOT.jar`. Then we need to add ***`Dockerfile`*** to the web app directory which would contain following docker commands.

```docker
FROM leegreiner/11-jre-alpine
VOLUME [ "/tmp" ]
ARG JAR_FILE=./target/spring-web-docker-0.0.1-SNAPSHOT.jar
COPY ${JAR_FILE} app.jar
ENTRYPOINT [ "java", "-Djava.security.egd=file:/dev/./urandom", "-jar", "/app.jar" ]
```

As we can see, our docker image is based on `leegreiner/11-jre-alpine` which about 60 MB in size and meet our needs. We also defined argument `JAR_FILE=./target/spring-web-docker-0.0.1-SNAPSHOT.jar` to tell docker where to find our application jar file. And with `ENTRYPOINT` we simply write the necessary command to start up the application. After saving the dockerfile, we built docker image with "`docker build -t ceyhunaydogdu/spring-web-greeting:latest .`" command. Following a successful built, we can see our newly created app image using `docker images` command under `ceyhunaydogdu/spring-web-greeting:latest` name.

As a last step we can run docker image using `docker run --rm -dit -p 80:8080 --name spring-web ceyhunaydogdu/spring-web-greeting:latest` command on Docker. Then we can go ahead and see our app running on http://localhost. Note that since we defined port 80 to be exposed to outside world, we don't need to add port 8080 to the URL.

### Alternative Way of Building Docker Image with Maven

To implement less tedious and faster way of building docker image, we can take advantage of the separation between dependencies and application resources in a Spring Boot fat jar file. We need to use following ***`Dockerfile`*** with different configuration.

```docker
FROM leegreiner/11-jre-alpine
VOLUME [ "/tmp" ]
ARG DEPENDENCY=target/dependency
COPY ${DEPENDENCY}/BOOT-INF/lib /app/lib
COPY ${DEPENDENCY}/META-INF /app/META-INF
COPY ${DEPENDENCY}/BOOT-INF/classes /app
ENTRYPOINT ["java","-cp","app:app/lib/*","com.ca.samples.springwebdocker.GreetingApplication"]
```

In this configuration, the `DEPENDENCY` argument points to a directory of unpacked fat jar which is in turn used for to locate the BOOT-INF/lib directory with the dependency jars in it, and the BOOT-INF/classes directory with the application classes in it. Note that we used the application main class `com.ca.samples.springwebdocker.GreetingApplication` in the `ENTRYPOINT` which provides a little bit faster startups.

In order to build the docker image with Maven, we need to add the following tool to `pom.xml` file. The repository tag with following configuration will result in `ceyhunaydogdu/spring-web-docker:latest` as the docker image name.

```maven
<properties>
   <docker.image.prefix>ceyhunaydogdu</docker.image.prefix>
</properties>
<build>
    <plugins>
        <plugin>
            <groupId>com.spotify</groupId>
            <artifactId>dockerfile-maven-plugin</artifactId>
            <version>1.4.10</version>
            <configuration>
                <repository>${docker.image.prefix}/${project.artifactId}</repository>
            </configuration>
        </plugin>
    </plugins>
</build>
```

Also, to ensure the application jar is unpacked before the docker image is created, we need to add some configuration the plugins to `pom.xml` file.

```maven
<plugin>
    <groupId>org.apache.maven.plugins</groupId>
    <artifactId>maven-dependency-plugin</artifactId>
    <executions>
        <execution>
            <id>unpack</id>
            <phase>package</phase>
            <goals>
                <goal>unpack</goal>
            </goals>
            <configuration>
                <artifactItems>
                    <artifactItem>
                        <groupId>${project.groupId}</groupId>
                        <artifactId>${project.artifactId}</artifactId>
                        <version>${project.version}</version>
                    </artifactItem>
                </artifactItems>
            </configuration>
        </execution>
    </executions>
</plugin>
```

After all setup done, we can use maven with `./mvnw install dockerfile:build` command to create docker image. Then, we can see docker images with `docker images` using Docker’s CLI, if Docker created the image properly or not. And we can also push the image to dockerhub with `./mvnw dockerfile:push`.

In addition, we can even make `dockerfile:push` automatically run in the install or deploy lifecycle phases by the followings to the plugin configuration.

```maven
<executions>
    <execution>
        <id>default</id>
        <phase>install</phase>
        <goals>
            <goal>build</goal>
            <goal>push</goal>
        </goals>
    </execution>
</executions>
```

After adding the above configuration, we can just use `./mvnw intsall` maven command to automatically build and push docker image along with maven install phase.

## Testing Greeting Application

Spring Boot eases to test components with ***MockMvc***. *`GreetingControllerTest`* class allows us to test the application with default and custom users.
