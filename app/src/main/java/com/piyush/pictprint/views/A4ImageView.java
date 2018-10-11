package com.piyush.pictprint.views;

import android.content.Context;
import android.support.v7.widget.AppCompatImageView;
import android.util.AttributeSet;

public class A4ImageView  extends AppCompatImageView {
    public A4ImageView(Context context, AttributeSet attrs) {
        super(context, attrs);
        }

    public A4ImageView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, Math.round(widthMeasureSpec*297/210));
    }
}
