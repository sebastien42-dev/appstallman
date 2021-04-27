package com.example.app_stallman;


import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Map;

public class ApiService {
    private final String USER_AGENT = "Mozilla/5.0";

    private String dataSend="";

    // HTTP request
    public String sendRequest(String url, String method, Map<String, Object> parameters) throws Exception {

        URL obj = new URL(url);
        HttpURLConnection con = (HttpURLConnection) obj.openConnection();

        //add request header
        con.setRequestMethod(method);
        con.setRequestProperty("User-Agent", USER_AGENT);
        con.setRequestProperty("Accept-Language", "en-US,en;q=0.5");

        JSONObject params = new JSONObject();

        String urlParameters = "";
        int i = 1;
        for (Map.Entry<String, Object> entry : parameters.entrySet()) {
            urlParameters += entry.getKey()+"="+entry.getValue();
            if (i < parameters.size()) {
                urlParameters += "&";
            }
            i++;
        }

        // Send request
        con.setDoOutput(true);
        DataOutputStream wr = new DataOutputStream(con.getOutputStream());

        wr.writeBytes(urlParameters);
        wr.flush();
        wr.close();

        //SI on souhaite recuperer le code de retour de la requete
        //int responseCode = con.getResponseCode();

        BufferedReader in = new BufferedReader(
                new InputStreamReader(con.getInputStream()));
        String inputLine;
        StringBuffer response = new StringBuffer();

        while ((inputLine = in.readLine()) != null) {
            response.append(inputLine);
        }
        in.close();

        //print result
        dataSend = response.toString();
        return dataSend;
    }
}