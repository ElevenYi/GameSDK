package com.game.sdk.widget;

//import com.game.sdk.R;
import com.game.sdk.config.Config;
import com.game.sdk.model.GameFloatModel;
import com.game.sdk.model.IFloatModel;
import com.game.sdk.presenter.UserPresenter;
import com.game.sdk.widget.FloatContainer.FloatView;
import com.game.sdk.widget.FloatContainer.OnFloatViewClickListener;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.Log;
import android.util.SparseArray;
import android.view.MotionEvent;
import android.view.View;
//import android.widget.Toast;

/**
 * 
 * @author Wilfredo Liu
 *
 */
public class GameFloatPanel extends RotateView implements FloatView<GameFloatPanel>, OnFloatViewClickListener {

    private static int[] ITEM_BKG_ARRAY;

    //private final static String[] ITEM_TITLE_ARRAY = new String[] { "礼包", "猜你喜欢", "我", "下载" };

    public final static String TAG = "GameFloatPanel";

    private int mWidth, mHeight;
    
    private IFloatModel mModel;
    
    private SparseArray<Bundle> mBundles;

    public GameFloatPanel(Context context, IFloatModel model) {
        this(context, null, model);
    }

    public GameFloatPanel(Context context, AttributeSet attrs, IFloatModel model) {
        this(context, attrs, Config.getDimensByName(context,"float_panel_width"), Config.getDimensByName(context,"float_panel_width"), model);
    }

    public GameFloatPanel(Context context, AttributeSet attrs, int widthId, int heightId, IFloatModel model) {
        super(context, attrs);
        ITEM_BKG_ARRAY = new int[] { Config.getDrawableByName(context,"float_panel_item_bg")};
        mModel = model;
        mWidth = (int) getResources().getDimension(widthId);
        mHeight = (int) getResources().getDimension(heightId);
        setArea(new Point(mWidth, mHeight));
        setLayoutParams(makeParams());
        setBackgroundResource(Config.getDrawableByName(context,"ic_float_panel_bg"));
        GameFloatIcon icon = new GameFloatIcon(context);
        Paint paint = new Paint();
        paint.setTextSize(getResources().getDimension(Config.getDimensByName(context,"float_panel_item_font_size")));
        paint.setAntiAlias(true);
        paint.setColor(Color.BLACK);
        mBundles = mModel.getPanelInfo().clone();
        int size = 0;
        CharSequence[] titleArray;
        int[] iconArray;
        if (mBundles != null && (size = mBundles.size()) > 0) {
            titleArray = new CharSequence[size];
            iconArray = new int[size];
            for (int i = 0 ; i < size ; i++) {
                Bundle bundle = mBundles.valueAt(i);
                titleArray[i] = bundle.getString(GameFloatModel.KEY_TITLE);
                if (bundle.getBoolean(GameFloatModel.KEY_ISHOT, false)) {
                    iconArray[i] = Config.getDrawableByName(context, "ic_message_notify_small_point");
                } else {
//                    iconArray[i] = Config.getDrawableByName(context, "ic_message_notify_small_point");
                }
            }
            setRotateSource(size, 180 / size, ITEM_BKG_ARRAY, iconArray, titleArray, paint, icon);
        }
        icon.setImageResource(Config.getDrawableByName(context,"float_icon_default"));
        itemFront.setItemWidth(icon.width());
        itemFront.setItemHeight(icon.height());
        //setFontSelfRotate(false); //可固定文字自滚动
        //freezeRotate(true); //可固定转盘转动
        setRotateItemPadding((int) getResources().getDimension(Config.getDimensByName(context,"float_panel_item_font_padding")));
        setOnRotateItemClickListener(new OnRotateItemClickListener() {
            @Override
            public void onItemClicked(int id) {
                String url = mBundles.valueAt(id).getString(GameFloatModel.KEY_URL);
                if (!TextUtils.isEmpty(url)) {
                    if (url.contains("{sid}")) {
                        url = url.replace("{sid}", "{" + UserPresenter.getUser().getSid() + "}");
                    }
                }
                //Toast.makeText(getContext(), "第" + id + "个Item被点了" + mBundles.valueAt(id).getString(GameFloatModel.KEY_TITLE) + "\n" + url, Toast.LENGTH_SHORT).show();
                if (mModel != null) {
                    mModel.switchFloatView(IFloatModel.FLAG_NORMAL, false);
                }
                Uri uri = null;
                try {
                    uri = Uri.parse(url);
                } catch (NullPointerException e) {
                    e.printStackTrace();
                }
                if (uri != null) {
                    Intent it = new Intent(Intent.ACTION_VIEW, uri);  
                    getContext().startActivity(it);
                }
            }
        });
    }

    @Override
    public void onFloatViewClicked(int action, boolean longClick, float eX, float eY, float rawX, float rawY) {
        switch (action) {
        case MotionEvent.ACTION_DOWN:
            Log.i(TAG, "onFloatViewClicked down longclick:" + longClick + " ex:" + eX + " eY:" + eY);
            if (itemFront != null) {
                itemFront.getItemSrc().setPressed(true);
                layoutFrontView(true);
            }
            break;
        case MotionEvent.ACTION_MOVE:
            // Log.i(TAG, "onFloatViewClicked move longclick:" + longClick + "
            // ex:" + eX + " eY:" + eY);
            break;
        case MotionEvent.ACTION_CANCEL:
            Log.i(TAG, "onFloatViewClicked cancel:" + longClick + " ex:" + eX + " eY:" + eY);
            if (itemFront != null) {
                itemFront.getItemSrc().setPressed(false);
                layoutFrontView(true);
            }
            break;
        case MotionEvent.ACTION_UP:
            if (itemFront != null) {
                itemFront.getItemSrc().setPressed(false);
                layoutFrontView(true);
            }
            if (longClick) {
                Log.i(TAG, "onFloatViewClicked up longclick:" + longClick + " ex:" + eX + " eY:" + eY);
            } else {
                if (mModel != null) {
                    mModel.switchFloatView(IFloatModel.FLAG_NORMAL, false);
                }
            }
            break;
        default:
            break;
        }

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
    public boolean dispatchTouchEvent(MotionEvent event) {
        if (itemFront != null && itemFront.getItemSrc().getVisibility() == View.VISIBLE) {
            float distance = (float) Math.sqrt(Math.pow(Math.abs(centerPoint.x - event.getX()), 2)
                    + Math.pow(Math.abs(centerPoint.y - event.getY()), 2));
            if (distance < ((itemFront.itemWidth + itemFront.itemHeight) >> 1) >> 1) {
                return false;
            }
        }
        return super.dispatchTouchEvent(event);
    }
    /*
    public void setMosdel(IFloatModel model) {
        mModel = model;
    }*/

    @Override
    public GameFloatPanel instance() {
        return this;
    }
}
