package com.pullupdownrefresh.demo;

import android.os.Bundle;
import android.widget.ListView;

import com.soli.pullupdownrefresh.ListLoadMoreAction;

/**
 * @author Soli
 * @Time 2017/10/24
 */
public class PageListView extends BaseMoreActivity {

    private ListView mListView;
    private ListLoadMoreAction loadMoreAction;

    private ListViewAdapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.page_list_view_layout);
        mListView = this.findViewById(R.id.test_list_view);

        loadMoreAction = new ListLoadMoreAction();
        loadMoreAction.setPageSize(pageSize);

        loadMoreAction.attachToListFor(mListView, actionFromClick -> {
            page++;
            addData();
        });

        mAdapter = new ListViewAdapter(this);
        mListView.setAdapter(mAdapter);

        setData();
        mAdapter.setData(mData);
        mAdapter.notifyDataSetChanged();
    }

    @Override
    protected void addData() {
        mListView.postDelayed(() -> {
            setData();
            mAdapter.setData(mData);
            mAdapter.notifyDataSetChanged();
            loadMoreAction.onloadMoreComplete();
        }, duration);
    }

}
