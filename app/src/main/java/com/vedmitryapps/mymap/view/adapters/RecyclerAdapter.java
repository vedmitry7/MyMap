package com.vedmitryapps.mymap.view.adapters;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.vedmitryapps.mymap.R;
import com.vedmitryapps.mymap.model.MarkerImage;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class RecyclerAdapter extends RecyclerView.Adapter<RecyclerAdapter.ViewHolder> {

    List<MarkerImage> list;
    static int selectedPos = 0;

    public int getSelectedImageId(){
        return list.get(selectedPos).getId();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder  {
        @Nullable
        @BindView(R.id.imageView)
        ImageView imageView;

        public ViewHolder(View v) {
            super(v);
            ButterKnife.bind(this, v);
        }
    }

    public RecyclerAdapter(List<MarkerImage> list) {
       this.list = list;
    }

    @Override
    public RecyclerAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v;
        if (viewType == 1){
            v = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.recycler_item_add_button, parent, false);
        } else {
            v = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.recycler_item, parent, false);
        }
        ViewHolder vh = new ViewHolder(v);
        return vh;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {
        if (position>=list.size()){
            return;
        }

        if(selectedPos == position){
            holder.imageView.setBackgroundColor(Color.BLUE);
        } else {
            holder.imageView.setBackgroundColor(Color.TRANSPARENT);
        }

        holder.imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                selectedPos = position;
                notifyDataSetChanged();
            }
        });
        holder.imageView.setImageBitmap(getBitmap(list.get(position).getImage()));
    }

    @Override
    public int getItemCount() {
        return list.size() + 1;
    }

    @Override
    public int getItemViewType(int position) {
        if(position == list.size())
            return 1;
        else
        return super.getItemViewType(position);
    }

    private Bitmap getBitmap(byte[] array){
        return BitmapFactory.decodeByteArray(array, 0, array.length);
    }
}