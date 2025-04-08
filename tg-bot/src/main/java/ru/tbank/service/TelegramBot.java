package ru.tbank.service;

import org.springframework.http.MediaType;
import lombok.extern.slf4j.Slf4j;
//import org.example.model.dto.database.CreateProjectWorkshopDto;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageReplyMarkup;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import ru.tbank.config.BotConfig;
import ru.tbank.model.dto.database.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
@Slf4j
public class TelegramBot extends TelegramLongPollingBot {

    private BotConfig config;
    private final WebClient webClient;

    @Autowired
    public TelegramBot(BotConfig config, WebClient.Builder webClientBuilder) {
        this.config = config;
        this.webClient = webClientBuilder.baseUrl("http://localhost:8080/").build();
    }

    @Override
    public String getBotUsername() {
        return config.getBotName();
        //return "botName";
    }

    @Override
    public String getBotToken() {
        return config.getToken();
        //return "token";
    }

    private int pwsYear;

    private Map<Long, String> userStates = new HashMap<>();

    /*private Map<String, String> RESPONCE = new HashMap<>();
    {
        RESPONCE.put("bot_name", "Мое имя: ");
        RESPONCE.put("token", "Мой токен: ");
        RESPONCE.put("bot_name", "Мое имя: ");
        RESPONCE.put("token", "Мой токен: ");
        RESPONCE.put("bye", "Пока! Если понадоблюсь еще: обязательно пиши!");
    }*/

    @Override
    public void onUpdateReceived(Update update) {
        //CurrencyModel currencyModel = new CurrencyModel();
        String currency = "default answer";

        if (update.hasCallbackQuery()) {
            var data = update.getCallbackQuery().getData();
            long chatId = update.getCallbackQuery().getMessage().getChatId();
            int messageId = update.getCallbackQuery().getMessage().getMessageId();

            // Обработка нажатия на кнопку "Назад"
            if ("cancel".equals(data)) {
                editAndSendNewButtons(chatId, messageId, sendStartButtons(chatId));
                return;
            }

            // Обработка нажатия на кнопку "О студенте"
            if ("about_student".equals(data)) {
                editAndSendNewButtons(chatId, messageId, sendStudentButtons(chatId));
                return;
            }

            if ("about_pws".equals(data)) {
                editAndSendNewButtons(chatId, messageId, sendPwsButtons(chatId));
                return;
            }

            //Найти студента по имени
            if ("find_student_name".equals(data)) {
                userStates.put(chatId, "AWAITING_NAME");
                editAndSendNewButtons(chatId, messageId, sendStudentNameButtons(chatId));
                return;
            }

            //Найти студента по телеграмму
            if ("find_student_tg".equals(data)) {
                userStates.put(chatId, "AWAITING_NICKNAME");
                editAndSendNewButtons(chatId, messageId, sendStudentTGNicknameButtons(chatId));
                return;
            }

            //Найти студентов и их роли
            if ("find_student_roles".equals(data)) {
                userStates.put(chatId, "AWAITING_YEAR && AWAITING_ROLES");
                editAndSendNewButtons(chatId, messageId, sendTeamsButtons(chatId));
                return;
            }

            //О командах
            /*if ("about_teams".equals(data)) {
                userStates.put(chatId, "AWAITING_YEAR");
                editAndSendNewButtons(chatId, messageId, sendTeamsButtons(chatId));
                return;
            }*/

            if ("add_pws".equals(data)) {
                userStates.put(chatId, "AWAITING_YEAR_TO_ADD");
                editAndSendNewButtons(chatId, messageId, sendAddPwsButtons(chatId));
                return;
            }

            //Найти команду по названию
            if ("find_team_name".equals(data)) {
                userStates.put(chatId, "AWAITING_TEAM_NAME");
                editAndSendNewButtons(chatId, messageId, sendTeamNameButtons(chatId));
                return;
            }

            //Найти команду по номеру
            if ("find_team_number".equals(data)) {
                userStates.put(chatId, "AWAITING_TEAM_NUMBER");
                editAndSendNewButtons(chatId, messageId, sendTeamNumberButtons(chatId));
                return;
            }

            //Вывести все команды
            if ("print_all_teams".equals(data)) {
                //TODO
            }

            //Другое
            if ("other".equals(data)) {
                editAndSendNewButtons(chatId, messageId, sendOtherButtons(chatId));
                return;
            }

            /*if (RESPONCE.containsKey(data)) {
                send(new SendMessage(String.valueOf(chatId), RESPONCE.get(data)));
            }*/
        }

        if(update.hasMessage() && update.getMessage().hasText()){
            String messageText = update.getMessage().getText();
            long chatId = update.getMessage().getChatId();
            //currency = update.getMessage().getChatId().toString();
            //currency = getBotUsername();
            /*currency = getBotToken();*/

            if (userStates.getOrDefault(chatId, "").equals("AWAITING_NAME")) {
                handleStudentNameInput(chatId, messageText);
                userStates.remove(chatId, "AWAITING_NAME");
                return;
            }

            if (userStates.getOrDefault(chatId, "").equals("AWAITING_NICKNAME")) {
                handleStudentNameInput(chatId, messageText);
                userStates.remove(chatId, "AWAITING_NICKNAME");
                return;
            }

            if (userStates.getOrDefault(chatId, "").equals("AWAITING_TEAM_NAME")) {
                handleTeamNameInput(chatId, messageText);
                userStates.remove(chatId, "AWAITING_TEAM_NAME");
                return;
            }

            if (userStates.getOrDefault(chatId, "").equals("AWAITING_TEAM_NUMBER")) {
                handleTeamNameInput(chatId, messageText);
                userStates.remove(chatId, "AWAITING_TEAM_NUMBER");
                return;
            }

            if (userStates.getOrDefault(chatId, "").equals("AWAITING_YEAR")) {
                handleYearInput(chatId, messageText);
                userStates.remove(chatId, "AWAITING_YEAR");
                return;
            }

            if (userStates.getOrDefault(chatId, "").equals("AWAITING_YEAR_TO_ADD")) {
                handleYearInputToAdd(chatId, messageText);
                userStates.remove(chatId, "AWAITING_YEAR_TO_ADD");
                return;
            }
            if (userStates.getOrDefault(chatId, "").equals("AWAITING_PWS_NAME")) {
                handleNameInputToAdd(chatId, messageText);
                userStates.remove(chatId, "AWAITING_PWS_NAME");
                return;
            }

            if (userStates.getOrDefault(chatId, "").equals("AWAITING_PWS") ||
                    userStates.getOrDefault(chatId, "").equals("AWAITING_ROLES")) {
                handlePWSInput(chatId, messageText);
                userStates.remove(chatId, "AWAITING_PWS");
                userStates.remove(chatId, "AWAITING_ROLES");
                return;
            }

            if (userStates.getOrDefault(chatId, "").equals("AWAITING_YEAR && AWAITING_ROLES")) {
                handleYearInput(chatId, messageText);
                userStates.remove(chatId, "AWAITING_YEAR && AWAITING_ROLES");
                userStates.put(chatId, "AWAITING_ROLES");
                return;
            }

            switch (messageText){
                case "/start":
                    startCommandReceived(chatId, update.getMessage().getChat().getFirstName());
                    send(sendStartButtons(chatId));
                    break;
                default:
                    //Пока просто сообщение
                    sendMessage(chatId, currency);
            }
        }

    }

    //Команда приветствия
    private void startCommandReceived(Long chatId, String name) {
        String answer = "Привет, " + name + ", я готов помочь тебе!" + "\n" +
                "Какой отчет ты хочешь получить?";
        sendMessage(chatId, answer);
    }

    //Метод отправки текстового сообщения
    private void sendMessage(Long chatId, String textToSend){
        SendMessage sendMessage = new SendMessage();
        sendMessage.setChatId(String.valueOf(chatId));
        sendMessage.setText(textToSend);
        send(sendMessage);
    }

    //Метод отправки сообщения
    private void send(SendMessage message) {
        try {
            execute(message);
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }

    //Кнопки
    public SendMessage sendStartButtons(long chatId) {
        SendMessage message = new SendMessage();
        message.setChatId(chatId);
        message.setText("Какую информацию ты хочешь получить?");
        var inlineKeyboardMarkup = new InlineKeyboardMarkup();
        List<List<InlineKeyboardButton>> keyboard = new ArrayList<>();
        keyboard.add(List.of(createBtn("Студенты \uD83D\uDE22", "about_student")));
        keyboard.add(List.of(createBtn("Мастерские \uD83D\uDE22", "about_pws")));
        keyboard.add(List.of(createBtn("Другое \uD83D\uDE10", "other")));
        inlineKeyboardMarkup.setKeyboard(keyboard);
        message.setReplyMarkup(inlineKeyboardMarkup);
        return message;
    }

    public SendMessage sendStudentButtons(long chatId) {
        SendMessage message = new SendMessage();
        message.setChatId(chatId);
        message.setText("Какую информацию о студенте ты хочешь получить?");
        var inlineKeyboardMarkup = new InlineKeyboardMarkup();
        List<List<InlineKeyboardButton>> keyboard = new ArrayList<>();
        keyboard.add(List.of(createBtn("Найти студента по имени \uD83D\uDE22", "find_student_name")));
        keyboard.add(List.of(createBtn("Найти студента по нику в телеграмме \uD83D\uDE22", "find_student_tg")));
        keyboard.add(List.of(createBtn("Найти студентов по роли \uD83D\uDE10", "find_student_roles")));
        keyboard.add(List.of(createBtn("Назад \uD83D\uDE04", "cancel")));
        inlineKeyboardMarkup.setKeyboard(keyboard);
        message.setReplyMarkup(inlineKeyboardMarkup);
        return message;
    }

    public SendMessage sendPwsButtons(long chatId) {
        SendMessage message = new SendMessage();
        message.setChatId(chatId);
        message.setText("Мастерские: что необходимо сделать?");
        var inlineKeyboardMarkup = new InlineKeyboardMarkup();
        List<List<InlineKeyboardButton>> keyboard = new ArrayList<>();
        keyboard.add(List.of(createBtn("Создать мастерскую \uD83D\uDE22", "add_pws")));
        keyboard.add(List.of(createBtn("Получить команды текущей мастерской \uD83D\uDE22", "find_student_tg")));
        keyboard.add(List.of(createBtn("Закрыть набор в мастерскую \uD83D\uDE10", "find_student_roles")));
        keyboard.add(List.of(createBtn("Сгенерировать команды для мастерской \uD83D\uDE10", "find_student_roles")));
        keyboard.add(List.of(createBtn("Назад \uD83D\uDE04", "cancel")));
        inlineKeyboardMarkup.setKeyboard(keyboard);
        message.setReplyMarkup(inlineKeyboardMarkup);
        return message;
    }

    public SendMessage sendStudentNameButtons(long chatId) {
        SendMessage message = new SendMessage();
        message.setChatId(chatId);
        message.setText("Напиши ФИО студента");
        var inlineKeyboardMarkup = new InlineKeyboardMarkup();
        List<List<InlineKeyboardButton>> keyboard = new ArrayList<>();
        keyboard.add(List.of(createBtn("Назад \uD83D\uDE04", "cancel")));
        inlineKeyboardMarkup.setKeyboard(keyboard);
        message.setReplyMarkup(inlineKeyboardMarkup);
        return message;
    }

    public SendMessage sendStudentTGNicknameButtons(long chatId) {
        SendMessage message = new SendMessage();
        message.setChatId(chatId);
        message.setText("Напиши никнейм студента в телеграмме");
        var inlineKeyboardMarkup = new InlineKeyboardMarkup();
        List<List<InlineKeyboardButton>> keyboard = new ArrayList<>();
        keyboard.add(List.of(createBtn("Назад \uD83D\uDE04", "cancel")));
        inlineKeyboardMarkup.setKeyboard(keyboard);
        message.setReplyMarkup(inlineKeyboardMarkup);
        return message;
    }

    public SendMessage sendTeamsButtons(long chatId) {
        SendMessage message = new SendMessage();
        message.setChatId(chatId);
        message.setText("Напиши, в каком году проводилась мастерская");
        var inlineKeyboardMarkup = new InlineKeyboardMarkup();
        List<List<InlineKeyboardButton>> keyboard = new ArrayList<>();
        keyboard.add(List.of(createBtn("Назад \uD83D\uDE04", "cancel")));
        inlineKeyboardMarkup.setKeyboard(keyboard);
        message.setReplyMarkup(inlineKeyboardMarkup);
        return message;
    }

    public SendMessage sendAddPwsButtons(long chatId) {
        SendMessage message = new SendMessage();
        message.setChatId(chatId);
        message.setText("Напиши, в каком году будет проводиться мастерская");
        var inlineKeyboardMarkup = new InlineKeyboardMarkup();
        List<List<InlineKeyboardButton>> keyboard = new ArrayList<>();
        keyboard.add(List.of(createBtn("Назад \uD83D\uDE04", "cancel")));
        inlineKeyboardMarkup.setKeyboard(keyboard);
        message.setReplyMarkup(inlineKeyboardMarkup);
        return message;
    }

    public SendMessage sendTeamNameButtons(long chatId) {
        SendMessage message = new SendMessage();
        message.setChatId(chatId);
        message.setText("Напиши название команды");
        var inlineKeyboardMarkup = new InlineKeyboardMarkup();
        List<List<InlineKeyboardButton>> keyboard = new ArrayList<>();
        keyboard.add(List.of(createBtn("Назад \uD83D\uDE04", "cancel")));
        inlineKeyboardMarkup.setKeyboard(keyboard);
        message.setReplyMarkup(inlineKeyboardMarkup);
        return message;
    }

    public SendMessage sendTeamNumberButtons(long chatId) {
        SendMessage message = new SendMessage();
        message.setChatId(chatId);
        message.setText("Напиши номер команды");
        var inlineKeyboardMarkup = new InlineKeyboardMarkup();
        List<List<InlineKeyboardButton>> keyboard = new ArrayList<>();
        keyboard.add(List.of(createBtn("Назад \uD83D\uDE04", "cancel")));
        inlineKeyboardMarkup.setKeyboard(keyboard);
        message.setReplyMarkup(inlineKeyboardMarkup);
        return message;
    }

    public SendMessage sendOtherButtons(long chatId) {
        SendMessage message = new SendMessage();
        message.setChatId(chatId);
        message.setText("Другое:");
        var inlineKeyboardMarkup = new InlineKeyboardMarkup();
        List<List<InlineKeyboardButton>> keyboard = new ArrayList<>();
        keyboard.add(List.of(createBtn("Получить список команд \uD83D\uDE22", "get_teams_list")));
        keyboard.add(List.of(createBtn("Сгенерировать список команд \uD83D\uDE22", "generate_teams_list")));
        keyboard.add(List.of(createBtn("Назад \uD83D\uDE04", "cancel")));
        inlineKeyboardMarkup.setKeyboard(keyboard);
        message.setReplyMarkup(inlineKeyboardMarkup);
        return message;
    }

    /*public SendMessage sendBackButtons(long chatId) {
        SendMessage message = new SendMessage();
        message.setChatId(chatId);
        message.setText("");
        var inlineKeyboardMarkup = new InlineKeyboardMarkup();
        List<List<InlineKeyboardButton>> keyboard = new ArrayList<>();
        keyboard.add(List.of(createBtn("Назад \uD83D\uDE04", "bye")));
        inlineKeyboardMarkup.setKeyboard(keyboard);
        message.setReplyMarkup(inlineKeyboardMarkup);
        return message;
    }*/

    private void editAndSendNewButtons(long chatId, int messageId, SendMessage newMessage) {
        try {
            EditMessageReplyMarkup editMarkup = new EditMessageReplyMarkup();
            editMarkup.setChatId(String.valueOf(chatId));
            editMarkup.setMessageId(messageId);
            editMarkup.setInlineMessageId(null);
            editMarkup.setReplyMarkup(null);
            execute(editMarkup);

            send(newMessage);
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }


    InlineKeyboardButton createBtn(String name, String data) {
        var inline = new InlineKeyboardButton();
        inline.setText(name);
        inline.setCallbackData(data);
        return inline;
    }

    private void handleStudentNameInput(long chatId, String name) {
        try {
            String student;
            if (userStates.getOrDefault(chatId, "").equals("AWAITING_NAME")){
                student = getStudentByName(name);
            } else {
                student = getStudentByNickName(name);
            }

            SendMessage response = new SendMessage();
            response.setChatId(chatId);
            var inlineKeyboardMarkup = new InlineKeyboardMarkup();
            List<List<InlineKeyboardButton>> keyboard = new ArrayList<>();
            if (!student.isEmpty()){
                response.setText("Студент  " + name + " найден!\n" + "Какую информацию ты хочешь получить?");

                keyboard.add(List.of(createBtn("Анкета \uD83D\uDE22", "questionnaire")));
                keyboard.add(List.of(createBtn("Тестирование \uD83D\uDE22", "test")));
                keyboard.add(List.of(createBtn("Мастерские, в которых участвовал \uD83D\uDE10", "student_pw")));
            } else {
                response.setText("Студент  " + name + " не найден!");
            }
            keyboard.add(List.of(createBtn("Назад", "cancel")));
            inlineKeyboardMarkup.setKeyboard(keyboard);
            response.setReplyMarkup(inlineKeyboardMarkup);

            send(response);
        } catch (NumberFormatException e) {
            sendMessage(chatId, "Информация о студенте не найдена\n" +
                    "Пожалуйста, проверь корректность ввода.");
        }
    }

    private void handleTeamNameInput(long chatId, String name) {
        try {
            String team;
            if (userStates.getOrDefault(chatId, "").equals("AWAITING_TEAM_NAME")){
                team = getTeamByName(name);
            } else {
                team = getTeamByNumber(name);
            }

            SendMessage response = new SendMessage();
            response.setChatId(chatId);
            var inlineKeyboardMarkup = new InlineKeyboardMarkup();
            List<List<InlineKeyboardButton>> keyboard = new ArrayList<>();
            if (!team.isEmpty()){
                response.setText("Команда  " + name + " найдена!\n" + team);
            } else {
                response.setText("Команда  " + name + " не найдена!");
            }
            keyboard.add(List.of(createBtn("Назад", "cancel")));
            inlineKeyboardMarkup.setKeyboard(keyboard);
            response.setReplyMarkup(inlineKeyboardMarkup);

            send(response);
        } catch (NumberFormatException e) {
            sendMessage(chatId, "Информация о команде не найдена\n" +
                    "Пожалуйста, проверь корректность ввода.");
        }
    }

    private void handleYearInput(long chatId, String year) {
        try {
            int yearInt = Integer.parseInt(year);
            if (yearInt < 2000 || yearInt > 2100) {
                sendMessage(chatId, "Пожалуйста, введи корректный год (например: 2023)");
                return;
            }

            String workshops = getWorkshopsByYear(yearInt);

            SendMessage response = new SendMessage();
            response.setChatId(chatId);
            var inlineKeyboardMarkup = new InlineKeyboardMarkup();
            List<List<InlineKeyboardButton>> keyboard = new ArrayList<>();
            if (!workshops.isEmpty()){
                response.setText("В " + year + " году проводились мастерские со следующими названиями:\n" + workshops);
                userStates.put(chatId, "AWAITING_PWS");
            } else {
                response.setText("Информация по " + year + " году не найдена");
            }
            keyboard.add(List.of(createBtn("Назад", "cancel")));
            inlineKeyboardMarkup.setKeyboard(keyboard);
            response.setReplyMarkup(inlineKeyboardMarkup);

            send(response);
        } catch (NumberFormatException e) {
            sendMessage(chatId, "Пожалуйста, введи год числом (например: 2023)");
        }
    }

    private void handleYearInputToAdd(long chatId, String year) {
        try {
            int yearInt = Integer.parseInt(year);
            if (yearInt < LocalDateTime.now().getYear() || yearInt > LocalDateTime.now().getYear() + 1) {
                sendMessage(chatId, "Разрешено вводить только текущий или следующий год числом");
                return;
            }
            pwsYear = yearInt;

            //String workshops = getWorkshopsByYear(yearInt);

            SendMessage response = new SendMessage();
            response.setChatId(chatId);
            var inlineKeyboardMarkup = new InlineKeyboardMarkup();
            List<List<InlineKeyboardButton>> keyboard = new ArrayList<>();
            response.setText("Введи название мастерской: \n");
            userStates.put(chatId, "AWAITING_PWS_NAME");
            keyboard.add(List.of(createBtn("Назад", "cancel")));
            inlineKeyboardMarkup.setKeyboard(keyboard);
            response.setReplyMarkup(inlineKeyboardMarkup);

            send(response);
        } catch (NumberFormatException e) {
            sendMessage(chatId, "Разрешено вводить только текущий или следующий год числом");
        }
    }

    private void handleNameInputToAdd(long chatId, String name) {
        try {
            /*if (name < LocalDateTime.now().getYear() || name > LocalDateTime.now().getYear() + 1) {
                sendMessage(chatId, "Разрешено вводить только текущий или следующий год числом");
                return;
            }*/

            //String workshops = getWorkshopsByYear(yearInt);

            SendMessage response = new SendMessage();
            response.setChatId(chatId);
            var inlineKeyboardMarkup = new InlineKeyboardMarkup();
            List<List<InlineKeyboardButton>> keyboard = new ArrayList<>();

            // Создаем DTO для новой мастерской
            CreateProjectWorkshopDto createDto = new CreateProjectWorkshopDto();
            createDto.setName(name); // название мастерской из параметра метода
            createDto.setYear(pwsYear); // текущий год

            try {
                webClient.post()
                        .uri("/api/project-workshop/add")
                        .contentType(MediaType.APPLICATION_JSON)
                        .bodyValue(createDto)
                        .retrieve()
                        .toBodilessEntity()
                        .block(); // Блокируем выполнение

                response.setText("Мастерская '" + name + "' (год проведения: " + pwsYear + ") успешно добавлена!");
            } catch (Exception e) {
                log.error("Ошибка при добавлении мастерской: {}", e.getMessage());
                response.setText("Ошибка при добавлении мастерской: " + e.getMessage());
            }

            response.setText("Мастерская '" + name + "' (год проведения: " + pwsYear + "успешно добавлена!");
            keyboard.add(List.of(createBtn("Назад", "cancel")));
            inlineKeyboardMarkup.setKeyboard(keyboard);
            response.setReplyMarkup(inlineKeyboardMarkup);

            send(response);
        } catch (NumberFormatException e) {
            sendMessage(chatId, "Ошибка ввода названия");
        }
    }

    private void handlePWSInput(long chatId, String pwsName) {
        try {
            String workshop = getWorkshop(pwsName);

            SendMessage response = new SendMessage();
            response.setChatId(chatId);
            var inlineKeyboardMarkup = new InlineKeyboardMarkup();
            List<List<InlineKeyboardButton>> keyboard = new ArrayList<>();
            if (!workshop.isEmpty()){
                if (userStates.getOrDefault(chatId, "").equals("AWAITING_ROLES")){
                    response.setText("Вот студенты и их роли в " + pwsName + ":\n" + getRoles(pwsName));
                } else {
                    response.setText(pwsName + " найдена!\n" + "Какую информацию ты хочешь получить?");
                    keyboard.add(List.of(createBtn("Найти команду по названию \uD83D\uDE22", "find_team_name")));
                    keyboard.add(List.of(createBtn("Найти команду по номеру \uD83D\uDE22", "find_team_number")));
                    keyboard.add(List.of(createBtn("Вывести все команды \uD83D\uDE10", "print_all_teams")));
                }
            } else {
                response.setText("Информация по " + pwsName + " не найдена\n" +
                        "Пожалуйста, проверь корректность ввода.");
            }
            keyboard.add(List.of(createBtn("Назад", "cancel")));
            inlineKeyboardMarkup.setKeyboard(keyboard);
            response.setReplyMarkup(inlineKeyboardMarkup);

            send(response);
        } catch (NumberFormatException e) {
            sendMessage(chatId, "Информация по " + pwsName + " не найдена\n" +
                    "Пожалуйста, проверь корректность ввода.");
        }
    }

    //Пока заглушки
    private String getStudentByName(String name) {
        if (name.equals("Иванов Сергей Иванович")){
            return "Тут объект студента должен быть";
        }
        return "";
    }

    private String getStudentByNickName(String name) {
        if (name.equals("Петров Андрей Петрович")){
            return "Тут объект студента должен быть";
        }
        return "";
    }

    private String getRoles(String name) {
        //Тут запрос на получение студентов с ролями у мастерсой
        return "Иванов Сергей Иванович - FrontEnd\n"+
                "Петров Андрей Петрович - BackEnd\n"+
                "Соколов Алексей Александрович - All";
    }

    private String getWorkshopsByYear(int year) {
        if (year == 2024) {
            return "• Весенняя_проектная_мастерская_24\n• Летняя_проектная_мастерская_24\n• Осенняя_проектная_мастерская \n" +
                    "По какой мастерской необходимо получить отчет?";
        }
        return "";
    }

    private String getWorkshop(String name) {
        if (name.equals("Весенняя_проектная_мастерская_24")){
            return "Тут объект мастерской должен быть";
        }
        return "";
    }

    private String getTeamByName(String name) {
        if (name.equals("Джедаи")){
            return "В теории тут должен быть состав команды с ролями";
        }
        return "";
    }

    private String getTeamByNumber(String name) {
        if (name.equals("1")){
            return "В теории тут должен быть состав команды с ролями";
        }
        return "";
    }
}