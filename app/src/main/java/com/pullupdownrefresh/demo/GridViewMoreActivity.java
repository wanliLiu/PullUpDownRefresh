package com.pullupdownrefresh.demo;

import android.os.Bundle;
import android.os.Handler;

import com.soli.pullupdownrefresh.PullRefreshLayout;
import com.soli.pullupdownrefresh.view.GridViewWithHeaderAndFooter;

/**
 * @author Soli
 * @Time 2017/10/24
 */
public class GridViewMoreActivity extends BaseMoreActivity {

    private PullRefreshLayout refreshLayout;
    private GridViewWithHeaderAndFooter mListView;
    private ListViewAdapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.gridview_demo_layout);
        refreshLayout = this.findViewById(R.id.test_list_view_frame);
        mListView = this.findViewById(R.id.test_grid_view);


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
