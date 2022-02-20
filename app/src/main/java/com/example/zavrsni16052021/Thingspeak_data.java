package com.example.zavrsni16052021;


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
    private String ChannelID; //=1120413";
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
      
        String stranica="";

        int how_much;
        if(number_of_minutes<=30) how_much = 60;
        else if(number_of_minutes<=180) how_much = 300;
        else if(number_of_minutes<=1440) how_much = 750;
        else if(number_of_minutes<=14400) how_much = 1500;
        else if(number_of_minutes<=43200) how_much = 3000;//30 dana
        else how_much=8000;



        Log.i("debuggg", "gdje");

        try{
            AsyncTask<String, String, String> execute = new CallAPI().execute("https://api.thingspeak.com/channels/"+ ChannelID +"/feeds.json?api_key=" + API_key + "&results="+how_much);

            Log.i("debuggg", "gdje2");
            stranica = execute.get();

            Log.i("debuggg", stranica);
            Log.i("debuggg", "gdje3");
        }
        catch (Exception e){
            Log.i("debuggg", "puko net");
            //return new ArrayList<>();
        }

        Log.i("debuggg", "gdje4");
        return JSON_interpreter(stranica, how_much);
    }


    @RequiresApi(api = Build.VERSION_CODES.O)
    private List<Location_data> JSON_interpreter(String stranica, int size){
        List<Location_data> list=new ArrayList<>();

        JSONObject reader;
        int i=-9;
        try{
            reader = new JSONObject(stranica);


            JSONArray field1Array = reader.getJSONArray("feeds");
            for(i=size-1;i>=0;i--){

                JSONObject entry=field1Array.getJSONObject(i);
                String locc_data=entry.getString("field1");
                String time_data=entry.getString("created_at");

                Location_data new_entry=new Location_data(locc_data, time_data);



                if(new_entry.getX()!=183 && new_entry.getX()!=182){
                    if(list.size()==0) {
                        list.add(new_entry);
                    }

                    else if(new_entry.Timestamp_history_calc(time_data)<number_of_minutes*60){
                        list.add(new_entry);
                    }
                }
                //if(list.size()>=1000) break;
            }

        }
        catch(Exception e){
            Log.i("debuggg", "iznimka");
            Log.i("debuggg", e.toString());
            Log.i("debuggg", ""+i);
        }


        Log.i("debuggg", "gdje5");

        Log.i("debuggg", list.toString());
        return list;
    }
    public void changeAPI(String mAPI){
        this.API_key=mAPI;
    }

    public void changeChannelID(String mID){
        this.ChannelID=mID;
    }

}

