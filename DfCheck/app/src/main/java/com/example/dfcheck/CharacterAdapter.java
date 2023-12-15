package com.example.dfcheck;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.bumptech.glide.Glide;

import java.util.List;

public class CharacterAdapter extends ArrayAdapter<Character> {
    private final Context context;
    private final List<Character> characters;
    public CharacterAdapter(Context context, List<Character> characters) {
        super(context, R.layout.character_item, characters);
        this.context = context;
        this.characters = characters;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View rowView = inflater.inflate(R.layout.character_item, parent, false);

        TextView textViewCharacterName = rowView.findViewById(R.id.textViewCharacterName);
        TextView textViewServerId = rowView.findViewById(R.id.textViewServerId);
        TextView textViewJobInfo = rowView.findViewById(R.id.textViewJobInfo);
        ImageView imageViewCharacter = rowView.findViewById(R.id.imageViewCharacter);

        Character character = characters.get(position);

        textViewCharacterName.setText("캐릭터 이름: " + character.getCharacterName());
        textViewServerId.setText("서버 ID: " + character.getServerId());
        textViewJobInfo.setText("직업: " + character.getJobGrowName());

        String ImageLink = ApiUtils.searchCharacterImage(character.getServerId(), character.getCharacterId(), 1);
        Glide.with(context).load(ImageLink).into(imageViewCharacter);

        rowView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 클릭된 항목의 정보를 가져옴
                Character selectedCharacter = characters.get(position);

                // Intent를 사용하여 다른 화면으로 이동
                Intent intent = new Intent(context, WeeklyGoalsActivity.class);
                intent.putExtra("characterName", selectedCharacter.getCharacterName());
                intent.putExtra("characterID", selectedCharacter.getCharacterId());
                intent.putExtra("serverID", selectedCharacter.getServerId());
                context.startActivity(intent);
            }
        });

        ImageButton deleteButton = rowView.findViewById(R.id.imageButtonDelete);

        deleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 삭제 버튼 클릭 시 수행할 동작
                Character characterToDelete = getItem(position);
                if (characterToDelete != null) {
                    // CharacterDB에서 해당 캐릭터 삭제
                    CharacterDB characterDB = new CharacterDB(getContext());
                    characterDB.deleteCharacter(characterToDelete);

                    // 리스트에서 해당 아이템 제거
                    remove(characterToDelete);
                }
            }
        });

        return rowView;
    }

    public void deleteCharacter(int position) {
        Character characterToDelete = getItem(position);
        if (characterToDelete != null) {
            CharacterDB characterDB = new CharacterDB(getContext());
            characterDB.deleteCharacter(characterToDelete);
            remove(characterToDelete);
            notifyDataSetChanged();
        }
    }
}
