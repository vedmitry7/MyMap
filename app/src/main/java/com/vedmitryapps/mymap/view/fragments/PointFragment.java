package com.vedmitryapps.mymap.view.fragments;

import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.vedmitryapps.mymap.R;

import butterknife.BindView;
import butterknife.ButterKnife;

public class PointFragment extends Fragment {

    @BindView(R.id.location)
    TextView textView;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.new_point, container, false);

        ButterKnife.bind(this, view);
        double lat = getArguments().getDouble("lat");
        double lon = getArguments().getDouble("lon");
        textView.setText(lat + "\r\n" + lon);
        return view;
    }
}
