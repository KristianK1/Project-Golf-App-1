package com.example.zavrsni16052021;

import android.graphics.Color;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link BluetoothFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class BluetoothFragment extends Fragment {
    private int device_status_var=0, ignore_status_var=0;
    private TextView dev_status;
    private TextView ignore_status;

    private FragmentManager manager;
    private oneButton oneBtn;
    private twoButtons twoBtns;
    private int which=1; //1 or 2


    public BluetoothFragment() {
        // Required empty public constructor
    }

    public static BluetoothFragment newInstance() {
        BluetoothFragment fragment = new BluetoothFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        manager=getChildFragmentManager();
        oneBtn = new oneButton();
        twoBtns = new twoButtons();
        FragmentTransaction ft=manager.beginTransaction();
        ft.add(R.id.bt_buttons_frame, oneBtn);
        ft.commit();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_bluetooth, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        dev_status=view.findViewById(R.id.device_status_tw);
        ignore_status=view.findViewById(R.id.ignore_tw);
    }

    private void set_device_status(int state){
        if(state==0) {
            dev_status.setText("Out of range");
            dev_status.setTextColor(0xFF0000);
            ignore_status.setText("IDK");
            ignore_status.setTextColor(0xFFFFFF);
        }
        else if(state==1){
            dev_status.setText("Connected");
            dev_status.setTextColor(0x00FF00);
        }
    }
    private int set_ignore_status(int state){
        if(state==0 && device_status_var==1) {
            ignore_status_var=state;
            ignore_status.setText("OFF");
            ignore_status.setTextColor(0x00FF00);
            return 1;
        }
        else if(state==1 && device_status_var==1){
            device_status_var=1;
            ignore_status.setText("ON");
            ignore_status.setTextColor(0xFF0000);
            return 1;
        }
        else if(device_status_var==0){
            ignore_status.setText("IDK");
            ignore_status.setTextColor(0xFFFFFF);
            return 0;
        }
        else{
            return 0;
        }
    }
}