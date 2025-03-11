package ru.tbank.service;

import ru.tbank.config.BotConfig;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
@Slf4j
public class TelegramBot extends TelegramLongPollingBot {

    private BotConfig config;

    @Autowired
    public TelegramBot(BotConfig config) {
        this.config = config;
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

    private Map<String, String> RESPONCE = new HashMap<>();

    {
        RESPONCE.put("bot_name", "Мое имя: ");
        RESPONCE.put("token", "Мой токен: ");
        RESPONCE.put("bye", "Пока! Если понадоблюсь еще: обязательно пиши!");
    }

    @Override
    public void onUpdateReceived(Update update) {
        //CurrencyModel currencyModel = new CurrencyModel();
        String currency = "default answer";

        if (update.hasCallbackQuery()) {
            var data = update.getCallbackQuery().getData();
            long chatId = update.getCallbackQuery().getMessage().getChatId();
            send(new SendMessage(String.valueOf(chatId), RESPONCE.get(data)));
        }

        if(update.hasMessage() && update.getMessage().hasText()){
            String messageText = update.getMessage().getText();
            long chatId = update.getMessage().getChatId();
            //currency = update.getMessage().getChatId().toString();
            //currency = getBotUsername();
            /*currency = getBotToken();*/


            switch (messageText){
                case "/start":
                    startCommandReceived(chatId, update.getMessage().getChat().getFirstName());
                    send(sendButtons(chatId));
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
    public SendMessage sendButtons(long chatId) {
        SendMessage message = new SendMessage();
        message.setChatId(chatId);
        message.setText("Выбери отчет, который хочешь получить");
        var inlineKeyboardMarkup = new InlineKeyboardMarkup();
        List<List<InlineKeyboardButton>> keyboard = new ArrayList<>();
        keyboard.add(List.of(createBtn("Имя бота \uD83D\uDE22", "bot_name")));
        keyboard.add(List.of(createBtn("Токен бота \uD83D\uDE10", "token")));
        keyboard.add(List.of(createBtn("Попрощаться \uD83D\uDE04", "bye")));
        inlineKeyboardMarkup.setKeyboard(keyboard);
        message.setReplyMarkup(inlineKeyboardMarkup);
        return message;
    }

    InlineKeyboardButton createBtn(String name, String data) {
        var inline = new InlineKeyboardButton();
        inline.setText(name);
        inline.setCallbackData(data);
        return inline;
    }
}