package com.boll.neworientalname.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;

import com.boll.neworientalname.R;

/**
 * created by zoro at 2023/3/9
 * 子项学习内容的标题
 */
public class ContentTitleView extends LinearLayout {

    private TextView tvContentTitle;

    public ContentTitleView(Context context) {
        super(context);
        initView(context, null);
    }

    public ContentTitleView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        initView(context, attrs);
    }

    public ContentTitleView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView(context, attrs);
    }

    private void initView(Context context, AttributeSet attrs) {
        LayoutInflater.from(context).inflate(R.layout.layout_content_title, this, true);
        tvContentTitle = findViewById(R.id.tv_content_title);
        TypedArray typedArray = context.obtainStyledAttributes(attrs,R.styleable.ContentTitleView);
        String title = typedArray.getString(R.styleable.ContentTitleView_title);
        tvContentTitle.setText(title);
    }

    public void setTitle(String title) {
        tvContentTitle.setText(title);
    }

}
