package com.steven.quickindex.widget;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import com.steven.quickindex.utils.DensityUtil;

/**
 * 自定义字母索引
 */

public class LetterSideBarView extends View {

    private Context mContext;
    //画笔
    private Paint mPaint;
    private String[] mLetters = {"A", "B", "C", "D", "E", "F", "G", "H", "I", "J", "K", "L", "M", "N",
            "O", "P", "Q", "R", "S", "T", "U", "V", "W", "X", "Y", "Z"};
    //手指当前触摸的字母
    private String mTouchLetter;
    //手指是否触摸
    private boolean mCurrentIsTouch;
    // 设置触摸监听
    private SideBarTouchListener mTouchListener;

    public LetterSideBarView(Context context) {
        this(context, null);
    }


    public LetterSideBarView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public LetterSideBarView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.mContext = context;
        mPaint = new Paint();
        mPaint.setAntiAlias(true);
        mPaint.setTextSize(DensityUtil.sp2px(this.getContext(), 14));
        mPaint.setColor(Color.BLACK);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        //每一个字母的高度
        float singleHeight = ( float ) getHeight() / mLetters.length;
        //不断循环把绘制字母
        for (int i = 0; i < mLetters.length; i++) {
            String letter = mLetters[i];
            //获取字体的宽度
            Rect rect = new Rect();
            mPaint.getTextBounds(letter, 0, letter.length(), rect);
            float measureTextWidth = rect.width();
            //获取内容的宽度
            int contentWidth = getWidth() - getPaddingLeft() - getPaddingRight();
            float x = getPaddingLeft() + (contentWidth - measureTextWidth) / 2;
            //计算基线位置
            Paint.FontMetrics fontMetrics = mPaint.getFontMetrics();
            float baseLine = singleHeight / 2 + (singleHeight * i) +
                    (fontMetrics.bottom - fontMetrics.top) / 2 - fontMetrics.bottom;
            //画字母，后面onTouch的时候需要处理高亮
            if (mLetters[i].equals(mTouchLetter) && mCurrentIsTouch) {
                mPaint.setTextSize(DensityUtil.sp2px(mContext, 18));
                mPaint.setColor(Color.RED);
                canvas.drawText(letter, x, baseLine, mPaint);
            } else {
                mPaint.setTextSize(DensityUtil.sp2px(mContext, 14));
                mPaint.setColor(Color.BLACK);
                canvas.drawText(letter, x, baseLine, mPaint);
            }
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
            case MotionEvent.ACTION_MOVE:
                //计算出当前触摸的字母，获取当前的位置
                float currentMoveY = ( int ) event.getY();
                int itemHeight = (getHeight() - getPaddingTop() - getPaddingBottom()) / mLetters.length;
                //当前的位置=currentMoveY/字母的高度
                int currentPosition = ( int ) currentMoveY / itemHeight;
                if (currentPosition < 0) {
                    currentPosition = 0;
                }
                if (currentPosition > mLetters.length - 1) {
                    currentPosition = mLetters.length - 1;
                }
                mTouchLetter = mLetters[currentPosition];
                mCurrentIsTouch = true;
                if (mTouchListener != null) {
                    mTouchListener.onTouch(mTouchLetter, true);
                }
                break;
            case MotionEvent.ACTION_UP:
                mCurrentIsTouch = false;
                if (mTouchListener != null) {
                    mTouchListener.onTouch(mTouchLetter, false);
                }
                break;
        }
        invalidate();

        return true;
    }


    public void setOnSideBarTouchListener(SideBarTouchListener touchListener) {
        this.mTouchListener = touchListener;
    }

}
