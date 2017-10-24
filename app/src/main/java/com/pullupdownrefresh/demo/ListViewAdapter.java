package com.pullupdownrefresh.demo;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Soli
 * @Time 2017/10/24
 */
public class ListViewAdapter extends BaseAdapter {
    private LayoutInflater inflater;
    private List<String> data = new ArrayList<>();

    public ListViewAdapter(Context context) {
        super();
        inflater = LayoutInflater.from(context);
    }

    public void setData(List<String> data) {
        this.data = data;
    }

    @Override
    public int getCount() {
        return data.size();
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = inflater.inflate(R.layout.listitem_layout, parent, false);
        }
        TextView textView = (TextView) convertView;
        textView.setText(data.get(position));
        return convertView;
    }
}
