package com.vedmitryapps.mymap.view.fragments;

import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.vedmitryapps.mymap.R;

public class AddPointFragment extends Fragment {

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.new_point, container, false);

        TextView textView = view.findViewById(R.id.location);
        double lat = getArguments().getDouble("lat");
        double lon = getArguments().getDouble("lon");
        textView.setText(lat + "\r\n" + lon);
        return view;
    }
}
