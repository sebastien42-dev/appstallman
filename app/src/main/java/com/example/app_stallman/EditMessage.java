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

public class EditMessage extends AppCompatActivity {

    private Button buttonMessageList;
    private Button buttonSenResponse;
    private TextView messageToResponse;
    private TextView titleToResponse;

    private EditText contentMessage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_message);

        this.buttonMessageList = findViewById(R.id.messageListButton);
        this.buttonSenResponse = findViewById(R.id.senResponseButton);

        this.contentMessage = (EditText) findViewById(R.id.contentResponse);

        this.messageToResponse = (TextView) findViewById(R.id.messageToResponse);
        this.titleToResponse = (TextView) findViewById(R.id.titleToResponse);

        SharedPreferences to = getApplicationContext().getSharedPreferences("response_message", MODE_PRIVATE);
        String nomFrom = to.getString("nom_user","toto");
        String prenomFrom = to.getString("prenom_user","tata");
        String titleFrom = to.getString("title_message","titi");
        Integer idTo = to.getInt("id_user",0);

        SharedPreferences sender = getApplicationContext().getSharedPreferences("connected_user", MODE_PRIVATE);
        Integer idFrom = sender.getInt("id_user",0);


        titleFrom = "repAppStallman:" + titleFrom;

        messageToResponse.setText("Pour : "+nomFrom + " "+ prenomFrom );
        titleToResponse.setText("titre : " + titleFrom);

        buttonMessageList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goMessageList();
            }
        });

        String finalTitleFrom = titleFrom;
        buttonSenResponse.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Thread thread = new Thread(new Runnable() {
                    public void run() {
                        try {
                            String contentToSend = contentMessage.getText().toString();

                            Map<String, Object> mapJava = new HashMap<String, Object>();
                            mapJava.put("content", contentToSend);
                            mapJava.put("title", finalTitleFrom);
                            mapJava.put("user_from", idFrom);
                            mapJava.put("user_to", idTo);
                            mapJava.put("is_important", 1);

                            ApiService http = new ApiService();

                            //mettre d'url de la machine sur la VM
                            String urlTest = "http://192.168.56.1/stallman2/public/api/message/new";
                            String retourJson = http.sendRequest(urlTest, "PUT", mapJava);
                            //System.out.println(retourJson);

                            goMessageList();

                        } catch (Exception e) {
                            e.printStackTrace();
                            Log.i("test api login", "recuperation echouee");
                        }
                    }
                });
                thread.start();
            }
        });
    }

    public void goMessageList() {
        Intent goMessageList = new Intent(getApplicationContext(),MessageList.class);
        startActivity(goMessageList);
        finish();
    }
}