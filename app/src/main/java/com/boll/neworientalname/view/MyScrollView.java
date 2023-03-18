package com.boll.neworientalname.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.util.AttributeSet;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.StyleRes;
import androidx.core.widget.NestedScrollView;

import com.boll.neworientalname.R;

/**
 * created by zoro at 2023/3/13
 * 增加自定义style属性
 */
public class MyScrollView extends NestedScrollView {

    private Context mContext;

    public MyScrollView(@NonNull Context context) {
        super(context);
        mContext = context;
    }

    public MyScrollView(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public MyScrollView(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    /**
     * 描述：动态设置 style
     */
    private void setStyle(@StyleRes int styleId) {
        TypedArray array = mContext.obtainStyledAttributes(styleId, R.styleable.ScrollbarStyle);
        Drawable thumbScrollBar = array.getDrawable(R.styleable.ScrollbarStyle_mScrollbarThumbVertical);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            this.setVerticalScrollbarThumbDrawable(thumbScrollBar);
        }

        array.recycle();
    }

}
