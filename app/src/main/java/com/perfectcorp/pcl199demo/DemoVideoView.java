package com.perfectcorp.pcl199demo;

import android.animation.ObjectAnimator;
import android.animation.PropertyValuesHolder;
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

import java.lang.reflect.Field;

public class DemoVideoView extends View {
    private static final long VIDEO_DURATION = 30000;
    private static final float ASPECT_RATIO = 16f / 9f;

    public interface EventListener {
        void onPlayTimeUpdated(long playTimeMs);
    }

    private EventListener mListener;
    private ValueAnimator mTimeCtrl = ValueAnimator.ofFloat(0f, 100f).setDuration(VIDEO_DURATION);
    private DrawObject[] mDrawObjects = new DrawObject[] {
            new DrawTree(
                    new float[] {1.2f, 0.2f},   // x values
                    new float[] {0.3f, 0.3f},   // y values
                    new float[] {0.05f, 0.05f}, // w values
                    new float[] {0.3f, 0.3f}    // h values
            ),
            new DrawTree(
                    new float[] {1.5f, 0.4f},   // x values
                    new float[] {0.4f, 0.4f},   // y values
                    new float[] {0.07f, 0.07f}, // w values
                    new float[] {0.4f, 0.4f}    // h values
            ),
            new DrawTree(
                    new float[] {1.8f, 0.6f},   // x values
                    new float[] {0.5f, 0.5f},   // y values
                    new float[] {0.09f, 0.09f}, // w values
                    new float[] {0.5f, 0.5f}    // h values
            ),
    };

    private GestureDetector mGestureDetector;
    private boolean mIsPlaying = false;
    private Paint mPlayBtnPaint = new Paint();

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
                long currentPlayTime = animation.getCurrentPlayTime();

                for (DrawObject drawObject : mDrawObjects) {
                    drawObject.anim.setCurrentPlayTime(currentPlayTime);
                }
                invalidate();

                invokePlayTimeUpdated(currentPlayTime);
            }
        });

        mGestureDetector = new GestureDetector(getContext(), mGestureListener);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        canvas.drawColor(Color.rgb(222,184,135));

        for (DrawObject drawObject : mDrawObjects) {
            drawObject.draw(canvas);
        }

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

        path.close();

        mPlayBtnPaint.setStrokeWidth(0);
        mPlayBtnPaint.setStyle(Paint.Style.FILL);
        canvas.drawPath(path, mPlayBtnPaint);

        radius *= 1.25f;
        mPlayBtnPaint.setStrokeWidth(radius / 8f);
        mPlayBtnPaint.setStyle(Paint.Style.STROKE);
        canvas.drawCircle(cx, cy, radius, mPlayBtnPaint);
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

            if (mIsPlaying) {
                if (mTimeCtrl.isStarted()) {
                    mTimeCtrl.resume();
                } else {
                    mTimeCtrl.start();
                }
            } else {
                mTimeCtrl.pause();
            }

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

    private abstract class DrawObject implements ValueAnimator.AnimatorUpdateListener {
        ObjectAnimator anim;

        DrawObject(PropertyValuesHolder[] values) {
            anim = ObjectAnimator.ofPropertyValuesHolder(this, values).setDuration(VIDEO_DURATION);
            anim.addUpdateListener(this);
            anim.setCurrentPlayTime(0);
        }

        abstract void draw(Canvas canvas);

        @Override
        public void onAnimationUpdate(ValueAnimator animation) {
            Log.bear();
            PropertyValuesHolder[] values = animation.getValues();
            for (PropertyValuesHolder value : values) {
                String propName = value.getPropertyName();
                Field field = findField(getClass(), propName);
                if (field == null) {
                    continue;
                }
                Log.bear(propName);

                field.setAccessible(true);
                try {
                    field.set(this, animation.getAnimatedValue(propName));
                    Log.bear(propName, field.get(this));
                } catch (IllegalAccessException ignored) {
                }
            }
        }

        private Field findField(Class<?> cls, String fieldName) {
            if (cls == null || fieldName == null) {
                return null;
            }

            try {
                return cls.getDeclaredField(fieldName);
            } catch (NoSuchFieldException ignored) {
                return findField(cls.getSuperclass(), fieldName);
            }
        }
    }

    private class DrawTree extends DrawObject {
        // Direct access properties:
        float x, y, w, h;

        // Private members:
        private Paint mLeafPaint = new Paint();
        private Paint mTrunkPaint = new Paint();

        DrawTree(float[] xValues, float[] yValues, float[] wValues, float[] hValues) {
            super(new PropertyValuesHolder[] {
                    PropertyValuesHolder.ofFloat("x", xValues),
                    PropertyValuesHolder.ofFloat("y", yValues),
                    PropertyValuesHolder.ofFloat("w", wValues),
                    PropertyValuesHolder.ofFloat("h", hValues),
            });
            mLeafPaint.setColor(Color.rgb(34,139,34));
            mLeafPaint.setStyle(Paint.Style.FILL);

            mTrunkPaint.setColor(Color.rgb(139,69,19));
            mTrunkPaint.setStyle(Paint.Style.FILL);
        }

        @Override
        void draw(Canvas canvas) {
            float cx = getWidth() * x;
            float cy = getHeight() * y;
            float width = getWidth() * w;
            float height = getHeight() * h;

            Path leafPath = new Path();
            leafPath.moveTo(cx, cy - height / 2f);
            leafPath.lineTo(cx + width / 2f, cy + height / 4f);
            leafPath.lineTo(cx - width / 2f, cy + height / 4f);
            leafPath.close();

            canvas.drawPath(leafPath, mLeafPaint);

            canvas.drawRect(cx - width / 6f, cy + height / 4f, cx + width / 6f, cy + height / 2f, mTrunkPaint);
        }
    }
}
