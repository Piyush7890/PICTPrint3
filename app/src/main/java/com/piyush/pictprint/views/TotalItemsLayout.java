package com.piyush.pictprint.views;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.piyush.pictprint.R;

public class TotalItemsLayout extends RelativeLayout {

    TextView total_text;
    TextView amount;
    TextView pages;

    public TotalItemsLayout(Context context) {
        super(context);
        init();
    }

    public TotalItemsLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();

    }

    public TotalItemsLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();

    }

    private void init()
    {
        View v = LayoutInflater.from(getContext())
                .inflate(R.layout.total_item_layout,
                        this,
                        true);
        total_text = v.findViewById(R.id.total_text);
        amount = v.findViewById(R.id.amount);
        pages = v.findViewById(R.id.pages);

    }

    public void setTotal_text(String total_text)
    {
        this.total_text.setText(total_text);
    }

    public void setAmount(int amount)
    {
        this.amount.setText(String.format("₹%s", String.valueOf(amount)));
    }

    public void setPages(int pages)
    {
        this.pages.setText(pages<=1?"1 page":String.valueOf(pages)+" pages");
    }
}
