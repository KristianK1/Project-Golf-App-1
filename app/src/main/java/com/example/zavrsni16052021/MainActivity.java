package com.example.zavrsni16052021;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;


import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.os.ParcelUuid;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;

import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

public class MainActivity extends AppCompatActivity implements Login_interface, logout, ignore_click {
    private List<Creds> all_creds;

    private int device_status_var=0, ignore_status_var=0;

    private Login Loginfragment;
    private MapFragment Mapfragment;
    private SettingsFragment Settingsfragment;
    private BluetoothFragment Bluetoothfragment;

    private FragmentManager Fragmentmanager;
    private int screen=0;

    private Thingspeak_data data_handler;

    private String Bluetooth_name="ProjectGolf";
    private String Bluetooth_uuid="3C:61:05:2E:7F:FA";

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        all_creds=new ArrayList<>();
        all_creds.add(new Creds("sviki", "Lenovo7"));
        all_creds.add(new Creds("admin", "RRJKZ"));
        all_creds.add(new Creds("user", "Motobecane2"));

        setContentView(R.layout.activity_main);
        Fragmentmanager = getSupportFragmentManager();
        data_handler= new Thingspeak_data();
        setUpFragments();
        Load_settings();
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    private void switchFragments() {
        FragmentTransaction fragmentTransaction = Fragmentmanager.beginTransaction();
        recreateFragments();
        if (screen==0) {
            fragmentTransaction.replace(R.id.frame_layout, Loginfragment);
        }
        else if(screen==1) {
            fragmentTransaction.replace(R.id.frame_layout, Mapfragment);
        }
        else if(screen==2){
            fragmentTransaction.replace(R.id.frame_layout, Settingsfragment);
        }
        else if(screen==3){
            fragmentTransaction.replace(R.id.frame_layout, Bluetoothfragment);
        }
        fragmentTransaction.commit();
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    private void recreateFragments() {
        if(screen==0) Loginfragment = (Login) recreateFragment(Loginfragment);
        if(screen==1) Mapfragment = (MapFragment) recreateFragment(Mapfragment);
        if(screen==2) Settingsfragment = (SettingsFragment) recreateFragment(Settingsfragment);
        if(screen==3) {
            Bluetoothfragment = (BluetoothFragment) recreateFragment(Bluetoothfragment);
        }

    }

    private Fragment recreateFragment(Fragment f){
        try {
            Fragment newInstance = f.getClass().newInstance();
            return newInstance;
        }
        catch (Exception e) {
            throw new RuntimeException("Cannot reinstantiate fragment " + f.getClass().getName(), e);
            //return new Fragment();
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    private void setUpFragments() {
        Loginfragment = new Login();
        Mapfragment = new MapFragment();
        Settingsfragment = new SettingsFragment();
        Bluetoothfragment = new BluetoothFragment();
        FragmentTransaction fragmentTransaction = Fragmentmanager.beginTransaction();

        SharedPreferences sharedPreferencessss = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        String saved_username = sharedPreferencessss.getString("username", null);
        String saved_password = sharedPreferencessss.getString("password", null);

        if(saved_username!=null && saved_password != null){
            for(int i=0;i<all_creds.size();i++){
                if(all_creds.get(i).isCorrect(saved_username, saved_password)){
                    screen=1;
                    Toast.makeText(getApplicationContext(), "Login successfull", Toast.LENGTH_SHORT).show();
                }
            }
        }
        if(screen==0) fragmentTransaction.add(R.id.frame_layout, Loginfragment);
        if(screen==1) fragmentTransaction.add(R.id.frame_layout, Mapfragment);
        fragmentTransaction.commit();
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public void login(String mUsername, String mPassword){
        if(screen==0){
            //login

            for(int i=0;i<all_creds.size();i++){
                if(all_creds.get(i).isCorrect(mUsername, mPassword)){
                    SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
                    SharedPreferences.Editor editor = sharedPreferences.edit();

                    editor.putString("username", mUsername);
                    editor.putString("password", mPassword);
                    editor.apply();

                    SharedPreferences sharedPreferencessss = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
                    String saved_username = sharedPreferencessss.getString("username", null);
                    String saved_password = sharedPreferencessss.getString("password", null);

                    screen=1;
                    switchFragments();
                    return;
                }
            }
            Toast.makeText(this, "Ne valja", Toast.LENGTH_SHORT).show();
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public void Refresh_from_program(){
        List<Location_data> loc_list = data_handler.download();
        List<LatLng> list = new ArrayList<>();
        for (int i = 0; i < loc_list.size(); i++)
            list.add(new LatLng(loc_list.get(i).getY(), loc_list.get(i).getX()));
        Mapfragment.googleMappp.addMarker(new MarkerOptions().position(list.get(0)).title("Last recorded location").visible(true));
        Polyline polyline = Mapfragment.googleMappp.addPolyline(new PolylineOptions());
        polyline.setPoints(list);
        polyline.setVisible(true);
        Mapfragment.googleMappp.animateCamera(CameraUpdateFactory.newLatLngZoom(list.get(0), 15));
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public void refresh_map(android.view.View v){
        Refresh_from_program();
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public void Save_settings(android.view.View v){
        String new_history=Settingsfragment.getSelectedHistory();
        if(new_history!=null) {
            data_handler.change_history_minutes(History_str_to_int(new_history));
            SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
            SharedPreferences.Editor editor = sharedPreferences.edit();

            editor.putString("history_settings", new_history);
            editor.apply();
        }
        screen=1;
        switchFragments();
    }

    public void Load_settings(){
        SharedPreferences sharedPreferencessss = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());

        String saved_history = sharedPreferencessss.getString("history_settings", null);

        if(saved_history!=null){
            data_handler.change_history_minutes(History_str_to_int(saved_history));
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public void Open_settings(android.view.View v){
        screen=2;
        switchFragments();

        SharedPreferences sharedPreferencessss = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        String saved_history = sharedPreferencessss.getString("history_settings", null);
        if(saved_history!=null)  Settingsfragment.saved_history=saved_history;

    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public void OpenBT(android.view.View v){
        screen=3;
        switchFragments();
        Bluetoothfragment.setStates(device_status_var, ignore_status_var);

    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public void logoutt() {
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        SharedPreferences.Editor editor = sharedPreferences.edit();

        editor.putString("username", "");
        editor.putString("password", "");
        editor.apply();
        screen=0;
        switchFragments();
    }

    private OutputStream outputStream;
    private InputStream inStream;


    private long current_miliseconds(){
        Calendar rightNow = Calendar.getInstance();
        long offset = rightNow.get(Calendar.ZONE_OFFSET) +  rightNow.get(Calendar.DST_OFFSET);
        long sinceMidnight = (rightNow.getTimeInMillis() + offset) %  (24 * 60 * 60 * 1000);
        return  sinceMidnight; //zasto since mindnight samo??????
    }

    public int History_str_to_int(String history){
        switch(history){
            case "10 min":
                return 10;
            case "20 min":
                return 20;
            case "30 min":
                return 30;
            case "1 hour":
                return 60;
            case "2 hours":
                return 120;
            case "3 hours":
                return 180;
            case "6 hours":
                return 360;
            case "12 hours":
                return 720;
            case "1 day":
                return 1440;
            case "2 days":
                return 2880;
            case "5 days":
                return 7200;
            case "10 days":
                return 14400;
            case "30 days":
                return 43200;
        }
        return -1;
    }

    @Override
    public void ignore_mess(int code, int minutes) {
        Log.i("tipke_switch","main4");

        if(code==0 && minutes==0){//connect
            if(Bluetoothfragment.getDevice_status_var()>=0){ //nebitno

                String reply=BTsend("T?");
                if(reply.contains("T=111")) {

                    Log.i("tipke_switch","main21");
                    device_status_var = 1;
                    Bluetoothfragment.set_device_status(1);
                    ignore_status_var=0;
                    Bluetoothfragment.set_ignore_status(0);
                }
                else if(reply.contains("T=222")) {

                    Log.i("tipke_switch","main22");
                    device_status_var = 1;
                    ignore_status_var=1;
                    Bluetoothfragment.set_ignore_status(1);
                }
                else{
                    Log.i("tipke_switch", "nema uredaja");
                    device_status_var = 0;
                    Bluetoothfragment.set_device_status(0);

                    ignore_status_var=0;
                    Bluetoothfragment.set_ignore_status(0);
                }
            }
        }
        else if(code==0 && minutes>0){
            if(Bluetoothfragment.getDevice_status_var()==1){
                String mess="T=222,";

                if(minutes<10) mess+="00"+Integer.toString(minutes);
                else if(minutes>=10 && minutes<100) mess+="0"+Integer.toString(minutes);
                else if(minutes>=100 && minutes<1000) mess+=Integer.toString(minutes);
                Log.i("tipke_switch", "poruka=" +mess);
                String reply=BTsend(mess);

                if(reply.contains("T=222")) {
                    device_status_var = 1;

                    ignore_status_var=1;
                    Bluetoothfragment.set_ignore_status(1);
                }
                else {
                    Log.i("inputBT", "nema uredaja");
                    device_status_var = 0;
                    Bluetoothfragment.set_device_status(0);

                    ignore_status_var=0;
                }
            }
        }
        else if(code==1){
                String reply=BTsend("T=111");
                if(reply.contains("T=111")) {
                    device_status_var = 1;
                    Bluetoothfragment.set_device_status(1);
                    ignore_status_var=0;
                    Bluetoothfragment.set_ignore_status(0);
                }
                else{
                    Log.i("inputBT", "nema uredaja");
                    device_status_var = 0;
                    Bluetoothfragment.set_device_status(0);

                    ignore_status_var=0;
                }
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public void nazad_od_bta(View v){
        screen=1;
        switchFragments();
        device_status_var=Bluetoothfragment.getDevice_status_var();
        ignore_status_var=Bluetoothfragment.getIgnore_status_var();
    }

    public String BTsend(String send){
        String bt_adresa;
        Log.i("inputBT", "start");
        BluetoothAdapter bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        if (bluetoothAdapter == null) {
            Log.i("bt", "error: bluetooth not supported");
            return "X";
        }

        Intent enableBluetoothIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
        startActivityForResult(enableBluetoothIntent, 0);
        Set<BluetoothDevice> bondedDevices = bluetoothAdapter.getBondedDevices();

        Object[] devices = (Object []) bondedDevices.toArray();
        for(int i=0;i<bondedDevices.size();i++) {
            BluetoothDevice device = (BluetoothDevice) devices[i];
            Log.i("bt","device name: "+device.getName());
            Log.i("bt","device adress: "+device.getAddress());
            ParcelUuid[] uuids = device.getUuids();
            try{
                BluetoothSocket socket = device.createRfcommSocketToServiceRecord(uuids[0].getUuid());
                Log.i("bt", "errordfdf");
                socket.connect();
                Log.i("bt", "errordf");
                outputStream = socket.getOutputStream();
                inStream = socket.getInputStream();
                outputStream.write(send.getBytes());
                long time1=current_miliseconds();
                while(current_miliseconds()-time1<3000);  //PONOC SHITTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTT
                String reply="";
                while(inStream.available()>0) reply+=(char)inStream.read();
                Log.i("inputBT", reply);
                socket.close();
                return reply;
            }
            catch (Exception e){
                Log.i("bt", "exception");
                Toast.makeText(getApplicationContext(), "Bluetooth communication failed",Toast.LENGTH_LONG);
                return "X";
            }
        }
        return "E";
    }
}