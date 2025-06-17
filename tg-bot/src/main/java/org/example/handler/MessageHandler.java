package org.example.handler;

import java.util.ArrayList;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.example.client.StudentAppClient;
import org.example.dto.*;
import org.example.dto.kafka.KafkaRequestDto;
import org.example.enums.BotState;
import org.example.kafka.producer.KafkaBotProducer;
import org.example.service.UserStateService;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardRow;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

@Component
@RequiredArgsConstructor
public class MessageHandler {
  private final UserStateService userStateService;
  private final StudentAppClient studentAppClient;
  private final KafkaBotProducer kafkaBotProducer;

  public SendMessage handleCreateWorkshopInput(long chatId, String message)
      throws TelegramApiException {
    if (message.equals("↩️ Отмена")) {
      userStateService.setState(chatId, BotState.DEFAULT);
      return null;
    }

    var workshopData = message.split(" ");

    studentAppClient.addProjectWorkshop(
        new CreateProjectWorkshopDto(workshopData[0], Integer.parseInt(workshopData[1])));

    userStateService.setState(chatId, BotState.DEFAULT);

    SendMessage successMessage = new SendMessage();
    successMessage.setChatId(chatId);
    successMessage.setText("Мастерская создана!");
    return successMessage;
  }

  public SendMessage handleCreateTeamInput(long chatId, String message)
      throws TelegramApiException {
//    if (message.equals("↩️ Отмена")) {
//      userStateService.setState(chatId, BotState.DEFAULT);
//      return null;
//    }
//
//    studentAppClient.createTeams(message);
//
//    userStateService.setState(chatId, BotState.DEFAULT);
//
//    SendMessage successMessage = new SendMessage();
//    successMessage.setChatId(chatId);
//    successMessage.setText("Команды сформированы!");
//    return successMessage;
    if (message.equals("↩️ Отмена")) {
      userStateService.setState(chatId, BotState.DEFAULT);
      return null;
    }

    var dto = new KafkaRequestDto();
    dto.setChatId(chatId);
    dto.setTeamCount(message);

    kafkaBotProducer.sendInferenceRequest(dto);

    userStateService.setState(chatId, BotState.DEFAULT);

    SendMessage successMessage = new SendMessage();
    successMessage.setChatId(chatId);
    successMessage.setText("Запрос отправлен");
    return successMessage;
  }

  public SendMessage handleGetStudentInfoInput(long chatId, String message)
      throws TelegramApiException {
    if (message.equals("↩️ Отмена")) {
      userStateService.setState(chatId, BotState.DEFAULT);
      return null;
    }

    StudentInfoDto studentInfoDto = studentAppClient.getStudentProjectWorkshopByTelegram(message);

    userStateService.setState(chatId, BotState.DEFAULT);

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
    return successMessage;
  }

  public SendMessage handleGetStudentInfoInputByName(long chatId, String message)
      throws TelegramApiException {
    if (message.equals("↩️ Отмена")) {
      userStateService.setState(chatId, BotState.DEFAULT);
      return null;
    }

    List<StudentInfoDto> students = studentAppClient.getAllStudentsProjectWorkshopByName(message);

    userStateService.setState(chatId, BotState.DEFAULT);

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
    return successMessage;
  }

  public SendMessage handleDefaultState(long chatId, String messageText)
      throws TelegramApiException {
    return switch (messageText) {
      case "/start", "↩️ Назад" -> createMainMenu(chatId);
      case "🛠️ Создать мастерскую" -> startWorkshopCreation(chatId);
      case "📋 Все мастерские" -> handleGetAllWorkshops(chatId);
      case "\uD83D\uDC68\u200D\uD83C\uDF93 Найти студента по ФИО" ->
          handleGetStudentInfoByName(chatId);
      case "✈️ Найти студента по нику" -> handleGetStudentInfo(chatId);
      case "👥 Сформировать команды для последней мастерской" ->
          handleFormTeamsForLastWorkshop(chatId);
      case "📋 Получить команды последней мастерской" -> handleGetTeamsForLastWorkshop(chatId);
      case "📋 Получить команды мастерской" -> handleGetTeamsForSomeWorkshop(chatId);
      default -> null;
    };
  }

  private SendMessage handleGetStudentInfo(long chatId) throws TelegramApiException {
    userStateService.setState(chatId, BotState.AWAITING_INPUT_NICKNAME);

    ReplyKeyboardMarkup keyboard = new ReplyKeyboardMarkup();
    keyboard.setResizeKeyboard(true);
    KeyboardRow row = new KeyboardRow();
    row.add("↩️ Отмена");
    keyboard.setKeyboard(List.of(row));

    SendMessage message = new SendMessage();
    message.setChatId(chatId);
    message.setText("Введите ник студента в телеграмме:");
    message.setReplyMarkup(keyboard);

    return message;
  }

  private SendMessage handleGetStudentInfoByName(long chatId) throws TelegramApiException {
    userStateService.setState(chatId, BotState.AWAITING_INPUT_STUDENT_NAME);

    ReplyKeyboardMarkup keyboard = new ReplyKeyboardMarkup();
    keyboard.setResizeKeyboard(true);
    KeyboardRow row = new KeyboardRow();
    row.add("↩️ Отмена");
    keyboard.setKeyboard(List.of(row));

    SendMessage message = new SendMessage();
    message.setChatId(chatId);
    message.setText("Введите фамилию, имя и отчество студента:");
    message.setReplyMarkup(keyboard);

    return message;
  }

  private SendMessage handleGetTeamsForLastWorkshop(long chatId) throws TelegramApiException {
    Long lastWorkshopId = studentAppClient.getLastProjectWorkshop().getId();
    if (lastWorkshopId == null) {
      SendMessage message = new SendMessage();
      message.setChatId(chatId);
      message.setText("Последняя мастерская не найдена.");
      return message;
    }

    List<TeamWithStudentInfoDto> teams =
        studentAppClient.getTeamsByProjectWorkshopId(lastWorkshopId);

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

    return message;
  }

  private SendMessage handleGetTeamsForSomeWorkshop(long chatId) throws TelegramApiException {
    List<ProjectWorkshopDto> workshops = studentAppClient.getAllProjectWorkshops();

    SendMessage message = new SendMessage();
    message.setChatId(chatId);

    if (workshops.isEmpty()) {
      message.setText("Список мастерских пуст.");
      return message;
    }

    InlineKeyboardMarkup inlineKeyboardMarkup = getInlineKeyboardMarkup(workshops);
    message.setText("Выберите мастерскую:");
    message.setReplyMarkup(inlineKeyboardMarkup);

    return message;
  }

  private static InlineKeyboardMarkup getInlineKeyboardMarkup(List<ProjectWorkshopDto> workshops) {
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
    return inlineKeyboardMarkup;
  }

  private SendMessage handleFormTeamsForLastWorkshop(Long chatId) throws TelegramApiException {
    userStateService.setState(chatId, BotState.AWAITING_CREATING_TEAMS);

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

    return message;
  }

  private SendMessage handleGetAllWorkshops(long chatId) throws TelegramApiException {
    List<ProjectWorkshopDto> workshops = studentAppClient.getAllProjectWorkshops();

    SendMessage message = new SendMessage();
    message.setChatId(chatId);

    if (workshops.isEmpty()) {
      message.setText("Список мастерских пуст.");
      return message;
    }

    InlineKeyboardMarkup inlineKeyboardMarkup = new InlineKeyboardMarkup();
    List<List<InlineKeyboardButton>> rows = new ArrayList<>();

    for (ProjectWorkshopDto workshop : workshops) {
      List<InlineKeyboardButton> row = getInlineKeyboardButtons(workshop);

      rows.add(row);
    }

    inlineKeyboardMarkup.setKeyboard(rows);
    message.setText("Выберите мастерскую:");
    message.setReplyMarkup(inlineKeyboardMarkup);

    return message;
  }

  private static List<InlineKeyboardButton> getInlineKeyboardButtons(ProjectWorkshopDto workshop) {
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
    return row;
  }

  public SendMessage createMainMenu(long chatId) {
    var keyboardMarkup = new ReplyKeyboardMarkup();
    keyboardMarkup.setResizeKeyboard(true);
    keyboardMarkup.setOneTimeKeyboard(false);

    var keyboard = new ArrayList<KeyboardRow>();

    KeyboardRow row1 = new KeyboardRow();
    row1.add("🛠️ Создать мастерскую");
    row1.add("📋 Все мастерские");

    KeyboardRow row2 = new KeyboardRow();
    row2.add("\uD83D\uDC68\u200D\uD83C\uDF93 Найти студента по ФИО");
    row2.add("✈️ Найти студента по нику");

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

  private SendMessage startWorkshopCreation(long chatId) throws TelegramApiException {
    userStateService.setState(chatId, BotState.AWAITING_CREATING_WORKSHOP);

    ReplyKeyboardMarkup keyboard = new ReplyKeyboardMarkup();
    keyboard.setResizeKeyboard(true);
    KeyboardRow row = new KeyboardRow();
    row.add("↩️ Отмена");
    keyboard.setKeyboard(List.of(row));

    SendMessage message = new SendMessage();
    message.setChatId(chatId);
    message.setText("Введите название и год мастерской в одном сообщении через пробел:");
    message.setReplyMarkup(keyboard);

    return message;
  }
}
