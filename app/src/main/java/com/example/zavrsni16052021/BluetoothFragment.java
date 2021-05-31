package com.example.zavrsni16052021;

import android.graphics.Color;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

public class BluetoothFragment extends Fragment {
    private int device_status_var=0, ignore_status_var=0;
    private TextView dev_status;
    private TextView ignore_status;
    private int which_buttons=1; //1 or 2;
    private FragmentManager manager;
    private oneButton oneBtn;
    private twoButtons twoBtns;

    public BluetoothFragment() {
        // Required empty public constructor
    }
    public void setStates(int D, int I){
        device_status_var=D;
        ignore_status_var=I;
    };
    public static BluetoothFragment newInstance() {
        BluetoothFragment fragment = new BluetoothFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        manager=getChildFragmentManager();
        if(device_status_var==0) oneBtn = new oneButton(-1);
        else oneBtn = new oneButton(0);

        twoBtns = new twoButtons();
        FragmentTransaction ft=manager.beginTransaction();
        if(ignore_status_var==0){
            ft.add(R.id.bt_buttons_frame, oneBtn);
            which_buttons=1;
        }
        else{
            ft.add(R.id.bt_buttons_frame, twoBtns);
            which_buttons=2;
        }
        ft.commit();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_bluetooth, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        dev_status=(TextView) view.findViewById(R.id.device_status_tw);
        ignore_status=(TextView) view.findViewById(R.id.ignore_tw);

        set_device_status(device_status_var);//ima smisla
        set_ignore_status(ignore_status_var);
    }

    private void switchFragments() {
        try {
            Log.i("fragr", "minus nula");

            FragmentTransaction fragmentTransaction =manager.beginTransaction();

            Log.i("fragr", "nula");
            if (device_status_var == 0 && which_buttons!=1) {
                Log.i("fragr", "jedan1");

                recreateFragments();
                fragmentTransaction.replace(R.id.bt_buttons_frame, oneBtn);
                Log.i("fragr", "dva1");
                which_buttons=1;
            }
            if (device_status_var == 1 && ignore_status_var == 0 && which_buttons!=1) {
                Log.i("fragr", "jedan2");

                recreateFragments();
                fragmentTransaction.replace(R.id.bt_buttons_frame, oneBtn);
                Log.i("fragr", "dva2");
                which_buttons=1;

            } else if (device_status_var == 1 && ignore_status_var == 1 && which_buttons!=2) {
                Log.i("fragr", "jedan3");

                recreateFragments();
                fragmentTransaction.replace(R.id.bt_buttons_frame, twoBtns);
                which_buttons=2;
                Log.i("fragr", "dva3");

            }
            Log.i("fragr", "tri");

            fragmentTransaction.commit();
            Log.i("fragr", "cetiri");
        }
        catch(Exception e){}
    }

    private void recreateFragments() {
        Log.i("fragr","recreate frag btns");
        if(device_status_var==0){
            oneBtn = (oneButton) recreateFragment(oneBtn);
        }
        if(device_status_var==1 && ignore_status_var==0){
            oneBtn = (oneButton) recreateFragment(oneBtn);
        }
        if(device_status_var==1 && ignore_status_var==1) {

            Log.i("fragr","recreate 2 btns");
            try {
                twoBtns = (twoButtons) recreateFragment(twoBtns);
            }
            catch (Exception e){ Log.i("fragr","failed but ok"); }
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

    public int getDevice_status_var() {
        return device_status_var;
    }
    public int getIgnore_status_var() {
        return ignore_status_var;
    }
    public void set_device_status(int state){
        device_status_var=state;
        if(state==0) {
            ignore_status_var=0;
            dev_status.setText("Out of range");
            dev_status.setTextColor(Color.parseColor("#ff0000"));
            ignore_status.setText("IDK");
            ignore_status.setTextColor(Color.parseColor("#000000"));
            switchFragments();

            oneBtn.setState(-1);
            oneBtn.setBtnText("Connect");
        }
        else if(state==1 && ignore_status_var==0){
            dev_status.setText("Connected");
            dev_status.setTextColor(Color.parseColor("#00ff00"));
            switchFragments();

            oneBtn.setState(0);
            oneBtn.setBtnText("Start ignoring");
        }
        Log.i("open", "boga2");

    }
    public int set_ignore_status(int state){
        if(state==0 && device_status_var==1) {
            ignore_status_var=state;

            dev_status.setText("Connected");
            dev_status.setTextColor(Color.parseColor("#00ff00"));
            ignore_status.setText("OFF");
            ignore_status.setTextColor(Color.parseColor("#00ff00"));
            switchFragments();
            return 1;
        }
        else if(state==1){
            ignore_status_var=state;
            device_status_var=1;
            dev_status.setText("Connected");
            dev_status.setTextColor(Color.parseColor("#00ff00"));
            ignore_status.setText("ON");
            ignore_status.setTextColor(Color.parseColor("#ff0000"));
            Log.i("crashing", "prijett");
            switchFragments();
            Log.i("crashing", "prije2tt");
            return 1;
        }
        else{
            return 0;
        }
    }


}