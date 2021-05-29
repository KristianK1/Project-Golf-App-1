package com.example.zavrsni16052021;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.preference.PreferenceManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;

public class SettingsFragment extends Fragment {
    private Spinner history_select;
    private String[] history_modes = {"10 min", "20 min", "30 min", "1 hour", "2 hours", "3 hours", "6 hours", "12 hours", "1 day", "2 days", "5 days", "10 days", "30 days"};
    public String saved_history;
    private AdapterView history_spinner;
    ArrayAdapter<String> adapter_history;
//
    private TextView logout;
    private logout logout_interface;
    public SettingsFragment() {

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_settings, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        history_select=(Spinner) getView().findViewById(R.id.history_spinner);
        history_spinner=view.findViewById(R.id.history_spinner);

        adapter_history = new ArrayAdapter<String>(getContext(), android.R.layout.simple_spinner_item, history_modes);

        history_spinner.setAdapter(adapter_history);
        starting_history_string(saved_history);
        logout=(TextView) getView().findViewById(R.id.logout_tw);
        setUpListeners();
    }

    public void starting_history_string(String histry){
        history_select.setSelection(getIndex(history_select, histry));
    }
    private int getIndex(Spinner spinner, String myString){
        for (int i=0;i<spinner.getCount();i++){
            if (spinner.getItemAtPosition(i).toString().equalsIgnoreCase(myString)){
                return i;
            }
        }
        return 0;
    }
    private void setUpListeners() {
        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                logout_interface.logoutt();
            }
        });
    }
    public String getSelectedHistory(){
        return history_select.getSelectedItem().toString();
    }
    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof Login_interface) {
            this.logout_interface = (logout) context;
        }

    }
    @Override
    public void onDetach() {
        super.onDetach();
        this.logout_interface = null;
    }
}