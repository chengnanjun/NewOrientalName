package com.boll.neworientalname.video;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.media.AudioManager;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.boll.neworientalname.R;
import com.boll.neworientalname.view.VerticalSeekBar;

import java.util.Timer;
import java.util.TimerTask;


/**
 * Created by Nathen
 * On 2016/04/18 16:15
 */
public class JzvdStd extends Jzvd {

    public static int LAST_GET_BATTERYLEVEL_PERCENT = 70;
    protected static Timer DISMISS_CONTROL_VIEW_TIMER;

    public ProgressBar loadingProgressBar;
    public ImageView thumbImageView,set_volume,ivSet_volume;;
    public TextView replayTextView;
    public PopupWindow clarityPopWindow;
    public TextView mRetryBtn;
    public LinearLayout mRetryLayout;
    protected DismissControlViewTimerTask mDismissControlViewTimerTask;
    protected Dialog mVolumeDialog;
    protected Dialog mBrightnessDialog;
//    private RelativeLayout rlBottom , rlBottomScreen;
    private LinearLayout llBottomTime;
    private OnClickNextOrFrontListener mOnClickNextOrFrontListener;
    private RelativeLayout volume_re,fu_volume_re;
    private VerticalSeekBar voiceSeekBarTop,fu_voiceSeekBarTop;
    private AudioManager audioManager;
    private int currentVolume;
    private int maxVolume;

    public JzvdStd(Context context) {
        super(context);
    }

    public JzvdStd(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public void setOnClickNextOrFrontListener(OnClickNextOrFrontListener mOnClickNextOrFrontListener) {
        this.mOnClickNextOrFrontListener = mOnClickNextOrFrontListener;
    }

    @Override
    public void init(Context context) {
        super.init(context);
        thumbImageView = findViewById(R.id.thumb);
        loadingProgressBar = findViewById(R.id.loading);
        replayTextView = findViewById(R.id.replay_text);
        mRetryBtn = findViewById(R.id.retry_btn);
        mRetryLayout = findViewById(R.id.retry_layout);
//        rlBottom = findViewById(R.id.rlBottom);
        llBottomTime = findViewById(R.id.llBottomTime);
//        rlBottomScreen = findViewById(R.id.rlBottomScreen);

        set_volume = findViewById(R.id.set_volume);
        ivSet_volume = findViewById(R.id.ivSet_volume);

        volume_re = findViewById(R.id.volume_re);
        fu_volume_re = findViewById(R.id.fu_volume_re);

        fu_voiceSeekBarTop = findViewById(R.id.fu_voiceSeekBarTop);
        voiceSeekBarTop = findViewById(R.id.voiceSeekBarTop);//声音


        //获取音量（AudioManager）
        audioManager = (AudioManager) context.getSystemService(context.AUDIO_SERVICE);
        //获取最大音量（媒体音量）
        maxVolume = audioManager.getStreamMaxVolume(AudioManager.STREAM_MUSIC);
        //获取当前音量
        currentVolume = audioManager.getStreamVolume(AudioManager.STREAM_MUSIC);
        if (currentVolume == 0){
            ivSet_volume.setImageResource(R.mipmap.ic_mute);
            set_volume.setImageResource(R.mipmap.ic_mute);
        }
        voiceSeekBarTop.setMax(maxVolume);
        fu_voiceSeekBarTop.setMax(maxVolume);
        voiceSeekBarTop.setProgress(currentVolume);
        fu_voiceSeekBarTop.setProgress(currentVolume);

        set_volume.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (fu_volume_re.getVisibility() == View.VISIBLE){
                    fu_volume_re.setVisibility(GONE);
                }else {
                    fu_volume_re.setVisibility(VISIBLE);
                }
            }
        });

        ivSet_volume.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (volume_re.getVisibility() == View.VISIBLE){
                    volume_re.setVisibility(GONE);
                }else {
                    volume_re.setVisibility(VISIBLE);
                }
            }
        });

        voiceSeekBarTop.setStartAndStopListener(new VerticalSeekBar.StartAndStopListener() {
            @Override
            public void startChange(VerticalSeekBar seekBar) {
                Log.e(TAG,"开始");
            }

            @Override
            public void stopChange(VerticalSeekBar seekBar, int progress) {
                audioManager.setStreamVolume(AudioManager.STREAM_MUSIC, progress, 0);
                currentVolume = progress;
                fu_voiceSeekBarTop.setProgress(currentVolume);
                Log.e(TAG,progress+"");
                if (progress == 0){
                    ivSet_volume.setImageResource(R.mipmap.ic_mute);
                    set_volume.setImageResource(R.mipmap.ic_mute);
                }else {
                    ivSet_volume.setImageResource(R.mipmap.set_volume);
                    set_volume.setImageResource(R.mipmap.set_volume);
                }

            }
        });

        fu_voiceSeekBarTop.setStartAndStopListener(new VerticalSeekBar.StartAndStopListener() {
            @Override
            public void startChange(VerticalSeekBar seekBar) {
                Log.e(TAG,"开始");
            }

            @Override
            public void stopChange(VerticalSeekBar seekBar, int progress) {
                audioManager.setStreamVolume(AudioManager.STREAM_MUSIC, progress, 0);
                currentVolume = progress;
                voiceSeekBarTop.setProgress(currentVolume);
                Log.e(TAG,progress+"");
                if (progress == 0){
                    ivSet_volume.setImageResource(R.mipmap.ic_mute);
                    set_volume.setImageResource(R.mipmap.ic_mute);
                }else {
                    ivSet_volume.setImageResource(R.mipmap.set_volume);
                    set_volume.setImageResource(R.mipmap.set_volume);
                }

            }
        });


        thumbImageView.setOnClickListener(this);
        mRetryBtn.setOnClickListener(this);
//        findViewById(R.id.ivPlayFront).setOnClickListener(this);
//        findViewById(R.id.ivPlayNext).setOnClickListener(this);
        findViewById(R.id.ivFullScreen).setOnClickListener(this);
//        findViewById(R.id.ivPlayFrontScreen).setOnClickListener(this);
//        findViewById(R.id.ivPlayNextScreen).setOnClickListener(this);
        findViewById(R.id.fullscreen).setOnClickListener(this);
    }

    public void setUp(JZDataSource jzDataSource, int screen, Class mediaInterfaceClass) {
        super.setUp(jzDataSource, screen, mediaInterfaceClass);
        setScreen(screen);
    }

    public void changeStartButtonSize(int size) {
        ViewGroup.LayoutParams lp = startButton.getLayoutParams();
//        lp.height = size;
//        lp.width = size;
        lp = loadingProgressBar.getLayoutParams();
        lp.height = size;
        lp.width = size;
    }

    @Override
    public int getLayoutId() {
        return R.layout.jz_layout_std;
    }

    @Override
    public void onStateNormal() {
        super.onStateNormal();
        changeUiToNormal();
    }

    @Override
    public void onStatePreparing() {
        super.onStatePreparing();
        changeUiToPreparing();
    }

    @Override
    public void onStatePlaying() {
        super.onStatePlaying();
        changeUiToPlayingClear();
    }

    @Override
    public void onStatePause() {
        super.onStatePause();
        changeUiToPauseShow();
        cancelDismissControlViewTimer();
    }

    @Override
    public void onStateError() {
        super.onStateError();
        changeUiToError();
    }

    @Override
    public void onStateAutoComplete() {
        super.onStateAutoComplete();
        changeUiToComplete();
        cancelDismissControlViewTimer();
    }

    @Override
    public void changeUrl(int urlMapIndex, long seekToInAdvance) {
        super.changeUrl(urlMapIndex, seekToInAdvance);
        startButton.setVisibility(INVISIBLE);
        replayTextView.setVisibility(View.GONE);
        mRetryLayout.setVisibility(View.GONE);
    }

    @Override
    public void changeUrl(JZDataSource jzDataSource, long seekToInAdvance) {
        super.changeUrl(jzDataSource, seekToInAdvance);
        startButton.setVisibility(INVISIBLE);
        replayTextView.setVisibility(View.GONE);
        mRetryLayout.setVisibility(View.GONE);
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        int id = v.getId();
        if (id == R.id.surface_container) {
            switch (event.getAction()) {
                case MotionEvent.ACTION_DOWN:
                    break;
                case MotionEvent.ACTION_MOVE:
                    break;
                case MotionEvent.ACTION_UP:
                    startDismissControlViewTimer();
                    if (!mChangePosition && !mChangeVolume) {
                        onClickUiToggle();
                    }
                    break;
            }
        } else if (id == R.id.bottom_seek_progress) {
            switch (event.getAction()) {
                case MotionEvent.ACTION_DOWN:
                    cancelDismissControlViewTimer();
                    break;
                case MotionEvent.ACTION_UP:
                    startDismissControlViewTimer();
                    break;
            }
        }
        return super.onTouch(v, event);
    }

    @Override
    public void onClick(View v) {
        super.onClick(v);
        int i = v.getId();
        if (i == R.id.thumb) {
            if (jzDataSource == null || jzDataSource.urlsMap.isEmpty() || jzDataSource.getCurrentUrl() == null) {
                Toast.makeText(getContext(), getResources().getString(R.string.no_url), Toast.LENGTH_SHORT).show();
                return;
            }
            if (state == STATE_NORMAL) {
//                if (!jzDataSource.getCurrentUrl().toString().startsWith("file") &&
//                        !jzDataSource.getCurrentUrl().toString().startsWith("/") &&
//                        !JZUtils.isWifiConnected(getContext()) && !WIFI_TIP_DIALOG_SHOWED) {
//                    showWifiDialog();
//                    return;
//                }
                startVideo();
            } else if (state == STATE_AUTO_COMPLETE) {
                onClickUiToggle();
            }
        } else if (i == R.id.surface_container) {
            startDismissControlViewTimer();
        } else if (i == R.id.back_tiny) {
            clearFloatScreen();
        } else if (i == R.id.retry_btn) {
            if (jzDataSource.urlsMap.isEmpty() || jzDataSource.getCurrentUrl() == null) {
                Toast.makeText(getContext(), getResources().getString(R.string.no_url), Toast.LENGTH_SHORT).show();
                return;
            }
//            if (!jzDataSource.getCurrentUrl().toString().startsWith("file") && !
//                    jzDataSource.getCurrentUrl().toString().startsWith("/") &&
//                    !JZUtils.isWifiConnected(getContext()) && !WIFI_TIP_DIALOG_SHOWED) {
//                showWifiDialog();
//                return;
//            }
//            addTextureView();
//            onStatePreparing();
            startVideo();
        }
//        else if(i == R.id.ivPlayFrontScreen || i == R.id.ivPlayFront){
//            if(mOnClickNextOrFrontListener != null){
//                mOnClickNextOrFrontListener.clickFront();
//            }
//        }else if(i == R.id.ivPlayNextScreen || i == R.id.ivPlayNext){
//            if(mOnClickNextOrFrontListener != null){
//                mOnClickNextOrFrontListener.clickNext();
//            }
//        }
        else if(i == R.id.ivFullScreen){
            Log.e(TAG , "click fullScreen");
            gotoScreenFullscreen();
        }
    }

    @Override
    public void setScreenNormal() {
        super.setScreenNormal();
        fullscreenButton.setVisibility(VISIBLE);
//        rlBottom.setVisibility(VISIBLE);
//        rlBottomScreen.setVisibility(GONE);
        llBottomTime.setVisibility(VISIBLE);
        bottomContainer.setVisibility(GONE);
//        fullscreenButton.setImageResource(R.drawable.jz_enlarge);
//        changeStartButtonSize((int) getResources().getDimension(R.dimen.jz_start_button_w_h_normal));
    }

    @Override
    public void setScreenFullscreen() {
        super.setScreenFullscreen();
        //进入全屏之后要保证原来的播放状态和ui状态不变，改变个别的ui
//        fullscreenButton.setImageResource(R.drawable.jz_shrink);
        fullscreenButton.setVisibility(VISIBLE);
//        rlBottom.setVisibility(GONE);
        llBottomTime.setVisibility(GONE);
//        rlBottomScreen.setVisibility(VISIBLE);
        bottomContainer.setVisibility(VISIBLE);
//        changeStartButtonSize((int) getResources().getDimension(R.dimen.jz_start_button_w_h_fullscreen));
//        setSystemTimeAndBattery();
    }

    @Override
    public void setScreenTiny() {
        super.setScreenTiny();
        setAllControlsVisiblity(View.INVISIBLE, View.INVISIBLE, View.INVISIBLE,
                View.INVISIBLE, View.INVISIBLE, View.INVISIBLE, View.INVISIBLE);
    }

    @Override
    public void showWifiDialog() {
        super.showWifiDialog();
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setMessage(getResources().getString(R.string.tips_not_wifi));
        builder.setPositiveButton(getResources().getString(R.string.tips_not_wifi_confirm), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                startVideo();
                WIFI_TIP_DIALOG_SHOWED = true;
            }
        });
        builder.setNegativeButton(getResources().getString(R.string.tips_not_wifi_cancel), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                clearFloatScreen();
            }
        });
        builder.setOnCancelListener(new DialogInterface.OnCancelListener() {
            @Override
            public void onCancel(DialogInterface dialog) {
                dialog.dismiss();
            }
        });
        builder.create().show();
    }

    @Override
    public void onStartTrackingTouch(SeekBar seekBar) {
        super.onStartTrackingTouch(seekBar);
        cancelDismissControlViewTimer();
    }

    @Override
    public void onStopTrackingTouch(SeekBar seekBar) {
        super.onStopTrackingTouch(seekBar);
        startDismissControlViewTimer();
    }

    public void onClickUiToggle() {//这是事件
        if (bottomContainer.getVisibility() != View.VISIBLE) {
            setSystemTimeAndBattery();
        }
        if (state == STATE_PREPARING) {
            changeUiToPreparing();
            if (bottomContainer.getVisibility() == View.VISIBLE) {
            } else {
                setSystemTimeAndBattery();
            }
        } else if (state == STATE_PLAYING) {
            if (bottomContainer.getVisibility() == View.VISIBLE) {
                changeUiToPlayingClear();
            } else {
                changeUiToPlayingShow();
            }
        } else if (state == STATE_PAUSE) {
            if (bottomContainer.getVisibility() == View.VISIBLE) {
                changeUiToPauseClear();
            } else {
                changeUiToPauseShow();
            }
        }
    }

    public void setSystemTimeAndBattery() {
//        SimpleDateFormat dateFormater = new SimpleDateFormat("HH:mm");
//        Date date = new Date();
//        videoCurrentTime.setText(dateFormater.format(date));
//        if ((System.currentTimeMillis() - LAST_GET_BATTERYLEVEL_TIME) > 30000) {
//            LAST_GET_BATTERYLEVEL_TIME = System.currentTimeMillis();
//            getContext().registerReceiver(
//                    battertReceiver,
//                    new IntentFilter(Intent.ACTION_BATTERY_CHANGED)
//            );
//        } else {
//            setBatteryLevel();
//        }
    }

    public void setBatteryLevel() {
//        int percent = LAST_GET_BATTERYLEVEL_PERCENT;
//        if (percent < 15) {
//            batteryLevel.setBackgroundResource(R.drawable.jz_battery_level_10);
//        } else if (percent >= 15 && percent < 40) {
//            batteryLevel.setBackgroundResource(R.drawable.jz_battery_level_30);
//        } else if (percent >= 40 && percent < 60) {
//            batteryLevel.setBackgroundResource(R.drawable.jz_battery_level_50);
//        } else if (percent >= 60 && percent < 80) {
//            batteryLevel.setBackgroundResource(R.drawable.jz_battery_level_70);
//        } else if (percent >= 80 && percent < 95) {
//            batteryLevel.setBackgroundResource(R.drawable.jz_battery_level_90);
//        } else if (percent >= 95 && percent <= 100) {
//            batteryLevel.setBackgroundResource(R.drawable.jz_battery_level_100);
//        }
    }

    public void onCLickUiToggleToClear() {
        if (state == STATE_PREPARING) {
            if (bottomContainer.getVisibility() == View.VISIBLE) {
                changeUiToPreparing();
            } else {
            }
        } else if (state == STATE_PLAYING) {
            if (bottomContainer.getVisibility() == View.VISIBLE) {
                changeUiToPlayingClear();
            } else {
            }
        } else if (state == STATE_PAUSE) {
            if (bottomContainer.getVisibility() == View.VISIBLE) {
                changeUiToPauseClear();
            } else {
            }
        } else if (state == STATE_AUTO_COMPLETE) {
            if (bottomContainer.getVisibility() == View.VISIBLE) {
                changeUiToComplete();
            } else {
            }
        }
    }


    @Override
    public void onProgress(int progress, long position, long duration) {
        super.onProgress(progress, position, duration);
    }

    @Override
    public void setBufferProgress(int bufferProgress) {
        super.setBufferProgress(bufferProgress);
    }

    @Override
    public void resetProgressAndTime() {
        super.resetProgressAndTime();
    }

    public void changeUiToNormal() {
        switch (screen) {
            case SCREEN_NORMAL:
                setAllControlsVisiblity(View.VISIBLE, View.INVISIBLE, View.VISIBLE,
                        View.INVISIBLE, View.VISIBLE, View.INVISIBLE, View.INVISIBLE);
                updateStartImage();
                break;
            case SCREEN_FULLSCREEN:
                setAllControlsVisiblity(View.VISIBLE, View.INVISIBLE, View.VISIBLE,
                        View.INVISIBLE, View.VISIBLE, View.INVISIBLE, View.INVISIBLE);
                updateStartImage();
                break;
            case SCREEN_TINY:
                break;
        }
    }

    public void changeUiToPreparing() {
        switch (screen) {
            case SCREEN_NORMAL:
            case SCREEN_FULLSCREEN:
                setAllControlsVisiblity(View.INVISIBLE, View.INVISIBLE, View.INVISIBLE,
                        View.VISIBLE, View.VISIBLE, View.INVISIBLE, View.INVISIBLE);
                updateStartImage();
                break;
            case SCREEN_TINY:
                break;
        }

    }

    public void changeUiToPlayingShow() {
        switch (screen) {
            case SCREEN_NORMAL:
                setAllControlsVisiblity(View.VISIBLE, View.VISIBLE, View.VISIBLE,
                        View.INVISIBLE, View.INVISIBLE, View.INVISIBLE, View.INVISIBLE);
                updateStartImage();
                break;
            case SCREEN_FULLSCREEN:
                setAllControlsVisiblity(View.VISIBLE, View.VISIBLE, View.VISIBLE,
                        View.INVISIBLE, View.INVISIBLE, View.INVISIBLE, View.INVISIBLE);
                updateStartImage();
                break;
            case SCREEN_TINY:
                break;
        }

    }

    public void changeUiToPlayingClear() {
        switch (screen) {
            case SCREEN_NORMAL:
                setAllControlsVisiblity(View.INVISIBLE, View.INVISIBLE, View.INVISIBLE,
                        View.INVISIBLE, View.INVISIBLE, View.VISIBLE, View.INVISIBLE);
                break;
            case SCREEN_FULLSCREEN:
                setAllControlsVisiblity(View.INVISIBLE, View.INVISIBLE, View.INVISIBLE,
                        View.INVISIBLE, View.INVISIBLE, View.VISIBLE, View.INVISIBLE);
                break;
            case SCREEN_TINY:
                break;
        }

    }

    public void changeUiToPauseShow() {
        switch (screen) {
            case SCREEN_NORMAL:
                setAllControlsVisiblity(View.VISIBLE, View.VISIBLE, View.VISIBLE,
                        View.INVISIBLE, View.INVISIBLE, View.INVISIBLE, View.INVISIBLE);
                updateStartImage();
                break;
            case SCREEN_FULLSCREEN:
                setAllControlsVisiblity(View.VISIBLE, View.VISIBLE, View.VISIBLE,
                        View.INVISIBLE, View.INVISIBLE, View.INVISIBLE, View.INVISIBLE);
                updateStartImage();
                break;
            case SCREEN_TINY:
                break;
        }
    }

    public void changeUiToPauseClear() {
        switch (screen) {
            case SCREEN_NORMAL:
                setAllControlsVisiblity(View.INVISIBLE, View.INVISIBLE, View.INVISIBLE,
                        View.INVISIBLE, View.INVISIBLE, View.VISIBLE, View.INVISIBLE);
                break;
            case SCREEN_FULLSCREEN:
                setAllControlsVisiblity(View.INVISIBLE, View.INVISIBLE, View.INVISIBLE,
                        View.INVISIBLE, View.INVISIBLE, View.VISIBLE, View.INVISIBLE);
                break;
            case SCREEN_TINY:
                break;
        }

    }

    public void changeUiToComplete() {
        switch (screen) {
            case SCREEN_NORMAL:
                setAllControlsVisiblity(View.VISIBLE, View.INVISIBLE, View.VISIBLE,
                        View.INVISIBLE, View.VISIBLE, View.INVISIBLE, View.INVISIBLE);
                updateStartImage();
                break;
            case SCREEN_FULLSCREEN:
                setAllControlsVisiblity(View.VISIBLE, View.INVISIBLE, View.VISIBLE,
                        View.INVISIBLE, View.VISIBLE, View.INVISIBLE, View.INVISIBLE);
                updateStartImage();
                break;
            case SCREEN_TINY:
                break;
        }

    }

    public void changeUiToError() {
        switch (screen) {
            case SCREEN_NORMAL:
                setAllControlsVisiblity(View.INVISIBLE, View.INVISIBLE, View.VISIBLE,
                        View.INVISIBLE, View.INVISIBLE, View.INVISIBLE, View.VISIBLE);
                updateStartImage();
                break;
            case SCREEN_FULLSCREEN:
                setAllControlsVisiblity(View.VISIBLE, View.INVISIBLE, View.VISIBLE,
                        View.INVISIBLE, View.INVISIBLE, View.INVISIBLE, View.VISIBLE);
                updateStartImage();
                break;
            case SCREEN_TINY:
                break;
        }

    }

    public void setAllControlsVisiblity(int topCon, int bottomCon, int startBtn, int loadingPro,
                                        int thumbImg, int bottomPro, int retryLayout) {
        startButton.setVisibility(startBtn);
        loadingProgressBar.setVisibility(loadingPro);
        thumbImageView.setVisibility(thumbImg);
        mRetryLayout.setVisibility(retryLayout);
    }

    public void updateStartImage() {
        Log.e(TAG , "updateStartImage : " + state);
        if (state == STATE_PLAYING) {
            startButton.setVisibility(VISIBLE);
            startButton.setImageResource(R.mipmap.ic_pause);
            ivPlayOrPause.setImageResource(R.mipmap.ic_pause_video);
            ivPlayOrPauseScreen.setImageResource(R.mipmap.ic_pause_video);
            replayTextView.setVisibility(GONE);
        } else if (state == STATE_ERROR) {
            startButton.setVisibility(INVISIBLE);
            replayTextView.setVisibility(GONE);
        } else if (state == STATE_AUTO_COMPLETE) {
            startButton.setVisibility(VISIBLE);
            startButton.setImageResource(R.drawable.jz_click_replay_selector);
            ivPlayOrPause.setImageResource(R.mipmap.ic_play_video);
            ivPlayOrPauseScreen.setImageResource(R.mipmap.ic_play_video);
            replayTextView.setVisibility(VISIBLE);
        }else if(state == STATE_PREPARING){
            startButton.setImageResource(R.mipmap.ic_pause);
            ivPlayOrPause.setImageResource(R.mipmap.ic_pause_video);
            ivPlayOrPauseScreen.setImageResource(R.mipmap.ic_pause_video);
            replayTextView.setVisibility(GONE);
        } else {
            startButton.setImageResource(R.mipmap.ic_play);
            ivPlayOrPause.setImageResource(R.mipmap.ic_play_video);
            ivPlayOrPauseScreen.setImageResource(R.mipmap.ic_play_video);
            replayTextView.setVisibility(GONE);
        }
    }

    @Override
    public void showProgressDialog(float deltaX, String seekTime, long seekTimePosition, String totalTime, long totalTimeDuration) {
        super.showProgressDialog(deltaX, seekTime, seekTimePosition, totalTime, totalTimeDuration);

//        mDialogSeekTime.setText(seekTime);
//        mDialogTotalTime.setText(" / " + totalTime);
//        mDialogProgressBar.setProgress(totalTimeDuration <= 0 ? 0 : (int) (seekTimePosition * 100 / totalTimeDuration));
//        if (deltaX > 0) {
//            mDialogIcon.setBackgroundResource(R.drawable.jz_forward_icon);
//        } else {
//            mDialogIcon.setBackgroundResource(R.drawable.jz_backward_icon);
//        }
//        onCLickUiToggleToClear();
    }

    @Override
    public void dismissProgressDialog() {
        super.dismissProgressDialog();
    }

    @Override
    public void showVolumeDialog(float deltaY, int volumePercent) {
        super.showVolumeDialog(deltaY, volumePercent);
    }

    @Override
    public void dismissVolumeDialog() {
        super.dismissVolumeDialog();
        if (mVolumeDialog != null) {
            mVolumeDialog.dismiss();
        }
    }

    @Override
    public void showBrightnessDialog(int brightnessPercent) {
        super.showBrightnessDialog(brightnessPercent);
    }

    @Override
    public void dismissBrightnessDialog() {
        super.dismissBrightnessDialog();
        if (mBrightnessDialog != null) {
            mBrightnessDialog.dismiss();
        }
    }

//    public Dialog createDialogWithView(View localView) {
//        Dialog dialog = new Dialog(getContext(), R.style.jz_style_dialog_progress);
//        dialog.setContentView(localView);
//        Window window = dialog.getWindow();
//        window.addFlags(Window.FEATURE_ACTION_BAR);
//        window.addFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL);
//        window.addFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
//        window.setLayout(-2, -2);
//        WindowManager.LayoutParams localLayoutParams = window.getAttributes();
//        localLayoutParams.gravity = Gravity.CENTER;
//        window.setAttributes(localLayoutParams);
//        return dialog;
//    }

    public void startDismissControlViewTimer() {
        cancelDismissControlViewTimer();
        DISMISS_CONTROL_VIEW_TIMER = new Timer();
        mDismissControlViewTimerTask = new DismissControlViewTimerTask();
        DISMISS_CONTROL_VIEW_TIMER.schedule(mDismissControlViewTimerTask, 2500);
    }

    public void cancelDismissControlViewTimer() {
        if (DISMISS_CONTROL_VIEW_TIMER != null) {
            DISMISS_CONTROL_VIEW_TIMER.cancel();
        }
        if (mDismissControlViewTimerTask != null) {
            mDismissControlViewTimerTask.cancel();
        }

    }

    @Override
    public void onAutoCompletion() {
        super.onAutoCompletion();
        cancelDismissControlViewTimer();
    }

    @Override
    public void reset() {
        super.reset();
        cancelDismissControlViewTimer();
        if (clarityPopWindow != null) {
            clarityPopWindow.dismiss();
        }
    }

    public void dissmissControlView() {
        if (state != STATE_NORMAL
                && state != STATE_ERROR
                && state != STATE_AUTO_COMPLETE) {
            post(new Runnable() {
                @Override
                public void run() {
                    startButton.setVisibility(View.INVISIBLE);
                    if (clarityPopWindow != null) {
                        clarityPopWindow.dismiss();
                    }
                }
            });
        }
    }

    public class DismissControlViewTimerTask extends TimerTask {

        @Override
        public void run() {
            dissmissControlView();
        }
    }

//    private BroadcastReceiver battertReceiver = new BroadcastReceiver() {
//        public void onReceive(Context context, Intent intent) {
//            String action = intent.getAction();
//            if (Intent.ACTION_BATTERY_CHANGED.equals(action)) {
//                int level = intent.getIntExtra("level", 0);
//                int scale = intent.getIntExtra("scale", 100);
//                int percent = level * 100 / scale;
//                LAST_GET_BATTERYLEVEL_PERCENT = percent;
//                setBatteryLevel();
//                try {
//                    getContext().unregisterReceiver(battertReceiver);
//                } catch (Exception e) {
//                    e.printStackTrace();
//                }
//            }
//    };


    public void onPause(){
        if(mediaInterface != null && mediaInterface.isPlaying()){
            mediaInterface.pause();
            onStatePause();
        }
    }

    public void onDestroy(){
        if(mediaInterface != null){
            mediaInterface.release();
        }
    }


    public interface OnClickNextOrFrontListener{
        void clickNext();
        void clickFront();
    }
}
