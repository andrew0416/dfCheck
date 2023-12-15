package com.example.dfcheck;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Spinner에 서버 목록 설정
        Spinner spinnerServer = findViewById(R.id.spinnerServer);
        EditText editTextNickname = findViewById(R.id.editTextNickname);
        LinearLayout resultLayout = findViewById(R.id.resultLayout);
        ListView listViewCharacters = findViewById(R.id.listViewCharacters);

        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(
                this,
                R.array.server_array,
                android.R.layout.simple_spinner_item
        );
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerServer.setAdapter(adapter);

        updateListView(MainActivity.this, listViewCharacters);

        Button buttonCheckCharacter = findViewById(R.id.buttonCheckCharacter);
        buttonCheckCharacter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String selectedServer = spinnerServer.getSelectedItem().toString();
                String characterName = editTextNickname.getText().toString();

                new TestExecutionTask(MainActivity.this, resultLayout, listViewCharacters).execute(selectedServer, characterName);
            }
        });
    }

    private void updateListView(Context context, ListView listViewCharacters) {
        // 데이터베이스에서 모든 캐릭터를 가져오기
        CharacterDB characterDB = new CharacterDB(context);
        List<Character> characters = characterDB.getAllCharacters();

        // 어댑터에 데이터 설정
        CharacterAdapter adapter = new CharacterAdapter(context, characters);

        listViewCharacters.setAdapter(adapter);
    }

    private class TestExecutionTask extends AsyncTask<String, Void, String> {
        private Context context;
        private LinearLayout resultLayout; // 변경된 부분
        private TextView resultTextView;
        private Button confirmButton;
        private Button cancelButton;
        private ListView listViewCharacters;

        public TestExecutionTask(Context context, LinearLayout resultLayout, ListView listViewCharacters) {
            this.context = context;
            this.resultLayout = resultLayout;
            this.resultTextView = resultLayout.findViewById(R.id.textViewResult);
            this.confirmButton = resultLayout.findViewById(R.id.buttonConfirm);
            this.cancelButton = resultLayout.findViewById(R.id.buttonCancel);
            this.listViewCharacters = listViewCharacters;
        }
        @Override
        protected String doInBackground(String... params) {
            String selectedServer = params[0];
            String characterName = params[1];
            return ApiUtils.callApiAndGetResponse(ApiUtils.searchCharacter(selectedServer, characterName, getApplicationContext()));
        }

        @Override
        protected void onPostExecute(String jsonResponse) {
            Character character = new Character();
            character.fromJson(jsonResponse);

            if (character != null) {
                String text = character.getFame() + " " + character.getCharacterName();
                resultLayout.setVisibility(View.VISIBLE);
                resultTextView.setText(text);

                resultLayout.post(new Runnable() {
                    @Override
                    public void run() {
                        confirmButton.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                CharacterDB characterDB = new CharacterDB(context);
                                characterDB.addCharacter(character);
                                resultLayout.setVisibility(View.GONE);

                                updateListView(context, listViewCharacters);
                            }
                        });

                        cancelButton.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                resultLayout.setVisibility(View.GONE);
                                updateListView(context, listViewCharacters);
                            }
                        });
                    }
                });

            } else {
            }
        }
    }

}
