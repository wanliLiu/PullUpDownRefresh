package com.pullupdownrefresh.demo;

import android.support.v7.app.AppCompatActivity;

import com.soli.pullupdownrefresh.PullRefreshLayout;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Soli
 * @Time 2017/10/24
 */
public abstract class BaseMoreActivity extends AppCompatActivity implements PullRefreshLayout.onRefrshListener {

    protected int page = 0;
    protected int pageSize = 40;
    protected int duration = 2000;

    protected List<String> mData = new ArrayList<String>();


    @Override
    public void onPullDownRefresh() {
        page = 0;
        mData.clear();
        addData();
    }

    @Override
    public void onPullupRefresh(boolean actionFromClick) {
        page++;
        addData();
    }

    /**
     *
     */
    protected void setData() {
        for (int i = 0; i < pageSize; i++) {
            mData.add(new String(i + "--" + page));
        }
    }

    protected abstract void addData();
}
