package com.example.app_stallman;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import java.util.HashMap;
import java.util.Map;

public class Message extends AppCompatActivity {

    private Button buttonMessageList;
    private Button buttonResponse;
    private Button buttonArchived;

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
        this.buttonArchived = findViewById(R.id.archivedButton);


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

        buttonResponse.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goMessageEdit();
            }
        });


        SharedPreferences id = getApplicationContext().getSharedPreferences("id_to_read", MODE_PRIVATE);
        String idToRead = id.getString("id_message","0");

        buttonArchived.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Thread thread = new Thread(new Runnable() {
                    public void run() {
                        try {
                            ApiService http = new ApiService();
                            Map<String, Object> mapJava = new HashMap<String, Object>();
                            String urlTest = http.urlApi+"message/archived/"+ idToRead;
                            String retourJson = http.sendRequest(urlTest, "PATCH", mapJava);
                            //System.out.println(retourJson);

                            goMessageList();

                        } catch (Exception e) {
                            e.printStackTrace();
                            Log.i("test api update message", "archivage raté");
                        }
                    }
                });
                thread.start();
            }
        });

        SharedPreferences prefs = getApplicationContext().getSharedPreferences("message_to_read"+idToRead, MODE_PRIVATE);
        String userFrom = prefs.getString("message_user_from","toto");
        String userFromSurname = prefs.getString("message_user_from_surname","titi");
        Integer userFromid = prefs.getInt("message_user_from_id",2);

        String content = prefs.getString("message_content","tutu");
        String title = prefs.getString("message_title","tata");
        String date = prefs.getString("message_date","0000-00-00");

        setSessionForEdit(userFrom,userFromSurname,userFromid,title);

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

    public void goMessageEdit() {
        Intent goMessageEdit = new Intent(getApplicationContext(), EditMessage.class);
        startActivity(goMessageEdit);
        finish();
    }

    public void setSessionForEdit(String name,String surname,Integer idUserFrom,String titleMessage) {

        SharedPreferences prefs = getApplicationContext().getSharedPreferences("response_message", MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit ();

        editor.putInt("id_user",idUserFrom );
        editor.putString("prenom_user", surname);
        editor.putString("nom_user", name);
        editor.putString("title_message", titleMessage);

        editor.commit ();
    }
}