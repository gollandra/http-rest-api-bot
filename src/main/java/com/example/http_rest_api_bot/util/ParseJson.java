package com.example.http_rest_api_bot.util;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

public class ParseJson {

    public static String parseJson(String source) {

        try {
            StringBuilder result = new StringBuilder();
            JSONParser jsonParser = new JSONParser();
            JSONObject jsonObject = (JSONObject) jsonParser.parse(source);

            try {
                JSONArray users = (JSONArray) jsonObject.get("data");
                users.forEach(user ->
                        result.append(userToString((JSONObject) user))
                );
            } catch (ClassCastException e) {
                JSONObject user = (JSONObject) jsonObject.get("data");
                result.append(userToString(user));
            }
            return result.toString();
        } catch (ParseException e) {
            e.printStackTrace();
            return "Ошибка исполнения запроса";
        }
    }

    public static String parseJsonId(String source) {
        try {
            JSONParser jsonParser = new JSONParser();
            JSONObject jsonObject = (JSONObject) jsonParser.parse(source);
            return (String) jsonObject.get("id");
        } catch (ParseException e) {
            e.printStackTrace();
            return "Ошибка исполнения запроса";
        }
    }

    private static String userToString(JSONObject user) {
        return user.get("id") + ": " +
                user.get("first_name") + " " +
                user.get("last_name") + ", " +
                user.get("email") + "\n";
    }
}
