package com.example.dfcheck;

import android.content.Context;
import android.content.res.Resources;

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
}