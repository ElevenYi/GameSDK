package com.game.sdk.widget;

//import com.game.sdk.R;
import com.game.sdk.config.Config;
import com.game.sdk.model.IFloatModel;
import com.game.sdk.presenter.UserPresenter;
import com.game.sdk.widget.FloatContainer.FloatView;
import com.game.sdk.widget.FloatContainer.OnFloatViewClickListener;

import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.animation.ValueAnimator.AnimatorUpdateListener;
import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.ViewGroup.LayoutParams;
import android.view.animation.AnticipateInterpolator;
import android.view.animation.BounceInterpolator;
import android.widget.ImageView;

/**
 * 
 * @author Wilfredo Liu
 *
 */
public class GameFloatIcon extends ImageView implements FloatView<GameFloatIcon>, OnFloatViewClickListener,  AnimatorUpdateListener {

    public final static String TAG = "GameFloatIcon";
    
    public static final int BEFORE_OUTCROP_DURATION = 3000;
    
    public static final int OUTCROP_DURATION = 700;

    private static final float PERCENT_OUTCROP_IN_START = 0;

    private static final float PERCENT_OUTCROP_IN_END = 0.7F;
    
    private static final String OUTCROP = "outcrop";
    
    public static final int SCALE_DURATION = 100;
    
    private static final float PERCENT_SCALE_IN_START = 1F;

    private static final float PERCENT_SCALE_IN_END = 0;
    
    private static final String SCALE = "scale";
    
    private final int POINT_PADDING;
    
    private final int POINT_SIZE;
    
    private int mWidth, mHeight;
    
    private IFloatModel mModel;
    
    private int mFloatFlag;
    
    private ObjectAnimator mOutCropAnimator, mScaleAnimator;
    
    private boolean toRightSide = false;
    
    private boolean hidePoint = false;
        
    private float mPointScale;
    
    Drawable mMessagePoint;
    
    Matrix mMatrix;

    public GameFloatIcon(Context context) {
        this(context, null);
    }
    
    public GameFloatIcon(Context context, int flag) {
        this(context, null);
        mFloatFlag = flag;
    }
    
    public GameFloatIcon(Context context, int flag, IFloatModel model) {
        this(context, null);
        mFloatFlag = flag;
        mModel = model;
    }

    public GameFloatIcon(Context context, AttributeSet attrs) {
        this(context, null, 0);
    }

    public GameFloatIcon(Context context, AttributeSet attrs, int defStyle) {
        this(context, attrs, defStyle, Config.getDimensByName(context,"float_icon_width"), Config.getDimensByName(context,"float_icon_width"));
    }

    @SuppressWarnings("deprecation")
    @SuppressLint("NewApi")
    public GameFloatIcon(Context context, AttributeSet attrs, int defStyle, int widthId, int heightId) {
        super(context, attrs, defStyle);
        POINT_PADDING = (int) getResources().getDimension(Config.getDimensByName(context,"float_message_notify_point_padding"));
        POINT_SIZE = (int) getResources().getDimension(Config.getDimensByName(context,"float_message_notify_point_size"));
        mWidth = (int) getResources().getDimension(widthId);
        mHeight = (int) getResources().getDimension(heightId);
        setFloatImageMatrix();
        setLayoutParams(makeParams());
        mOutCropAnimator = ObjectAnimator.ofFloat(this, OUTCROP, PERCENT_OUTCROP_IN_START, PERCENT_OUTCROP_IN_END);
        mOutCropAnimator.addUpdateListener(this);
        mOutCropAnimator.setInterpolator(new AnticipateInterpolator(1));
        mOutCropAnimator.setDuration(OUTCROP_DURATION);
        mScaleAnimator = ObjectAnimator.ofFloat(this, SCALE, PERCENT_SCALE_IN_START, PERCENT_SCALE_IN_END);
        mScaleAnimator.addUpdateListener(this);
        mScaleAnimator.setInterpolator(new BounceInterpolator());
        mScaleAnimator.setDuration(SCALE_DURATION);
        if (Build.VERSION.SDK_INT >= 21) {
            mMessagePoint = getResources().getDrawable(Config.getDrawableByName(context,"ic_message_notify_point"), null);
        } else {
            mMessagePoint = getResources().getDrawable(Config.getDrawableByName(context,"ic_message_notify_point"));
        }
        mMessagePoint.setBounds(POINT_PADDING, POINT_PADDING, POINT_SIZE + POINT_PADDING , POINT_SIZE + POINT_PADDING);
        mMatrix = new Matrix();
    }
    
    public void setModel(IFloatModel model) {
        mModel = model;
    }

    public void setWidth(int width) {
        mWidth = width;
    }

    public void setHeight(int height) {
        mHeight = height;
    }
    
    public int getFloatFlag() {
        return mFloatFlag;
    }

    public void setFloatFlag(int flag) {
        mFloatFlag = flag;
    }

    public LayoutParams makeParams() {
        return new LayoutParams(mWidth, mHeight);
    }

    @Override
    public int width() {
        return mWidth;
    }

    @Override
    public int height() {
        return mHeight;
    }

    @Override
    public void setImageResource(int resId) {
        super.setImageResource(resId);
        setFloatImageMatrix();
    }

    private void setFloatImageMatrix() {
        Drawable img = getDrawable();
        if (img == null) {
            return;
        }
        setScaleType(ScaleType.FIT_XY);
        int dwidth = img.getIntrinsicWidth();
        int dheight = img.getIntrinsicHeight();
        float scale;

        if (dwidth <= mWidth && dheight <= mHeight) {
            scale = 1.0f;
        } else {
            scale = Math.min((float) mWidth / (float) dwidth, (float) mHeight / (float) dheight);
        }

        Log.d(TAG, "dwidth = " + dwidth + ", dheight = " + dheight + ", mWidth = " + mWidth + ", mHeight = " + mHeight);

        int dx = (int) ((mWidth - dwidth * scale) * 0.5f + 0.5f);
        int dy = (int) ((mHeight - dheight * scale) * 0.5f + 0.5f);

        Matrix matrix = getImageMatrix();
        matrix.setScale(scale, scale);
        matrix.postTranslate(dx, dy);
        setImageMatrix(matrix);
    }

    @Override
    public void onFloatViewClicked(int action, boolean longClick, float eX, float eY, float rawX, float rawY) {
        switch (action) {
        case MotionEvent.ACTION_DOWN:
            if (getTranslationX() != 0) {
                clickCount = 0;
                mOutCropAnimator.reverse();
                setTranslationX(0);
            }
            Log.i(TAG, "onFloatViewClicked down longclick:" + longClick + " ex:" + eX + " eY:" + eY);
            setPressed(true);
            if (IFloatModel.FLAG_TRIAL == mFloatFlag) {
                startScaleAnimation(lastShowSideIsLeft, true);
            }
            break;
        case MotionEvent.ACTION_MOVE:
            // Log.i(TAG, "onFloatViewClicked move longclick:" + longClick + "
            // ex:" + eX + " eY:" + eY);
            if (mOutCropAnimator.isRunning()) {
                mOutCropAnimator.cancel();
                setTranslationX(0);
            }
            break;
        case MotionEvent.ACTION_CANCEL:
            Log.i(TAG, "onFloatViewClicked cancel:" + longClick + " ex:" + eX + " eY:" + eY);
            setPressed(false);
            //startTranslationAnimation(rawX);
            if (IFloatModel.FLAG_TRIAL == mFloatFlag) {
                startScaleAnimation(toRightSide, false);
            }
            break;
        case MotionEvent.ACTION_UP:
            setPressed(false);
            if (clickCount <= 0 & startTranslationAnimation(rawX)) {
                if (IFloatModel.FLAG_TRIAL == mFloatFlag) {
                    startScaleAnimation(toRightSide, false);
                }
                clickCount++;
                break;
            } else {
                if (IFloatModel.FLAG_TRIAL == mFloatFlag) {
                    startScaleAnimation(toRightSide, false);
                }
            }
            if (longClick) {
                clickCount = 0;
                Log.i(TAG, "onFloatViewClicked up longclick:" + longClick + " ex:" + eX + " eY:" + eY);
            } else {
                if (IFloatModel.FLAG_TRIAL == mFloatFlag) {
                    UserPresenter.showBindActivity(getContext());
                } else if (mModel != null){
                    mModel.switchFloatView(IFloatModel.FLAG_PANEL, false);
                }
            }
            break;
        default:
            break;
        }

    }

    @Override
    public GameFloatIcon instance() {
        return this;
    }

    @Override
    public void onAnimationUpdate(ValueAnimator animation) {
        if (animation == null) {
            return;
        }
        Object value = null;
        if ((value = animation.getAnimatedValue(OUTCROP)) != null) {
            if (toRightSide) {
                setTranslationX((Float)value*width());
                //mMatrix.
            } else {
                setTranslationX(-(Float)value*width());
            }
            invalidate();
        }
        if ((value = animation.getAnimatedValue(SCALE)) != null) {
            if (hidePoint) {
                mPointScale = (Float)value;
            } else {
                mPointScale = (1 - (Float)value);
            }
            invalidate();
        }
    }

    private final Runnable startOutCropAnim = new Runnable() {
        @Override
        public void run() {
            mOutCropAnimator.start();
        }
    };

    int clickCount = 0;
    
    public boolean startTranslationAnimation(float rawX) {
        boolean anim = false;
        if (mOutCropAnimator.isRunning()) {
            mOutCropAnimator.cancel();
        }
        removeCallbacks(startOutCropAnim);
        setTranslationX(0);
        if (mModel != null) {
            Log.i(TAG, "startAnimation rawX:" + rawX + " left:" + mModel.getDisplayFrame().left + " right:" + mModel.getDisplayFrame().right);
            if (width() > rawX) {
                mModel.updateFloatViewXY(0, mModel.getLastPosition().y - (height() >> 1));
                toRightSide = false;
                anim = true;
                postDelayed(startOutCropAnim, BEFORE_OUTCROP_DURATION);
                //mOutCropAnimator.start();
            } else if (mModel.getDisplayFrame().right - width() < rawX) {
                mModel.updateFloatViewXY(mModel.getDisplayFrame().right, mModel.getLastPosition().y - (height() >> 1));
                toRightSide = true;
                anim = true;
                postDelayed(startOutCropAnim, BEFORE_OUTCROP_DURATION);
                //mOutCropAnimator.start();
            } else {
                toRightSide = false;
            }
        } else {
            toRightSide = false;
        }
        return anim;
    }
    
    private boolean lastShowSideIsLeft = false;
    
    public void startScaleAnimation(boolean toRightSide, boolean hidePoint) {
        if (toRightSide) {
            mMessagePoint.setBounds(POINT_PADDING, POINT_PADDING, POINT_SIZE + POINT_PADDING , POINT_SIZE + POINT_PADDING);
        } else {
            mMessagePoint.setBounds(width() - POINT_SIZE - POINT_PADDING, POINT_PADDING, width() - POINT_PADDING , POINT_SIZE + POINT_PADDING);
        }
        if (mScaleAnimator.isRunning()) {
            mScaleAnimator.cancel();
        }
        this.hidePoint = hidePoint;
        if (!hidePoint) {
            lastShowSideIsLeft = toRightSide;
        }
        mScaleAnimator.start();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        int state = canvas.save();
        mMatrix.reset();
        mMatrix.setTranslate(getTranslationX(), getTranslationY());
        if (toRightSide) {
            mMatrix.postScale(mPointScale, mPointScale, (POINT_SIZE >> 1) + POINT_PADDING, (POINT_SIZE >> 1) + POINT_PADDING);
        } else {
            mMatrix.postScale(mPointScale, mPointScale, width() - (POINT_SIZE >> 1) - POINT_PADDING, (POINT_SIZE >> 1) + POINT_PADDING);
        }
        canvas.setMatrix(mMatrix);
        mMessagePoint.draw(canvas);
        canvas.restoreToCount(state);
    }
}
