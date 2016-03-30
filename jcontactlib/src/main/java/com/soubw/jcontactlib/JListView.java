package com.soubw.jcontactlib;

import android.content.Context;
import android.graphics.Canvas;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.DecelerateInterpolator;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Scroller;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by WX_JIN on 2016/3/10.
 */
public class JListView extends ListView implements OnScrollListener,JIndexBarFilter {

	private float mLastY = -1;

	private LinearLayout headerLayout;
	private LinearLayout footerLayout;

	private int mTotalItemCount;

	private Scroller mScroller;
	private OnScrollListener mScrollListener;
	private int mScrollBack;

	private final static int SCROLL_BACK_HEADER = 0;
	private final static int SCROLL_BACK_FOOTER = 1;
	private final static int SCROLL_DURATION = 400;
	private final static float OFFSET_RADIO = 1.8f;

	private JClearEditText jClearEditText;


	View mJIndexBarView;

	int mJIndexBarViewWidth;

	int mJIndexBarViewHeight;

	int mJIndexBarViewMargin;

	ArrayList<Integer> mListSections;

	boolean mJIndexBarVisibility=true;

	View mPreviewTextView;

	boolean mPreviewVisibility=false;
	int mPreviewTextViewWidth;
	int	mPreviewTextViewHeight;

	float mIndexBarY;

	public void setJIndexBarView(View indexBarView,ArrayList<Integer> mListSections) {
		mJIndexBarViewMargin = 10;
		this.mJIndexBarView = indexBarView;
		this.mListSections = mListSections;
	}


	public void setPreviewView(View previewTextView) {
		this.mPreviewTextView=previewTextView;
	}

	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		super.onMeasure(widthMeasureSpec, heightMeasureSpec);

		if (mJIndexBarView != null && mJIndexBarVisibility) {
			measureChild(mJIndexBarView, widthMeasureSpec, heightMeasureSpec);
			mJIndexBarViewWidth = mJIndexBarView.getMeasuredWidth();
			mJIndexBarViewHeight = mJIndexBarView.getMeasuredHeight();
		}

		if (mPreviewTextView != null && mPreviewVisibility) {
			measureChild(mPreviewTextView, widthMeasureSpec, heightMeasureSpec);
			mPreviewTextViewWidth = mPreviewTextView.getMeasuredWidth();
			mPreviewTextViewHeight = mPreviewTextView.getMeasuredHeight();
		}
	}
	@Override
	protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
		super.onLayout(changed, left, top, right, bottom);
		int tempJIndexViewHeight = getMeasuredHeight()*mListSections.size()/JIndexBarView.INDEX_MAX_HEIGHT;
		if (mJIndexBarView != null && mJIndexBarVisibility) {
			mJIndexBarView.layout(getMeasuredWidth()- mJIndexBarViewMargin - mJIndexBarViewWidth,(getMeasuredHeight()-tempJIndexViewHeight)/2
					, getMeasuredWidth()- mJIndexBarViewMargin, getMeasuredHeight()/2+tempJIndexViewHeight/2);
		}

		if (mPreviewTextView != null && mPreviewVisibility) {
			mPreviewTextView.layout(mJIndexBarView.getLeft()-mPreviewTextViewWidth, (int)mIndexBarY-(mPreviewTextViewHeight/2)
					, mJIndexBarView.getLeft(), (int)(mIndexBarY-(mPreviewTextViewHeight/2))+mPreviewTextViewHeight);
		}
	}


	public void setIndexBarVisibility(Boolean isVisible) {
		if(isVisible) {
			mJIndexBarVisibility=true;
		}
		else {
			mJIndexBarVisibility=false;
		}
	}


	private void setPreviewTextVisibility(Boolean isVisible) {
		if(isVisible) {
			mPreviewVisibility=true;
		}
		else {
			mPreviewVisibility=false;
		}
	}



	@Override
	protected void dispatchDraw(Canvas canvas) {
		super.dispatchDraw(canvas);

		if (mJIndexBarView != null && mJIndexBarVisibility) {
			drawChild(canvas, mJIndexBarView, getDrawingTime());
		}
		if (mPreviewTextView != null && mPreviewVisibility) {
			drawChild(canvas, mPreviewTextView, getDrawingTime());
		}
	}

	@Override
	public void filterList(float indexBarY, int position,String previewText) {
		this.mIndexBarY=indexBarY;

		if(mPreviewTextView instanceof TextView)
			((TextView)mPreviewTextView).setText(previewText);

		setSelection(position);
	}

	public JListView(Context context) {
		this(context,null);
	}

	public JListView(Context context, AttributeSet attrs) {
		this(context, attrs,0);
	}

	public JListView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		init(context);
	}

	private void init(Context context) {

		mScroller = new Scroller(context, new DecelerateInterpolator());
		super.setOnScrollListener(this);

		initHeader(context);

		initJClearEditText(context);

		initFooter(context);

	}

	private void initHeader(Context context){
		headerLayout = new LinearLayout(context);
		LayoutParams layoutParams = new LayoutParams(LayoutParams.MATCH_PARENT,LayoutParams.WRAP_CONTENT);
		headerLayout.setLayoutParams(layoutParams);
		addHeaderView(headerLayout);
	}

	private void initJClearEditText(Context context){
		LinearLayout editTextLayout = new LinearLayout(context);
		LayoutParams layoutParams = new LayoutParams(LayoutParams.MATCH_PARENT,LayoutParams.WRAP_CONTENT);
		editTextLayout.setLayoutParams(layoutParams);
		jClearEditText = new JClearEditText(context);
		editTextLayout.addView(jClearEditText);
		addHeaderView(editTextLayout);
	}

	private void initFooter(Context context){
		footerLayout = new LinearLayout(context);
		footerLayout.setVisibility(View.INVISIBLE);
		footerLayout.setClickable(false);
		LayoutParams layoutParams = new LayoutParams(LayoutParams.MATCH_PARENT,0);
		footerLayout.setLayoutParams(layoutParams);
		addFooterView(footerLayout);
		setFooterDividersEnabled(false);
	}

	private void setVisibleHeaderHeight(int height) {
		if (height < 0)
			height = 0;
		LayoutParams lp = (LayoutParams) headerLayout.getLayoutParams();
		lp.height = height;
		headerLayout.setLayoutParams(lp);
	}

	private int getVisibleHeaderHeight() {
		return headerLayout.getLayoutParams().height;
	}

	private void updateHeaderHeight(float delta) {
		setVisibleHeaderHeight((int) delta + getVisibleHeaderHeight());
		setSelection(0); // scroll to top each time
	}

	private void resetHeaderHeight() {
		int height = getVisibleHeaderHeight();
		if (height == 0)
			return;
		int finalHeight = 0;
		mScrollBack = SCROLL_BACK_HEADER;
		mScroller.startScroll(0, height, 0, finalHeight - height, SCROLL_DURATION);
		invalidate();
	}

	public void setJClearEditTextListener(JClearEditTextListener jClearEditTextListener){
		jClearEditText.setJClearEditTextListener(jClearEditTextListener);
	}
	private void setFooterHeight(int height) {
		if (height < 0) return ;
		LayoutParams lp = (LayoutParams)footerLayout.getLayoutParams();
		lp.height = height;
		footerLayout.setLayoutParams(lp);
	}

	private int getFooterHeight() {
		LayoutParams lp = (LayoutParams)footerLayout.getLayoutParams();
		return lp.height;
	}

	private void updateFooterHeight(float delta) {
		int height = getFooterHeight() + (int) delta;
		setFooterHeight(height);
	}

	private void resetFooterHeight() {
		int bottomMargin = getFooterHeight();
		if (bottomMargin > 0) {
			mScrollBack = SCROLL_BACK_FOOTER;
			mScroller.startScroll(0, bottomMargin, 0, -bottomMargin,
					SCROLL_DURATION);
			invalidate();
		}
	}

	public void setJClearEditTextFoucus(){
		jClearEditText.requestFocus();
	}

	@Override
	public boolean onTouchEvent(MotionEvent ev) {
		if (mJIndexBarView != null && ((JIndexBarView)mJIndexBarView).onTouchEvent(ev)) {
			setPreviewTextVisibility(true);
			return true;
		}
		else {
			setPreviewTextVisibility(false);

		}
		switch (ev.getAction()) {
			case MotionEvent.ACTION_DOWN:
				mLastY = ev.getRawY();
				break;
			case MotionEvent.ACTION_MOVE:
				final float deltaY = ev.getRawY() - mLastY;
				mLastY = ev.getRawY();
				if (getFirstVisiblePosition() == 0 &&  deltaY > 0) {
					updateHeaderHeight(deltaY / OFFSET_RADIO);
				} else if (getLastVisiblePosition() == mTotalItemCount - 1 && deltaY < 0) {
					updateFooterHeight(-deltaY / OFFSET_RADIO);
				}
				break;
			case MotionEvent.ACTION_UP:
				if (getFirstVisiblePosition() == 0) {
					resetHeaderHeight();
				} else if (getLastVisiblePosition() == mTotalItemCount - 1) {
					resetFooterHeight();
				}
			default:
				break;
		}

		return super.onTouchEvent(ev);
	}

	@Override
	public void computeScroll() {
		if (mScroller.computeScrollOffset()) {
			if (mScrollBack == SCROLL_BACK_HEADER) {
				setVisibleHeaderHeight(mScroller.getCurrY());
			} else {
				setFooterHeight(mScroller.getCurrY());
			}
			postInvalidate();
		}
		super.computeScroll();
	}

	@Override
	public void setOnScrollListener(OnScrollListener l) {
		mScrollListener = l;
	}

	@Override
	public void onScrollStateChanged(AbsListView view, int scrollState) {
		if (mScrollListener != null) {
			mScrollListener.onScrollStateChanged(view, scrollState);
		}
	}

	@Override
	public void onScroll(AbsListView view, int firstVisibleItem,
						 int visibleItemCount, int totalItemCount) {
		mTotalItemCount = totalItemCount;
		if (mScrollListener != null) {
			mScrollListener.onScroll(view, firstVisibleItem, visibleItemCount,
					totalItemCount);
		}
	}

	/**
	 * 设置清除监听
	 */
	public interface JClearEditTextListener{
		void requestRefreshAdapter(CharSequence s);
	}

}
