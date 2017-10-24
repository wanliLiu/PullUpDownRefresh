package com.soli.pullupdownrefresh.Header;

import android.content.Context;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.widget.FrameLayout;

import com.airbnb.lottie.LottieAnimationView;
import com.soli.pullupdownrefresh.ptr.PtrFrameLayout;
import com.soli.pullupdownrefresh.ptr.PtrUIHandler;
import com.soli.pullupdownrefresh.ptr.indicator.PtrIndicator;


/**
 * @author Soli
 * @Time 2017/10/18
 */
public class RefreshEyeBlinkHeader extends FrameLayout implements PtrUIHandler {

    private LottieAnimationView animationView;

    public RefreshEyeBlinkHeader(Context context) {
        super(context);
        init(context);
    }

    public RefreshEyeBlinkHeader(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public RefreshEyeBlinkHeader(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    /**
     * @param dipValue
     * @param ctx
     * @return
     */
    private int dip2px(float dipValue, Context ctx) {
        return (int) (dipValue * ctx.getResources().getDisplayMetrics().density + 0.5f);
    }

    /**
     * @param ctx
     */
    private void init(Context ctx) {

//        setBackgroundColor(Color.WHITE);

        animationView = new LottieAnimationView(ctx);
        LayoutParams params = new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
        params.gravity = Gravity.CENTER;
        params.topMargin = params.bottomMargin = dip2px(10, ctx);
        animationView.setAnimation("refresh_eye_blink.json", LottieAnimationView.CacheStrategy.Strong);
        animationView.setScale(0.55f);
        addView(animationView, params);
    }


    @Override
    public void onUIReset(PtrFrameLayout frame) {
        animationView.cancelAnimation();
        animationView.loop(false);
        animationView.setVisibility(VISIBLE);
    }

    @Override
    public void onUIRefreshPrepare(PtrFrameLayout frame) {
        animationView.setProgress(0.0f);
    }

    @Override
    public void onUIRefreshBegin(PtrFrameLayout frame) {

        if (!animationView.isAnimating()) {
            animationView.loop(true);
            animationView.setMinAndMaxProgress(0.29986f, 1f);
            animationView.playAnimation();
        }
    }

    @Override
    public void onUIRefreshComplete(PtrFrameLayout frame) {
    }

    /**
     * 动画完成 才进行刷新完成
     */
    public void onRefreshComplete(final PtrFrameLayout ptrLayout) {
        if (ptrLayout.isRefreshing()) {
            AlphaAnimation animation = new AlphaAnimation(1.0f, 0.0f);
            animation.setDuration(400);
            animation.setAnimationListener(new Animation.AnimationListener() {
                @Override
                public void onAnimationStart(Animation animation) {

                }

                @Override
                public void onAnimationEnd(Animation animation) {
                    animationView.cancelAnimation();
                    animationView.loop(false);
                    animationView.setVisibility(INVISIBLE);
                    ptrLayout.performRefreshComplete_eyeblink();
                }

                @Override
                public void onAnimationRepeat(Animation animation) {

                }
            });
            animationView.setAnimation(animation);
            animation.start();
        }
    }

    /**
     * Map a value within a given range to another range.
     *
     * @param value    the value to map
     * @param fromLow  the low end of the range the value is within
     * @param fromHigh the high end of the range the value is within
     * @param toLow    the low end of the range to map to
     * @param toHigh   the high end of the range to map to
     * @return the mapped value
     */
    private double mapValueFromRangeToRange(
            double value,
            double fromLow,
            double fromHigh,
            double toLow,
            double toHigh) {
        double fromRangeSize = fromHigh - fromLow;
        double toRangeSize = toHigh - toLow;
        double valueScale = (value - fromLow) / fromRangeSize;
        return toLow + (valueScale * toRangeSize);
    }

    @Override
    public void onUIPositionChange(PtrFrameLayout frame, boolean isUnderTouch, byte status, PtrIndicator ptrIndicator) {
        final int mOffsetToRefresh = frame.getOffsetToRefresh();
        final int currentPos = ptrIndicator.getCurrentPosY();
        if (currentPos >= (mOffsetToRefresh / 2) && currentPos < mOffsetToRefresh) {
            double progress = (currentPos - mOffsetToRefresh / 2) * 1.0d / (mOffsetToRefresh / 2) * 1.0d;
            animationView.setProgress((float) mapValueFromRangeToRange(progress, 0.0d, 1.0d, 0.0d, 0.29986d));
        } else if (currentPos >= mOffsetToRefresh) {
            if (!animationView.isAnimating()) {
                animationView.loop(true);
                animationView.setMinAndMaxProgress(0.29986f, 1f);
                animationView.playAnimation();
            }
        }
    }

    /**
     * 动画完成后
     */
    public interface onAnimiationEndListener {
        void onAnimationEnd();
    }
}
