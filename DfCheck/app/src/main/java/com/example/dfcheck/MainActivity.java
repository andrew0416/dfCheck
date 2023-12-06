package com.example.dfcheck;

import androidx.appcompat.app.AppCompatActivity;

import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Spinner에 서버 목록 설정
        Spinner spinnerServer = findViewById(R.id.spinnerServer);
        EditText editTextNickname = findViewById(R.id.editTextNickname);
        TextView textViewApiResponse = findViewById(R.id.textViewApiResponse); // TextView 초기화

        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(
                this,
                R.array.server_array,
                android.R.layout.simple_spinner_item
        );
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerServer.setAdapter(adapter);

        Button buttonCheckCharacter = findViewById(R.id.buttonCheckCharacter);
        buttonCheckCharacter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String selectedServer = spinnerServer.getSelectedItem().toString();
                String characterName = editTextNickname.getText().toString();

                new CharacterSearchTask(textViewApiResponse).execute(selectedServer, characterName);
            }
        });
    }
    private class CharacterSearchTask extends AsyncTask<String, Void, String> {
        private final TextView textViewApiResponse;

        CharacterSearchTask(TextView textViewApiResponse) {
            this.textViewApiResponse = textViewApiResponse;
        }
        @Override
        protected String doInBackground(String... params) {
            // params[0]: selectedServer, params[1]: characterName
            String selectedServer = params[0];
            String characterName = params[1];

            // API 호출 및 JSON 응답을 문자열로 반환
            return ApiUtils.callApiAndGetResponse(ApiUtils.searchCharacter(selectedServer, characterName, getApplicationContext()));
        }

        @Override
        protected void onPostExecute(String jsonResponse) {
            // UI 갱신: API 응답을 받아서 처리하는 부분
            if (jsonResponse != null) {
                // JSON을 Character 객체로 파싱
                Character character = Character.fromJson(jsonResponse);

                if (character != null) {
                    // Character 객체에서 캐릭터의 이름을 가져와서 표시
                    String characterNameResult = "캐릭터 이름: " + character.getCharacterName();
                    textViewApiResponse.setText(characterNameResult);

                    // textViewApiResponse를 보이게 설정
                    textViewApiResponse.setVisibility(View.VISIBLE);
                } else {
                    // JSON 파싱 실패
                    Toast.makeText(MainActivity.this, "JSON 파싱 실패", Toast.LENGTH_SHORT).show();
                }
            } else {
                // API 호출 실패 또는 응답이 없는 경우
                Toast.makeText(MainActivity.this, "API 호출 실패 또는 응답 없음", Toast.LENGTH_SHORT).show();
            }
        }
    }
}