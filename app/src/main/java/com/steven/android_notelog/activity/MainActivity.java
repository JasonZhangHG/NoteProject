package com.steven.android_notelog.activity;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.steven.android_notelog.R;
import com.steven.android_notelog.adapter.MyAdapter;
import com.steven.greendao.NoteEntity;
import com.steven.greendao.NoteEntityDao;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends BaseActivity {
    private Context mContext = this;
    private ListView listView_main;
    private TextView textView_empty;
    private MyAdapter mAdapter = null;

    private List<NoteEntity> mTotalList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initView();

        loadData();
    }

    private void initView() {
        textView_empty = (TextView) findViewById(R.id.textView_empty);
        listView_main = (ListView) findViewById(R.id.listView_main);

        mAdapter = new MyAdapter(mContext , mTotalList);
        listView_main.setAdapter(mAdapter);
        listView_main.setEmptyView(textView_empty);

        //设置ListView按键监听器
        listView_main.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                NoteEntity entity = (NoteEntity) adapterView.getItemAtPosition(i);
                Long _id = entity.getId();
                Intent intent = new Intent();
                //传递备忘录的noteId
                intent.putExtra("noteId", _id);
                intent.setClass(mContext, LookoverActivity.class);
                //查看备忘录
                startActivity(intent);
            }
        });

        //设置ListView长按监听器
        listView_main.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, int i, long l) {
                NoteEntity entity = (NoteEntity) adapterView.getItemAtPosition(i);
                final Long _id = entity.getId();
                String title = entity.getTitle();

                new AlertDialog.Builder(mContext)
                        .setTitle(title)
                        //设置弹出选项
                        .setItems(new String[]{"删除", "修改"},
                                new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        switch (which) {
                                            //删除
                                            case 0:
                                                mNoteDao.deleteByKey(_id);
                                                Toast.makeText(mContext, "删除成功",
                                                        Toast.LENGTH_SHORT).show();
                                                //重新加载数据
                                                loadData();
                                                break;
                                            //修改
                                            case 1:
                                                Intent intent = new Intent();
                                                intent.putExtra("noteId", _id);
                                                intent.setClass(mContext, AddActivity.class);
                                                //进入编辑页面
                                                startActivity(intent);
                                                break;
                                        }
                                    }
                                })
                        //显示对话框
                        .show();
                return true;
            }
        });
    }

    private void loadData() {
        List<NoteEntity> list = mNoteDao.queryBuilder()
                .orderDesc(NoteEntityDao.Properties.Id)
                .list();
        mAdapter.reloadListView(list , true);
    }

    public void clickView(View view) {
        switch (view.getId()) {
            case R.id.button_main_add:
                //显示进度条
                //进入添加页面
                Intent intent = new Intent();
                intent.setClass(mContext, AddActivity.class);
                startActivity(intent);
                break;
        }
    }


}
