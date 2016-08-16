package com.game.sdk.widget;

import com.game.sdk.model.IFloatModel;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.Configuration;
import android.graphics.Point;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.VelocityTracker;
import android.view.View;
import android.view.ViewConfiguration;
import android.widget.FrameLayout;

/**
 * 
 * @author Wilfredo Liu
 *
 */
public class FloatContainer extends FrameLayout {

    public final static int MIN_LONG_CLICKED_DURATION = 400;

    private static final String TAG = "FloatContainer";
    private static final boolean DEBUG = true;

    private int mTouchSlop;

    private float mTouchStartX;
    private float mTouchStartY;
    private float mLastestRawX;
    private float mLastestRawY;

    private IFloatModel mModel;

    private int mMeasuredWidth;
    private int mMeasuredHeight;

    private FloatView<?> mFloatView;

    private Point mFloatXY;

    //private int mRadius;
    //private int mRadiusInc;

    private OnFloatViewClickListener mListener;

    public interface FloatView<V extends View> {
        public int width();
        public int height();
        public V instance();
    }

    public interface OnFloatViewClickListener {
        public void onFloatViewClicked(int action, boolean longClick, float eX, float eY, float rawX, float rawY);
    }

    public FloatContainer(Context context) {
        this(context, null);
    }

    public FloatContainer(Context context, IFloatModel model) {
        this(context, model, null);
    }

    public FloatContainer(Context context, IFloatModel model, FloatView<View> floatView) {
        this(context, null, 0, model, floatView);
    }

    public FloatContainer(Context context, AttributeSet attrs, int defStyle, IFloatModel model,
            FloatView<View> floatView) {
        super(context, attrs, defStyle);
        mModel = model;
        mFloatView = floatView;
        if (mFloatView != null) {
            addView(mFloatView.instance());
        }
        setLongClickable(true);

        final ViewConfiguration cfg = ViewConfiguration.get(context);

        mTouchSlop = cfg.getScaledTouchSlop() / 3;
        mFloatXY = new Point(0, 0);
        measureLayoutView();
    }

    public void setOnFloatViewClickListener(OnFloatViewClickListener listener) {
        mListener = listener;
    }
    
    public FloatView<?> getFloatView() {
        return mFloatView;
    }

    @Override
    public void addView(View child) {
        if (getChildCount() > 0) {
            removeAllViews();
        }
        if (child instanceof FloatView<?>) {
            mFloatView = (FloatView<?>) child;
            super.addView(child);
            measureLayoutView(true);
        }
    }

    @Override
    public void addView(View child, int index) {
        if (getChildCount() > 0) {
            removeAllViews();
        }
        if (child instanceof FloatView<?>) {
            mFloatView = (FloatView<?>) child;
            super.addView(child, index);
            measureLayoutView(true);
        }
    }

    @Override
    public void addView(View child, int width, int height) {
        if (getChildCount() > 0) {
            removeAllViews();
        }
        if (child instanceof FloatView<?>) {
            mFloatView = (FloatView<?>) child;
            super.addView(child, width, height);
            measureLayoutView(true);
        }
    }

    public void setModel(IFloatModel model) {
        mModel = model;
    }

    //public int getOutRadius() {
    //    return mRadius + mRadiusInc;
    //}

    private void measureLayoutView() {
        measureLayoutView(true);
    }

    private void measureLayoutView(boolean invalidate) {
        if (DEBUG) {
            Log.d(TAG, "measureLayoutView");
        }
        measureView();
        layoutView();
        if (invalidate)
            invalidate();
    }

    private void measureView() {
        if (mFloatView == null) {
            return;
        }
        mMeasuredWidth = mFloatView.width();
        mMeasuredHeight = mFloatView.height();
        mFloatXY.x = 0;
        mFloatXY.y = 0;
        measure(mMeasuredWidth, mMeasuredHeight);
    }

    private void layoutView() {
        if (mFloatView != null) {
            mFloatView.instance().layout(mFloatXY.x, mFloatXY.y, mFloatView.width(), mFloatView.height());
        }
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        if (DEBUG) {
            Log.d(TAG, "onMeasure: mMeasuredWidth = " + mMeasuredWidth + ", mMeasuredHeight = " + mMeasuredHeight);
        }
        setMeasuredDimension(mMeasuredWidth, mMeasuredHeight);
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);
        if (DEBUG) {
            Log.d(TAG, "onLayout");
        }
        if (mFloatView != null) {
            mFloatView.instance().layout(left, top, right, bottom);
        }
    }

    @Override
    protected void onConfigurationChanged(Configuration newConfig) {
        if (DEBUG) {
            Log.d(TAG, "onConfigurationChanged");
        }
        super.onConfigurationChanged(newConfig);
        measureLayoutView();
    }

    /*
     * @Override public boolean performClick() { // Calls the super
     * implementation, which generates an AccessibilityEvent // and calls the
     * onClick() listener on the view, if any super.performClick();
     * 
     * // Handle the action for the custom click here
     * 
     * return true; }
     */
    
    private VelocityTracker tracker;
    private int pointid;
    
    @SuppressLint("ClickableViewAccessibility")
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (mModel == null) {
            return false;
        }
        Rect frame = mModel.getDisplayFrame();
        float rawX = event.getRawX();
        float rawY = event.getRawY() - frame.top;
        float x = event.getX();
        float y = event.getY();
        if (tracker == null) {
            tracker = VelocityTracker.obtain();
        }
        tracker.addMovement(event);
        boolean dropEvent = false;
        switch (event.getAction()) {
        case MotionEvent.ACTION_DOWN:
            mTouchStartX = x;
            mTouchStartY = y;
            Log.d(TAG, "Enter mode NONE: point count = " + event.getPointerCount());
            break;
        case MotionEvent.ACTION_MOVE:
            if (Math.abs(rawX - mLastestRawX) < mTouchSlop && Math.abs(rawY - mLastestRawY) < mTouchSlop) {
                break;
            }
            float floatY = rawY - mTouchStartY;// - (mCenter.y -
                                               // mFloatView.width() / 2);

            if (floatY < frame.top)
                floatY = frame.top;
            else if (floatY > frame.bottom - mMeasuredHeight)
                floatY = frame.bottom - mMeasuredHeight;

            float floatX = rawX - mTouchStartX;// - (mCenter.x -
                                               // mFloatView.height() / 2);
            if (floatX < frame.left)
                floatX = frame.left;
            else if (floatX > frame.right - mMeasuredWidth)
                floatX = frame.right - mMeasuredWidth;
            mModel.updateFloatViewXY(floatX, floatY);
            break;
        case MotionEvent.ACTION_CANCEL:
            if (DEBUG)
                Log.d(TAG, "Action_cancel");
            if (tracker != null) {
                tracker.recycle();
                tracker = null;
            }
            break;
        case MotionEvent.ACTION_UP:
            if (DEBUG) {
                Log.d(TAG, "ACTION_UP: point count = " + event.getPointerCount());
            }
            if (tracker != null) {
                pointid = event.getPointerId(event.getPointerCount() - 1);
                tracker.computeCurrentVelocity(1000, 100);
                if(Math.abs(tracker.getXVelocity(pointid)) + Math.abs(tracker.getYVelocity(pointid)) >100) {
                    dropEvent = true;
                }
                tracker.recycle();
                tracker = null;
            }
            // if (event.getPointerCount() > 1)
            // break;
            break;
        }
        if (mFloatView != null) {
            mFloatView.instance().onTouchEvent(event);
        }
        if (mListener != null) {
            //if (dropEvent) {
            //    event.setAction(MotionEvent.ACTION_CANCEL);
            //}
            mListener.onFloatViewClicked(event.getAction(),
                    event.getEventTime() - event.getDownTime() > MIN_LONG_CLICKED_DURATION | dropEvent, mTouchStartX, mTouchStartY, rawX, rawY);
        }
        mLastestRawX = rawX;
        mLastestRawY = rawY;

        return true;
    }
}
