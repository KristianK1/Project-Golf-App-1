package com.example.zavrsni16052021;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link twoButtons#newInstance} factory method to
 * create an instance of this fragment.
 */
public class twoButtons extends Fragment {

    public twoButtons() {
        // Required empty public constructor
    }
    public static twoButtons newInstance() {
        twoButtons fragment = new twoButtons();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_two_buttons, container, false);
    }


}