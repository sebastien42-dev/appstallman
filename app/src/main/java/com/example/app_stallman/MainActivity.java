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

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MainActivity extends AppCompatActivity {
    private Button messageMList;
    private EditText login;
    private EditText password;
    private TextView alertLogin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        this.messageMList = (Button) findViewById(R.id.connectButton);
        this.login = (EditText) findViewById(R.id.login);
        this.password = (EditText) findViewById(R.id.password);
        this.alertLogin = (TextView) findViewById(R.id.alertLogin);

        this.messageMList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Thread thread = new Thread(new Runnable() {
                    public void run() {
                        try {
                            String userLogin = login.getText().toString();
                            String userPwd = password.getText().toString();
                            Map<String, Object> mapJava = new HashMap<String, Object>();
                            mapJava.put("email", userLogin);
                            mapJava.put("password", userPwd);

                            ApiService http = new ApiService();
                            //mettre d'url de la machine sur la VM
                            String urlTest = "http://192.168.56.1/stallman2/public/api/login";
                            String retourJson = http.sendRequest(urlTest, "POST", mapJava);
                            System.out.println(retourJson);

                            JSONObject jsonUser = new JSONObject(retourJson);
                            String name = (String)jsonUser.get("nom");
                            String surname = (String)jsonUser.get("prenom");
                            Integer iduser = (Integer) jsonUser.get("id");

                            SharedPreferences prefs = getApplicationContext().getSharedPreferences("preferences-key-name", MODE_PRIVATE);
                            SharedPreferences.Editor editor = prefs.edit ();
                            editor.putInt("id_user",iduser );
                            editor.putString("prenom_user", surname);
                            editor.putString("nom_user", name);
                            editor.commit ();

                            goMessage();

                        } catch (Exception e) {
                            e.printStackTrace();
                            Log.i("test api login", "recuperation echouée");
                            goMainAlert();
                        }
                    }

                });
                thread.start();
            }
        });
    }
    // renvoie la page de l'activty list message
    private void goMessage() {
        Intent goMessageList = new Intent(getApplicationContext(),MessageList.class);
        startActivity(goMessageList);
        finish();
    }
    // renvoie l'alerte si le login est faux
    private void goMainAlert() {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                alertLogin.setText("Pas bon");
            }
        });
    }

    public void setSession() {
//        SharedPreferences prefs = getApplicationContext().getSharedPreferences("preferences-key-name", MODE_PRIVATE);
//        SharedPreferences.Editor editor = prefs.edit ();
//        editor.putString ("id_user", 2);
//        editor.commit ();
    }
}