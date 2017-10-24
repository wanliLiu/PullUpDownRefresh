package com.pullupdownrefresh.demo;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

/**
 * TODO: 2017/10/24 此处需要输入描述文字
 *
 * @author Soli
 * @Time 2017/10/24
 */
public class RecyclerAdapter extends RecyclerView.Adapter {

    private List<String> mData = new ArrayList<>();

    private LayoutInflater inflater;

    public RecyclerAdapter(Context context) {
        super();
        inflater = LayoutInflater.from(context);
    }

    public void setData(List<String> data) {
        this.mData = data;
    }

    @Override
    public int getItemCount() {
        return mData.size();
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder viewHolder, int position) {
        ChildViewHolder holder = (ChildViewHolder) viewHolder;
        holder.itemTv.setText(mData.get(position));
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup viewHolder, int position) {
        View view = inflater.inflate(R.layout.listitem_layout, viewHolder, false);
        return new ChildViewHolder(view);
    }


    public class ChildViewHolder extends RecyclerView.ViewHolder {
        public TextView itemTv;

        public ChildViewHolder(View view) {
            super(view);
            itemTv = (TextView) view;
        }

    }
}


