package com.soubw.jcontactlib;

import java.util.ArrayList;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.MotionEvent;
import android.view.View;


/**
 * Created by WX_JIN on 2016/3/10.
 */
public class JIndexBarView<T extends JContacts> extends View {


    public static final int INDEX_MAX_HEIGHT = 26;//以26个字母来平分导航条，显示所涉及字母的高度

    // indexBarMargin默认为10dp
    float mJIndexBarMargin = 10;

    // 默认字体大小为12sp
    private float mJIndexBarTextSize = 12;

    float mSideIndexY;

    boolean mIsIndexing = false;

    int mCurrentSectionPosition = -1;

    public ArrayList<Integer> mListSections;

    ArrayList<T> mListItems;
    
    Paint mIndexPaint;

    Context mContext;

    JIndexBarFilter mIndexBarFilter;

    
    public JIndexBarView(Context context) {
        super(context);
        this.mContext = context;
    }

    
    public JIndexBarView(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.mContext = context;
    }
    

    public JIndexBarView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        this.mContext = context;
    }
    

    public void setData(JListView listView, ArrayList<T> listItems,ArrayList<Integer> listSections) {
        this.mListItems = listItems;
        this.mListSections = listSections;

        mIndexBarFilter = listView;

        mJIndexBarMargin = (int) TypedValue.applyDimension(
                TypedValue.COMPLEX_UNIT_DIP, mJIndexBarMargin, mContext
                        .getResources().getDisplayMetrics());

        mIndexPaint = new Paint();
        mIndexPaint.setColor(Color.parseColor("#000000"));
        mIndexPaint.setAntiAlias(true);
        mIndexPaint.setTextSize((int) TypedValue.applyDimension(
                TypedValue.COMPLEX_UNIT_SP, mJIndexBarTextSize, mContext
                        .getResources().getDisplayMetrics()));
    }


    @Override
    protected void onDraw(Canvas canvas) {
        if (mListSections != null && mListSections.size() > 1) {
            float sectionHeight = (getMeasuredHeight()*mListSections.size()/INDEX_MAX_HEIGHT)/ mListSections.size();
            float paddingTop = (sectionHeight - (mIndexPaint.descent() - mIndexPaint.ascent())) / 2;

            for (int i = 0; i < mListSections.size(); i++) {
                float paddingLeft = (getMeasuredWidth() - mIndexPaint.measureText(getSectionText(mListSections.get(i)))) / 2;
                canvas.drawText(getSectionText(mListSections.get(i)),
                        paddingLeft,
                        mJIndexBarMargin + (sectionHeight * i) + paddingTop + mIndexPaint.descent(),
                        mIndexPaint);
            }
        }
        super.onDraw(canvas);
    }

    
    public String getSectionText(int sectionPosition) {
        return mListItems.get(sectionPosition).getjFirstWord();
    }

    
    boolean contains(float x, float y) {

        return (x >= getLeft() && y >= getTop() && y <= getTop() + getMeasuredHeight());
    }

    
    void filterListItem(float sideIndexY) {
        mSideIndexY = sideIndexY;
        mCurrentSectionPosition = (int) (((mSideIndexY) - getTop() - mJIndexBarMargin) /
                                    ((getMeasuredHeight()*mListSections.size()/INDEX_MAX_HEIGHT) / mListSections.size()));

        if (mCurrentSectionPosition >= 0 && mCurrentSectionPosition < mListSections.size()) {
            int position = mListSections.get(mCurrentSectionPosition);
            String previewText = mListItems.get(position).getjFirstWord();
            mIndexBarFilter.filterList(mSideIndexY, position+2, previewText);
        }
    }

    
    public boolean onTouchEvent(MotionEvent ev) {
        switch (ev.getAction()) {
            
            case MotionEvent.ACTION_DOWN:
                if (contains(ev.getX(), ev.getY())) {
                    mIsIndexing = true;
                    filterListItem(ev.getY());
                    return true;
                }
                else {
                    mCurrentSectionPosition = -1;
                    return false;
                }
            case MotionEvent.ACTION_MOVE:
                if (mIsIndexing) {

                    if (contains(ev.getX(), ev.getY())) {
                        filterListItem(ev.getY());
                        return true;
                    }
                    else {
                        mCurrentSectionPosition = -1;
                        return false;
                    }
                }
                break;
            case MotionEvent.ACTION_UP:
                if (mIsIndexing) {
                    mIsIndexing = false;
                    mCurrentSectionPosition = -1;
                }
                break;
        }
        return false;
    }
}
