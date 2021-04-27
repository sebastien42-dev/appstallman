package com.example.app_stallman;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

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
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        this.messageMList = (Button) findViewById(R.id.connectButton);

        this.messageMList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goMessage();
            }
        });
    }

    private void goMessage() {
        Intent goMessageList = new Intent(getApplicationContext(),MessageList.class);
        startActivity(goMessageList);
        finish();
    }
}