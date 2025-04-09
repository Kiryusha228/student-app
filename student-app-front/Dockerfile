# Используем официальный образ Node.js в качестве базового образа
FROM node:16-alpine as builder

# Устанавливаем рабочую директорию в контейнере
WORKDIR /app

# Копируем package.json и package-lock.json (или yarn.lock)
COPY package*.json ./

# Устанавливаем зависимости
RUN npm install

# Копируем все файлы проекта в контейнер
COPY . .

# Собираем React-приложение для production
RUN npm run build

# Используем образ Nginx для обслуживания статических файлов
FROM nginx:alpine

# Копируем собранные файлы из предыдущего этапа в директорию Nginx
COPY --from=builder /app/build /usr/share/nginx/html

# Открываем порт 80 для доступа к приложению
EXPOSE 80

# Запускаем Nginx в фоновом режиме
CMD ["nginx", "-g", "daemon off;"]