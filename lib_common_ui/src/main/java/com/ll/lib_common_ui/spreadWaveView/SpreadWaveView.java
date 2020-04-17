package com.ll.lib_common_ui.spreadWaveView;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.support.annotation.NonNull;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

import com.ll.lib_common_ui.R;
import com.ll.lib_common_ui.controller.BaseViewAnimatorController;
import com.ll.lib_common_ui.spreadWaveView.controller.SpreadWaveViewAnimatorController;
import com.ll.lib_common_ui.utils.ScreenUtil;

public class SpreadWaveView extends View {
    private static final int DEFAULTSIZE = 500;
    public BaseViewAnimatorController mController;
    private int w = 0;
    private int h = 0;

    public SpreadWaveView(Context context) {
        this(context, null);
    }

    public SpreadWaveView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public SpreadWaveView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initAttrs(context, attrs, defStyleAttr);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        startDraw(canvas);
    }

    private void initAttrs(Context context, @NonNull AttributeSet attrs, int defStyleAttr) {
        Log.d("kylin", "initAttrs");
        TypedArray ta = context.obtainStyledAttributes(attrs, R.styleable.SpreadWaveView);
        initContronller(context, ta);
        ta.recycle();
    }

    private void initContronller(Context context, TypedArray a) {
        mController = new SpreadWaveViewAnimatorController();
        if (null != mController) {
            mController.init(context, a);
            mController.setView(this);
        }
    }

    private void startDraw(Canvas canvas) {
        if (null != mController) {
            mController.draw(canvas);
        }
    }


    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        w = measureDimension(ScreenUtil.dip2px(getContext(), DEFAULTSIZE), widthMeasureSpec);
        h = measureDimension(ScreenUtil.dip2px(getContext(), DEFAULTSIZE), heightMeasureSpec);
        Log.d("kylin", "onMeasure center w=" + w);
        Log.d("kylin", "onMeasure center h=" + h);
        setMeasuredDimension(w, h);
//        postInvalidate();
    }


    private int measureDimension(int defaultSize, int measureSpec) {
        int result = defaultSize;
        int specMode = MeasureSpec.getMode(measureSpec);
        int specSize = MeasureSpec.getSize(measureSpec);
        Log.d("kylin", "onMeasure specSize =" + specSize);
        if (specMode == MeasureSpec.AT_MOST) {
            result = specSize;
        } else if (specMode == MeasureSpec.EXACTLY) {
            result = Math.min(specSize, defaultSize);
        } else {
            result = defaultSize;
        }
        return result;
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        if (null != mController) {
            mController.setAnimatorsStaus(BaseViewAnimatorController.AnimatorStatus.CANCEL);
        }
    }

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        if (null != mController) {
            mController.setAnimatorsStaus(BaseViewAnimatorController.AnimatorStatus.START);
        }
    }

    @Override
    public void setVisibility(int visibility) {
        super.setVisibility(visibility);
        if (null != mController) {
            if (visibility == GONE || visibility == INVISIBLE) {
                mController.setAnimatorsStaus(BaseViewAnimatorController.AnimatorStatus.END);
            } else {
                mController.setAnimatorsStaus(BaseViewAnimatorController.AnimatorStatus.START);
            }
        }
    }

    public int getW() {
        return w;
    }

    public int getH() {
        return h;
    }

}
