Для запуска:
1. Закинуть .env в корень

<img width="306" alt="image" src="https://github.com/user-attachments/assets/e030b9c4-8c0d-44d6-a985-1670c47dc349" />

<img width="439" alt="image" src="https://github.com/user-attachments/assets/ccd9cbd5-6ee3-4560-bcc3-e638602c6765" />

Будем надеятся что злоумышленники не будут читать этот файл

2. Зайти в консоль из папки проекта

<img width="440" alt="image" src="https://github.com/user-attachments/assets/8ff849a2-bcf0-46ab-81e4-a6f882bc0050" />

Запускаем docker-compose (Предварительно открыть docker desktop)

<img width="571" alt="image" src="https://github.com/user-attachments/assets/904871d7-425b-4fd0-b5a7-0cc5c77a7abc" />

3. Среда Keycloak тоже создается в докере при помощи docker-compose (файл уже прописан), предварительно надо в корне проекта создать папку keycloak, и положить туда realm-export.json

После запуска контейнера переходим на http://localhost:8081, вводим логин и пароль (лежат в .env)

4. Добавляем новый client, импортируя туда springsecurity.json

<img width="870" alt="image" src="https://github.com/user-attachments/assets/bd52cdc0-f1ba-42fa-9aee-4bde30b208b8" />

5. Теперь можно запускать проект

После запуска его можно потыкать либо в постгресе, либо в сваггере http://localhost:8080/swagger-ui/index.html



