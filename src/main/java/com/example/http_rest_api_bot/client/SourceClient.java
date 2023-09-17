package com.example.http_rest_api_bot.client;

import com.example.http_rest_api_bot.util.ParseJson;
import okhttp3.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class SourceClient {

    private final OkHttpClient okHttpClient;

    @Value("${source.url}")
    private String url;

    public SourceClient(OkHttpClient okHttpClient) {
        this.okHttpClient = okHttpClient;
    }

    public String getFromSource(String path) {
        Request request = new Request.Builder()
                .url(url + path)
                .build();

        try {
            Response response = okHttpClient.newCall(request).execute();
            ResponseBody body = response.body();
            return body == null ? null : ParseJson.parseJson(body.string());
        } catch (IOException e) {
            e.printStackTrace();
            return "Ошибка исполнения запроса";
        }
    }

    public String postToSource(String path, String firstName, String lastName) {
        RequestBody formBody = new FormBody.Builder()
                .add("first_name", firstName)
                .add("last_name", lastName)
                .build();

        Request request = new Request.Builder()
                .url(url + path)
                .post(formBody)
                .build();

        try {
            ResponseBody body = okHttpClient.newCall(request).execute().body();
            return body == null ? null : ParseJson.parseJsonId(body.string());
        } catch (IOException e) {
            e.printStackTrace();
            return "Ошибка исполнения запроса";
        }
    }
}
