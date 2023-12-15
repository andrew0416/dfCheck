package com.example.dfcheck;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class Character {
    private String serverId;
    private String characterId;
    private String characterName;
    private int level;
    private String jobId;
    private String jobGrowId;
    private String jobName;
    private String jobGrowName;
    private int fame;
    private boolean valid;

    public Character() {
    }

    public void fromJson(String jsonString) {
        String jsonText = jsonString.substring(jsonString.indexOf("{"));

        try {
            if (ApiUtils.isErrorResponse(jsonString)){
                this.valid = false;
                return;
            }
            JSONObject jsonObject = new JSONObject(jsonText);
            JSONArray rowsArray = jsonObject.getJSONArray("rows");
            JSONObject characterInfo = rowsArray.getJSONObject(0);

            this.serverId = characterInfo.getString("serverId");
            this.characterId = characterInfo.getString("characterId");
            this.characterName = characterInfo.getString("characterName");
            this.level = characterInfo.getInt("level");
            this.jobId = characterInfo.getString("jobId");
            this.jobGrowId = characterInfo.getString("jobGrowId");
            this.jobName = characterInfo.getString("jobName");
            this.jobGrowName = characterInfo.getString("jobGrowName");
            this.fame = characterInfo.getInt("fame");

            this.valid = true;
            if(this.characterId == null) {
                this.valid = false;
            }


        } catch (JSONException e) {
            e.printStackTrace();
            // 또는 필요한 예외 처리 로직을 여기에 추가
        }
    }

    public String getCharacterName() {
        return this.characterName;
    }

    public int getFame() {
        return this.fame;
    }

    public String getServerId() {
        return this.serverId;
    }

    public String getCharacterId() {
        return this.characterId;
    }

    public int getLevel() {
        return this.level;
    }

    public String getJobId() {
        return this.jobId;
    }

    public String getJobGrowId() {
        return this.jobGrowId;
    }

    public String getJobName() {
        return this.jobName;
    }

    public String getJobGrowName() {
        return this.jobGrowName;
    }

    public boolean isValid() {
        return this.valid;
    }

    public void setServerId(String serverId) {
        this.serverId = serverId;
    }

    public void setCharacterId(String characterId) {
        this.characterId = characterId;
    }

    public void setCharacterName(String characterName) {
        this.characterName = characterName;
    }

    public void setLevel(int level) {
        this.level = level;
    }

    public void setJobId(String jobId) {
        this.jobId = jobId;
    }

    public void setJobGrowId(String jobGrowId) {
        this.jobGrowId = jobGrowId;
    }

    public void setJobName(String jobName) {
        this.jobName = jobName;
    }

    public void setJobGrowName(String jobGrowName) {
        this.jobGrowName = jobGrowName;
    }

    public void setFame(int fame) {
        this.fame = fame;
    }
}
