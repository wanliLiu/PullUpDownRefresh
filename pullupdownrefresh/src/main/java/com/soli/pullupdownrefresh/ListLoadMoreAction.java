package com.soli.pullupdownrefresh;

import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.View;
import android.widget.AbsListView;
import android.widget.ListView;

import com.soli.pullupdownrefresh.more.GridViewHandler;
import com.soli.pullupdownrefresh.more.ILoadMoreViewFactory;
import com.soli.pullupdownrefresh.more.ListViewHandler;
import com.soli.pullupdownrefresh.more.LoadMoreHandler;
import com.soli.pullupdownrefresh.more.LoadMoreRecyclerAdapter;
import com.soli.pullupdownrefresh.more.OnLoadMoreListener;
import com.soli.pullupdownrefresh.more.RecyclerViewHandler;
import com.soli.pullupdownrefresh.view.GridViewWithHeaderAndFooter;

/**
 * Created by soli on 22/10/2017.
 */

public class ListLoadMoreAction {

    private boolean isLoadingMore = false;
    //滑动到倒数第几个的时候就开始加载
    protected int ITEM_LEFT_TO_LOAD_MORE = 2;

    private int pageSize = 6;
    private int lastItemCount = 0;

    private ILoadMoreViewFactory loadMoreViewFactory;
    private ILoadMoreViewFactory.ILoadMoreView mLoadMoreView;
    private LoadMoreHandler mLoadMoreHandler;

    //能滚动的视图、比如Listivew GridView RecycleView
    private View mContentView;

    private OnLoadMoreListener mOnLoadMoreListener;

    /**
     * @param listView
     * @param mListener
     */
    public void attachToListFor(View listView, OnLoadMoreListener mListener) {

        if (listView == null) throw new IllegalArgumentException("需要添加的加载更多视图不能为空");

        mContentView = listView;

        mOnLoadMoreListener = mListener;

        loadMoreViewFactory = new DefaultLoadMoreViewFooter();
        mLoadMoreView = loadMoreViewFactory.getLoadMoreView();
        if (mContentView instanceof ListView) {
            mLoadMoreHandler = new ListViewHandler();
        } else if (mContentView instanceof GridViewWithHeaderAndFooter) {
            mLoadMoreHandler = new GridViewHandler();
        } else if (mContentView instanceof RecyclerView) {
            mLoadMoreHandler = new RecyclerViewHandler();
        }

        if (null == mLoadMoreHandler) {
            throw new IllegalStateException("unSupported contentView !");
        }

        mLoadMoreHandler.handleSetAdapter(mContentView, mLoadMoreView, new loadMoreClickListener());

        if (mContentView instanceof AbsListView) {
            mLoadMoreHandler.setAbsListScollListener(mContentView, new ablListScrollListener());
        } else {
            mLoadMoreHandler.setRecycleScollListener(mContentView, new recycleViewScroollListener());
        }

    }

    /**
     * RecycleView 的加载跟多逻辑处理
     */
    private class recycleViewScroollListener extends RecyclerView.OnScrollListener {

        private int[] lastScrollPositions;

        private static final int Layout_None = 0;
        private static final int Layout_Linear = 1;
        private static final int Layout_Grid = 2;
        private static final int Layout_Stragged = 3;
        private int layoutType = Layout_None;

        @Override
        public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
            super.onScrolled(recyclerView, dx, dy);
            processOnMore(recyclerView);
        }

        @Override
        public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
            super.onScrollStateChanged(recyclerView, newState);
        }

        /**
         *
         */
        private void processOnMore(RecyclerView mRecycler) {
            RecyclerView.LayoutManager layoutManager = mRecycler.getLayoutManager();
            int lastVisibleItemPosition = getLastVisibleItemPosition(layoutManager);
            int visibleItemCount = layoutManager.getChildCount();
            int totalItemCount = layoutManager.getItemCount();

            if (((totalItemCount - lastVisibleItemPosition) <= ITEM_LEFT_TO_LOAD_MORE ||
                    (totalItemCount - lastVisibleItemPosition) == 0 && totalItemCount > visibleItemCount)
                    && !isLoadingMore) {

                if (((LoadMoreRecyclerAdapter) mRecycler.getAdapter()).getItemCountHF() >= pageSize && lastItemCount != totalItemCount) {
                    lastItemCount = totalItemCount;
                    onloadMoreBegin(false);
                }
            }
        }

        /**
         * @param layoutManager
         * @return
         */
        private int getLastVisibleItemPosition(RecyclerView.LayoutManager layoutManager) {
            int lastVisibleItemPosition = -1;
            if (layoutManager instanceof LinearLayoutManager) {
                layoutType = Layout_Linear;
            } else if (layoutManager instanceof GridLayoutManager) {
                layoutType = Layout_Grid;
            } else if (layoutManager instanceof StaggeredGridLayoutManager) {
                layoutType = Layout_Stragged;
            } else {
                throw new RuntimeException("Unsupported LayoutManager used. Valid ones are LinearLayoutManager, GridLayoutManager and StaggeredGridLayoutManager");
            }

            switch (layoutType) {
                case Layout_Linear:
                    lastVisibleItemPosition = ((LinearLayoutManager) layoutManager).findLastVisibleItemPosition();
                    break;
                case Layout_Grid:
                    lastVisibleItemPosition = ((GridLayoutManager) layoutManager).findLastVisibleItemPosition();
                    break;
                case Layout_Stragged:
                    StaggeredGridLayoutManager staggeredGridLayoutManager = (StaggeredGridLayoutManager) layoutManager;
                    if (lastScrollPositions == null)
                        lastScrollPositions = new int[staggeredGridLayoutManager.getSpanCount()];

                    staggeredGridLayoutManager.findLastVisibleItemPositions(lastScrollPositions);
                    lastVisibleItemPosition = findMax(lastScrollPositions);
                    break;
            }
            return lastVisibleItemPosition;
        }

        /**
         * @param lastPositions
         * @return
         */
        private int findMax(int[] lastPositions) {
            int max = Integer.MIN_VALUE;
            for (int value : lastPositions) {
                if (value > max)
                    max = value;
            }
            return max;
        }
    }

    /**
     * ListView GridViewWithHeaderAndFooter 加载更多的逻辑处理
     */
    private class ablListScrollListener implements AbsListView.OnScrollListener {

        @Override
        public void onScrollStateChanged(AbsListView view, int scrollState) {
        }

        /**
         * @return
         */
        private int getHeaderViewsCount(AbsListView listView) {
            if (listView instanceof ListView) {
                return ((ListView) listView).getHeaderViewsCount();
            } else if (listView instanceof GridViewWithHeaderAndFooter) {
                return ((GridViewWithHeaderAndFooter) listView).getHeaderViewsCount();
            }
            return 0;
        }

        /**
         * @return
         */
        private int getFooterViewsCount(AbsListView listView) {
            if (listView instanceof ListView) {
                return ((ListView) listView).getFooterViewsCount();
            } else if (listView instanceof GridViewWithHeaderAndFooter) {
                return ((GridViewWithHeaderAndFooter) listView).getFooterViewsCount();
            }
            return 0;
        }

        @Override
        public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
            if (view.getAdapter() != null) {
                totalItemCount -= (getHeaderViewsCount(view) + getFooterViewsCount(view));
                if (totalItemCount > 0) {
                    int lastVisibleItem = firstVisibleItem + visibleItemCount;
                    if (((totalItemCount - lastVisibleItem) <= ITEM_LEFT_TO_LOAD_MORE ||
                            (totalItemCount - lastVisibleItem) == 0 && totalItemCount > visibleItemCount) && !isLoadingMore) {
                        if (view.getAdapter().getCount() >= pageSize && lastItemCount != totalItemCount) {
                            lastItemCount = totalItemCount;
                            onloadMoreBegin(false);
                        }
                    }
                }
            }
        }
    }

    /**
     *
     */
    public void resetLastItemCount() {
        lastItemCount = 0;
    }

    /**
     * 数据加载更多显示
     */
    private void onloadMoreBegin(boolean isFromClick) {
        mLoadMoreHandler.addFooter();
//        mLoadMoreView.setFooterVisibility(true);
        mLoadMoreView.showLoading();
        isLoadingMore = true;
        if (mOnLoadMoreListener != null) {
            mOnLoadMoreListener.loadMore(isFromClick);
        }
    }


    /**
     *
     */
    public void onloadMoreComplete() {
        mLoadMoreHandler.addFooter();
        mLoadMoreView.showNomore();
        isLoadingMore = false;
    }

    /**
     * @param pageSize
     */
    public void setPageSize(int pageSize) {
        this.pageSize = pageSize;
    }

    /**
     * 点击加载更多需要的进入的
     */
    private class loadMoreClickListener implements View.OnClickListener {
        @Override
        public void onClick(View v) {
            if (!isLoadingMore) {
                onloadMoreBegin(true);
            }
        }
    }
}
