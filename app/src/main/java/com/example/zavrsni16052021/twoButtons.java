package com.example.zavrsni16052021;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

public class twoButtons extends Fragment {
    private Button btnL, btnD;
    private ignore_click Click;
    public twoButtons() {
        // Required empty public constructor
    }
    public static twoButtons newInstance() {
        twoButtons fragment = new twoButtons();
        return fragment;
    }
    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof ignore_click) {
            this.Click = (ignore_click) context;
        }
    }
    @Override
    public void onDetach() {
        super.onDetach();
        this.Click = null;
    }
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.i("fragr", "stvoren 2 btns");
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        Log.i("fragr", "onCreateview 2 btns");
        return inflater.inflate(R.layout.fragment_two_buttons, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        Log.i("fragr", "onview created 2 btns prije btns");
        btnL = (Button) getView().findViewById(R.id.left_button);
        btnD = (Button) getView().findViewById(R.id.right_button);
        Log.i("fragr", "onview created 2 btns");
        setUpListeners();
    }

    private void setUpListeners() {
        btnL.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //ona velika poruka
                Log.i("btt", "left button");
                Click.ignore_mess(0, 15);
            }
        });
        btnD.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.i("btt", "right button");
                Click.ignore_mess(1, 0);

            }
        });
    }


}