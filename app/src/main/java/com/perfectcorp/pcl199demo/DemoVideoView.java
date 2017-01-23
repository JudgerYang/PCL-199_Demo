package com.perfectcorp.pcl199demo;

import android.animation.ValueAnimator;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.util.AttributeSet;
import android.view.View;

import com.perfectcorp.pcl199demo.utility.Log;

public class DemoVideoView extends View {
    private static long VIDEO_DURATION = 140000;

    public interface EventListener {
        void onPlayTimeUpdated(long playTimeMs);
    }

    private EventListener mListener;
    private ValueAnimator mTimeCtrl = ValueAnimator.ofFloat(0f, 100f).setDuration(VIDEO_DURATION);

    public DemoVideoView(Context context) {
        super(context);
        init(null, 0);
    }

    public DemoVideoView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(attrs, 0);
    }

    public DemoVideoView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init(attrs, defStyle);
    }

    private void init(AttributeSet attrs, int defStyle) {
        // Load attributes
        final TypedArray a = getContext().obtainStyledAttributes(
                attrs, R.styleable.DemoVideoView, defStyle, 0);

//        mExampleString = a.getString(R.styleable.DemoVideoView_exampleString);
//        mExampleColor = a.getColor(R.styleable.DemoVideoView_exampleColor, mExampleColor);
//        // Use getDimensionPixelSize or getDimensionPixelOffset when dealing with
//        // values that should fall on pixel boundaries.
//        mExampleDimension = a.getDimension(R.styleable.DemoVideoView_exampleDimension, mExampleDimension);
//
//        if (a.hasValue(R.styleable.DemoVideoView_exampleDrawable)) {
//            mExampleDrawable = a.getDrawable(R.styleable.DemoVideoView_exampleDrawable);
//            mExampleDrawable.setCallback(this);
//        }

        a.recycle();

        mTimeCtrl.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                invokePlayTimeUpdated(animation.getCurrentPlayTime());
            }
        });
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        canvas.drawColor(Color.RED);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        if (getMeasuredHeight() == 0) {
            int width = getMeasuredWidth();
            int height = (int) (width / (16f / 9f));
            setMeasuredDimension(width, height);
        }
    }

    public void setEventListener(EventListener listener) {
        mListener = listener;
    }

    private void invokePlayTimeUpdated(long playTimeMs) {
        EventListener listener = mListener;
        if (listener != null) {
            listener.onPlayTimeUpdated(playTimeMs);
        }
        Log.bear(playTimeMs);
    }

    public long getVideoDuration() {
        return VIDEO_DURATION;
    }

    public void setPlayTime(long playTimeMs) {
        mTimeCtrl.setCurrentPlayTime(playTimeMs);
    }

    private class DrawObject {
        private ValueAnimator mAnimX;
        private ValueAnimator mAnimY;
        private ValueAnimator mAnimW;
        private ValueAnimator mAnimH;

        DrawObject(ValueAnimator animX, ValueAnimator animY, ValueAnimator animW, ValueAnimator animH) {
            mAnimX = animX;
            mAnimY = animY;
            mAnimW = animW;
            mAnimH = animH;
        }
    }
}
