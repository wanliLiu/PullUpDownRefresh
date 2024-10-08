package com.soli.pullupdownrefresh.ptr;

import android.os.Build;
import android.view.View;
import android.widget.AbsListView;

import androidx.core.view.ViewCompat;

public abstract class PtrDefaultHandler implements PtrHandler {

    /**
     * @param view
     * @return
     */
    public static boolean canChildScrollUp(View view) {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.ICE_CREAM_SANDWICH) {
            if (view instanceof AbsListView) {
                final AbsListView absListView = (AbsListView) view;
                return absListView.getChildCount() > 0
                        && (absListView.getFirstVisiblePosition() > 0 || absListView.getChildAt(0).getTop() <
                        absListView.getPaddingTop());
            } else {
//              return view.getScrollY() > 0;
                return ViewCompat.canScrollVertically(view, -1) || view.getScrollY() > 0;
            }
        } else {

//            if (view instanceof CoordinatorLayout) {
//                int top = ((CoordinatorLayout) view).getChildAt(0).getTop();
//                if (top != 0) {
//                    return true;
//                }
//            }
            return view.canScrollVertically(-1);
        }
    }

    /**
     * Default implement for check can perform pull to refresh
     *
     * @param frame
     * @param content
     * @param header
     * @return
     */
    public static boolean checkContentCanBePulledDown(PtrFrameLayout frame, View content, View header) {
        return !canChildScrollUp(content);
    }

    @Override
    public boolean checkCanDoRefresh(PtrFrameLayout frame, View content, View header) {
        return checkContentCanBePulledDown(frame, content, header);
    }
}