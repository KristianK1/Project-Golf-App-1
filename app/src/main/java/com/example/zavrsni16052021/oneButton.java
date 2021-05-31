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
    int state;

    public oneButton(int mState) {
        state=mState;
    }
    public oneButton() {

    }

    public void setState(int state) {
        this.state = state;
    }

    public static oneButton newInstance() {
        oneButton fragment = new oneButton();
        return fragment;
    }
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof ignore_click) {
            this.Click = (ignore_click) context;
            Log.i("btt", "attached");
        }
    }
    @Override
    public void onDetach() {
        super.onDetach();
        Log.i("btt", "detached");
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
                if(state==-1) {
                    Click.ignore_mess(0,0);
                    Log.i("crashing", "prije77");

                    return;
                }
                else if(state==0) {
                    Log.i("open", "startppp");
                    Click.ignore_mess(0,15);  // ovisi
                    Log.i("crashing", "pooslije");

                    return;
                }
            }
        });
    }
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        btn = (Button) getView().findViewById(R.id.one_button_bt);
        setUpListeners();
        if(state==-1) setBtnText("Connect");
        if(state==0) setBtnText("Start ignoring");

        Log.i("btt", "pocetak prve tipke");
    }

    public void setBtnText(String text){
        if(btn!= null) btn.setText(text);
        else Log.i("metla", "heh");
    }

}