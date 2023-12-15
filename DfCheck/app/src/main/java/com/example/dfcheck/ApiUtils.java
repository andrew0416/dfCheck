package com.example.dfcheck;

import android.content.Context;
import android.content.res.Resources;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.io.IOException;
import java.nio.charset.StandardCharsets;

public class ApiUtils {

    private static final String BASE_URL = "https://api.neople.co.kr";
    private static final String IMAGE_URL = "https://img-api.neople.co.kr/";
    private static final String API_KEY = "sdKwTj8OPv2qYUo9LiESRxIM9D3lyJVF";

    public static String callApiAndGetResponse(String apiUrl) {
        try {
            // API 요청
            URL url = new URL(apiUrl);
            HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();

            try {
                // InputStream으로부터 데이터 읽기
                InputStream inputStream = urlConnection.getInputStream();
                BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream, StandardCharsets.UTF_8));
                StringBuilder result = new StringBuilder();
                String line;

                while ((line = reader.readLine()) != null) {
                    result.append(line);
                }

                return result.toString();
            } finally {
                // 연결 해제
                urlConnection.disconnect();
            }
        } catch (IOException e) {
            e.printStackTrace();
            // API 호출 중에 예외가 발생한 경우 null 또는 에러 메시지 반환
            return null;
        }
    }


    public static String getServerId(String selectedServer, Context context) {
        Resources resources = context.getResources();
        String[] serverNames = resources.getStringArray(R.array.server_array);
        String[] serverIds = resources.getStringArray(R.array.server_ids);

        for (int i = 0; i < serverNames.length; i++) {
            if (selectedServer.equals(serverNames[i])) {
                return serverIds[i];
            }
        }
        return null;
    }

    public static String searchCharacter(String selectedServer, String characterName, Context context) {

        String serverId = ApiUtils.getServerId(selectedServer, context);

        if (serverId == null) {
            return null;
        }
        try {
            String encodedServer = URLEncoder.encode(serverId, "UTF-8");
            String encodedCharacterName = URLEncoder.encode(characterName, "UTF-8");

            return BASE_URL + "/df/servers/" + encodedServer + "/characters?characterName=" + encodedCharacterName + "&apikey=" + API_KEY;

        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static String timeLineLegion(String serverId, String characterId, String startDate, String endDate, Context context) {

        try {
            String encodedServer = URLEncoder.encode(serverId, "UTF-8");
            String encodedCharacterName = URLEncoder.encode(characterId, "UTF-8");
            String encodedSDate = URLEncoder.encode(startDate, "UTF-8");
            String encodedEDate = URLEncoder.encode(endDate, "UTF-8");

            return BASE_URL + "/df/servers/" + encodedServer + "/characters?characterName=" + encodedCharacterName + "/timeline?code=209&startDate="+ encodedSDate + "&endDate="+ encodedEDate + "&apikey=" + API_KEY;

        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static String searchCharacterImage(String serverId, String characterId, int zoom) {
        try {
            String encodedServer = URLEncoder.encode(serverId, "UTF-8");
            String encodedCharacterID = URLEncoder.encode(characterId, "UTF-8");
            String stringZoom = zoom + "";
            return IMAGE_URL + "/df/servers/" + encodedServer + "/characters/" + encodedCharacterID + "?zoom=" + stringZoom;

        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static boolean isErrorResponse(String jsonString) {
        try {
            JSONObject jsonObject = new JSONObject(jsonString);
            if (jsonObject.has("error")) {
                JSONObject errorObject = jsonObject.getJSONObject("error");
                if (errorObject.has("status") &&
                        errorObject.has("code") &&
                        errorObject.has("message")) {
                    return true;
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return false;
    }
}