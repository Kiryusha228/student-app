# Базовый образ с Java и Gradle
FROM gradle:jdk17-focal AS build

# Устанавливаем рабочую директорию
WORKDIR /app

# Копируем файлы, необходимые для сборки
COPY build.gradle.kts build.gradle.kts
COPY settings.gradle.kts settings.gradle.kts
COPY .env .env

# Копируем исходный код
COPY tg-bot/src tg-bot/src
COPY tg-bot/build.gradle.kts tg-bot/build.gradle.kts

# Собираем проект
RUN gradle build --no-daemon

# Используем официальный образ Java для запуска приложения
FROM openjdk:17-slim

# Устанавливаем рабочую директорию
WORKDIR /app

# Копируем собранный JAR из предыдущего этапа
COPY --from=build /app/tg-bot/build/libs/*.jar app/tg-bot/app.jar
COPY --from=build /app/.env .env

# Команда для запуска приложения
CMD ["java", "-jar", "app/tg-bot/app.jar"]