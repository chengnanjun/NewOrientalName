package com.boll.neworientalname.app;

import android.app.Application;
import android.content.Context;
import android.util.Log;

import com.github.hiteshsondhi88.libffmpeg.FFmpeg;
import com.github.hiteshsondhi88.libffmpeg.LoadBinaryResponseHandler;
import com.github.hiteshsondhi88.libffmpeg.exceptions.FFmpegNotSupportedException;

/**
 * created by zoro at 2023/3/13
 */
public class MyApplication extends Application {

    private static final String TAG = "MyApplication";
    public static Context mContext;
    @Override
    public void onCreate() {
        super.onCreate();
        initFFmpegBinary(this);
    }

    private void initFFmpegBinary(Context context) {
        try {
            FFmpeg.getInstance(context).loadBinary(new LoadBinaryResponseHandler() {
                @Override
                public void onFailure() {
                    Log.e(TAG, "initFFmpegBinary onFailure");
                }
            });

        } catch (FFmpegNotSupportedException e) {
            e.printStackTrace();
        }
    }

}
