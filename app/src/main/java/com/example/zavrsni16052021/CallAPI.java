package com.example.zavrsni16052021;


import android.os.AsyncTask;
import android.util.Log;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class CallAPI extends AsyncTask<String, String, String> {
    @Override
    protected String doInBackground(String... params) {

        String urlString = params[0]; // URL to call
        String result = "";
        InputStream in = null;

        // HTTP Get
        try {
            URL url = new URL(urlString);
            HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
            in = new BufferedInputStream(urlConnection.getInputStream());
            InputStreamReader ISreader = new InputStreamReader(urlConnection.getInputStream());
            BufferedReader reader = new BufferedReader(ISreader);
            StringBuffer sb = new StringBuffer();
            String str;
            while ((str = reader.readLine()) != null) {
                sb.append(str);
            }
            result = sb.toString();
        } catch (Exception e) {
            return e.getMessage();
        }
        return result;

    }

    protected void onPostExecute(String result) {

    }
}