package com.example.zavrsni16052021;


import android.os.AsyncTask;
import android.os.AsyncTask;
import android.os.Build;
import android.util.Log;

import androidx.annotation.RequiresApi;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class Thingspeak_data {
    private String API_key;//="LSD5QYMX5FJGL4WE";
    private String ChannelID="1120413";
    private int number_of_minutes;

    public Thingspeak_data(){
        number_of_minutes=30;
    }
    public Thingspeak_data(int minutes){
        number_of_minutes=minutes;
    }
    public void change_history_minutes(int minutes){
        number_of_minutes=minutes;
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public List<Location_data> download(){
        AsyncTask<String, String, String> execute = new CallAPI().execute("https://api.thingspeak.com/channels/"+ ChannelID +"/feeds.json?api_key=" + API_key + "&results=500");

        String stranica="";
        try{
            stranica = execute.get();
        }
        catch (Exception e){
            //sta da mu ja radim
        }
        return JSON_interpreter(stranica);
    }


    @RequiresApi(api = Build.VERSION_CODES.O)
    private List<Location_data> JSON_interpreter(String stranica){
        List<Location_data> list=new ArrayList<>();

        JSONObject reader;
        try{
            reader = new JSONObject(stranica);


            JSONArray field1Array = reader.getJSONArray("feeds");
            for(int i=499;i>=0;i--){
                JSONObject entry=field1Array.getJSONObject(i);
                String locc_data=entry.getString("field1");
                String time_data=entry.getString("created_at");

                Location_data new_entry=new Location_data(locc_data, time_data);



                if(new_entry.getX()!=183 && new_entry.getX()!=182){
                    if(list.size()==0) list.add(new_entry);

                    if(new_entry.Timestamp_history_calc(time_data)<number_of_minutes*60){
                        list.add(new_entry);
                    }
                }
                if(list.size()>=1000) break;
            }

        }
        catch(Exception e){
        }

        for(int i=0;i<list.size();i++){
        }
        return list;
    }
    public void changeAPI(String mAPI){
        this.API_key=mAPI;
    }

}

