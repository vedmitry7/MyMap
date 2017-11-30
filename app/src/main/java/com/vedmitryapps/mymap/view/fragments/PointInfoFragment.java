package com.vedmitryapps.mymap.view.fragments;

import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import com.vedmitryapps.mymap.R;
import com.vedmitryapps.mymap.presenter.Presenter;

import butterknife.BindView;
import butterknife.ButterKnife;

public class PointInfoFragment extends Fragment {

    @BindView(R.id.location)
    TextView textView;

    @BindView(R.id.descriptionEt)
    EditText editText;

    Presenter presenter;

    public void setPresenter(Presenter presenter){
        this.presenter = presenter;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.point_info, container, false);

        ButterKnife.bind(this, view);
        double lat = getArguments().getDouble("lat");
        double lon = getArguments().getDouble("lon");
        final long id = getArguments().getLong("id");
        editText.setText(getArguments().getString("desc"));
        editText.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                Log.i("TAG21", "focus " + b);
                if(!b){
                }
            }
        });
        editText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                presenter.updateMarkerDescription(id, editable.toString());
            }
        });
        textView.setText(lat + "\r\n" + lon);
        return view;
    }
}
