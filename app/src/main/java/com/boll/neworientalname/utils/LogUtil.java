package com.boll.neworientalname.utils;

import android.util.Log;

public class LogUtil {

    public static void i(String tag, String msg) {
        if (Const.DEBUG) {
            Log.i(tag, msg);
        }
    }

    public static void d(String tag, String msg) {
        if (Const.DEBUG) {
            Log.d(tag, msg);
        }
    }

    public static void w(String tag, String msg) {
        if (Const.DEBUG) {
            Log.w(tag, msg);
        }
    }

    public static void e(String tag, String msg) {
        if (Const.DEBUG) {
            Log.e(tag, msg);
        }
    }
}
