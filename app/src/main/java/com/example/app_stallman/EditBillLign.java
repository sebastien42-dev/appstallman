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

public class EditBillLign extends AppCompatActivity {

    private Button billListButton;
    private Button sendOutPackageButton;

    private TextView billText;

    private EditText EditOutPackageName;
    private EditText EditOutPackageValue;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_bill_lign);

        this.billListButton = findViewById(R.id.billListButton);
        this.sendOutPackageButton = findViewById(R.id.sendOutPackageButton);

        this.EditOutPackageName = (EditText) findViewById(R.id.EditOutPackageName);
        this.EditOutPackageValue = (EditText) findViewById(R.id.EditOutPackageValue);

        this.billText = (TextView) findViewById(R.id.billText);

        SharedPreferences to = getApplicationContext().getSharedPreferences("id_bill_to_response", MODE_PRIVATE);
        String idBill = to.getString("id_bill","0");
        String numBill = to.getString("num_bill","0");


        billText.setText("ajouter sur facture " + numBill);

        billListButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goBillList();
            }
        });


        sendOutPackageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Thread thread = new Thread(new Runnable() {
                    public void run() {
                        try {
                            String packageName = EditOutPackageName.getText().toString();
                            String packageValue = EditOutPackageValue.getText().toString();

                            Map<String, Object> mapJava = new HashMap<String, Object>();
                            mapJava.put("out_package_name", packageName);
                            mapJava.put("out_package_value", packageValue);

                            ApiService http = new ApiService();

                            //mettre d'url de la machine sur la VM
                            String urlTest = http.urlApi+"outpackage/new/"+idBill;
                            String retourJson = http.sendRequest(urlTest, "PUT", mapJava);
                            //System.out.println(retourJson);

                            goBillList();

                        } catch (Exception e) {
                            e.printStackTrace();
                            Log.i("test api put d'un pack", "recuperation echouee");
                        }
                    }
                });
                thread.start();
            }
        });
    }
    public void goBillList() {
        Intent goBill = new Intent(getApplicationContext(),bill.class);
        startActivity(goBill);
        finish();
    }
}