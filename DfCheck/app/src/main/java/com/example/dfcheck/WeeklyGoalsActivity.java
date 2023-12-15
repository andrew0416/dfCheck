package com.example.dfcheck;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class WeeklyGoalsActivity extends AppCompatActivity {
    private TextView textViewCharacterName;
    private TextView textViewCurrentDate;
    private TextView textViewStartOfWeek;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_weekly_goals);


        // Intent에서 캐릭터 이름 가져오기
        String characterName = getIntent().getStringExtra("characterName");
        String characterID = getIntent().getStringExtra("characterID");
        String serverID = getIntent().getStringExtra("serverID");

        // 캐릭터 이름을 최상단 TextView에 표시
        textViewCharacterName = findViewById(R.id.textViewCharacterName);
        textViewCharacterName.setText(characterName);

        // 현재 날짜 설정
        textViewCurrentDate = findViewById(R.id.textViewCurrentDate);
        textViewStartOfWeek = findViewById(R.id.textViewStartOfWeek);
        // 현재 날짜 설정
        Date currentDate = new Date();
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMdd'T'HHmm", Locale.getDefault());
        String endDate = dateFormat.format(currentDate);
        textViewCurrentDate.setText("현재 날짜: " + endDate);

        // 일주일의 시작 날짜 설정
        Date startOfWeekThursday = DateUtils.getStartOfThisWeekThursday(currentDate);
        String startDate = dateFormat.format(startOfWeekThursday);
        textViewStartOfWeek.setText("시작 날짜: " + startDate);

        String apiText = ApiUtils.timeLineLegion(serverID, characterID, startDate, endDate, WeeklyGoalsActivity.this);
        LinearLayout Layout = findViewById(R.id.assignmentLayout);
        new WeeklyGoalsActivity.ApiTask(WeeklyGoalsActivity.this, Layout, apiText).execute();

        // 돌아가기 버튼 설정
        Button backButton = findViewById(R.id.buttonBackToMain);
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish(); // 현재 액티비티 종료
            }
        });
    }
    public class ApiTask extends AsyncTask<Void, Void, String> {

        private String apiText;
        private Context context;
        private LinearLayout Layout;
        private TextView textView1;
        private TextView textView2;
        private TextView textView3;


        public ApiTask(Context context, LinearLayout Layout,  String apiText) {
            this.context = context;
            this.Layout = Layout;
            this.textView1 = Layout.findViewById(R.id.textView1);
            this.textView2 = Layout.findViewById(R.id.textView2);
            this.textView3 = Layout.findViewById(R.id.textView3);
            this.apiText = apiText;
        }

        @Override
        protected String doInBackground(Void... voids) {

            String jsonText = ApiUtils.callApiAndGetResponse(apiText);
            jsonText = jsonText.substring(jsonText.indexOf("{"));
            try {
                JSONObject jsonResponse = new JSONObject(jsonText);
                JSONObject timeline = jsonResponse.getJSONObject("timeline");
                JSONArray rows = timeline.optJSONArray("rows");

                // 만약 rows가 null이면 모든 지역이 클리어되지 않은 것으로 가정
                boolean ispinzCleared = false;
                boolean chawonCleared = false;
                boolean odokCleared = false;

                if (rows != null) {
                    for (int i = 0; i < rows.length(); i++) {
                        JSONObject row = rows.getJSONObject(i);
                        JSONObject data = row.getJSONObject("data");
                        String regionName = data.getString("regionName");

                        // 클리어한 지역에 대한 처리
                        if (regionName == "이스핀즈"){
                            ispinzCleared = true;
                        }
                        if (regionName == "차원회랑"){
                            chawonCleared = true;
                        }
                        if (regionName == "어둑섬"){
                            odokCleared = true;
                        }
                    }
                    // 여기서 결과를 출력하거나 저장할 수 있습니다.
                    String result1 = String.format("이스핀즈: %s", ispinzCleared ? "O" : "X");
                    String result2 = String.format("차원회랑: %s", chawonCleared ? "O" : "X");
                    String result3 = String.format("어둑섬: %s", odokCleared ? "O" : "X");

                    textView1.setText(result1);
                    textView2.setText(result2);
                    textView3.setText(result3);
                }

            } catch (JSONException e) {
                e.printStackTrace();
            }
            return jsonText;
        }

        @Override
        protected void onPostExecute(String jsonText) {
        }
    }
}