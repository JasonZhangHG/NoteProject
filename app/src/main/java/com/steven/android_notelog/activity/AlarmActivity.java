package com.steven.android_notelog.activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.AssetFileDescriptor;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;

import com.steven.android_notelog.R;

import java.io.IOException;

public class AlarmActivity extends BaseActivity {
    //媒体播放器
    private MediaPlayer mMediaPlayer = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_alarm);

        SharedPreferences prefs = getSharedPreferences("alarm_music" , MODE_PRIVATE);
        String path = prefs.getString("path" , "");

        initMediaPlayer(path);
        //开始播放
        mMediaPlayer.start();

        Intent intent = getIntent();
        //获得标题
        String messageTitle = intent.getStringExtra("messageTitle");
        //获得内容
        String messageContent = intent.getStringExtra("messageContent");
        //新建对话框
        new AlertDialog.Builder(mContext)
                .setTitle(messageTitle)
                .setMessage(messageContent)
                .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        //关闭媒体播放器
                        mMediaPlayer.stop();
                        mMediaPlayer.release();
                        finish();
                    }
                })
                //显示对话框
                .show();
    }

    private void initMediaPlayer(String path) {
        //设置播放的音量
        AssetFileDescriptor fd = null;
        try {
            if (path != "") {
                //播放指定的音乐
                mMediaPlayer = MediaPlayer.create(mContext, Uri.parse(path));
            } else {
                if (mMediaPlayer == null) {
                    mMediaPlayer = new MediaPlayer();
                }
                //将mediaPlay原有的流文件进行释放
                fd = getAssets().openFd("Imagine.mp3");
                mMediaPlayer.setDataSource(fd.getFileDescriptor(), fd.getStartOffset(), fd
                        .getLength());
                mMediaPlayer.prepare();
            }
            mMediaPlayer.setVolume(400, 350);
            mMediaPlayer.reset();
            //设置循环
            mMediaPlayer.setLooping(true);

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (fd != null) {
                    fd.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        mMediaPlayer.setOnErrorListener(
                new MediaPlayer.OnErrorListener() {
                    @Override
                    public boolean onError(MediaPlayer mp, int what, int extra) {
                        mp.reset();
                        return false;
                    }
                }
        );
    }


}
