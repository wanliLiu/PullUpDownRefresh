/*
Copyright 2015 chanven

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

   http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.
 */
package com.soli.pullupdownrefresh.more;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AbsListView;

import com.soli.pullupdownrefresh.view.GridViewWithHeaderAndFooter;

public class GridViewHandler implements LoadMoreHandler {

    private GridViewWithHeaderAndFooter mGridView;
    private View mFooter;

    @Override
    public void handleSetAdapter(View contentView, ILoadMoreViewFactory.ILoadMoreView loadMoreView, OnClickListener onClickLoadMoreListener) {
        mGridView = (GridViewWithHeaderAndFooter) contentView;
        if (loadMoreView != null) {
            final Context context = mGridView.getContext().getApplicationContext();
            loadMoreView.init(new ILoadMoreViewFactory.FootViewAdder() {

                @Override
                public View addFootView(int layoutId) {
                    View view = LayoutInflater.from(context).inflate(layoutId, mGridView, false);
                    mFooter = view;
                    return view;
//                    return addFootView(view);
                }

                @Override
                public View addFootView(View view) {
                    mGridView.addFooterView(view);
                    return view;
                }
            }, onClickLoadMoreListener);
        }
    }

    @Override
    public void setAbsListScollListener(View contentView, AbsListView.OnScrollListener listener) {
        GridViewWithHeaderAndFooter listView = (GridViewWithHeaderAndFooter) contentView;
        listView.setOnScrollListener(listener);
    }

    @Override
    public void setRecycleScollListener(View contentView, RecyclerView.OnScrollListener listener) {

    }

    @Override
    public void addFooter() {
        if (mGridView.getFooterViewsCount() <= 0 && mFooter != null) {
            mGridView.addFooterView(mFooter);
        }
    }

    @Override
    public void removeFooter() {
        if (mGridView.getFooterViewsCount() > 0 && mFooter != null) {
            mGridView.removeFooterView(mFooter);
        }
    }

}
