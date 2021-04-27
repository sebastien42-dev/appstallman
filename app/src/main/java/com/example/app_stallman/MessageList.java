package com.example.app_stallman;

import androidx.appcompat.app.AppCompatActivity;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.ListView;

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

public class MessageList extends AppCompatActivity {
    ListView simpleList;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_message_list);

        Thread thread = new Thread(new Runnable() {
            public void run() {
                try {
                    SharedPreferences prefs = getApplicationContext().getSharedPreferences("connected_user", MODE_PRIVATE);
                    Integer userid = prefs.getInt("id_user", 0);
                    String userName = prefs.getString("nom_user","toto");
                    String userSurname = prefs.getString("prenom_user","tutu");
                    Log.i("test_session_message", userid.toString()+userName+userSurname);


                    Map<String, Object> mapJava = new HashMap<String, Object>();
                    ApiService http = new ApiService();
                    //mettre d'url de la machine sur la VM
                    String urlTest = "http://192.168.56.1/stallman2/public/api/message/list/"+userid.toString();
                    String retourJson = http.sendRequest(urlTest, "GET", mapJava);
                    System.out.println(retourJson);




                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
        //Lancement du thread
        thread.start();

    }
}