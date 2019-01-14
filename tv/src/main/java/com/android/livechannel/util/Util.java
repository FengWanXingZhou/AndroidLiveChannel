package com.android.livechannel.util;

import android.util.Log;

import com.android.livechannel.BuildConfig;


/**
 * @Author: wj
 * @Date: 2018/1/26
 * @Description:
 **/
public class Util {
    private final static String TAG = "LiveChannel";
    private final static boolean DEBUG = BuildConfig.DEBUG;

    public static void LOG(String message){

        //if(DEBUG){
            Log.i(TAG," LiveChannel"+" || "+message);
        //}
    }


}
