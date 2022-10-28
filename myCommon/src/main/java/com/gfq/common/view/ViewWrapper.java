package com.gfq.common.view;

import android.view.View;
import android.view.ViewGroup;

/**
 * @author gaofuq
 * 方便给View做宽高改变动画的辅助类
 * ObjectAnimator.ofInt(viewWrapper,"height",view.height,0).start()
 */
public class ViewWrapper {

    private View view;

    public ViewWrapper(View target) {
        view = target;
    }

    public int getWidth() {
        return view.getLayoutParams().width;
    }

    public void setWidth(int width) {
        view.getLayoutParams().width = width;
        view.requestLayout();
    }

    public int getHeight() {
        return view.getLayoutParams().height;
    }

    public void setHeight(int height) {
        view.getLayoutParams().height = height;
        view.requestLayout();
    }


    public int getLeftMargin() {
        ViewGroup.MarginLayoutParams params = (ViewGroup.MarginLayoutParams) view.getLayoutParams();
        return params.leftMargin;
    }

    public void setLeftMargin(int leftMargin) {
        ViewGroup.MarginLayoutParams params = (ViewGroup.MarginLayoutParams) view.getLayoutParams();
        params.leftMargin = leftMargin;
        view.requestLayout();
    }

    public int getTopMargin() {
        ViewGroup.MarginLayoutParams params = (ViewGroup.MarginLayoutParams) view.getLayoutParams();
        return params.topMargin;
    }

    public void setTopMargin(int topMargin) {
        ViewGroup.MarginLayoutParams params = (ViewGroup.MarginLayoutParams) view.getLayoutParams();
        params.topMargin = topMargin;
        view.requestLayout();
    }

    public int getRightMargin() {
        ViewGroup.MarginLayoutParams params = (ViewGroup.MarginLayoutParams) view.getLayoutParams();
        return params.rightMargin;
    }

    public void setRightMargin(int rightMargin) {
        ViewGroup.MarginLayoutParams params = (ViewGroup.MarginLayoutParams) view.getLayoutParams();
        params.rightMargin = rightMargin;
        view.requestLayout();
    }

    public int getBottomMargin() {
        ViewGroup.MarginLayoutParams params = (ViewGroup.MarginLayoutParams) view.getLayoutParams();
        return params.bottomMargin;
    }

    public void setBottomMargin(int bottomMargin) {
        ViewGroup.MarginLayoutParams params = (ViewGroup.MarginLayoutParams) view.getLayoutParams();
        params.bottomMargin = bottomMargin;
        view.requestLayout();
    }

}