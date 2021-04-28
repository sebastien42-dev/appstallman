package com.example.app_stallman;

import androidx.appcompat.app.AppCompatActivity;

import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
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
    private TextView openText;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_message_list);

        this.openText = (TextView) findViewById(R.id.textMessageList);

        final String[] retourJson = new String[1];
        Thread thread = new Thread(new Runnable() {
            public void run() {
                try {
                    SharedPreferences prefs = getApplicationContext().getSharedPreferences("connected_user", MODE_PRIVATE);

                    Integer userid = prefs.getInt("id_user", 0);
                    String userName = prefs.getString("nom_user","toto");
                    String userSurname = prefs.getString("prenom_user","tutu");

                    TextView textOpen = new TextView(MessageList.this,null);
                    textOpen.findViewById(R.id.textMessageList);
                    openText.setText("Bonjour " + userName + " " + userSurname);

                    Map<String, Object> mapJava = new HashMap<String, Object>();
                    ApiService http = new ApiService();
                    //mettre d'url de la machine sur la VM
                    String urlTest = "http://192.168.56.1/stallman2/public/api/message/list/"+userid.toString();
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
        TableLayout containerTable = (TableLayout) findViewById(R.id.containerTable);

        // Recuperation du table layout sur lequel nous allons agir
        // Chacune des entrées de cette liste est l'entête de la colonne
        List<String> colonnes = new ArrayList<String>();
        colonnes.add("Message de");
        colonnes.add("Titre");
        colonnes.add("Voir");

        JSONArray arrayJSON = new JSONArray();
        try {
            arrayJSON = new JSONArray(retourJson);
        } catch (JSONException e) {
            e.printStackTrace();
            Log.i("coucou", "c'est moi: ");
        }

        // On va calculer la largeur des colonnes en fonction de la marge de 10
        // On affiche l'enreg dans une ligne
        TableRow tableRow = new TableRow(MessageList.this);
        containerTable.addView(tableRow,
                new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT, TableRow.LayoutParams.WRAP_CONTENT));

        // On crée une ligne de x players colonnes
        tableRow.setLayoutParams(new TableRow.LayoutParams(colonnes.size()));

        // On va commencer par renseigner une ligne de titre par joueur

        //DEFINITION DES COLONNES DU TABLEAU
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

        for (int d = 0; d < arrayJSON.length(); d++) {
            //ON RECUPERE L'OBJET JSON DE CHAQUE LIGNE
            try {
                JSONObject jsonMessage = arrayJSON.getJSONObject(d);

                tableRow = new TableRow(MessageList.this);
                containerTable.addView(tableRow,
                        new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT, TableRow.LayoutParams.WRAP_CONTENT));

                //i sert a definir si on est en fin de ligne ou pas
                i = 0;

                String textId = jsonMessage.get("id").toString();

                JSONObject userFrom = jsonMessage.getJSONObject("user_from");

                String nameUserFrom = userFrom.get("nom").toString();
                String textName = jsonMessage.get("title").toString();
                //Integer id = Integer.parseInt(jsonArtiste.get("id").toString());

                //TEXTE COLONNE 1
                TextView text = createTextView(d == 10, i == 2);
                text.setText(nameUserFrom);
                tableRow.addView(text, i++);
                text.setGravity(Gravity.LEFT);
                text.setTextColor(Color.parseColor("#3446eb"));
                text.setSingleLine(false);

                //TEXTE COLONNE 2
                text = createTextView(d == 10, i == 2);
                text.setText(textName);
                tableRow.addView(text, i++);
                text.setGravity(Gravity.CENTER);

                //TEXTE COLONNE 2
                text = createTextView(d == 10, i == 2);
                text.setText("voir");
                tableRow.addView(text, i++);
                text.setGravity(Gravity.CENTER);

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
}