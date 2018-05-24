package com.soli.pullupdownrefresh.more;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AbsListView.OnScrollListener;
import android.widget.ListView;

/**
 *
 */
public class ListViewHandler implements LoadMoreHandler {

    private ListView mListView;
    private View mFooter;

    @Override
    public void handleSetAdapter(View contentView, ILoadMoreViewFactory.ILoadMoreView loadMoreView, OnClickListener onClickLoadMoreListener) {
        final ListView listView = (ListView) contentView;
        mListView = listView;
        if (loadMoreView != null) {
            final Context context = listView.getContext().getApplicationContext();
            loadMoreView.init(new ILoadMoreViewFactory.FootViewAdder() {

                @Override
                public View addFootView(int layoutId) {
                    View view = LayoutInflater.from(context).inflate(layoutId, listView, false);
                    mFooter = view;
                    return view;
//                    return addFootView(view);
                }

                @Override
                public View addFootView(View view) {
                    listView.addFooterView(view);
                    return view;
                }
            }, onClickLoadMoreListener);
        }
    }

    @Override
    public void setAbsListScollListener(View contentView, OnScrollListener listener) {
        ListView listView = (ListView) contentView;
        listView.setOnScrollListener(listener);
    }

    @Override
    public void setRecycleScollListener(View listView, RecyclerView.OnScrollListener listener) {

    }

    @Override
    public void removeFooter() {
        if (mListView.getFooterViewsCount() > 0 && mFooter != null) {
            mListView.removeFooterView(mFooter);
        }
    }

    @Override
    public void addFooter() {
        if (mListView.getFooterViewsCount() <= 0 && mFooter != null) {
            mListView.addFooterView(mFooter);
        }
    }

}
