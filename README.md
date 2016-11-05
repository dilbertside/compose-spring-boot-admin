# Spring Boot Admin

This demo shows how to start in Docker a Spring Boot Admin application and two Spring Boot application using Docker Compose.

First, you need to install [Docker and Docker Compose](https://docs.docker.com/compose/#installation-and-set-up).

## Run the demo
To run the demo, you just need to start Docker Compose:

```bash
docker network create monitor
docker-compose up
```

This command creates three docker containers one for the admin application and two for the client applications.

You can access the application with the URL: [http://localhost:8080](http://localhost:8080)

You can check the containers status using the command:

```bash
docker-compose ps
```

## fork changes

* upgrade [Spring Boot](http://projects.spring.io/spring-boot/) to 1.4.1.RELEASE
* upgrade [Spring Boot Admin](https://github.com/codecentric/spring-boot-admin) to 1.4.3
* use [docker compose file definition version 2](https://docs.docker.com/compose/compose-file/#/version-2)
* use [docker network](https://docs.docker.com/engine/userguide/networking/) instead of link


