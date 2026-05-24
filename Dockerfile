FROM gradle:9.1.0-jdk17-alpine AS build

WORKDIR /workspace
COPY settings.gradle.kts build.gradle.kts ./
COPY src ./src
RUN gradle clean bootJar --no-daemon

FROM eclipse-temurin:17-jre-alpine

WORKDIR /app
RUN addgroup -S nestly && adduser -S nestly -G nestly
COPY --from=build /workspace/build/libs/*.jar app.jar
USER nestly

EXPOSE 8080
ENTRYPOINT ["java", "-jar", "/app/app.jar"]
