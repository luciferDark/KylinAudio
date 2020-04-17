package com.ll.lib_common_ui.spreadWaveView.controller;

import android.animation.Animator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.Log;
import android.view.View;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.Animation;

import com.ll.lib_common_ui.R;
import com.ll.lib_common_ui.controller.BaseViewAnimatorController;
import com.ll.lib_common_ui.spreadWaveView.SpreadWaveView;

import java.util.ArrayList;
import java.util.List;

public class SpreadWaveViewAnimatorController extends BaseViewAnimatorController {
    private final static int DEFUALT_CIRCLE_COUNT = 2;
    private int circleNum = DEFUALT_CIRCLE_COUNT; // 画圆数量
    private int startRadius = 0;
    private int maxRadius = 120;
    /**
     * 每个圆动画延时时间
     */
    private int[] delays;
    /**
     * 进度中每个圆Alpha缩放集合
     */
    private int[] circleAlphas;
    /**
     * 进度中每个圆半径的缩放集合
     */
    private int[] radius;
    private int delayTime = 120;//延时间隔时间
    private int animatorDuration = 1000;
    private final static int ALPHA = 255;

    private int mPaintColor;
    private int mPaintWidth;
    private Paint mPaint;
    private Point centerPoint;

    public SpreadWaveViewAnimatorController() {
        initData(this.circleNum);
        initAnimators();
    }

    public SpreadWaveViewAnimatorController(int num) {
        this.circleNum = num;
        initData(this.circleNum);
        initAnimators();
    }

    private void initData(int num) {
        Log.d("kylin", "initData" + num);
        delayTime = (int) animatorDuration / num;
        delays = new int[num];
        radius = new int[num];
        circleAlphas = new int[num];
        for (int i = 0; i < delays.length; i++) {
            delays[i] = delayTime * i;
        }
    }

    @Override
    public int getViewWidth() {
        if (mView == null){
            return 0;
        }
        if (mView instanceof SpreadWaveView){
            return ((SpreadWaveView)mView).getW();
        } else {
            return super.getViewWidth();
        }
    }

    @Override
    public int getViewHeigth() {
        if (mView == null){
            return 0;
        }
        if (mView instanceof SpreadWaveView){
            return ((SpreadWaveView)mView).getH();
        } else {
            return super.getViewHeigth();
        }
    }

    @Override
    public void init(Context context, TypedArray a) {
        mPaintColor = a.getColor(R.styleable.SpreadWaveView_spread_wave_color, Color.RED);
        mPaintWidth = a.getColor(R.styleable.SpreadWaveView_spread_circle_stroke_width, 5);
        int circleCount = a.getInteger(R.styleable.SpreadWaveView_spread_circle_num, 3);
        if (circleCount != getDefualtCircleCount()) {
            this.circleNum = circleCount;
        }
        startRadius = a.getInteger(R.styleable.SpreadWaveView_spread_start_radius, 0);
        maxRadius = a.getInteger(R.styleable.SpreadWaveView_spread_max_radius, 120);
        animatorDuration = a.getInteger(R.styleable.SpreadWaveView_spread_duration_time, 1000);
        Log.d("kylin", "circleCount" + circleCount);
        mPaint = new Paint();
        mPaint.setColor(mPaintColor);
        mPaint.setStyle(Paint.Style.STROKE);
        mPaint.setStrokeWidth(mPaintWidth);
        mPaint.setAntiAlias(true);

        initData(this.circleNum);
        initAnimators();
    }

    @Override
    public void draw(Canvas canvas) {
        Log.d("kylin", "draw");
        for (int i = 0; i < circleNum; i++) {
            canvas.save();
            //获取每个点的坐标点
//            canvas.translate(0, 0);
            if (circleAlphas[i] > 0){
                mPaint.setAlpha(circleAlphas[i]);
                canvas.drawCircle(getViewWidth() / 2, getViewHeigth() / 2, radius[i], mPaint);
                canvas.restore();
            }
        }
    }

    @Override
    public List<Animator> createAnimators() {
        Log.d("kylin", "createAnimators");
        List<Animator> animators = new ArrayList<Animator>();
        int repeatCount = Animation.INFINITE;
        for (int i = 0; i < circleNum; i++) {
            //设置缩放动画
            final int index = i;
            ValueAnimator scaleAnimate = ValueAnimator.ofInt(startRadius, maxRadius);
            scaleAnimate.setDuration(animatorDuration);
            scaleAnimate.setRepeatMode(ValueAnimator.RESTART);
            scaleAnimate.setRepeatCount(repeatCount);
            scaleAnimate.setStartDelay(delays[i]);
            scaleAnimate.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                @Override
                public void onAnimationUpdate(ValueAnimator valueAnimator) {
                    radius[index] = Math.min(getViewMinLength()/2,(int) valueAnimator.getAnimatedValue());
                    postInvalidate();
                }
            });
            scaleAnimate.setInterpolator(new AccelerateDecelerateInterpolator());
            scaleAnimate.start();
            animators.add(scaleAnimate);
            //设置透明动画
            ValueAnimator alphaAnimator = ValueAnimator.ofInt(ALPHA, 0);
            alphaAnimator.setDuration(animatorDuration);
            scaleAnimate.setRepeatMode(ValueAnimator.RESTART);
            alphaAnimator.setRepeatCount(repeatCount);
            alphaAnimator.setStartDelay(delays[i]);
            alphaAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                @Override
                public void onAnimationUpdate(ValueAnimator valueAnimator) {
                    circleAlphas[index] = (int) valueAnimator.getAnimatedValue();
                    postInvalidate();
                }
            });
            alphaAnimator.setInterpolator(new AccelerateDecelerateInterpolator());
            alphaAnimator.start();
            animators.add(alphaAnimator);
        }
        return animators;
    }


    public static  int getDefualtCircleCount(){
        return DEFUALT_CIRCLE_COUNT;
    }
}
