package com.ll.lib_common_ui.controller;

import android.animation.Animator;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.view.View;

import java.util.List;

public abstract class BaseViewAnimatorController {
    protected View mView;//View
    protected List<Animator> mAnimators;//动画集合

    /**
     * 动画状态
     */
    public enum AnimatorStatus {
        START,
        END,
        CANCEL
    }

    public View getView() {
        return mView;
    }

    protected void postInvalidate() {
        if (mView == null) {
            return;
        }
        mView.postInvalidate();
    }

    public void setView(View mView) {
        this.mView = mView;
    }

    /**
     * 获取View宽度
     *
     * @return
     */
    public int getViewWidth() {
        if (mView == null) {
            return 0;
        }
        return mView.getMeasuredWidth();
    }

    /**
     * 获取View宽度
     *
     * @return
     */
    public int getViewHeigth() {
        if (mView == null) {
            return 0;
        }
        return mView.getMeasuredHeight();
    }

    /**
     * 获取View的最小边长
     *
     * @return
     */
    public int getViewMinLength() {
        return Math.min(getViewHeigth(), getViewWidth());
    }

    /**
     * 获取View的中心点
     *
     * @return
     */
    public Point getViewCenterPoint(){
        return new Point(getViewWidth() /2, getViewHeigth()/2);
    }

    /**
     * 初始化
     *
     * @param context
     * @param a
     */
    public abstract void init(Context context, TypedArray a);

    /**
     * 绘制控制器
     *
     * @param canvas
     */
    public abstract void draw(Canvas canvas);

    /**
     * 创建动画集合
     *
     * @return
     */
    public abstract List<Animator> createAnimators();

    /**
     * 初始化对应动画集合
     */
    public void initAnimators() {
        this.mAnimators = createAnimators();
    }


    /**
     * 统一设置所有动画集合状态
     *
     * @param staus
     */
    public void setAnimatorsStaus(AnimatorStatus staus) {
        if (mAnimators == null || mAnimators.size() <= 0) {
            return;
        }
        for (int i = 0; i < mAnimators.size(); i++) {
            Animator animator = mAnimators.get(i);
            if (animator == null) {
                continue;
            }
            boolean isRunning = animator.isRunning();
            switch (staus) {
                case START:
                    if (!isRunning) {
                        animator.start();
                    }
                    break;
                case END:
                    if (isRunning) {
                        animator.end();
                    }
                    break;
                case CANCEL:
                    if (isRunning) {
                        animator.cancel();
                    }
                    break;
            }
        }
    }

    /**
     * 坐标点类
     */
    public final class Point {
        public float x;
        public float y;

        public Point(float x, float y) {
            this.x = x;
            this.y = y;
        }
    }
}
