package com.perfectcorp.pcl199demo;

import android.animation.ValueAnimator;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.util.AttributeSet;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;

import com.perfectcorp.pcl199demo.utility.Log;

public class DemoVideoView extends View {
    private static final long VIDEO_DURATION = 140000;
    private static final float ASPECT_RATIO = 16f / 9f;
    private GestureDetector mGestureDetector;
    private boolean mIsPlaying = false;
    private Paint mPlayBtnPaint = new Paint();

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
        final TypedArray a = getContext().obtainStyledAttributes(attrs, R.styleable.DemoVideoView, defStyle, 0);

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

        mPlayBtnPaint.setAntiAlias(true);
        mPlayBtnPaint.setColor(Color.WHITE);

        mTimeCtrl.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                invokePlayTimeUpdated(animation.getCurrentPlayTime());
            }
        });

        mGestureDetector = new GestureDetector(getContext(), mGestureListener);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        canvas.drawColor(Color.RED);

        if (!mIsPlaying) {
            drawPlayButton(canvas);
        }
    }

    private void drawPlayButton(Canvas canvas) {
        float cx = getWidth() / 2f;
        float cy = getHeight() / 2f;
        float radius = Math.min(getWidth(), getHeight()) /5f;

        Path path = new Path();

        float theta = 0;
        path.moveTo((float) (cx + radius * Math.cos(theta)), (float) (cy + radius * Math.sin(theta)));

        theta = (float) Math.toRadians(120);
        path.lineTo((float) (cx + radius * Math.cos(theta)), (float) (cy + radius * Math.sin(theta)));

        theta = (float) Math.toRadians(240);
        path.lineTo((float) (cx + radius * Math.cos(theta)), (float) (cy + radius * Math.sin(theta)));

        theta = (float) Math.toRadians(0);
        path.lineTo((float) (cx + radius * Math.cos(theta)), (float) (cy + radius * Math.sin(theta)));

        mPlayBtnPaint.setStrokeWidth(0);
        mPlayBtnPaint.setStyle(Paint.Style.FILL);
        canvas.drawPath(path, mPlayBtnPaint);

        radius *= 1.25f;
        mPlayBtnPaint.setStrokeWidth(radius / 8f);
        mPlayBtnPaint.setStyle(Paint.Style.STROKE);
        canvas.drawCircle(cx, cy, radius, mPlayBtnPaint);
//        canvas.drawRect(centerX - 100, centerY - 100, centerX + 100, centerY + 100, mPlayBtnPaint);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        if (getMeasuredHeight() == 0) {
            int width = getMeasuredWidth();
            int height = (int) (width / ASPECT_RATIO);
            setMeasuredDimension(width, height);
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        return mGestureDetector.onTouchEvent(event);
    }

    private GestureDetector.SimpleOnGestureListener mGestureListener = new GestureDetector.SimpleOnGestureListener() {
        @Override
        public boolean onDown(MotionEvent e) {
            return true;
        }

        @Override
        public boolean onSingleTapUp(MotionEvent e) {
            mIsPlaying = !mIsPlaying;
            Log.bear(mIsPlaying ? "playing" : "stopped");
            invalidate();
            return true;
        }
    };

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
