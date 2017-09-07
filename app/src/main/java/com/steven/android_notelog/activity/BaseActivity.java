package com.steven.android_notelog.activity;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;

import com.steven.android_notelog.R;
import com.steven.android_notelog.util.BaseApplication;
import com.steven.greendao.NoteEntityDao;

public class BaseActivity extends AppCompatActivity {
    public Context mContext = this;
    public NoteEntityDao mNoteDao = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_base);

        mNoteDao = BaseApplication.newInstance().getDaoSession().getNoteEntityDao();
    }

    //新建菜单选项
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_add, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            //关于
            case R.id.action_about:
                new AlertDialog.Builder(mContext)
                        .setTitle("关于")
                        .setMessage("备忘录V1.0")
                        .setPositiveButton("确定", null)
                        .show();
                break;
            //设置闹铃铃声
            case R.id.action_alarm:
                Intent intent = new Intent();
                intent.setClass(mContext, SetAlarmMusicActivity.class);
                startActivity(intent);
                break;
            //退出
            case R.id.action_quit:
                new AlertDialog.Builder(mContext)
                        .setTitle("消息")
                        .setMessage("真的要退出吗？")
                        .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                BaseApplication.newInstance().onTerminate();
                            }
                        })
                        .setNegativeButton("取消", null)
                        //显示对话框
                        .show();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mNoteDao != null) {
            mNoteDao = null;
        }
        BaseApplication.newInstance().removeActivity(this);
    }
}
