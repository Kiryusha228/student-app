package org.example.handler;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.example.client.StudentAppClient;
import org.example.dto.ProjectWorkshopDto;
import org.example.dto.StudentInTeamDto;
import org.example.dto.StudentInfoDto;
import org.example.dto.TeamWithStudentInfoDto;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.AnswerCallbackQuery;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageText;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

@Component
@RequiredArgsConstructor
public class CallbackHandler {
  private final StudentAppClient studentAppClient;

  private String formatDateTime(LocalDateTime dateTime) {
    if (dateTime == null) {
      return "Не указана";
    }
    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm");
    return dateTime.format(formatter);
  }

  public EditMessageText handleCallbackQueryWithEditMessage(
      String callbackData, Long chatId, String callbackId, Integer messageId)
      throws TelegramApiException {
    if (callbackData.startsWith("ENABLE:")) {
      Long workshopId = Long.parseLong(callbackData.split(":")[1]);
      studentAppClient.enableProjectWorkshop(workshopId);

      return editMessageWithNewStatus(chatId, messageId);
    } else if (callbackData.startsWith("DISABLE:")) {
      Long workshopId = Long.parseLong(callbackData.split(":")[1]);
      studentAppClient.disableProjectWorkshop(workshopId);

      return editMessageWithNewStatus(chatId, messageId);
    }
    return null;
  }

  public AnswerCallbackQuery handleCallbackQueryWithAnswer(
      String callbackData, Long chatId, String callbackId, Integer messageId)
      throws TelegramApiException {

    if (callbackData.startsWith("WORKSHOP_INFO:")) {
      Long workshopId = Long.parseLong(callbackData.split(":")[1]);
      ProjectWorkshopDto workshop = studentAppClient.getProjectWorkshopById(workshopId);

      if (workshop != null) {
        return getAnswerCallbackQuery(
            String.format(
                "Название: %s\nГод: %d\nНачало: %s\nОкончание: %s\nАктивна: %s",
                workshop.getName(),
                workshop.getYear(),
                formatDateTime(workshop.getStartDateTime()),
                formatDateTime(workshop.getEndDateTime()),
                workshop.getIsEnable() ? "Да" : "Нет"),
            callbackId);
      }
    } else if (callbackData.startsWith("STUDENT_INFO:")) {
      Long studentId = Long.parseLong(callbackData.split(":")[1]);

      StudentInfoDto student = studentAppClient.getStudentProjectWorkshopById(studentId);

      if (student != null) {
        return getAnswerCallbackQuery(
            String.format(
                "Роль: %s\nТелеграм: %s\nОпыт: %s\nЗнание языка: %s\nОпыт работы с языком: %s\nРезультат теста: %d",
                student.getRole(),
                student.getTelegram(),
                student.getExperience(),
                student.getLanguageProficiency(),
                student.getLanguageExperience(),
                student.getTestResult()),
            callbackId);
      } else {
        AnswerCallbackQuery answerCallbackQuery = new AnswerCallbackQuery();
        answerCallbackQuery.setCallbackQueryId(callbackId);
        answerCallbackQuery.setShowAlert(true);
        answerCallbackQuery.setText("Информация о студенте не найдена.");
        return answerCallbackQuery;
      }
    }
    return null;
  }

  private static AnswerCallbackQuery getAnswerCallbackQuery(String student, String callbackId) {

    AnswerCallbackQuery answerCallbackQuery = new AnswerCallbackQuery();
    answerCallbackQuery.setCallbackQueryId(callbackId);
    answerCallbackQuery.setShowAlert(true);
    answerCallbackQuery.setText(student);
    return answerCallbackQuery;
  }

  public SendMessage handleCallbackQuery(
      String callbackData, Long chatId, String callbackId, Integer messageId)
      throws TelegramApiException {
    if (callbackData.startsWith("WORKSHOP_TEAM:")) {
      Long workshopId = Long.parseLong(callbackData.split(":")[1]);

      List<TeamWithStudentInfoDto> teams = studentAppClient.getTeamsByProjectWorkshopId(workshopId);

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
      return message;
    } else if (callbackData.startsWith("STUDENTS:")) {
      Long workshopId = Long.parseLong(callbackData.split(":")[1]);
      List<StudentInfoDto> students =
          studentAppClient.getAllStudentsByProjectWorkshopId(workshopId);

      if (students.isEmpty()) {
        SendMessage sendMessage = new SendMessage();
        sendMessage.setChatId(chatId);
        sendMessage.setText("На этой мастерской пока нет студентов.");
        return sendMessage;
      } else {
        return getSendMessage(chatId, students);
      }
    }
    return null;
  }

  private static SendMessage getSendMessage(Long chatId, List<StudentInfoDto> students) {
    InlineKeyboardMarkup inlineKeyboardMarkup = new InlineKeyboardMarkup();
    List<List<InlineKeyboardButton>> rows = getLists(students);

    inlineKeyboardMarkup.setKeyboard(rows);

    SendMessage sendMessage = new SendMessage();
    sendMessage.setChatId(chatId);
    sendMessage.setText("Выберите студента для просмотра информации:");
    sendMessage.setReplyMarkup(inlineKeyboardMarkup);
    return sendMessage;
  }

  private static List<List<InlineKeyboardButton>> getLists(List<StudentInfoDto> students) {
    List<List<InlineKeyboardButton>> rows = new ArrayList<>();

    for (StudentInfoDto student : students) {
      InlineKeyboardButton studentButton = new InlineKeyboardButton();
      studentButton.setText("Telegram: " + student.getTelegram()); // todo поменять на имя
      studentButton.setCallbackData("STUDENT_INFO:" + student.getStudentProjectWorkshopId());

      List<InlineKeyboardButton> row = new ArrayList<>();
      row.add(studentButton);
      rows.add(row);
    }
    return rows;
  }

  private EditMessageText editMessageWithNewStatus(long chatId, int messageId)
      throws TelegramApiException {
    List<ProjectWorkshopDto> workshops = studentAppClient.getAllProjectWorkshops();

    if (workshops.isEmpty()) {
      return null;
    }

    String messageText = "Список мастерских:\n";
    InlineKeyboardMarkup inlineKeyboardMarkup = new InlineKeyboardMarkup();
    List<List<InlineKeyboardButton>> rows = new ArrayList<>();

    for (ProjectWorkshopDto workshop : workshops) {

      List<InlineKeyboardButton> row = getInlineKeyboardButtons(workshop);

      rows.add(row);
    }

    inlineKeyboardMarkup.setKeyboard(rows);

    EditMessageText editMessageText = new EditMessageText();
    editMessageText.setChatId(chatId);
    editMessageText.setMessageId(messageId);
    editMessageText.setText(messageText);
    editMessageText.setReplyMarkup(inlineKeyboardMarkup);

    return editMessageText;
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
}
