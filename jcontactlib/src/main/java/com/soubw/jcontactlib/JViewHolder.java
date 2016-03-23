package com.soubw.jcontactlib;

import android.content.Context;
import android.content.Intent;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;
import android.widget.ProgressBar;
import android.widget.TextView;


public class JViewHolder {
	
	private SparseArray<View> mViews;
	private int mPosition;
	private View mConvertView;
	
	public JViewHolder(Context context, ViewGroup parent, int layoutId, int position){
		this.mPosition = position;
		this.mViews = new SparseArray<>();
		mConvertView = LayoutInflater.from(context).inflate(layoutId, parent,false);
		mConvertView.setTag(this);
	}
	
	public static JViewHolder getViewHolder(Context context, View convertView,
											ViewGroup parent, int layoutId , int position){
		if(convertView == null)
			return new JViewHolder(context, parent, layoutId, position);
		return (JViewHolder) convertView.getTag();
	}
	
	public <T extends View> T getView(int viewId){
		View view = mViews.get(viewId);
		if(view == null){
			view = mConvertView.findViewById(viewId);
			mViews.put(viewId, view);
		}
		return(T)view;
	}
	
	public View getConvertView(){
		return mConvertView;
	}

	/**
	 * 设置文本内容
	 * @param viewId 控件Id
	 * @param text 内容
     * @return
     */
	public JViewHolder setText(int viewId, String text){
		TextView tv = getView(viewId);
		tv.setText(text);
		return this;
	}

	/**
	 * 设置图片
	 * @param viewId 控件Id
	 * @param res 图片Resource
	 * @return
	 */
	public JViewHolder setImageView(int viewId, int res){
		ImageView iv = getView(viewId);
		iv.setScaleType(ScaleType.FIT_XY);
		iv.setImageResource(res);
		return this;
	}

}
