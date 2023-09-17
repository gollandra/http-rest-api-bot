package com.example.http_rest_api_bot.config;

import com.example.http_rest_api_bot.bot.ProcessingHttpBot;
import okhttp3.OkHttpClient;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.telegram.telegrambots.meta.TelegramBotsApi;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.updatesreceivers.DefaultBotSession;

@Configuration
public class ProcessingHttpBotConfiguration {

    @Bean
    public TelegramBotsApi telegramBotsApi(ProcessingHttpBot processingHttpBot)
            throws TelegramApiException {
        TelegramBotsApi telegramBotsApi = new TelegramBotsApi(DefaultBotSession.class);
        telegramBotsApi.registerBot(processingHttpBot);
        return telegramBotsApi;
    }

    @Bean
    public OkHttpClient okHttpClient() {
        return new OkHttpClient();
    }
}
