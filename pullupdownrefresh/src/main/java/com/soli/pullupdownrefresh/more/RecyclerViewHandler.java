package com.soli.pullupdownrefresh.more;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AbsListView;

public class RecyclerViewHandler implements LoadMoreHandler {

    private RecyclerView recyclerView;
    private View mFooter;

    /**
     * @return
     */
    private LoadMoreRecyclerAdapter getAdapter() {
        return (LoadMoreRecyclerAdapter) recyclerView.getAdapter();
    }

    @Override
    public void handleSetAdapter(View contentView, ILoadMoreViewFactory.ILoadMoreView loadMoreView, OnClickListener onClickLoadMoreListener) {
        recyclerView = (RecyclerView) contentView;
        if (loadMoreView != null) {
            final Context context = recyclerView.getContext().getApplicationContext();
            loadMoreView.init(new ILoadMoreViewFactory.FootViewAdder() {

                @Override
                public View addFootView(int layoutId) {
                    View view = LayoutInflater.from(context).inflate(layoutId,null);
                    mFooter = view;
//                    return addFootView(view);
                    return view;
                }

                @Override
                public View addFootView(View view) {
                    getAdapter().addFooter(view);
                    return view;
                }
            }, onClickLoadMoreListener);
        }
    }

    @Override
    public void setAbsListScollListener(View contentView, AbsListView.OnScrollListener listener) {

    }

    @Override
    public void setRecycleScollListener(View contentView, RecyclerView.OnScrollListener listener) {
        final RecyclerView recyclerView = (RecyclerView) contentView;
        recyclerView.addOnScrollListener(listener);
    }

    @Override
    public void addFooter() {
        if (getAdapter().getFootSize() <= 0 && mFooter != null) {
            getAdapter().addFooter(mFooter);
        }
    }

    @Override
    public void removeFooter() {
        if (getAdapter().getFootSize() > 0 && mFooter != null) {
            getAdapter().removeFooter(mFooter);
        }
    }
}
