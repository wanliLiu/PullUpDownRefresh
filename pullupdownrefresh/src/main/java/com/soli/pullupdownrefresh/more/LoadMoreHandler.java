package com.soli.pullupdownrefresh.more;

import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AbsListView;

import androidx.recyclerview.widget.RecyclerView;

public interface LoadMoreHandler {

    /**
     * @param contentView
     * @param loadMoreView
     * @param onClickLoadMoreListener
     * @return 是否有 init ILoadMoreView
     */
    void handleSetAdapter(View contentView, ILoadMoreViewFactory.ILoadMoreView loadMoreView, OnClickListener onClickLoadMoreListener);

    /**
     * 设置ListView GridViewWithHeaderAndFooter
     *
     * @param contentView
     * @param listener
     */
    void setAbsListScollListener(View contentView, AbsListView.OnScrollListener listener);

    /**
     * @param contentView
     * @param listener
     */
    void setRecycleScollListener(View contentView, RecyclerView.OnScrollListener listener);

    void removeFooter();

    void addFooter();
}
