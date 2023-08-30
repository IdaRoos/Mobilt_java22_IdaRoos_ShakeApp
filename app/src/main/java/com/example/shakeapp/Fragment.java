package com.example.shakeapp;

import android.os.Bundle;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;


public class Fragment extends androidx.fragment.app.Fragment {

   // private TextView sensorValueTextView;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_sensor_data, container, false);

        // Hitta referens till TextView i layouten
      //  sensorValueTextView = rootView.findViewById(R.id.textviewfragment);

        return rootView;
    }


}