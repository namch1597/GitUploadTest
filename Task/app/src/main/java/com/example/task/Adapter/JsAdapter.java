package com.example.task.Adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.task.R;

import java.util.ArrayList;

public class JsAdapter extends RecyclerView.Adapter<JsAdapter.ViewHolder> {

    private ArrayList<String> mList;

    public class ViewHolder extends RecyclerView.ViewHolder {
        protected TextView item;


        public ViewHolder(View view) {
            super(view);
            this.item = (TextView) view.findViewById(R.id.tv_item);
        }
    }


    public JsAdapter(ArrayList<String> list) {
        this.mList = list;
    }


    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {

        View view = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.item_recycler, viewGroup, false);

        ViewHolder viewHolder = new ViewHolder(view);

        return viewHolder;
    }


    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewholder, int position) {
        viewholder.item.setText(mList.get(position));
    }

    @Override
    public int getItemCount() {
        return (null != mList ? mList.size() : 0);
    }
}