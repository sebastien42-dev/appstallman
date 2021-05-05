package com.example.app_stallman;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class bill extends AppCompatActivity {
    private TextView openTextBill;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bill);

        this.openTextBill = (TextView) findViewById(R.id.openTextBill);

        final String[] retourJson = new String[1];
        Thread thread = new Thread(new Runnable() {
            public void run() {
                try {
                    SharedPreferences prefs = getApplicationContext().getSharedPreferences("connected_user", MODE_PRIVATE);

                    Integer userid = prefs.getInt("id_user", 0);
                    String userName = prefs.getString("nom_user","toto");
                    String userSurname = prefs.getString("prenom_user","tutu");

                    TextView textOpen = new TextView(bill.this,null);
                    textOpen.findViewById(R.id.openTextBill);
                    openTextBill.setText("de " + userName + " " + userSurname);

                    Map<String, Object> mapJava = new HashMap<String, Object>();
                    ApiService http = new ApiService();
                    //mettre d'url de la machine sur la VM
                    String urlTest = "http://192.168.56.1/bddstall/public/api/bill/list/"+userid.toString();
                    retourJson[0] = http.sendRequest(urlTest, "GET", mapJava);
                    //System.out.println(retourJson);

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
        //Lancement du thread
        thread.start();
        try {
            thread.join();
            createTableLayout(retourJson[0]);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }


    private void createTableLayout(String retourJson) {

        TableLayout containerTable = (TableLayout) findViewById(R.id.tableBill);
        //entete du tableau des factures
        List<String> colonnes = new ArrayList<String>();
        colonnes.add("Numéro");
        colonnes.add("Date");
        colonnes.add("Montant");
        colonnes.add("Etat");
        colonnes.add("");

        JSONArray arrayJSON = new JSONArray();
        try {
            arrayJSON = new JSONArray(retourJson);
        } catch (JSONException e) {
            e.printStackTrace();
            Log.i("api", "retourne rien");
        }

        TableRow tableRow = new TableRow(bill.this);
        containerTable.addView(tableRow,
                new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT, TableRow.LayoutParams.WRAP_CONTENT));
        tableRow.setLayoutParams(new TableRow.LayoutParams(colonnes.size()));

        //DEFINITION DES COLONNES DU TABLEAU
        //ligne entete
        int i = 0;
        for (String texteColonne : colonnes) {
            TextView text = createTextView(false, i == colonnes.size() - 1);
            text.setText(texteColonne);
            text.setTextSize(20);
            text.setTextColor(Color.parseColor("#3446eb"));
            text.setTypeface(null, Typeface.BOLD);
            text.setGravity(Gravity.CENTER);
            tableRow.addView(text, i++);
        }

        //contenu
        for (int d = 0; d < arrayJSON.length(); d++) {
            //ON RECUPERE L'OBJET JSON DE CHAQUE LIGNE
            try {
                JSONObject jsonBill = arrayJSON.getJSONObject(d);

                tableRow = new TableRow(bill.this);
                containerTable.addView(tableRow,
                        new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT, TableRow.LayoutParams.WRAP_CONTENT));

                i = 0;
                String textId = jsonBill.get("id").toString();

                //je récuperer le user des factures
                JSONObject userBill = jsonBill.getJSONObject("user");
                String nameUserBill = userBill.get("nom").toString();
                String surnameUserBill = userBill.get("prenom").toString();
                Integer idUserBill = (Integer) userBill.get("id");

                String stateColor = "#000000";
                //je recupere l etat de la facture
                JSONObject billState = jsonBill.getJSONObject("bill_state");
                String billStateName = billState.get("state_name").toString();
                switch (billStateName) {
                    case "Créée":
                        stateColor = "#fc0303";
                        break;
                    case "En attente":
                        stateColor = "#fca103";
                        break;
                    case "Validée":
                        stateColor = "#034efc";
                        break;
                    case "Payée":
                        stateColor = "#17e300";
                        break;
                    default:
                        stateColor = "#000000";
                }

                String numBill = jsonBill.get("bill_provider_num").toString();
                String dateBill = jsonBill.get("created_at").toString();
                String valueBill = jsonBill.get("global_bill_value").toString();
                dateBill = dateBill.substring(0,10);

                //NUM de la facture
                TextView text = createTextView(d == 10, i == 2);
                text.setText(numBill);
                tableRow.addView(text, i++);
                text.setGravity(Gravity.LEFT);
                text.setTextColor(Color.parseColor("#3446eb"));
                //text.setSingleLine(false);

                //DATE de la facture
                text = createTextView(d == 10, i == 2);
                text.setText(dateBill);
                tableRow.addView(text, i++);
                text.setGravity(Gravity.CENTER);

                //MONTANT de la facture
                text = createTextView(d == 10, i == 2);
                text.setText(valueBill);
                tableRow.addView(text, i++);
                text.setGravity(Gravity.CENTER);

                //ETAT de la facture
                text = createTextView(d == 10, i == 2);
                text.setText(billStateName);
                text.setTextColor(Color.parseColor(stateColor));
                tableRow.addView(text, i++);
                text.setGravity(Gravity.CENTER);

                //BUTTON pour voir les lignes
                Button button = new Button(bill.this);
                button.setText("voir");

                int bottom = d==10 ? 1 : 0;
                int right = i==2 ? 1 : 0;
                TableRow.LayoutParams params = new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT, TableRow.LayoutParams.MATCH_PARENT, 0.1f);
                params.setMargins(1, 1, right, bottom);
                button.setLayoutParams(params);
                button.setPadding(4, 4, 10, 4);

                button.setTextColor(Color.parseColor("#3446eb"));
                button.setTypeface(null,Typeface.BOLD);
                tableRow.addView(button, i++);
                button.setGravity(Gravity.CENTER);

                //
//                SharedPreferences prefs = getApplicationContext().getSharedPreferences("message_to_read"+textId, MODE_PRIVATE);
//                SharedPreferences.Editor editor = prefs.edit ();
//                editor.putString("message_title", titleMessaqe);
//                editor.putString("message_user_from", nameUserFrom);
//                editor.putString("message_user_from_surname", surnameUserFrom);
//                editor.putInt("message_user_from_id", idUserFrom);
//                editor.putString("message_date", dateMessage);
//                editor.putString("message_content", contentMessage );
//
//                editor.commit ();

//                button.setOnClickListener(new View.OnClickListener() {
//                    @Override
//                    public void onClick(View v) {
//                        goMessageRead(textId);
//                    }
//                });

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    private TextView createTextView(boolean endline, boolean endcolumn) {
        TextView text = new TextView(this, null);
        int bottom = endline ? 1 : 0;
        int right = endcolumn ? 1 : 0;
        TableRow.LayoutParams params = new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT, TableRow.LayoutParams.MATCH_PARENT, 0.1f);
        params.setMargins(1, 1, right, bottom);
        text.setLayoutParams(params);
        text.setPadding(4, 4, 10, 4);
        text.setBackgroundColor(this.getColor(R.color.white));
        return text;
    }




    private void goMessage() {
        Intent goMessageList = new Intent(getApplicationContext(),MessageList.class);
        startActivity(goMessageList);
        finish();
    }

    private void goDashboard() {
        Intent goDashboard = new Intent(getApplicationContext(),Dashboard.class);
        startActivity(goDashboard);
        finish();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_bill, menu);
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle item selection
        switch (item.getItemId()) {
            case R.id.menuBillGoMessage:
                goMessage();
                return true;
            case R.id.menuBillGoDsh:
                goDashboard();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}