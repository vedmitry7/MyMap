package com.vedmitryapps.mymap.view.fragments;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import com.vedmitryapps.mymap.R;

import butterknife.BindView;
import butterknife.ButterKnife;

public class AddPointFragment extends Fragment {

    @BindView(R.id.point_description)
    EditText textView;
    @BindView(R.id.btnAddPoint)
    Button buttonAdd;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.add_point, container, false);

        ButterKnife.bind(this, view);
        return view;
    }

    public String getDescription(){
        return String.valueOf(textView.getText());
    }
}