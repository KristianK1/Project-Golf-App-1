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

public class oneButton extends Fragment {
    private Button btn;
    ignore_click Click;

    public oneButton() {

    }

    public static oneButton newInstance() {
        oneButton fragment = new oneButton();
        return fragment;
    }
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
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_one_button, container, false);
    }

    private void setUpListeners() {
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Click.ignore_mess(0,0);
                return;
            }
        });
    }
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        btn = (Button) getView().findViewById(R.id.one_button_bt);
        setUpListeners();
        setBtnText("Connect");

    }

    public void setBtnText(String text){
        if(btn!= null) btn.setText(text);
    }

}