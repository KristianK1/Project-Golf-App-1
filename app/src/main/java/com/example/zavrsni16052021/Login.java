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

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link Login#newInstance} factory method to
 * create an instance of this fragment.
 */
public class Login extends Fragment {
    ChangeScreen change;
    Button login_bt;
    ChangeScreen ch_screen;

   /* @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }*/

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        return inflater.inflate(R.layout.fragment_login, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        login_bt= getView().findViewById(R.id.login_button);
        setUpListeners();
    }

    private void setUpListeners() {
        login_bt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                ch_screen.click(1);
            }
        });
    }
    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof ChangeScreen) {
            this.ch_screen = (ChangeScreen) context;
        }
    }
    @Override
    public void onDetach() {
        super.onDetach();
        this.ch_screen = null;
    }

}