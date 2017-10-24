package com.pullupdownrefresh.demo;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }


    public void onClickListViewMore(View view) {
        startActivity(new Intent(this, ListViewMoreActivity.class));
    }

    public void onClickGridViewMore(View view) {
        startActivity(new Intent(this, GridViewMoreActivity.class));
    }

    public void onClickRecyclerViewMore(View view) {
        startActivity(new Intent(this, RecyclerViewMoreActivity.class));
    }

    public void onClickPageListViewMore(View view) {
        startActivity(new Intent(this, PageListView.class));
    }
}
