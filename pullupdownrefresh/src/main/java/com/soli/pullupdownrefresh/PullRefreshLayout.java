package com.soli.pullupdownrefresh;

import static com.soli.pullupdownrefresh.ptr.util.PtrLocalDisplay.dp2px;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewConfiguration;

import androidx.core.view.ViewConfigurationCompat;

import com.soli.pullupdownrefresh.Header.MaterialHeader;
import com.soli.pullupdownrefresh.Header.RefreshEyeBlinkHeader;
import com.soli.pullupdownrefresh.ptr.PtrDefaultHandler;
import com.soli.pullupdownrefresh.ptr.PtrFrameLayout;


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

    private boolean isCanPullUp = false;
    private boolean preventHortinal = true;

    private boolean preventScroll = false;

    private View scrollView = null;

    private float startY;
    private float startX;
    // 记录viewPager是否拖拽的标记
    private boolean mIsHorizontalMove;
    // 记录事件是否已被分发
    private int mTouchSlop;

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
        setDurationToCloseHeader(800);

        //水平滑动
//        disableWhenHorizontalMove(true);


        final ViewConfiguration configuration = ViewConfiguration.get(getContext());
        mTouchSlop = ViewConfigurationCompat.getScaledPagingTouchSlop(configuration);
    }

    /**
     * @param preventScroll
     */
    public void setPreventScroll(boolean preventScroll) {
        this.preventScroll = preventScroll;
        if (!preventScroll)
            resetStatus();
    }

    /**
     * 屏蔽水平滑动的数据，相比作者的这个，更管用，因为是先截断的
     *
     * @param ev
     * @return
     */
    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {

        if (preventScroll) {
            return dispatchTouchEventSupper(ev);
        }

        if (!preventHortinal)
            super.dispatchTouchEvent(ev);

        int action = ev.getAction();
        switch (action) {
            case MotionEvent.ACTION_DOWN:
                // 记录手指按下的位置
                startY = ev.getY();
                startX = ev.getX();
                // 初始化标记
                mIsHorizontalMove = false;
                break;
            case MotionEvent.ACTION_MOVE:
                // 如果已经判断出是否由横向还是纵向处理，则跳出
                /**拦截禁止交给Ptr的 dispatchTouchEvent处理**/
                mIsHorizontalMove = true;
                // 获取当前手指位置
                float endY = ev.getY();
                float endX = ev.getX();
                float distanceX = Math.abs(endX - startX);
                float distanceY = Math.abs(endY - startY);
                if (distanceX != distanceY) {
                    // 如果X轴位移大于Y轴位移，那么将事件交给父视图处理。
                    if (distanceX > mTouchSlop && distanceX > distanceY) {
                        mIsHorizontalMove = true;
                    } else {
                        mIsHorizontalMove = false;
                    }
                }
                break;
            case MotionEvent.ACTION_UP:
            case MotionEvent.ACTION_CANCEL:
                //下拉刷新状态时如果滚动了viewpager 此时mIsHorizontalMove为true 会导致PtrFrameLayout无法恢复原位
                // 初始化标记,
                mIsHorizontalMove = false;
                break;
        }
        if (mIsHorizontalMove) {
            return dispatchTouchEventSupper(ev);
        }
        return super.dispatchTouchEvent(ev);
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
        if (isCanPullUp)
            getLoadMoreAction().setPageSize(pageSize);
    }

    /**
     * @param loadMore
     */
    public void setCanLoadMore(boolean loadMore) {
        if (isCanPullUp)
            getLoadMoreAction().setCanLoadMore(loadMore);
    }

    /**
     *
     */
    private void setNeedPullDownRefresh() {
        setEnabled(true);

        setPtrHandler(new PtrDefaultHandler() {
            @Override
            public boolean checkCanDoRefresh(PtrFrameLayout frame, View content, View header) {
                return super.checkCanDoRefresh(frame, scrollView != null ? scrollView : content, header);
            }

            @Override
            public void onRefreshBegin(PtrFrameLayout frame) {
                resetLastItemCount();
                if (mRefreshListener != null)
                    mRefreshListener.onPullDownRefresh();
            }
        });
    }

    /**
     *
     */
    public void resetLastItemCount() {
        if (isCanPullUp)
            getLoadMoreAction().resetLastItemCount();
    }

    /**
     * 需要自动加载更多
     */
    private void setNeedLoadMoreAction() {
        scrollView = getLoadMoreAction().attachToListFor(getContentView(), actionFromClick -> {
            if (mRefreshListener != null)
                mRefreshListener.onPullupRefresh(actionFromClick);
        });

        isCanPullUp = scrollView != null;
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
        if (isCanPullUp)
            getLoadMoreAction().onloadMoreComplete();
    }

    /**
     * 加载更多，列表 分页的时候，出错，点击再次加载
     */
    public void onLoadMoreErrorHappen() {
        onRefreshComplete();
        if (isCanPullUp)
            getLoadMoreAction().onloadErrorHappen();
    }

    /**
     * @param mRefreshListener
     */
    public void setRefreshListener(onRefrshListener mRefreshListener) {
        this.mRefreshListener = mRefreshListener;

    }

    /**
     * @param preventHortinal
     */
    public void setPreventHortinal(boolean preventHortinal) {
        this.preventHortinal = preventHortinal;
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
