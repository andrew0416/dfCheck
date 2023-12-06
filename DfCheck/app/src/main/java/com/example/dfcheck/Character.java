package com.example.dfcheck;

import com.google.gson.Gson;

public class Character {
    private String serverId;
    private String characterId;
    private String characterName;
    private String jobName;
    private String jobGrowName;
    private int fame;

    public Character() {
    }

    public static Character fromJson(String json) {
        Gson gson = new Gson();
        return gson.fromJson(json, Character.class);
    }

    public void update() {

    }
    public String getCharacterName() {
        return characterName;
    }

    public int getFame() {
        return fame;
    }

}
