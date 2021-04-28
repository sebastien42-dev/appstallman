package com.example.app_stallman;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class Message extends AppCompatActivity {

    private Button buttonMessageList;
    private Button buttonResponse;

    private TextView contentMessage;
    private TextView titleMessage;
    private TextView userFromMessage;
    private TextView dateMessage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_message);

        this.buttonMessageList = findViewById(R.id.messageListButton);
        this.buttonResponse = findViewById(R.id.responseButton);


        this.contentMessage = findViewById(R.id.contentMessage);
        this.titleMessage = findViewById(R.id.titleMessage);
        this.userFromMessage = findViewById(R.id.fromMessage);
        this.dateMessage = findViewById(R.id.dateMessage);

        buttonMessageList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goMessageList();
            }
        });


        SharedPreferences id = getApplicationContext().getSharedPreferences("id_to_read", MODE_PRIVATE);
        String idToRead = id.getString("id_message","0");

        SharedPreferences prefs = getApplicationContext().getSharedPreferences("message_to_read"+idToRead, MODE_PRIVATE);
        String userFrom = prefs.getString("message_user_from","toto");
        String userFromSurname = prefs.getString("message_user_from_surname","titi");
        String content = prefs.getString("message_content","tutu");
        String title = prefs.getString("message_title","tata");
        String date = prefs.getString("message_date","0000-00-00");

        contentMessage.setText(content);
        titleMessage.setText(title);
        userFromMessage.setText(userFrom + " " +userFromSurname);
        dateMessage.setText(date);

    }

    public void goMessageList() {
        Intent goMessageList = new Intent(getApplicationContext(),MessageList.class);
        startActivity(goMessageList);
        finish();
    }
}