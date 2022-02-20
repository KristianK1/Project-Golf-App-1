package com.example.zavrsni16052021;

import android.accessibilityservice.AccessibilityService;
import android.content.Context;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;

import android.service.autofill.UserData;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.autofill.AutofillManager;
import android.widget.Button;
import android.widget.EditText;

public class Login extends Fragment {
    EditText CH_ID_et;
    EditText WRITE_API_KEY_et; // Micem zbog APi logina
    EditText BT_adress_et; // Micem zbog APi logina

    Button login_bt;

    Login_interface ch_screen;

    AutofillManager afm;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_login, container, false);
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        login_bt = getView().findViewById(R.id.login_button);

        CH_ID_et = getView().findViewById(R.id.et_login_CH_ID);
        WRITE_API_KEY_et = getView().findViewById(R.id.et_login_API);
        BT_adress_et = getView().findViewById(R.id.et_login_BT_adr);


        afm= getContext().getSystemService(AutofillManager.class);
        afm.requestAutofill(CH_ID_et);
        afm.requestAutofill(WRITE_API_KEY_et);
        afm.requestAutofill(BT_adress_et);


        setUpListeners();
    }

    private void setUpListeners() {
        login_bt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(CH_ID_et.getText().toString().length()>5) {
                    ch_screen.login(CH_ID_et.getText().toString(), WRITE_API_KEY_et.getText().toString(), BT_adress_et.getText().toString());

                }
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