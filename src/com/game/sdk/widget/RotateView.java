package com.game.sdk.widget;

import java.util.ArrayList;
import java.util.List;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.content.res.Resources.NotFoundException;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.PointF;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.view.GestureDetector;
import android.view.GestureDetector.OnGestureListener;
import android.view.MotionEvent;
import android.view.View;
import android.widget.FrameLayout;

/**
 * 转盘自定义视图
 * 
 * @author Wilfredo.Liu
 * @EMail stein.www@gmail.com
 * @Time 2012/09/23
 * @UpdateTime 2016/05/11
 * @version 2.2.1
 */
public class RotateView extends FrameLayout
{
    public final static String TAG = "RotateView";
    
	private final static int[] PRESSED = new int[]{android.R.attr.state_pressed};
	private final static int[] UNPRESSED = new int[]{-android.R.attr.state_pressed};

	private GestureDetector detectorMain;

	protected PointF centerPoint; //View 的中心点
	
	private OnRotateItemClickListener onRotateItemClickListener;
	
	private Matrix matrix;
	private Paint paintTitle;

	private float currentAngel = 0; //当前旋转的偏移角度
	
	private float currentTouch = 0; //按下的点的角度

	private List<ItemRotate<Drawable>> itemList;
	private int padding;
	private boolean fontSelfRotate = true;
	
	private boolean freeze;
	
	private int clickItemId;
	
	private boolean isTouched = false;
	private float tmpAngel = 0;
	
	protected ItemRotate<View> itemFront ;
	
	public RotateView(Context context, AttributeSet attrs)
	{
		super(context, attrs);
		initInstance();
	}

	/**
	 * 设置转盘的各项资源(itemSrcIconArray还没有实现)
	 * 
	 * @param itemCount
	 *            转盘的Item个数
	 * @param initAngel
	 *            初始旋转矫正角度
	 * @param bkgDrawableArray
	 *            Item背景：[正常背景，被点击背景]，注意，如果bkgDrawableArray.length小于itemCount，
	 *            item背景会被循环套用
	 * @param itemSrcIconArray
	 *            Item图标（还未实现）
	 * @param itemTitleArray
	 *            Item标题
	 *@param frontIcon
	 *            前置图标
	 */
	public void setRotateSource(int itemCount, float initAngel,
			int[] bkgDrawableArray, int[] itemSrcIconArray,
			CharSequence[] itemTitleArray, Paint paintTitle, View frontIcon)
	{
		currentAngel = initAngel;
		this.paintTitle = paintTitle;
		Resources res = getResources();
		if (itemFront == null) {
		    itemFront = new ItemRotate<View>();
		}
		itemFront.setItemSrc(frontIcon);
		itemFront.setItemWidth(frontIcon.getWidth());
		itemFront.setItemHeight(frontIcon.getHeight());
		itemList = initResource(res, itemCount, itemTitleArray, bkgDrawableArray,
				itemSrcIconArray);
		layoutFrontView(false);
	}
	
	public void setArea(Point size) {
	    if (centerPoint != null) {
	        centerPoint.set(size.x >> 1, size.y >> 1);
	    } else {
	        centerPoint = new PointF(size.x >> 1, size.y >> 1);
	    }
	}

	private void initInstance()
	{
		if (detectorMain == null)
			detectorMain = new GestureDetector(getContext(), new OnGestureListener() {
                @Override
                public boolean onSingleTapUp(MotionEvent e) {
                    if (onRotateItemClickListener != null)
                        onRotateItemClickListener.onItemClicked(clickItemId);
                    return false;
                }
                
                @Override
                public void onShowPress(MotionEvent e) {
                    
                }
                
                @Override
                public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
                    return false;
                }
                
                @Override
                public void onLongPress(MotionEvent e) {

                }
                
                @Override
                public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
                    return false;
                }
                
                @Override
                public boolean onDown(MotionEvent e) {
                    return false;
                }
            });
		if (centerPoint == null)
			centerPoint = new PointF();
		if (matrix == null)
			matrix = new Matrix();
	}

	@SuppressLint("NewApi")
    @SuppressWarnings("deprecation")
    private List<ItemRotate<Drawable>> initResource(Resources res, int itemCount, CharSequence[] titleArray,
			int[] bkgDrawableArray, int[] srcArray)
	{
		if (bkgDrawableArray == null/* || srcArray == null*/) {
			return null;
		}
		boolean hasTitleIcon = false;
		if (srcArray != null && srcArray.length == itemCount) {
		    hasTitleIcon = true;
		}
		List<ItemRotate<Drawable>> list = new ArrayList<ItemRotate<Drawable>>();
		double sumAngel = 0;
		for (int i = 0; i < itemCount; i++)
		{
			ItemRotate<Drawable> itemRotate = new ItemRotate<Drawable>();
			Drawable item = null;
            Drawable srcIcon = null;
            try {
			    if (Build.VERSION.SDK_INT >= 21) {
			        item = res.getDrawableForDensity(bkgDrawableArray[i % bkgDrawableArray.length], DisplayMetrics.DENSITY_HIGH, null);
	                if (hasTitleIcon) {
                        srcIcon = res.getDrawableForDensity(srcArray[i], DisplayMetrics.DENSITY_HIGH, null);
	                }
		    	} else {
			        item = res.getDrawableForDensity(bkgDrawableArray[i % bkgDrawableArray.length], DisplayMetrics.DENSITY_HIGH);
			        if (hasTitleIcon) {
                        srcIcon = res.getDrawableForDensity(srcArray[i], DisplayMetrics.DENSITY_HIGH);
			        }
		    	}
            } catch (NotFoundException e) {
//                e.printStackTrace();
            }
			item.setBounds(0, 0, (int) centerPoint.x, (int) (centerPoint.x * Math.sin(2 * Math.PI / itemCount)));
			if (srcIcon != null) {
			    srcIcon.setBounds(0, 0, srcIcon.getIntrinsicWidth(),srcIcon.getIntrinsicHeight());
			}
			itemRotate.setItemBackground(item);
			itemRotate.setItemSrc(srcIcon);
			if (titleArray != null) {
			    CharSequence cs = titleArray[i % titleArray.length];
				itemRotate.setItemTitle(cs);
				Rect rect = new Rect();  
	            paintTitle.getTextBounds(cs.toString(), 0, cs.length(), rect);  
	            itemRotate.fontWitdth = rect.width();  
	            itemRotate.fontHeight = rect.height();
			}
			itemRotate.setStartAngel(sumAngel);
			itemRotate.setCurrentStartAngel(sumAngel);
			itemRotate.setItemAngel(Math.toDegrees(Math.asin((double) item
					.getIntrinsicHeight() / (double) item.getIntrinsicWidth())));
			itemRotate.setId(i);
			sumAngel = sumAngel + itemRotate.getItemAngel();
			list.add(itemRotate);
		}
		return list;
	}

	/**
	 * 转盘Item监听器
	 * 
	 * @author Wilfredo.Liu
	 * 
	 */
	public interface OnRotateItemClickListener
	{
		/**
		 * 当Item被点击时
		 * 
		 * @param id
		 *            被点击的对应Item id，为初始化的Item顺序 ，第一个Item id是0，以此类推
		 */
		public abstract void onItemClicked(int id);
	}

	/**
	 * 设置转盘点击监听器
	 * 
	 * @param onRotateItemClickListener
	 */
	public void setOnRotateItemClickListener(
			OnRotateItemClickListener onRotateItemClickListener)
	{
		this.onRotateItemClickListener = onRotateItemClickListener;
	}

	@Override
	public void onWindowFocusChanged(boolean hasWindowFocus)
	{
		centerPoint.set(getWidth() >> 1, getHeight() >> 1);
		super.onWindowFocusChanged(hasWindowFocus);
	}

	@SuppressLint("ClickableViewAccessibility")
    @Override
	public boolean onTouchEvent(MotionEvent event)
	{
		currentTouch = getDegrees(event, centerPoint);
		if(itemFront != null && itemFront.itemSrc.getVisibility() == View.VISIBLE) {
            float distance  =(float) Math.sqrt(Math.pow(Math.abs(centerPoint.x - event.getX()), 2) + Math.pow(Math.abs(centerPoint.y - event.getY()), 2));
            if (distance < ((itemFront.itemWidth + itemFront.itemHeight) >> 1) >>1) {
                return true;
            }
        }
		switch (event.getAction() & MotionEvent.ACTION_MASK)
		{
			case MotionEvent.ACTION_DOWN:
			    if(itemFront != null && itemFront.itemSrc.getVisibility() == View.VISIBLE) {
		            float distance  =(float) Math.sqrt(Math.pow(Math.abs(centerPoint.x - event.getX()), 2) + Math.pow(Math.abs(centerPoint.y - event.getY()), 2));
		            if (distance >= ((itemFront.itemWidth + itemFront.itemHeight) >> 1) >>1) {
		                isTouched = true;
		            }
		        }
				// startTouchAngel = getDegrees(event, centerPoint);
				break;
			case MotionEvent.ACTION_MOVE:
			    if (freeze || !isTouched) {
			        break;
			    }
				if (tmpAngel == 0)
					tmpAngel = getDegrees(event, centerPoint);
				else
				{
					float tmpNext = getDegrees(event, centerPoint);
					currentAngel += tmpNext - tmpAngel;
					tmpAngel = tmpNext;
				}
				break;
			case MotionEvent.ACTION_UP:
				isTouched = false;
				tmpAngel = 0;
				break;
		}
		if (detectorMain != null)
			detectorMain.onTouchEvent(event);
		invalidate();
		return true;//super.onTouchEvent(event);
	}

	private float getDegrees(MotionEvent event, PointF centerPoint)
	{
		return centerPoint == null ? 0 : (float) Math.toDegrees(Math.atan2(
				event.getY() - centerPoint.y, event.getX() - centerPoint.x));
	}

	@Override
	protected synchronized void onDraw(Canvas canvas)
	{
		if (itemList != null)
		{
			for (ItemRotate<Drawable> itemRotate : itemList)
			{
			    Drawable drawableBkg = setItemPressed(itemRotate);
				matrix.reset();
				int state = canvas.save();
				matrix.postRotate((float) itemRotate.startAngel + currentAngel, centerPoint.x,
						centerPoint.y);
				canvas.setMatrix(matrix);
				drawableBkg.draw(canvas);
				float dDegrees;
				if (fontSelfRotate) {
				    dDegrees = - currentAngel;
				} else {
				    dDegrees = (float) (- 180F / itemList.size() + itemRotate.startAngel);
				}
				canvas.rotate(dDegrees - (float) itemRotate.startAngel, centerPoint.x / 2 + padding,
                        centerPoint.y / 2 + padding);
	            canvas.drawText(itemRotate.itemTitle, 0, itemRotate.itemTitle.length(), centerPoint.x / 2 - itemRotate.fontWitdth / 2 + padding,
	                    centerPoint.x / 2 + itemRotate.fontHeight / 2 + padding, paintTitle);
	            if (itemRotate.itemSrc != null) {
	                int stateIcon = canvas.save();
	                canvas.translate(centerPoint.x / 2 + itemRotate.fontWitdth / 2 + padding * (itemRotate.itemTitle.length() > 3 ? 0.82F : 1F), centerPoint.x / 2 - itemRotate.fontHeight / 2 + padding * 0.8F);
	                itemRotate.itemSrc.draw(canvas);
	                canvas.restoreToCount(stateIcon);
	            }
	            canvas.rotate((float) itemRotate.startAngel, centerPoint.x,
                        centerPoint.y);
	            canvas.restoreToCount(state);
			}
			matrix.reset();
		}
		if (itemFront != null && itemFront.itemSrc.getVisibility() == View.VISIBLE) {
            int state = canvas.save();
            matrix.postTranslate(centerPoint.x - (itemFront.getItemWidth() >> 1),centerPoint.y - (itemFront.getItemHeight() >> 1));
            canvas.setMatrix(matrix);
            itemFront.itemSrc.draw(canvas);
            canvas.restoreToCount(state);
        }
		super.onDraw(canvas);
	}

	private synchronized Drawable setItemPressed(ItemRotate<Drawable> itemRotate)
	{
	    Drawable drawable = itemRotate.itemBackground;
		double start = (itemRotate.getStartAngel()+currentAngel)%360;
		double end = (itemRotate.getStartAngel()+currentAngel + itemRotate.getItemAngel()) % 360;
		float touch = (currentTouch + 180) % 360;
		if(0 > start)
			start += 360;
		if (0 >= end)
			end += 360;
		if (0 == touch)
			touch = 360;
		if ((isTouched && start < end && touch > start && touch <= end)
				|| (isTouched && start > end && ((touch > start && touch <= 360) || (touch > 0 && touch <= end))))
		{
			clickItemId = itemRotate.getId();
			drawable.setState(PRESSED);
		} else {
		    drawable.setState(UNPRESSED);
		}
		return drawable;
	}

	public class ItemRotate<T>
	{
		private CharSequence itemTitle;
		private T itemSrc;
		private T itemBackground;
		private double startAngel;
		private double itemAngel;
		private int id;
		protected int itemHeight;
		protected int itemWidth;
		private double currentStartAngel;
		
		private float fontWitdth;
		private float fontHeight;
		
		public double getCurrentStartAngel()
		{
			return currentStartAngel;
		}

		public void setCurrentStartAngel(double currentStartAngel)
		{
			this.currentStartAngel = currentStartAngel;
		}

		public int getItemHeight()
		{
			return itemHeight;
		}

		public void setItemHeight(int itemHeight)
		{
			this.itemHeight = itemHeight;
		}

		public int getItemWidth()
		{
			return itemWidth;
		}

		public void setItemWidth(int itemWidth)
		{
			this.itemWidth = itemWidth;
		}

		public int getId()
		{
			return id;
		}

		public void setId(int id)
		{
			this.id = id;
		}

		public CharSequence getItemTitle()
		{
			return itemTitle;
		}

		public void setItemTitle(CharSequence itemTitle)
		{
			this.itemTitle = itemTitle;
		}

		public T getItemSrc()
		{
			return itemSrc;
		}

		public void setItemSrc(T itemSrc)
		{
			this.itemSrc = itemSrc;
		}

		public T getItemBackground()
		{
			return itemBackground;
		}

		public void setItemBackground(T itemBackground)
		{
			this.itemBackground = itemBackground;
		}

		public double getStartAngel()
		{
			return startAngel;
		}

		public void setStartAngel(double startAngel)
		{
			this.startAngel = startAngel;
		}

		public double getItemAngel()
		{
			return itemAngel;
		}

		public void setItemAngel(double itemAngel)
		{
			this.itemAngel = itemAngel;
		}
	}
	
	protected void layoutFrontView(boolean invalidate) {
        itemFront.itemSrc.layout(0, 0, itemFront.itemWidth,
                itemFront.itemHeight);
        invalidate();
    }

    @Override
    protected void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        layoutFrontView(true);
    }
    
    public void setFontSelfRotate(boolean selfRotate) {
        fontSelfRotate = selfRotate;
    }
    
    public void setRotateItemPadding(int padding) {
        this.padding =padding;
    }
    
    public void freezeRotate(boolean freeze) {
        this.freeze = freeze;
    }
}
