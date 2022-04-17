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

public class fourButtons extends Fragment {
    private Button btn1, btn2, btn3, btn4;
    private ignore_click Click;
    private int state;

    public fourButtons(int stateee) {
        state = stateee;
        // Required empty public constructor
    }

    public fourButtons() {
        // Required empty public constructor
    }
    public static fourButtons newInstance(int statee) {
        fourButtons fragment = new fourButtons(statee);
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
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        return inflater.inflate(R.layout.fragment_four_buttons, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        btn1 = (Button) getView().findViewById(R.id.four_button1);
        btn2 = (Button) getView().findViewById(R.id.four_button2);
        btn3 = (Button) getView().findViewById(R.id.four_button3);
        btn4 = (Button) getView().findViewById(R.id.four_button4);

        if (state == 0) btn1.setText("30 sec");
        else btn1.setText("End");

        btn2.setText("2 min");
        btn3.setText("5 min");
        btn4.setText("20 min");

        setUpListeners();
    }

    public void setState(int state) {
        this.state = state;
        Log.i("BTstate", "STATE CHANGED " +  state);
        if(btn1 != null) {
            if (state == 0) btn1.setText("30 sec");
            else btn1.setText("End");
        }
    }

    private void setUpListeners() {
        btn1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //ona velika poruka
                Log.i("BTstate", "STANJE JE " + state);
                if(state == 0) Click.ignore_mess(0, -1);
                if(state == 1) Click.ignore_mess(1, 0);

            }
        });

        btn2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.i("BTstate", "STANJE JE " + state);
                Click.ignore_mess(0, 2);
            }
        });

        btn3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Click.ignore_mess(0, 5);

            }
        });

        btn4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Click.ignore_mess(0, 20);

            }
        });

    }


}