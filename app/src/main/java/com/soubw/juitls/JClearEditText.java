package com.soubw.juitls;

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

import com.soubw.jcontact.R;
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
		// 这里构造方法也很重要，不加自定义EditText的很多属性就失效了
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
		setCompoundDrawables(mIconDrawable,null,null,null);//按setBounds进行设置
		//setCompoundDrawablesWithIntrinsicBounds(mIconDrawable,null,null,null);按图片大小进行设置
		LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,LinearLayout.LayoutParams.WRAP_CONTENT);
		lp.setMargins(20,10,20,10);
		setLayoutParams(lp);
		setHint("请输入关键字");

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

	/**
	 * 因为我们不能直接给EditText设置点击事件，所以我们用记住我们按下的位置来模拟点击事件 当我们按下的位置 在 EditText的宽度 -
	 * 图标到控件右边的间距 - 图标的宽度 和 EditText的宽度 - 图标到控件右边的间距之间我们就算点击了图标，竖直方向没有考虑
	 */
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

	/**
	 * 当ClearEditText焦点发生变化的时候，判断里面字符串长度设置清除图标的显示与隐藏
	 */
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

	/**
	 * 设置清除图标的显示与隐藏，调用setCompoundDrawables为EditText绘制上去
	 * 
	 * @param visible
	 */
	protected void setClearIconVisible(boolean visible) {
		Drawable right = visible ? mClearDrawable : null;
		setCompoundDrawables(getCompoundDrawables()[0], getCompoundDrawables()[1], right, getCompoundDrawables()[3]);
	}

	/**
	 * 当输入框里面内容发生变化的时候回调的方法
	 */
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
