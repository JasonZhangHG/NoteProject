package com.steven.android_notelog.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.steven.android_notelog.activity.AlarmActivity;

public class AlarmNoteReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        //获得标题
        String messageTitle = intent.getStringExtra("messageTitle");
        //获得内容
        String messageContent = intent.getStringExtra("messageContent");
        Intent myIntent = new Intent();
        myIntent.setClass(context, AlarmActivity.class);
        myIntent.putExtra("messageTitle", messageTitle);
        myIntent.putExtra("messageContent", messageContent);
        myIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        //调用Alarm
        context.startActivity(myIntent);
    }
}
