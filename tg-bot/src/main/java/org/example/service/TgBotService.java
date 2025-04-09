package org.example.service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.example.dto.*;
import org.example.properties.TgBotProperties;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.AnswerCallbackQuery;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageText;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardRow;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

@Service
// @RequiredArgsConstructor
public class TgBotService extends TelegramLongPollingBot {

  private final TgBotProperties tgBotProperties;
  private final StudentAppService studentAppService;

  private final Map<Long, BotState> userStates = new HashMap<>();

  private enum BotState {
    DEFAULT,
    AWAITING_CREATING_WORKSHOP,
    AWAITING_CREATING_TEAMS,
    AWAITING_INPUT_NICKNAME,
    AWAITING_INPUT_STUDENT_NAME,
  }

  public TgBotService(TgBotProperties tgBotProperties, StudentAppService studentAppService) {
    super(tgBotProperties.getToken());
    this.tgBotProperties = tgBotProperties;
    this.studentAppService = studentAppService;
  }

  private String formatDateTime(LocalDateTime dateTime) {
    if (dateTime == null) {
      return "Не указана";
    }
    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm");
    return dateTime.format(formatter);
  }

  @Override
  public void onUpdateReceived(Update update) {
    if (update.hasMessage() && update.getMessage().hasText()) {
      long chatId = update.getMessage().getChatId();
      String messageText = update.getMessage().getText();
      BotState currentState = userStates.getOrDefault(chatId, BotState.DEFAULT);

      try {
        switch (currentState) {
          case AWAITING_CREATING_WORKSHOP:
            handleCreateWorkshopInput(chatId, messageText);
            break;
          case AWAITING_CREATING_TEAMS:
            handleCreateTeamInput(chatId, messageText);
            break;
          case AWAITING_INPUT_NICKNAME:
            handleGetStudentInfoInput(chatId, messageText);
            break;
          case AWAITING_INPUT_STUDENT_NAME:
            handleGetStudentInfoInputByName(chatId, messageText);

          default:
            handleDefaultState(chatId, messageText);
        }
      } catch (Exception e) {
        userStates.put(chatId, BotState.DEFAULT);
        try {
          execute(createMainMenu(chatId));
        } catch (TelegramApiException ex) {
          throw new RuntimeException(ex);
        }
      }
    } else if (update.hasCallbackQuery()) {
      try {
        handleCallbackQuery(
            update.getCallbackQuery().getData(),
            update.getCallbackQuery().getMessage().getChatId(),
            update.getCallbackQuery().getId(),
            update.getCallbackQuery().getMessage().getMessageId());
      } catch (TelegramApiException e) {
        throw new RuntimeException(e);
      }
    }
  }

  private void handleCallbackQuery(
      String callbackData, Long chatId, String callbackId, Integer messageId)
      throws TelegramApiException {
    if (callbackData.startsWith("WORKSHOP_INFO:")) {
      Long workshopId = Long.parseLong(callbackData.split(":")[1]);
      ProjectWorkshopDto workshop = studentAppService.getProjectWorkshopById(workshopId);

      if (workshop != null) {
        String workshopInfo =
            String.format(
                "Название: %s\nГод: %d\nНачало: %s\nОкончание: %s\nАктивна: %s",
                workshop.getName(),
                workshop.getYear(),
                formatDateTime(workshop.getStartDateTime()),
                formatDateTime(workshop.getEndDateTime()),
                workshop.getIsEnable() ? "Да" : "Нет");

        AnswerCallbackQuery answerCallbackQuery = new AnswerCallbackQuery();
        answerCallbackQuery.setCallbackQueryId(callbackId);
        answerCallbackQuery.setShowAlert(true);
        answerCallbackQuery.setText(workshopInfo);
        execute(answerCallbackQuery);
      }
    } else if (callbackData.startsWith("WORKSHOP_TEAM:")) {
      Long workshopId = Long.parseLong(callbackData.split(":")[1]);

      if (workshopId == null) {
        SendMessage message = new SendMessage();
        message.setChatId(chatId);
        message.setText("Мастерская не найдена.");
        execute(message);
        return;
      }

      List<TeamWithStudentInfoDto> teams =
          studentAppService.getTeamsByProjectWorkshopId(workshopId);

      SendMessage message = new SendMessage();
      message.setChatId(chatId);

      if (teams.isEmpty()) {
        message.setText("Команды для данной мастерской пока не сформированы.");
      } else {
        StringBuilder teamsInfo = new StringBuilder("Список команд:\n");
        for (TeamWithStudentInfoDto team : teams) {
          teamsInfo.append("- Команда ID: ").append(team.getId()).append("\n  Участники:\n");

          for (StudentInTeamDto student : team.getStudents()) {
            teamsInfo
                .append("    - Имя: ")
                .append(student.getName())
                .append(", Телеграм: ")
                .append(student.getTelegram())
                .append(", Роль: ")
                .append(student.getRole())
                .append("\n");
          }
        }

        message.setText(teamsInfo.toString());
      }
      execute(message);
    } else if (callbackData.startsWith("ENABLE:")) {
      Long workshopId = Long.parseLong(callbackData.split(":")[1]);
      studentAppService.enableProjectWorkshop(workshopId);

      editMessageWithNewStatus(chatId, messageId);
    } else if (callbackData.startsWith("DISABLE:")) {
      Long workshopId = Long.parseLong(callbackData.split(":")[1]);
      studentAppService.disableProjectWorkshop(workshopId);

      editMessageWithNewStatus(chatId, messageId);
    } else if (callbackData.startsWith("STUDENTS:")) {
      Long workshopId = Long.parseLong(callbackData.split(":")[1]);
      List<StudentInfoDto> students =
          studentAppService.getAllStudentsByProjectWorkshopId(workshopId);

      if (students.isEmpty()) {
        SendMessage sendMessage = new SendMessage();
        sendMessage.setChatId(chatId);
        sendMessage.setText("На этой мастерской пока нет студентов.");
        execute(sendMessage);
      } else {
        InlineKeyboardMarkup inlineKeyboardMarkup = new InlineKeyboardMarkup();
        List<List<InlineKeyboardButton>> rows = new ArrayList<>();

        for (StudentInfoDto student : students) {
          InlineKeyboardButton studentButton = new InlineKeyboardButton();
          studentButton.setText("Telegram: " + student.getTelegram()); // todo поменять на имя
          studentButton.setCallbackData("STUDENT_INFO:" + student.getStudentProjectWorkshopId());

          List<InlineKeyboardButton> row = new ArrayList<>();
          row.add(studentButton);
          rows.add(row);
        }

        inlineKeyboardMarkup.setKeyboard(rows);

        SendMessage sendMessage = new SendMessage();
        sendMessage.setChatId(chatId);
        sendMessage.setText("Выберите студента для просмотра информации:");
        sendMessage.setReplyMarkup(inlineKeyboardMarkup);

        execute(sendMessage);
      }
    } else if (callbackData.startsWith("STUDENT_INFO:")) {
      Long studentId = Long.parseLong(callbackData.split(":")[1]);

      StudentInfoDto student = studentAppService.getStudentProjectWorkshopById(studentId);

      if (student != null) {
        String studentInfo =
            String.format(
                "Роль: %s\nТелеграм: %s\nОпыт: %s\nЗнание языка: %s\nОпыт работы с языком: %s\nРезультат теста: %d",
                student.getRole(),
                student.getTelegram(),
                student.getExperience(),
                student.getLanguageProficiency(),
                student.getLanguageExperience(),
                student.getTestResult());

        AnswerCallbackQuery answerCallbackQuery = new AnswerCallbackQuery();
        answerCallbackQuery.setCallbackQueryId(callbackId);
        answerCallbackQuery.setShowAlert(true);
        answerCallbackQuery.setText(studentInfo);
        execute(answerCallbackQuery);
      } else {
        AnswerCallbackQuery answerCallbackQuery = new AnswerCallbackQuery();
        answerCallbackQuery.setCallbackQueryId(callbackId);
        answerCallbackQuery.setShowAlert(true);
        answerCallbackQuery.setText("Информация о студенте не найдена.");
        execute(answerCallbackQuery);
      }
    }
  }

  private void editMessageWithNewStatus(long chatId, int messageId) throws TelegramApiException {
    List<ProjectWorkshopDto> workshops = studentAppService.getAllProjectWorkshops();

    if (workshops.isEmpty()) {
      return;
    }

    String messageText = "Список мастерских:\n";
    InlineKeyboardMarkup inlineKeyboardMarkup = new InlineKeyboardMarkup();
    List<List<InlineKeyboardButton>> rows = new ArrayList<>();

    for (ProjectWorkshopDto workshop : workshops) {

      InlineKeyboardButton workshopButton = new InlineKeyboardButton();
      workshopButton.setText(workshop.getName());
      workshopButton.setCallbackData("WORKSHOP_INFO:" + workshop.getId());

      InlineKeyboardButton enableDisableButton = new InlineKeyboardButton();
      String emoji = workshop.getIsEnable() ? "🟢" : "🔴";
      enableDisableButton.setText(emoji);
      enableDisableButton.setCallbackData(
          (workshop.getIsEnable() ? "DISABLE:" : "ENABLE:") + workshop.getId());

      InlineKeyboardButton studentsButton = new InlineKeyboardButton();
      studentsButton.setText("👥 Студенты");
      studentsButton.setCallbackData("STUDENTS:" + workshop.getId());

      List<InlineKeyboardButton> row = new ArrayList<>();
      row.add(workshopButton);
      row.add(enableDisableButton);
      row.add(studentsButton);

      rows.add(row);
    }

    inlineKeyboardMarkup.setKeyboard(rows);

    EditMessageText editMessageText = new EditMessageText();
    editMessageText.setChatId(chatId);
    editMessageText.setMessageId(messageId);
    editMessageText.setText(messageText);
    editMessageText.setReplyMarkup(inlineKeyboardMarkup);

    execute(editMessageText);
  }

  private void handleCreateWorkshopInput(long chatId, String message) throws TelegramApiException {
    if (message.equals("↩️ Отмена")) {
      userStates.put(chatId, BotState.DEFAULT);
      execute(createMainMenu(chatId));
      return;
    }

    var workshopData = message.split(" ");

    studentAppService.addProjectWorkshop(
        new CreateProjectWorkshopDto(workshopData[0], Integer.parseInt(workshopData[1])));

    userStates.put(chatId, BotState.DEFAULT);

    SendMessage successMessage = new SendMessage();
    successMessage.setChatId(chatId);
    successMessage.setText("Мастерская создана!");
    execute(successMessage);

    execute(createMainMenu(chatId));
  }

  private void handleCreateTeamInput(long chatId, String message) throws TelegramApiException {
    if (message.equals("↩️ Отмена")) {
      userStates.put(chatId, BotState.DEFAULT);
      execute(createMainMenu(chatId));
      return;
    }

    studentAppService.createTeams(message);

    userStates.put(chatId, BotState.DEFAULT);

    SendMessage successMessage = new SendMessage();
    successMessage.setChatId(chatId);
    successMessage.setText("Команды сформированы!");
    execute(successMessage);

    execute(createMainMenu(chatId));
  }

  private void handleGetStudentInfoInput(long chatId, String message) throws TelegramApiException {
    if (message.equals("↩️ Отмена")) {
      userStates.put(chatId, BotState.DEFAULT);
      execute(createMainMenu(chatId));
      return;
    }

    StudentInfoDto studentInfoDto = studentAppService.getStudentProjectWorkshopByTelegram(message);

    userStates.put(chatId, BotState.DEFAULT);

    SendMessage successMessage = new SendMessage();
    successMessage.setChatId(chatId);
    successMessage.setText(
        String.format(
            "Студент: %s\nРезультат тестов: %d\nРоль: %s\nОпыт: %s\nЯзыки: %s\nОпыт использования языков: %s",
            studentInfoDto.getTelegram(),
            studentInfoDto.getTestResult(),
            studentInfoDto.getRole(),
            studentInfoDto.getExperience(),
            studentInfoDto.getLanguageProficiency(),
            studentInfoDto.getLanguageExperience()));
    execute(successMessage);

    execute(createMainMenu(chatId));
  }

  private void handleGetStudentInfoInputByName(long chatId, String message)
      throws TelegramApiException {
    if (message.equals("↩️ Отмена")) {
      userStates.put(chatId, BotState.DEFAULT);
      execute(createMainMenu(chatId));
      return;
    }

    List<StudentInfoDto> students = studentAppService.getAllStudentsProjectWorkshopByName(message);

    userStates.put(chatId, BotState.DEFAULT);

    SendMessage successMessage = new SendMessage();
    successMessage.setChatId(chatId);
    for (StudentInfoDto student : students) {
      successMessage.setText(
          String.format(
              "Студент: %s\nРезультат тестов: %d\nРоль: %s\nОпыт: %s\nЯзыки: %s\nОпыт использования языков: %s\n",
              student.getTelegram(),
              student.getTestResult(),
              student.getRole(),
              student.getExperience(),
              student.getLanguageProficiency(),
              student.getLanguageExperience()));
    }
    execute(successMessage);

    execute(createMainMenu(chatId));
  }

  private void startWorkshopCreation(long chatId) throws TelegramApiException {
    userStates.put(chatId, BotState.AWAITING_CREATING_WORKSHOP);

    ReplyKeyboardMarkup keyboard = new ReplyKeyboardMarkup();
    keyboard.setResizeKeyboard(true);
    KeyboardRow row = new KeyboardRow();
    row.add("↩️ Отмена");
    keyboard.setKeyboard(List.of(row));

    SendMessage message = new SendMessage();
    message.setChatId(chatId);
    message.setText("Введите название и год мастерской в одном сообщении через пробел:");
    message.setReplyMarkup(keyboard);

    execute(message);
  }

  private void handleDefaultState(long chatId, String messageText) throws TelegramApiException {
    if (messageText.equals("/start") || messageText.equals("↩️ Назад")) {
      execute(createMainMenu(chatId));
    } else if (messageText.equals("🛠️ Создать мастерскую")) {
      startWorkshopCreation(chatId);
    } else if (messageText.equals("📋 Все мастерские")) {
      handleGetAllWorkshops(chatId);
    } else if (messageText.equals("\uD83D\uDC68\u200D\uD83C\uDF93 Найти студента по ФИО")) {
      handleGetStudentInfoByName(chatId);
    } else if (messageText.equals("✈\uFE0F Найти студента по нику")) {
      handleGetStudentInfo(chatId);
    } else if (messageText.equals("👥 Сформировать команды для последней мастерской")) {
      handleFormTeamsForLastWorkshop(chatId);
    } else if (messageText.equals("📋 Получить команды последней мастерской")) {
      handleGetTeamsForLastWorkshop(chatId);
    } else if (messageText.equals("📋 Получить команды мастерской")) {
      handleGetTeamsForSomeWorkshop(chatId);
    }
  }

  private void handleGetStudentInfo(long chatId) throws TelegramApiException {
    userStates.put(chatId, BotState.AWAITING_INPUT_NICKNAME);

    ReplyKeyboardMarkup keyboard = new ReplyKeyboardMarkup();
    keyboard.setResizeKeyboard(true);
    KeyboardRow row = new KeyboardRow();
    row.add("↩️ Отмена");
    keyboard.setKeyboard(List.of(row));

    SendMessage message = new SendMessage();
    message.setChatId(chatId);
    message.setText("Введите ник студента в телеграмме:");
    message.setReplyMarkup(keyboard);

    execute(message);
  }

  private void handleGetStudentInfoByName(long chatId) throws TelegramApiException {
    userStates.put(chatId, BotState.AWAITING_INPUT_STUDENT_NAME);

    ReplyKeyboardMarkup keyboard = new ReplyKeyboardMarkup();
    keyboard.setResizeKeyboard(true);
    KeyboardRow row = new KeyboardRow();
    row.add("↩️ Отмена");
    keyboard.setKeyboard(List.of(row));

    SendMessage message = new SendMessage();
    message.setChatId(chatId);
    message.setText("Введите фамилию, имя и отчество студента:");
    message.setReplyMarkup(keyboard);

    execute(message);
  }

  private void handleGetTeamsForLastWorkshop(long chatId) throws TelegramApiException {
    Long lastWorkshopId = studentAppService.getLastProjectWorkshop().getId();
    if (lastWorkshopId == null) {
      SendMessage message = new SendMessage();
      message.setChatId(chatId);
      message.setText("Последняя мастерская не найдена.");
      execute(message);
      return;
    }

    List<TeamWithStudentInfoDto> teams =
        studentAppService.getTeamsByProjectWorkshopId(lastWorkshopId);

    SendMessage message = new SendMessage();
    message.setChatId(chatId);

    if (teams.isEmpty()) {
      message.setText("Команды для последней мастерской пока не сформированы.");
    } else {
      StringBuilder teamsInfo = new StringBuilder("Список команд:\n");
      for (TeamWithStudentInfoDto team : teams) {
        teamsInfo.append("- Команда ID: ").append(team.getId()).append("\n  Участники:\n");

        for (StudentInTeamDto student : team.getStudents()) {
          teamsInfo
              .append("    - Имя: ")
              .append(student.getName())
              .append(", Телеграм: ")
              .append(student.getTelegram())
              .append(", Роль: ")
              .append(student.getRole())
              .append("\n");
        }
      }
      message.setText(teamsInfo.toString());
    }

    execute(message);
  }

  private void handleGetTeamsForSomeWorkshop(long chatId) throws TelegramApiException {
    List<ProjectWorkshopDto> workshops = studentAppService.getAllProjectWorkshops();

    SendMessage message = new SendMessage();
    message.setChatId(chatId);

    if (workshops.isEmpty()) {
      message.setText("Список мастерских пуст.");
      execute(message);
      return;
    }

    InlineKeyboardMarkup inlineKeyboardMarkup = new InlineKeyboardMarkup();
    List<List<InlineKeyboardButton>> rows = new ArrayList<>();

    for (ProjectWorkshopDto workshop : workshops) {
      InlineKeyboardButton workshopButton = new InlineKeyboardButton();
      workshopButton.setText(workshop.getName());
      workshopButton.setCallbackData("WORKSHOP_TEAM:" + workshop.getId());

      List<InlineKeyboardButton> row = new ArrayList<>();
      row.add(workshopButton);
      rows.add(row);
    }

    inlineKeyboardMarkup.setKeyboard(rows);
    message.setText("Выберите мастерскую:");
    message.setReplyMarkup(inlineKeyboardMarkup);

    execute(message);
  }

  private void handleFormTeamsForLastWorkshop(Long chatId) throws TelegramApiException {
    userStates.put(chatId, BotState.AWAITING_CREATING_TEAMS);

    ReplyKeyboardMarkup keyboard = new ReplyKeyboardMarkup();
    keyboard.setResizeKeyboard(true);
    KeyboardRow row = new KeyboardRow();
    row.add("↩️ Отмена");
    keyboard.setKeyboard(List.of(row));

    SendMessage message = new SendMessage();
    message.setChatId(chatId);
    message.setText(
        "Введите количество участников в команде (Можно использовать неточное число, например 3-5):");
    message.setReplyMarkup(keyboard);

    execute(message);
  }

  private void handleGetAllWorkshops(long chatId) throws TelegramApiException {
    List<ProjectWorkshopDto> workshops = studentAppService.getAllProjectWorkshops();

    SendMessage message = new SendMessage();
    message.setChatId(chatId);

    if (workshops.isEmpty()) {
      message.setText("Список мастерских пуст.");
      execute(message);
      return;
    }

    InlineKeyboardMarkup inlineKeyboardMarkup = new InlineKeyboardMarkup();
    List<List<InlineKeyboardButton>> rows = new ArrayList<>();

    for (ProjectWorkshopDto workshop : workshops) {
      InlineKeyboardButton workshopButton = new InlineKeyboardButton();
      workshopButton.setText(workshop.getName());
      workshopButton.setCallbackData("WORKSHOP_INFO:" + workshop.getId());

      InlineKeyboardButton enableDisableButton = new InlineKeyboardButton();
      String emoji = workshop.getIsEnable() ? "🟢" : "🔴";
      enableDisableButton.setText(emoji);
      enableDisableButton.setCallbackData(
          (workshop.getIsEnable() ? "DISABLE:" : "ENABLE:") + workshop.getId());

      InlineKeyboardButton studentsButton = new InlineKeyboardButton();
      studentsButton.setText("👥 Студенты");
      studentsButton.setCallbackData("STUDENTS:" + workshop.getId());

      List<InlineKeyboardButton> row = new ArrayList<>();
      row.add(workshopButton);
      row.add(enableDisableButton);
      row.add(studentsButton);

      rows.add(row);
    }

    inlineKeyboardMarkup.setKeyboard(rows);
    message.setText("Выберите мастерскую:");
    message.setReplyMarkup(inlineKeyboardMarkup);

    execute(message);
  }

  @Override
  public String getBotUsername() {
    return tgBotProperties.getName();
  }

  private SendMessage createMainMenu(long chatId) {
    var keyboardMarkup = new ReplyKeyboardMarkup();
    keyboardMarkup.setResizeKeyboard(true);
    keyboardMarkup.setOneTimeKeyboard(false);

    var keyboard = new ArrayList<KeyboardRow>();

    KeyboardRow row1 = new KeyboardRow();
    row1.add("🛠️ Создать мастерскую");
    row1.add("📋 Все мастерские");

    KeyboardRow row2 = new KeyboardRow();
    row2.add("\uD83D\uDC68\u200D\uD83C\uDF93 Найти студента по ФИО");
    row2.add("✈\uFE0F Найти студента по нику");

    KeyboardRow row3 = new KeyboardRow();
    row3.add("📋 Получить команды последней мастерской");
    row3.add("📋 Получить команды мастерской");

    KeyboardRow row4 = new KeyboardRow();
    row4.add("👥 Сформировать команды для последней мастерской");

    keyboard.add(row1);
    keyboard.add(row2);
    keyboard.add(row3);
    keyboard.add(row4);

    keyboardMarkup.setKeyboard(keyboard);

    SendMessage message = new SendMessage();
    message.setChatId(chatId);
    message.setText("Привет, я помощник для HR, выберите действие:");
    message.setReplyMarkup(keyboardMarkup);

    return message;
  }
}
