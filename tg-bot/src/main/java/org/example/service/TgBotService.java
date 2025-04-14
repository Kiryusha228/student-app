package org.example.service;


import org.example.enums.BotState;
import org.example.handler.CallbackHandler;
import org.example.handler.MessageHandler;
import org.example.properties.TgBotProperties;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

@Service
public class TgBotService extends TelegramLongPollingBot {

  private final TgBotProperties tgBotProperties;
  private final CallbackHandler callbackHandler;
  private final MessageHandler messageHandler;
  private final UserStateService userStateService;

  public TgBotService(
          TgBotProperties tgBotProperties,
          CallbackHandler callbackHandler,
          MessageHandler messageHandler, UserStateService userStateService) {
    super(tgBotProperties.getToken());
    this.tgBotProperties = tgBotProperties;
    this.callbackHandler = callbackHandler;
    this.messageHandler = messageHandler;
      this.userStateService = userStateService;
  }

  @Override
  public void onUpdateReceived(Update update) {
    if (update.hasMessage() && update.getMessage().hasText()) {
      long chatId = update.getMessage().getChatId();
      String messageText = update.getMessage().getText();
      BotState currentState = userStateService.getState(chatId);

      try {
        execute(messageHandler.createMainMenu(chatId));
        switch (currentState) {
          case AWAITING_CREATING_WORKSHOP:
            execute(messageHandler.handleCreateWorkshopInput(chatId, messageText));
            break;
          case AWAITING_CREATING_TEAMS:
            execute(messageHandler.handleCreateTeamInput(chatId, messageText));
            break;
          case AWAITING_INPUT_NICKNAME:
            execute(messageHandler.handleGetStudentInfoInput(chatId, messageText));
            break;
          case AWAITING_INPUT_STUDENT_NAME:
            execute(messageHandler.handleGetStudentInfoInputByName(chatId, messageText));

          default:
            execute(messageHandler.handleDefaultState(chatId, messageText));
        }
      } catch (Exception e) {
        userStateService.setState(chatId, BotState.DEFAULT);
      }
    } else if (update.hasCallbackQuery()) {
      try {
        // todo придумать как упростить
        var message =
            callbackHandler.handleCallbackQuery(
                update.getCallbackQuery().getData(),
                update.getCallbackQuery().getMessage().getChatId(),
                update.getCallbackQuery().getId(),
                update.getCallbackQuery().getMessage().getMessageId());
        if (message != null) {
          execute(message);
        }
        var answer =
            callbackHandler.handleCallbackQueryWithAnswer(
                update.getCallbackQuery().getData(),
                update.getCallbackQuery().getMessage().getChatId(),
                update.getCallbackQuery().getId(),
                update.getCallbackQuery().getMessage().getMessageId());
        if (answer != null) {
          execute(answer);
        }
        var editMessage =
            callbackHandler.handleCallbackQueryWithEditMessage(
                update.getCallbackQuery().getData(),
                update.getCallbackQuery().getMessage().getChatId(),
                update.getCallbackQuery().getId(),
                update.getCallbackQuery().getMessage().getMessageId());
        if (editMessage != null) {
          execute(editMessage);
        }
      } catch (TelegramApiException e) {
        throw new RuntimeException(e);
      }
    }
  }

  @Override
  public String getBotUsername() {
    return tgBotProperties.getName();
  }
}
