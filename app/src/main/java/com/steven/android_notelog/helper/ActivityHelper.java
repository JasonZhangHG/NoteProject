package com.steven.android_notelog.helper;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

//活动管理器
public class ActivityHelper {
    private static final String TAG = "ActivityHelper";
    //用静态变量存储实例
    private static ActivityHelper instance;
    private List<Activity> list;
    //默认铃声的地址
    private static Uri uri = Uri.parse("/storage/sdcard/Music/life.mp3");

    public static ActivityHelper getInstance() {
        if (instance == null)
            instance = new ActivityHelper();
        return instance;
    }

    //添加Activity进列表
    public void addActivity(Activity activity) {
        //如果列表为空则新建列表
        if (list == null)
            list = new ArrayList<Activity>();
        if (activity != null) {
            list.add(activity);
        }
    }

    //获得铃声的路径
    public static Uri getUri() {
        return uri;
    }

    //设置铃声
    public static void setUri(Uri uri) {
        ActivityHelper.uri = uri;
    }

    //退出所有程序
    public void exitAllProgress() {
        for (int i = 0; i < list.size(); i++) {
            Activity activity = list.get(i);
            activity.finish();
        }
    }

    //添加Activity进列表
    public void startActivity(Activity activity, Activity target , Class<?> cls) {
        //如果列表为空则新建列表
        if (list == null) {
            list = new ArrayList<Activity>();
        }
        if (target != null) {
            list.add(target);
        }
        Log.i(TAG, "----->>>>startActivity: " + list);
        Log.i(TAG, "----->>>>startActivity: " + list.size());
        Intent intent = new Intent();
        intent.setClass(activity, cls);
        //进入添加页面
        activity.startActivity(intent);
    }

}