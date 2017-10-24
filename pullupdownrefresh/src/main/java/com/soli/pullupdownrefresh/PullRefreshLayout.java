package com.soli.pullupdownrefresh;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;

import com.soli.pullupdownrefresh.Header.MaterialHeader;
import com.soli.pullupdownrefresh.Header.RefreshEyeBlinkHeader;
import com.soli.pullupdownrefresh.ptr.PtrDefaultHandler;
import com.soli.pullupdownrefresh.ptr.PtrFrameLayout;

import static com.soli.pullupdownrefresh.ptr.util.PtrLocalDisplay.dp2px;


/**
 * 下拉刷新统一的视图
 *
 * @author Soli
 * @Time 2017/10/23
 */
public class PullRefreshLayout extends PtrFrameLayout {

    private RefreshEyeBlinkHeader mEyeHeader;
    private MaterialHeader materialHeader;
    private ListLoadMoreAction moreAction;

    private boolean isUseMaterialHeader = false;

    /**
     * 上拉监听器, 到了最底部的上拉加载操作
     */
    private onRefrshListener mRefreshListener;

    public PullRefreshLayout(Context context) {
        super(context);
        initViews();
    }

    public PullRefreshLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        initViews();
    }

    public PullRefreshLayout(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        initViews();
    }

    /**
     *
     */
    private void initViews() {
        if (isUseMaterialHeader) {
            materialHeader = new MaterialHeader(getContext());
            int[] colors = getResources().getIntArray(R.array.google_colors);
            materialHeader.setColorSchemeColors(colors);
            materialHeader.setLayoutParams(new PtrFrameLayout.LayoutParams(-1, -2));
            materialHeader.setPadding(0, dp2px(15), 0, dp2px(10));
            materialHeader.setPtrFrameLayout(this);
            setHeaderView(materialHeader);
            addPtrUIHandler(materialHeader);
            setPinContent(true);
        } else {
            mEyeHeader = new RefreshEyeBlinkHeader(getContext());
            setHeaderView(mEyeHeader);
            addPtrUIHandler(mEyeHeader);
        }

        //保证动画能执行的最小时间，从开始加载到完成，增强用户体验，不会一闪消失，这种情况一般是数据加载很快
        setLoadingMinTime(1000);
        setDurationToCloseHeader(500);
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        setNeedPullDownRefresh();
        setNeedLoadMoreAction();
    }

    /**
     * @return
     */
    private ListLoadMoreAction getLoadMoreAction() {
        if (moreAction == null)
            moreAction = new ListLoadMoreAction();

        return moreAction;
    }

    /**
     * @param pageSize
     */
    public void setPageSize(int pageSize) {
        getLoadMoreAction().setPageSize(pageSize);
    }

    /**
     *
     */
    private void setNeedPullDownRefresh() {
        setEnabled(true);

        setPtrHandler(new PtrDefaultHandler() {
            @Override
            public void onRefreshBegin(PtrFrameLayout frame) {
                getLoadMoreAction().resetLastItemCount();
                if (mRefreshListener != null)
                    mRefreshListener.onPullDownRefresh();
            }
        });
    }

    /**
     * 需要自动加载更多
     */
    private void setNeedLoadMoreAction() {
        View content = getContentView();
        if (content != null) {
            getLoadMoreAction().attachToListFor(content, actionFromClick -> {
                if (mRefreshListener != null)
                    mRefreshListener.onPullupRefresh(actionFromClick);
            });
        }
    }

    @Override
    protected void performRefreshComplete() {
        if (isUseMaterialHeader) {
            super.performRefreshComplete();
        } else {
            if (mEyeHeader != null)
                mEyeHeader.onRefreshComplete(this);
        }
    }

    /**
     * 刷新完成 这里有动画效果
     */
    public void onRefreshComplete() {
        refreshComplete();
        getLoadMoreAction().onloadMoreComplete();
    }

    /**
     * @param mRefreshListener
     */
    public void setRefreshListener(onRefrshListener mRefreshListener) {
        this.mRefreshListener = mRefreshListener;

    }

    /**
     * 加载更多的监听器
     *
     * @author mrsimple
     */
    public interface onRefrshListener {
        /**
         * 下拉刷新
         */
        void onPullDownRefresh();

        /**
         * 上拉自动加载
         */
        void onPullupRefresh(boolean actionFromClick);
    }
}
