Для запуска:
1. Закинуть .env в корень

<img width="306" alt="image" src="https://github.com/user-attachments/assets/e030b9c4-8c0d-44d6-a985-1670c47dc349" />

<img width="439" alt="image" src="https://github.com/user-attachments/assets/ccd9cbd5-6ee3-4560-bcc3-e638602c6765" />

Будем надеятся что злоумышленники не будут читать этот файл

2. Зайти в консоль из папки проекта

<img width="440" alt="image" src="https://github.com/user-attachments/assets/8ff849a2-bcf0-46ab-81e4-a6f882bc0050" />

Запускаем docker-compose (Предварительно открыть docker desktop)

<img width="571" alt="image" src="https://github.com/user-attachments/assets/904871d7-425b-4fd0-b5a7-0cc5c77a7abc" />

3. Среда Keycloak тоже создается в докере при помощи docker-compose (файл уже прописан)

После запуска контейнера переходим на http://localhost:8081, вводим логин и пароль (лежат в .env)

4. Добавляем новый realm

<img width="275" alt="image" src="https://github.com/user-attachments/assets/bb4d3cf6-546b-4ce5-85b9-e80e7017e078" />

5. Загружаем туда realm-export.json

<img width="1064" alt="image" src="https://github.com/user-attachments/assets/b4f9d1f9-89da-44c9-ba30-d253d1474b98" />

6. Теперь можно запускать проект

После запуска его можно потыкать либо в постгресе, либо в сваггере http://localhost:8080/swagger-ui.html



