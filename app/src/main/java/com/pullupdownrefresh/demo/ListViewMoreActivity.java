package com.pullupdownrefresh.demo;

import android.os.Bundle;
import android.os.Handler;
import android.widget.ListView;

import com.soli.pullupdownrefresh.PullRefreshLayout;

/**
 * @author Soli
 * @Time 2017/10/24
 */
public class ListViewMoreActivity extends BaseMoreActivity {

    private PullRefreshLayout refreshLayout;
    private ListView mListView;
    private ListViewAdapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.listview_demo_layout);
        refreshLayout = this.findViewById(R.id.test_list_view_frame);
        mListView = this.findViewById(R.id.test_list_view);

        refreshLayout.setPageSize(pageSize);

        mAdapter = new ListViewAdapter(this);
        mListView.setAdapter(mAdapter);

        refreshLayout.setRefreshListener(this);

        new Handler().postDelayed(() -> refreshLayout.autoRefresh(), 400);
    }

    @Override
    protected void addData() {
        refreshLayout.postDelayed(() -> {
            setData();
            mAdapter.setData(mData);
            mAdapter.notifyDataSetChanged();
            refreshLayout.onRefreshComplete();
        }, 100);
    }

}
