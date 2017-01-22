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

        ValueAnimator anim1 = ValueAnimator.ofFloat(0f, 100f).setDuration(140000);
        anim1.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                Log.bear(animation.getCurrentPlayTime(), animation.getAnimatedValue());
            }
        });

        anim1.setCurrentPlayTime(10000);
        anim1.setCurrentPlayTime(100000);
        anim1.setCurrentPlayTime(140000);
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
}
