# Shopping

The shopping service is a tool to manage shopping lists.

## Build

The service can be build using a Docker build file. It builds both backend and frontend and combines them in the
resulting docker image.

## Start development environment

### Dependencies
The service requires an OAuth Server and a MongoDB Server that can be spun up using Docker Compose.
```bash
cd docker && docker compose up -d
```

### Backend
The service's backend can be started using the Gradle build system.

```bash
cd backend && ./gradlew bootRrun
```
### Frontend
The service's frontend can be started using the Npm build system. It also provides a proxy that forwards all
calls to the api endpoints to the development backend instance. 
```bash
cd frontend && npm run dev 
```

## Tech Stack

### Backend

- [Gradle](https://gradle.org/)
- [Spring Boot Webflux](https://docs.spring.io/spring-framework/reference/web/webflux.html)
- [Spring Security OAuth2](https://docs.spring.io/spring-security/reference/servlet/oauth2/index.html)
- [KMongo](https://litote.org/kmongo/)
- [TestContainers](https://testcontainers.com/)
- [KTor Http Client](https://ktor.io/docs/getting-started-ktor-client.html)


### Frontend

- [Vite](https://vitejs.dev/)
- [Axios](https://github.com/axios/axios)
- [React Router](https://reactrouter.com/en/main)
- [React Query](https://tanstack.com/query/v3/)
- [React Material](https://mui.com/material-ui/)