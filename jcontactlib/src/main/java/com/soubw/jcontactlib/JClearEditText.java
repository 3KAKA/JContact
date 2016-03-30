package com.soubw.jcontactlib;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnFocusChangeListener;
import android.widget.EditText;
import android.widget.LinearLayout;

/**
 * Created by WX_JIN on 2016/3/10.
 */
public class JClearEditText extends EditText implements OnFocusChangeListener, TextWatcher {

	private Drawable mClearDrawable;
	private Drawable mIconDrawable;
	private JListView.JClearEditTextListener jClearEditTextListener;

	public JClearEditText(Context context) {
		this(context, null);
	}


	public JClearEditText(Context context, AttributeSet attrs) {
		this(context, attrs, android.R.attr.editTextStyle);
	}

	public JClearEditText(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		init();
	}

	private void init() {
		mClearDrawable = getCompoundDrawables()[2];
		if (mClearDrawable == null) {
			mClearDrawable = getResources().getDrawable(R.drawable.jclearedittext_btn_bg);
		}
		mClearDrawable.setBounds(0, 0, mClearDrawable.getIntrinsicWidth()*2/3, mClearDrawable.getIntrinsicHeight()*2/3);
		mIconDrawable = getCompoundDrawables()[0];
		if(mIconDrawable == null){
			mIconDrawable = getResources().getDrawable(R.drawable.jclearedittext_icon);
		}
		mIconDrawable.setBounds(0, 0, mIconDrawable.getIntrinsicWidth()/2, mIconDrawable.getIntrinsicHeight()/2);
		setClearIconVisible(false);
		setOnFocusChangeListener(this);
		addTextChangedListener(this);
		setBackgroundResource(R.drawable.jclearedittext_bg);
		//按setBounds进行设置
		setCompoundDrawables(mIconDrawable,null,null,null);
		//setCompoundDrawablesWithIntrinsicBounds(mIconDrawable,null,null,null);
		LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,LinearLayout.LayoutParams.WRAP_CONTENT);
		lp.setMargins(20,10,20,10);
		setLayoutParams(lp);
		setHint(R.string.hint_notice_msg);

	}

	public void setJClearEditTextListener(JListView.JClearEditTextListener jClearEditTextListener){
		this.jClearEditTextListener = jClearEditTextListener;
	}

	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		super.onMeasure(widthMeasureSpec, heightMeasureSpec);
	}

	@Override
	protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
		super.onLayout(changed, left, top, right, bottom);
	}


	@Override
	public boolean onTouchEvent(MotionEvent event) {
		if (getCompoundDrawables()[2] != null) {
			if (event.getAction() == MotionEvent.ACTION_UP) {
				boolean touchable = event.getX() > (getWidth() - getPaddingRight() - mClearDrawable.getIntrinsicWidth()) && (event.getX() < ((getWidth() - getPaddingRight())));
				if (touchable) {
					this.setText("");
				}
			}
		}

		return super.onTouchEvent(event);
	}


	@Override
	public void onFocusChange(View v, boolean hasFocus) {
		if (hasFocus) {
			setClearIconVisible(getText().length() > 0);
		} else {
			if(getText().length() > 0){
				setClearIconVisible(getText().length() > 0);
			}else{
				setClearIconVisible(false);
			}
		}
	}


	protected void setClearIconVisible(boolean visible) {
		Drawable right = visible ? mClearDrawable : null;
		setCompoundDrawables(getCompoundDrawables()[0], getCompoundDrawables()[1], right, getCompoundDrawables()[3]);
	}


	@Override
	public void onTextChanged(CharSequence s, int start, int count, int after) {
		if (jClearEditTextListener != null) jClearEditTextListener.requestRefreshAdapter(s);
		setClearIconVisible(s.length() > 0);
	}

	@Override
	public void beforeTextChanged(CharSequence s, int start, int count, int after) {

	}

	@Override
	public void afterTextChanged(Editable s) {

	}

}
