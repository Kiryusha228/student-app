FROM gradle:jdk17-focal AS build

WORKDIR /app

COPY build.gradle.kts build.gradle.kts
COPY settings.gradle.kts settings.gradle.kts
COPY .env .env

COPY student-app-back/src student-app-back/src
COPY student-app-back/build.gradle.kts student-app-back/build.gradle.kts

RUN gradle student-app-back:build --no-daemon

FROM openjdk:17-slim

WORKDIR /app

COPY --from=build /app/student-app-back/build/libs/*.jar app/student-app-back/app.jar
COPY --from=build /app/.env .env

CMD ["java", "-jar", "app/student-app-back/app.jar"]
