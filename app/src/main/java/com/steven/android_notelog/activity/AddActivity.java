package com.steven.android_notelog.activity;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.DatePickerDialog;
import android.app.PendingIntent;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TimePicker;
import android.widget.Toast;

import com.steven.android_notelog.R;
import com.steven.android_notelog.customview.NoteEditText;
import com.steven.android_notelog.receiver.AlarmNoteReceiver;
import com.steven.android_notelog.util.BaseApplication;
import com.steven.greendao.NoteEntity;
import com.steven.greendao.NoteEntityDao;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class AddActivity extends BaseActivity {
    //标题、内容和时间
    private EditText editText_add_title, editText_add_time;
    private NoteEditText noteEditText_add_content;
    //年月日时分秒，用于保存日历详细信息
    private int mYear, mMonth, mDay, mHours, mMinute, mSecond;
    private Calendar mCalendar;
    private AlarmManager mAlarmManager;
    //编辑模式标志
    private boolean mEdit = false;
    private Long noteId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add);

        initView();

        initData();
    }

    private void initData() {
        noteId = getIntent().getLongExtra("noteId", -1);
        //如果noteId值不为空，则进入编辑模式
        if (noteId != -1) {
            mEdit = true;
        } else {
            mEdit = false;
        }
        //数据库连接类
        //mDbHelper = new MySQLiteOpenHelper(mContext);

        //Log.i("Add", "----->>>>initData: " + mEdit);

        if (mEdit) {
            //通过noteId取得对应的信息
            NoteEntity entity = mNoteDao.queryBuilder()
                    .where(NoteEntityDao.Properties.Id.eq(noteId))
                    .unique();
            editText_add_title.setText(entity.getTitle());
            noteEditText_add_content.setText(entity.getContent());
            editText_add_time.setText(entity.getDate() + "");

            //设置文本颜色为灰色
            editText_add_time.setTextColor(Color.GRAY);
        } else {
            //设置默认闹钟为当前时间
            editText_add_time.setText(formatTime());
            //设置文本颜色为红色
            editText_add_time.setTextColor(Color.RED);
        }
    }

    private void initView() {
        editText_add_title = (EditText) findViewById(R.id.editText_add_title);
        editText_add_time = (EditText) findViewById(R.id.editText_add_time);
        noteEditText_add_content = (NoteEditText) findViewById(R.id.noteEditText_add_content);
        //实例化日历
        mCalendar = Calendar.getInstance();

        //为闹钟设置长按监听器，弹出日期选择界面
        editText_add_time.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                //取得日历信息中的年月日时分秒
                mYear = mCalendar.get(Calendar.YEAR);
                mMonth = mCalendar.get(Calendar.MONTH);
                mDay = mCalendar.get(Calendar.DAY_OF_MONTH);
                mHours = mCalendar.get(Calendar.HOUR);
                mMinute = mCalendar.get(Calendar.MINUTE);
                mSecond = mCalendar.get(Calendar.SECOND);
                //新建一个日期选择控件
                DatePickerDialog dDialog = new DatePickerDialog(mContext,
                        new DatePickerDialog.OnDateSetListener() {
                            //设置日期的时候触发
                            @Override
                            public void onDateSet(DatePicker view, int y,
                                                  int monthOfYear, int dayOfMonth) {
                                String[] time = {"", mHours + ":" + mMinute + ":" + mSecond};
                                try {
                                    //将日期和时间分割
                                    String[] time2 = editText_add_time.getText().toString().trim
                                            ().split(" ");
                                    //取得时间的信息保存到time[1]中
                                    if (time2.length == 2) {
                                        time[1] = time2[1];
                                    }
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                                String mo = "", da = "";
                                //将月份转换成两位数
                                if (monthOfYear < 9) {
                                    mo = "0" + (monthOfYear + 1);
                                } else {
                                    mo = monthOfYear + 1 + "";
                                }
                                //将天数转换成两位数
                                if (dayOfMonth < 10) {
                                    da = "0" + dayOfMonth;
                                } else {
                                    da = dayOfMonth + "";
                                }
                                //将设置的结果保存到etTime中
                                editText_add_time.setText(y + "-" + mo + "-" + da + " " + time[1]);
                            }
                        }, mYear, mMonth, mDay);
                dDialog.setTitle("设置日期");
                //显示日期控件
                dDialog.show();
                return true;
            }
        });

        //设置单击监听器，弹出时间选择界面
        editText_add_time.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //取得当前的年月日信息
                mYear = mCalendar.get(Calendar.YEAR);
                mMonth = mCalendar.get(Calendar.MONTH);
                mDay = mCalendar.get(Calendar.DAY_OF_MONTH);
                //注意这里不是HOUR,HOUR返回的是12制的时间格式
                mHours = mCalendar.get(Calendar.HOUR_OF_DAY);
                mMinute = mCalendar.get(Calendar.MINUTE);
                mSecond = mCalendar.get(Calendar.SECOND);
                //新建时间选择器
                TimePickerDialog tDialog = new TimePickerDialog(mContext,
                        new TimePickerDialog.OnTimeSetListener() {
                            @Override
                            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                                String[] time = {mYear + "-" + mMonth + "-" + mDay, ""};
                                try {
                                    //分割时间和日期
                                    time = editText_add_time.getText().toString().trim().split(" ");
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                                String ho = "", mi = "";
                                //设置小时
                                if (hourOfDay < 10) {
                                    ho = "0" + hourOfDay;
                                } else {
                                    ho = hourOfDay + "";
                                }
                                //设置分钟
                                if (minute < 10) {
                                    mi = "0" + minute;
                                } else {
                                    mi = minute + "";
                                }
                                //将设置的结果保存到etTime中
                                editText_add_time.setText(time[0] + " " + ho + ":" + mi);
                            }
                        }, mHours, mMinute, true);
                tDialog.setTitle("设置时间");
                //显示时间控件
                tDialog.show();
            }
        });
    }

    //按键判断
    /*@Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        //当按键是返回键时
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            new AlertDialog.Builder(mContext)
                    .setTitle("消息")
                    .setMessage("是否要保存？")
                    .setPositiveButton("保存", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            //保存备忘录
                            saveNote();
                        }
                    })
                    .setNegativeButton("不保存", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            Intent intent2 = new Intent();
                            intent2.setClass(AddActivity.this, MainActivity.class);
                            //回到主页面
                            startActivity(intent2);
                        }
                    })
                    //显示对话框
                    .show();
        }
        return super.onKeyDown(keyCode, event);
    }*/

    public void clickView(View view) {
        switch (view.getId()) {
            case R.id.button_add_save:
                //保存备忘录信息
                saveNote();
                break;
            case R.id.button_add_cacel:
                AddActivity.this.finish();
                break;
        }
    }

    //保存备忘录
    public void saveNote() {
        //取得输入的内容
        String title = editText_add_title.getText().toString().trim();
        String content = noteEditText_add_content.getText().toString().trim();
        String time = editText_add_time.getText().toString().trim();
        //内容和标题都不能为空
        if ("".equals(title) || "".equals(content)) {
            Toast.makeText(mContext, "名称和内容都不能为空", Toast.LENGTH_SHORT).show();
        } else {
            if (mEdit) {
                updateNote(title, content, time, noteId);
                Toast.makeText(mContext, "更新成功", Toast.LENGTH_SHORT).show();
            } else {
                long newid = insertNote(title, content, time);
                Toast.makeText(mContext, "添加成功" + newid, Toast.LENGTH_SHORT).show();
            }
            //分割日期和时间
            String[] t = editText_add_time.getText().toString().trim().split(" ");
            //分割日期
            String[] t1 = t[0].split("-");
            //分割时间
            String[] t2 = t[1].split(":");
            //实例化日历
            Calendar calendar = Calendar.getInstance();
            //设置日历为闹钟的时间
            calendar.set(Integer.parseInt(t1[0]), Integer.parseInt(t1[1]) - 1,
                    Integer.parseInt(t1[2]), Integer.parseInt(t2[0]),
                    Integer.parseInt(t2[1]));
            //mCalendar = Calendar.getInstance();
            //闹钟的时间应至少比现在多10s
            if (mCalendar.getTimeInMillis() + 1000 * 10 <= calendar.getTimeInMillis()) {
                String messageContent;
                //当内容字数大于20个字时，切掉一部分以‘...’代替，并存储在messageContent中
                if (content.length() > 20) {
                    messageContent = content.substring(0, 18) + "...";
                } else {
                    messageContent = content;
                }
                Intent intent = new Intent();
                intent.setClass(mContext, AlarmNoteReceiver.class);
                //传递标题和内容信息
                intent.putExtra("messageTitle", title);
                intent.putExtra("messageContent", messageContent);
                PendingIntent pIntent = PendingIntent.getBroadcast(this, 0, intent,
                        PendingIntent.FLAG_UPDATE_CURRENT);
                //获得闹钟服务
                mAlarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);
                //设置闹钟
                mAlarmManager.set(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), pIntent);
            }
            Intent intent = new Intent();
            intent.setClass(mContext, MainActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            //回到主目录
            startActivity(intent);
            finish();
            BaseApplication.newInstance().removeActivity((Activity) mContext);
        }
    }


    //更新文件
    public void updateNote(String title, String content, String time, Long noteId) {
        NoteEntity entity = new NoteEntity(noteId, title, time, content);
        mNoteDao.update(entity);
    }

    //添加文件
    public long insertNote(String title, String content, String time) {
        NoteEntity entity = new NoteEntity(null, title, time, content);
        return mNoteDao.insert(entity);
    }


    //返回当前的时间
    public String formatTime() {
        Date d = new Date();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String time = sdf.format(d);
        return time;
    }

}
