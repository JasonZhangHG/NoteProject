package com.steven.android_notelog.activity;

import android.Manifest;
import android.app.Activity;
import android.content.ContentResolver;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Toast;

import com.steven.android_notelog.R;
import com.steven.android_notelog.util.BaseApplication;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SetAlarmMusicActivity extends BaseActivity {
    private static final String TAG = "SetAlarmMusicActivity";
    //显示音乐文件的列表
    private ListView listView_alarm;
    //音乐文件搜索路径
    private final static Uri EXTERNAL_AUDIO_URI = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
    private List<Map<String, String>> mTotalList = new ArrayList<Map<String, String>>();

    //设置动态权限相关属性
    private static final int REQUESTCODE_WRITE_EXTERNAL_STORAGE = 102;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setalarm);

        requestExternalStorageRuntimePermission(this);

        initView();
    }

    public void requestExternalStorageRuntimePermission(Activity activity) {
        int hasPermission = ContextCompat.checkSelfPermission(activity, Manifest
                .permission.READ_EXTERNAL_STORAGE);
        if (hasPermission != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(activity, new String[]{
                    Manifest.permission.READ_EXTERNAL_STORAGE}, REQUESTCODE_WRITE_EXTERNAL_STORAGE);
        } else {
            loadData();
        }
    }

    private void loadData() {
        ContentResolver resolver = getContentResolver();
        Cursor cursor = resolver.query(EXTERNAL_AUDIO_URI,
                new String[]{MediaStore.Audio.Media._ID, MediaStore.Audio.Media.DISPLAY_NAME,
                        MediaStore.Audio.Media.DATA}, null, null, null);
        mTotalList = cursorToList(cursor);
        //Log.i(TAG, "----->>>>>initData: " + totalList);
    }

    private void initView() {
        listView_alarm = (ListView) findViewById(R.id.listView_alarm);
        //显示音乐文件
        SimpleAdapter adapter = new SimpleAdapter(mContext, mTotalList,
                R.layout.item_listview_musicitems,
                new String[]{"displayname", "data"},
                new int[]{R.id.item_textView_musicname, R.id.item_textView_path});
        listView_alarm.setAdapter(adapter);

        //设置按键监听器
        listView_alarm.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Map<String, String> map = (Map<String, String>) adapterView.getItemAtPosition(i);
                //取得音乐文件名称
                final String displayname = map.get("displayname");
                final String data = map.get("data");
                //创建对话框
                new AlertDialog.Builder(mContext)
                        .setTitle("提示")
                        .setMessage("确定要将 " + displayname + " 设置为默认闹铃声吗？")
                        .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                //设置闹铃的路径
                                SharedPreferences prefs = mContext.getSharedPreferences
                                        ("alarm_music",MODE_PRIVATE);
                                SharedPreferences.Editor editor = prefs.edit();
                                editor.putString("path" , data);
                                editor.commit();

                                Toast.makeText(mContext, "设置成功", Toast.LENGTH_SHORT).show();
                                //关闭当前页面
                                finish();
                                BaseApplication.newInstance().removeActivity((Activity) mContext);
                            }
                        })
                        .setNegativeButton("取消", null)
                        //显示对话框
                        .show();
            }
        });
    }

    private List<Map<String, String>> cursorToList(Cursor cursor) {
        List<Map<String, String>> list = new ArrayList<Map<String, String>>();
        while (cursor.moveToNext()) {
            Map<String, String> map = new HashMap<String, String>();
            map.put("_id", cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media._ID)));
            map.put("displayname",
                    cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.DISPLAY_NAME)));
            String filePath = cursor.getString(cursor
                    .getColumnIndex(MediaStore.Audio.Media.DATA));
            map.put("data", filePath);
            list.add(map);
        }
        if (cursor != null) {
            cursor.close();
        }
        return list;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[]
            grantResults) {
        switch (requestCode) {
            case REQUESTCODE_WRITE_EXTERNAL_STORAGE: {
                if (grantResults.length == 1 && grantResults[0] == PackageManager
                        .PERMISSION_GRANTED) {
                    Toast.makeText(mContext, "权限申请OK！", Toast.LENGTH_SHORT).show();
                    //加载数据
                    loadData();
                } else {
                    Toast.makeText(mContext, "权限申请错误！", Toast.LENGTH_SHORT).show();
                }
                return;
            }
        }
    }


}
