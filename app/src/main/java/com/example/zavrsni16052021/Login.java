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
import android.widget.EditText;

public class Login extends Fragment {
    EditText username_et;
//    EditText password_et; // Micem zbog APi logina
    Button login_bt;

    Login_interface ch_screen;

   @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_login, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        login_bt = getView().findViewById(R.id.login_button);
        username_et = getView().findViewById(R.id.et_login_username);
//        password_et = getView().findViewById(R.id.et_login_pass);
        setUpListeners();
    }

    private void setUpListeners() {
        login_bt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(username_et.getText().toString().length()>10)
                    ch_screen.login(username_et.getText().toString()/*, password_et.getText().toString()*/);
            }
        });
    }
    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof Login_interface) {
            this.ch_screen = (Login_interface) context;
        }
    }
    @Override
    public void onDetach() {
        super.onDetach();
        this.ch_screen = null;
    }

}