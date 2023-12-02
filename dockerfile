FROM eclipse-temurin:21-alpine as builder
WORKDIR application
COPY backend/build/libs/*-application.jar application.jar
RUN java -Djarmode=layertools -jar application.jar extract

FROM eclipse-temurin:21-alpine
COPY frontend/dist /frontend
COPY --from=builder application/dependencies/ backend/
COPY --from=builder application/snapshot-dependencies/ backend/
COPY --from=builder application/spring-boot-loader/ backend/
COPY --from=builder application/application/ backend/
WORKDIR /backend
ENV SPRING_WEB_RESOURCES_STATIC_LOCATIONS=file:/frontend
ENTRYPOINT ["java", "org.springframework.boot.loader.launch.JarLauncher"]