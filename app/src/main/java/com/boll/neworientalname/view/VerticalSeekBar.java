package com.boll.neworientalname.view;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Canvas;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.SeekBar;

public class VerticalSeekBar extends androidx.appcompat.widget.AppCompatSeekBar {

    private StartAndStopListener startAndStopListener;

    public VerticalSeekBar(Context context) {
        super(context);
    }

    public VerticalSeekBar(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    public VerticalSeekBar(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    protected void onSizeChanged(int w, int h, int oldW, int oldH) {
        super.onSizeChanged(h, w, oldW, oldH);
    }

    @Override
    protected synchronized void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(heightMeasureSpec, widthMeasureSpec);
        setMeasuredDimension(getMeasuredHeight(), getMeasuredWidth());
    }

    protected void onDraw(Canvas c) {
        c.rotate(-90);
        c.translate(-getHeight(), 0);
        super.onDraw(c);
    }

    @Override
    public synchronized void setProgress(int progress) {
        super.setProgress(progress);
        onSizeChanged(getWidth(), getHeight(), 0, 0);
    }

    @SuppressLint("ClickableViewAccessibility")
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (!isEnabled()) {
            return false;
        }
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                if (startAndStopListener != null) {
                    startAndStopListener.startChange(this);
                }
            case MotionEvent.ACTION_MOVE:
                setProgress((int) (getMax() - (getMax() * event.getY() / getHeight())));
                break;
            case MotionEvent.ACTION_UP:
                if (startAndStopListener != null) {
                    startAndStopListener.stopChange(this,getProgress());
                }
                break;
            default:
                return super.onTouchEvent(event);
        }
        return true;
    }

    public void setStartAndStopListener(StartAndStopListener startAndStopListener) {
        this.startAndStopListener = startAndStopListener;
    }

    public interface StartAndStopListener {
        void startChange(VerticalSeekBar seekBar);
        void stopChange(VerticalSeekBar seekBar, int progress);
    }

}