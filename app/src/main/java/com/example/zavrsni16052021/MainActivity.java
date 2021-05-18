package com.example.zavrsni16052021;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;


import android.os.Build;
import android.os.Bundle;
import android.util.Log;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class MainActivity extends AppCompatActivity implements ChangeScreen {
    private Login Loginfragment;
    private MapFragment Mapfragment;

    private FragmentManager Fragmentmanager;
    private int screen=0;

    private Thingspeak_data data_handler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Fragmentmanager = getSupportFragmentManager();
        data_handler= new Thingspeak_data();
        setUpFragments();
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

        }
        fragmentTransaction.commit();
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    private void recreateFragments() {
        Log.i("crash", "yor");
        if(screen==0) Loginfragment = (Login) recreateFragment(Loginfragment);
        Log.i("crash", "yoe");
        if(screen==1) {
            Mapfragment = (MapFragment) recreateFragment(Mapfragment);
            Refresh_from_program();
        }
        Log.i("crash", "yor");
    }


    private Fragment recreateFragment(Fragment f){
        try {
            Log.i("crash", "c1");
            //Fragment.SavedState savedState = Fragmentmanager.saveFragmentInstanceState(f);
            Log.i("crash", "c2");
            Fragment newInstance = f.getClass().newInstance();
            Log.i("crash", "c3");
            //newInstance.setInitialSavedState(savedState);
            Log.i("crash", "c4");
            return newInstance;
        }
        catch (Exception e) {
            throw new RuntimeException("Cannot reinstantiate fragment " + f.getClass().getName(), e);
            //return new Fragment();
        }
    }

    private void setUpFragments() {
        Loginfragment = new Login();
        Mapfragment = new MapFragment();
        FragmentTransaction fragmentTransaction = Fragmentmanager.beginTransaction();
        fragmentTransaction.add(R.id.frame_layout, Loginfragment);
        fragmentTransaction.commit();
    }

    @Override
    public void click(int Mscreen) {
        screen=Mscreen;
        Log.i("crash", "yo1");
        switchFragments();
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


}