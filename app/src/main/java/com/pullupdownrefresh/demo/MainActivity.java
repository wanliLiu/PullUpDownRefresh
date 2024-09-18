package com.pullupdownrefresh.demo;

import android.content.Intent;
import android.os.Bundle;
import android.util.TypedValue;
import android.view.View;
import android.widget.LinearLayout;

import androidx.appcompat.app.AppCompatActivity;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.soli.pullupdownrefresh.Header.CircleImageView;
import com.soli.pullupdownrefresh.Header.CircularProgressDrawable;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        SwipeRefreshLayout layout = findViewById(R.id.refreshLayout);
        layout.setColorSchemeResources(R.color.colorAccent, R.color.colorPrimary, R.color.colorPrimaryDark);

        createProgressView();
    }

    private void createProgressView() {
        LinearLayout content = findViewById(R.id.content);
        int CIRCLE_BG_LIGHT = 0xFFFAFAFA;
        CircleImageView mCircleView = new CircleImageView(this, CIRCLE_BG_LIGHT);
        CircularProgressDrawable mProgress = new CircularProgressDrawable(this);
        mProgress.setStyle(CircularProgressDrawable.DEFAULT);
        mCircleView.setImageDrawable(mProgress);
        mCircleView.setVisibility(View.GONE);

        int _50dp = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 50, this.getResources().getDisplayMetrics());
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(_50dp, _50dp);
        content.addView(mCircleView, params);
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
