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
        mAdapter.setManagerType(LoadMoreRecyclerAdapter.TYPE_MANAGER_GRID);
        final GridLayoutManager manager = new GridLayoutManager(this,2);
        manager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
            @Override
            public int getSpanSize(int position) {
                if (mAdapter.isHeader(position) || mAdapter.isFooter(position))
                    return manager.getSpanCount();
                return 1;
            }
        });
        mListView.setLayoutManager(manager);
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
