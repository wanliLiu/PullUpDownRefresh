package com.pullupdownrefresh.demo;

import android.os.Bundle;
import android.os.Handler;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.soli.pullupdownrefresh.PullRefreshLayout;
import com.soli.pullupdownrefresh.more.LoadMoreRecyclerAdapter;

/**
 * @author Soli
 * @Time 2017/10/24
 */
public class RecyclerViewMoreActivity extends BaseMoreActivity {

    private PullRefreshLayout refreshLayout;
    private RecyclerView mListView;
    private LoadMoreRecyclerAdapter mAdapter;
    private RecyclerAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.recycleview_demo_layout);
        refreshLayout = this.findViewById(R.id.test_list_view_frame);
        mListView = this.findViewById(R.id.test_recycler_view);

        refreshLayout.setPageSize(pageSize);

        adapter = new RecyclerAdapter(this);
        mAdapter = new LoadMoreRecyclerAdapter(adapter);
        mListView.setLayoutManager(new GridLayoutManager(this, 2));
        mListView.setAdapter(mAdapter);

        refreshLayout.setRefreshListener(this);

        new Handler().postDelayed(() -> refreshLayout.autoRefresh(), 400);
    }

    @Override
    protected void addData() {
        refreshLayout.postDelayed(() -> {
            setData();
            adapter.setData(mData);
            mAdapter.notifyDataSetChanged();
            refreshLayout.onRefreshComplete();
        }, 100);
    }
}
