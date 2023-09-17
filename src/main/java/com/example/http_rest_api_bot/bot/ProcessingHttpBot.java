package com.example.http_rest_api_bot.bot;

import com.example.http_rest_api_bot.client.SourceClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

@Component
public class ProcessingHttpBot extends TelegramLongPollingBot {

    private static final String GET_USERS = "/get_users";
    private static final String GET_USER_BY_ID = "/get_user";
    private static final String CREATE_USER = "/create_user";

    private final SourceClient sourceClient;

    public ProcessingHttpBot(@Value("${bot.token}") String botToken, SourceClient sourceClient) {
        super(botToken);
        this.sourceClient = sourceClient;
    }

    @Override
    public void onUpdateReceived(Update update) {
        if (!update.hasMessage() || !update.getMessage().hasText()) {
            return;
        }

        String message = update.getMessage().getText();
        Long chatId = update.getMessage().getChatId();
        String[] mes = message.split(" ");

        switch (mes[0]) {
            case "/start" -> {
                String text = """
                        /get_users N - выдает информацию в следующем формате: id: ФИО, email
                        /get_user ID - выдает информацию по конкретномому пользователю
                        /create_user FIRST_NAME LAST_NAME - создает пользователя и возвращает его ID
                        """;
                sendMessage(chatId, text);
            }
            case GET_USERS -> {
                String N = mes.length == 1 ? "1" : mes[1];
                String text = sourceClient.getFromSource("api/users?page=" + N);
                sendMessage(chatId, text);

            }
            case GET_USER_BY_ID -> {
                if (mes.length == 1) {
                    sendMessage(chatId, "Неверный формат сообщения");
                } else {
                    String text = sourceClient.getFromSource("api/users/" + mes[1]);
                    sendMessage(chatId, text);
                }
            }
            case CREATE_USER -> {
                String text = sourceClient.postToSource("api/users", mes[1], mes[2]);
                sendMessage(chatId, text);
            }
        }
    }

    @Override
    public String getBotUsername() {
        return "rest_api_bot";
    }

    private void sendMessage(Long chatId, String text) {
        SendMessage sendMessage = new SendMessage(String.valueOf(chatId), text);
        try {
            execute(sendMessage);
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }
}
