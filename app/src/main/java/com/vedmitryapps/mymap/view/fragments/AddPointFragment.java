package com.vedmitryapps.mymap.view.fragments;


import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import com.vedmitryapps.mymap.R;
import com.vedmitryapps.mymap.model.MarkerImage;
import com.vedmitryapps.mymap.view.adapters.RecyclerAdapter;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.realm.Realm;
import io.realm.RealmResults;

public class AddPointFragment extends Fragment {

    @BindView(R.id.point_description)
    EditText textView;
    @BindView(R.id.btnAddPoint)
    Button buttonAdd;

    @BindView(R.id.recyclerView)
    RecyclerView mRecyclerView;

    private RecyclerAdapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    private Realm mRealm;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.add_point, container, false);
        ButterKnife.bind(this, view);

        mRealm = Realm.getDefaultInstance();

        RealmResults<MarkerImage> points = mRealm.where(MarkerImage.class).findAll();

        mRecyclerView.setHasFixedSize(true);

        mLayoutManager = new LinearLayoutManager(getActivity().getApplicationContext(), LinearLayoutManager.HORIZONTAL, false);
        mRecyclerView.setLayoutManager(mLayoutManager);
        mAdapter = new RecyclerAdapter(points);
        mRecyclerView.setAdapter(mAdapter);

        return view;
    }

    public String getDescription(){
        return String.valueOf(textView.getText());
    }

    public int getSelectedImageId(){
        return mAdapter.getSelectedImageId();
    }
}