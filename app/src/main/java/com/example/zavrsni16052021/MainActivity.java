package com.example.zavrsni16052021;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;


import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.os.ParcelUuid;
import android.preference.PreferenceManager;
import android.service.autofill.UserData;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.autofill.AutofillManager;
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
import java.util.Set;

public class MainActivity extends AppCompatActivity implements Login_interface, logout, ignore_click {
    private List<Creds> all_creds;

    private int device_status_var = 0, ignore_status_var = 0;

    private Login Loginfragment;
    private MapFragment Mapfragment;
    private SettingsFragment Settingsfragment;
    private BluetoothFragment Bluetoothfragment;

    private FragmentManager Fragmentmanager;
    private int screen = 0;

    private Thingspeak_data data_handler;

    private String Bluetooth_name = "ProjectGolf";
    private String Bluetooth_uuid; //= "3C:61:05:2E:7F:FA";

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        all_creds = new ArrayList<>();
//        all_creds.add(new Creds("sviki", "Lenovo7"));  // Micem zbog APi logina
//        all_creds.add(new Creds("admin", "RRJKZ"));
//        all_creds.add(new Creds("user", "Motobecane2"));

        setContentView(R.layout.activity_main);
        Fragmentmanager = getSupportFragmentManager();
        data_handler = new Thingspeak_data();
        setUpFragments();
        Load_settings();
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    private void switchFragments() {
        FragmentTransaction fragmentTransaction = Fragmentmanager.beginTransaction();
        recreateFragments();
        if (screen == 0) {
            fragmentTransaction.replace(R.id.frame_layout, Loginfragment);
        } else if (screen == 1) {
            fragmentTransaction.replace(R.id.frame_layout, Mapfragment);
        } else if (screen == 2) {
            fragmentTransaction.replace(R.id.frame_layout, Settingsfragment);
        } else if (screen == 3) {
            fragmentTransaction.replace(R.id.frame_layout, Bluetoothfragment);
        }
        fragmentTransaction.commit();
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    private void recreateFragments() {
        if (screen == 0) Loginfragment = (Login) recreateFragment(Loginfragment);
        if (screen == 1) Mapfragment = (MapFragment) recreateFragment(Mapfragment);
        if (screen == 2) Settingsfragment = (SettingsFragment) recreateFragment(Settingsfragment);
        if (screen == 3)
            Bluetoothfragment = (BluetoothFragment) recreateFragment(Bluetoothfragment);


    }

    private Fragment recreateFragment(Fragment f) {
        try {
            Fragment newInstance = f.getClass().newInstance();
            return newInstance;
        } catch (Exception e) {
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
        String saved_CH_ID = sharedPreferencessss.getString("CH_ID", null);
        String saved_API = sharedPreferencessss.getString("API", null);
        String saved_MAC = sharedPreferencessss.getString("MAC", null);

        if (saved_CH_ID != null && saved_API != null && saved_MAC != null) {
            /*for(int i=0;i<all_creds.size();i++){   // Micem zbog APi logina
                if(all_creds.get(i).isCorrect(saved_username, saved_password)){
                    screen=1;
                    Toast.makeText(getApplicationContext(), "Login successfull", Toast.LENGTH_SHORT).show();
                }
            }*/
            if (saved_CH_ID.length() > 5) {
                screen = 1;
                Toast.makeText(getApplicationContext(), "Login successfull", Toast.LENGTH_SHORT).show();
                data_handler.changeChannelID(saved_CH_ID);
                data_handler.changeAPI(saved_API);
                Bluetooth_uuid = saved_MAC;

            }


        }
        if (screen == 0) fragmentTransaction.add(R.id.frame_layout, Loginfragment);
        if (screen == 1) fragmentTransaction.add(R.id.frame_layout, Mapfragment);
        fragmentTransaction.commit();
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public void login(String CH_ID , String API, String MAC) {

        if (CH_ID.length()>5 && API.length() > 10) {
            screen = 1;
            switchFragments();
            data_handler.changeChannelID(CH_ID);
            data_handler.changeAPI(API);


            SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
            SharedPreferences.Editor editor = sharedPreferences.edit();

            editor.putString("CH_ID", CH_ID);
            editor.putString("API", API);
            editor.putString("MAC", MAC);
            Bluetooth_uuid = MAC;

            //AutofillManager afm = getApplicationContext().getSystemService(AutofillManager.class);



            editor.apply();


            return;
        }
                Toast.makeText(getApplicationContext(), "Neuspješan pokušaj logiranja",Toast.LENGTH_LONG);
                    /*SharedPreferences sharedPreferencessss = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
                    String saved_username = sharedPreferencessss.getString("username", null);
                    String saved_password = sharedPreferencessss.getString("password", null); // Micem zbog APi logina


                    screen=1; //bitno za nastavak
                    switchFragments(); //bitno za nastavak

                    return;*/

        // }
        //}

        //Toast.makeText(this, "Ne valja", Toast.LENGTH_SHORT).show();
        //}
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public void Refresh_from_program() {
        List<Location_data> loc_list = data_handler.download();

        if(loc_list.size()==0){
            Toast.makeText(getApplicationContext(),"Provjerite internetsku konekciju",Toast.LENGTH_SHORT);
        }
        List<LatLng> list = new ArrayList<>();
        for (int i = 0; i < loc_list.size(); i++)
            list.add(new LatLng(loc_list.get(i).getY(), loc_list.get(i).getX()));

        if(list.size()!=0) {
            try {
                Mapfragment.googleMappp.clear();
                Mapfragment.googleMappp.addMarker(new MarkerOptions().position(list.get(0)).title("Last recorded location").visible(true));
                Polyline polyline = Mapfragment.googleMappp.addPolyline(new PolylineOptions());
                polyline.setPoints(list);
                polyline.setVisible(true);
                Mapfragment.googleMappp.animateCamera(CameraUpdateFactory.newLatLngZoom(list.get(0), 15),400,null);

            } catch (Exception e) {
                Log.i("debuggg", "helllo");
                Toast.makeText(getApplicationContext(), "Nesto je poslo po krivu", Toast.LENGTH_SHORT);
            }
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public void refresh_map(android.view.View v) {
        Refresh_from_program();
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public void Save_settings(android.view.View v) {
        String new_history = Settingsfragment.getSelectedHistory();
        if (new_history != null) {
            data_handler.change_history_minutes(History_str_to_int(new_history));
            SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
            SharedPreferences.Editor editor = sharedPreferences.edit();

            editor.putString("history_settings", new_history);
            editor.apply();
        }
        screen = 1;
        switchFragments();
    }

    public void Load_settings() {
        SharedPreferences sharedPreferencessss = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());

        String saved_history = sharedPreferencessss.getString("history_settings", null);

        if (saved_history != null) {
            data_handler.change_history_minutes(History_str_to_int(saved_history));
        }

        String saved_MAC = sharedPreferencessss.getString("MAC", null);
        if(saved_MAC != null){
            Bluetooth_uuid = saved_MAC;
        }



    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public void Open_settings(android.view.View v) {
        screen = 2;
        switchFragments();

        SharedPreferences sharedPreferencessss = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        String saved_history = sharedPreferencessss.getString("history_settings", null);
        if (saved_history != null) Settingsfragment.saved_history = saved_history;

    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public void OpenBT(android.view.View v) {
        screen = 3;
        switchFragments();
        Bluetoothfragment.setStates(device_status_var, ignore_status_var);

    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public void logoutt() {
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        SharedPreferences.Editor editor = sharedPreferences.edit();

        editor.putString("CH_ID", "");
        editor.putString("API", "");
        editor.putString("MAC", "");

        editor.apply();
        screen = 0;
        switchFragments();
    }

    private OutputStream outputStream;
    private InputStream inStream;


    private long current_miliseconds() {
        Calendar rightNow = Calendar.getInstance();
        long offset = rightNow.get(Calendar.ZONE_OFFSET) + rightNow.get(Calendar.DST_OFFSET);
        long sinceMidnight = (rightNow.getTimeInMillis() + offset) % (24 * 60 * 60 * 1000);
        return sinceMidnight; //zasto since mindnight samo??????
    }

    public int History_str_to_int(String history) {
        switch (history) {
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
                return 430;
            case "90 days":
                return 129600;

        }
        return -1;
    }

    @Override
    public void ignore_mess(int code, int minutes) {

        if (code == 0 && minutes == 0) {//connect
            if (Bluetoothfragment.getDevice_status_var() >= 0) { //nebitno

                String reply = BTsend("T?");
                if (reply.contains("T=111")) {

                    device_status_var = 1;
                    Bluetoothfragment.set_device_status(1);
                    ignore_status_var = 0;
                    Bluetoothfragment.set_ignore_status(0);
                } else if (reply.contains("T=222")) {

                    device_status_var = 1;
                    ignore_status_var = 1;
                    Bluetoothfragment.set_ignore_status(1);
                } else {
                    device_status_var = 0;
                    Bluetoothfragment.set_device_status(0);

                    ignore_status_var = 0;
                    Bluetoothfragment.set_ignore_status(0);
                }
            }
        } else if (code == 0 && minutes > 0) {
            if (Bluetoothfragment.getDevice_status_var() == 1) {
                String mess = "T=222,";

                if (minutes < 10) mess += "00" + Integer.toString(minutes);
                else if (minutes >= 10 && minutes < 100) mess += "0" + Integer.toString(minutes);
                else if (minutes >= 100 && minutes < 1000) mess += Integer.toString(minutes);
                String reply = BTsend(mess);

                if (reply.contains("T=222")) {
                    device_status_var = 1;

                    ignore_status_var = 1;
                    Bluetoothfragment.set_ignore_status(1);
                } else {
                    device_status_var = 0;
                    Bluetoothfragment.set_device_status(0);

                    ignore_status_var = 0;
                }
            }
        } else if (code == 1) {
            String reply = BTsend("T=111");
            if (reply.contains("T=111")) {
                device_status_var = 1;
                Bluetoothfragment.set_device_status(1);
                ignore_status_var = 0;
                Bluetoothfragment.set_ignore_status(0);
            } else {
                device_status_var = 0;
                Bluetoothfragment.set_device_status(0);

                ignore_status_var = 0;
            }
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public void nazad_od_bta(View v) {
        screen = 1;
        switchFragments();
        device_status_var = Bluetoothfragment.getDevice_status_var();
        ignore_status_var = Bluetoothfragment.getIgnore_status_var();
    }

    public String BTsend(String send) {
        String bt_adresa;
        BluetoothAdapter bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        if (bluetoothAdapter == null) {
            return "X";
        }

        Intent enableBluetoothIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
        startActivityForResult(enableBluetoothIntent, 0);
        Set<BluetoothDevice> bondedDevices = bluetoothAdapter.getBondedDevices();

        Object[] devices = (Object[]) bondedDevices.toArray();
        for (int i = 0; i < bondedDevices.size(); i++) {
            BluetoothDevice device = (BluetoothDevice) devices[i];
            Log.i("bt", "device nameX: |" + device.getName() + "|");
            Log.i("bt", "device adressX: " + device.getAddress());
            ParcelUuid[] uuids = device.getUuids();
            if (device.getName().contentEquals(Bluetooth_name) && device.getAddress().contentEquals(Bluetooth_uuid)) {
                Log.i("bt", "naso");
                try {
                    BluetoothSocket socket = device.createRfcommSocketToServiceRecord(uuids[0].getUuid());
                    socket.connect();
                    outputStream = socket.getOutputStream();
                    inStream = socket.getInputStream();
                    outputStream.write(send.getBytes());
                    long time1 = current_miliseconds();
                    String reply = "";
                    while (current_miliseconds() - time1 < 3000) {  //PONOC fix this
                        while (inStream.available() > 0) reply += (char) inStream.read();
                        if(reply.contains("T=111")) break;
                        if(reply.contains("T=222,") && reply.length()==12) break;
                        if(reply.contains("Error")) break;
                    }
                    if(reply.length()==0) Toast.makeText(getApplicationContext(), "Bluetooth communication failed", Toast.LENGTH_LONG).show();

                    socket.close();
                    return reply;
                } catch (Exception e) {
                    Toast.makeText(getApplicationContext(), "Bluetooth communication failed", Toast.LENGTH_LONG).show();
                    Log.i("bt", "nope");
                    return "X";
                }
            } else {
                Log.i("bt", "nisam naso");
            }
        }
        return "E";
    }
}