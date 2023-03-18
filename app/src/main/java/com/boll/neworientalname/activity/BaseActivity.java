package com.boll.neworientalname.activity;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.pm.ActivityInfo;
import android.os.Build;
import android.os.Bundle;
import android.os.Looper;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.lifecycle.Lifecycle;
import androidx.lifecycle.LifecycleEventObserver;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.LifecycleRegistry;

import com.boll.neworientalname.utils.ActivityCollector;
import com.boll.neworientalname.utils.LogUtil;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

/**
 * Activity基类
 *
 * @author Created by zoro on 2020/10/8
 */
public abstract class BaseActivity extends Activity implements LifecycleOwner {

    private static final String TAG = "BaseActivity";

    private Context mContext;
    private static Toast toast;
    private LifecycleRegistry mRegistry;
    private String activityName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActivityCollector.addActivity(this);
        mContext = this;
        setStatus();
        setFullScreen(true);//全屏
        setHideVirtualKey(true);//隐藏底部导航栏
        //setRequestedOrientation(true);//竖屏设置
        setContentView(initLayout());
        initView();
        initData();
        mRegistry = new LifecycleRegistry(this);
        mRegistry.markState(Lifecycle.State.CREATED);
        activityName = getClass().getSimpleName();
        getLifecycle().addObserver(new LifecycleEventObserver() {
            @Override
            public void onStateChanged(@NonNull LifecycleOwner source, @NonNull Lifecycle.Event event) {
                LogUtil.d(TAG, "Activity === " + activityName + " event === " + event);
            }
        });
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        LogUtil.d(TAG, "Activity === " + activityName + " event === ON_RESTART");
    }

    @Override
    protected void onStart() {
        super.onStart();
        mRegistry.markState(Lifecycle.State.STARTED);
        mRegistry.handleLifecycleEvent(Lifecycle.Event.ON_START);
        EventBus.getDefault().register(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        mRegistry.markState(Lifecycle.State.RESUMED);
        mRegistry.handleLifecycleEvent(Lifecycle.Event.ON_RESUME);
    }

    @Override
    protected void onPause() {
        super.onPause();
        mRegistry.handleLifecycleEvent(Lifecycle.Event.ON_PAUSE);
    }

    @Override
    protected void onStop() {
        super.onStop();
        mRegistry.handleLifecycleEvent(Lifecycle.Event.ON_STOP);
        EventBus.getDefault().unregister(this);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessageEvent(Integer code) {
        Log.d(TAG, "===onMessageEvent===");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        ActivityCollector.removeActivity(this);
        mRegistry.markState(Lifecycle.State.DESTROYED);
        mRegistry.handleLifecycleEvent(Lifecycle.Event.ON_DESTROY);
    }

    @Override
    public Lifecycle getLifecycle() {
        return mRegistry;
    }

    /**
     * 设置各种状态
     */
    public abstract void setStatus();

    /**
     * 初始化布局
     * @return 布局id
     */
    public abstract int initLayout();

    /**
     * 初始化控件
     */
    public abstract void initView();

    /**
     * 初始化数据
     */
    public abstract void initData();

    /**
     * 设置是否隐藏Android底部的虚拟按键
     * @param isHide
     */
    public void setHideVirtualKey(boolean isHide){
        if (isHide){
            Window window = getWindow();
            WindowManager.LayoutParams params = window.getAttributes();
            params.systemUiVisibility = View.SYSTEM_UI_FLAG_HIDE_NAVIGATION;
            window.setAttributes(params);
        }
    }

    /**
     * 设置是否全屏
     * @param isFullScreen
     */
    public void setFullScreen(boolean isFullScreen){
        if (isFullScreen) {
            getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                    WindowManager.LayoutParams.FLAG_FULLSCREEN);
        }
    }

    /**
     * 设置状态栏颜色
     * @param color
     */
    public void setStatusColor(int color){
//        StatusBarUtil.setColor(this,color);
    }

    /**
     * 设置状态栏是否半透明
     * @param isTranslucent
     */
    public void setStatusTranslucent(boolean isTranslucent){
        if (isTranslucent){
//            StatusBarUtil.setTranslucent(this);
        }
    }

    /**
     * 设置状态栏是否全透明
     * @param isTransparent
     */
    public void setStatusTransparent(boolean isTransparent){
        if (isTransparent) {
//            StatusBarUtil.setTransparent(BaseActivity.this);
        }
    }

    /**
     * 横竖屏设置
     * @param isAllowScreenRoate
     */
    public void setRequestedOrientation(boolean isAllowScreenRoate){
        if (!isAllowScreenRoate) {
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        } else {
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        }
    }

    /**
     * 显示提示  toast
     * @param msg 提示信息
     */
    @SuppressLint("ShowToast")
    public void showToast(String msg) {
        try {
            if (null == toast) {
                toast = Toast.makeText(mContext, msg, Toast.LENGTH_SHORT);
            } else {
                toast.setText(msg);
            }
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    toast.show();
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
            //解决在子线程中调用Toast的异常情况处理
            Looper.prepare();
            Toast.makeText(mContext, msg, Toast.LENGTH_SHORT).show();
            Looper.loop();
        }
    }

    /**
     * 隐藏虚拟按键，并且全屏
     */
    protected void hideBottomUIMenu() {

        //隐藏虚拟按键，并且全屏
        if (Build.VERSION.SDK_INT > 11 && Build.VERSION.SDK_INT < 19) { // lower api
            View v = this.getWindow().getDecorView();
            v.setSystemUiVisibility(View.GONE);
        } else if (Build.VERSION.SDK_INT >= 19) {
            //for new api versions.
            View decorView = getWindow().getDecorView();
            int uiOptions = View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                    | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY | View.SYSTEM_UI_FLAG_FULLSCREEN;
            decorView.setSystemUiVisibility(uiOptions);
            Window window = getWindow();
            WindowManager.LayoutParams params = window.getAttributes();
            params.systemUiVisibility = View.SYSTEM_UI_FLAG_HIDE_NAVIGATION|View.SYSTEM_UI_FLAG_IMMERSIVE;
            window.setAttributes(params);
        }
    }

}