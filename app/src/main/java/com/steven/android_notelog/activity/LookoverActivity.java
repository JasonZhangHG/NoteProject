package com.steven.android_notelog.activity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.EditText;

import com.steven.android_notelog.R;
import com.steven.greendao.NoteEntity;
import com.steven.greendao.NoteEntityDao;

public class LookoverActivity extends BaseActivity {
    private EditText editText_lookover_title, editText_lookover_time,
            editText_lookover_content;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lookover);

        initView();

        loadData();
    }

    private void initView() {
        editText_lookover_title = (EditText) findViewById(R.id.editText_lookover_title);
        editText_lookover_time = (EditText) findViewById(R.id.editText_lookover_time);
        editText_lookover_content = (EditText) findViewById(R.id.editText_lookover_content);
    }

    private void loadData() {
        Intent intent = getIntent();
        Long noteId = intent.getLongExtra("noteId", -1);
        if (noteId != -1) {
            NoteEntity entity = mNoteDao.queryBuilder()
                    .where(NoteEntityDao.Properties.Id.eq(noteId))
                    .unique();
            editText_lookover_title.setText(entity.getTitle());
            editText_lookover_content.setText(entity.getContent());
            editText_lookover_time.setText(entity.getDate().toString());
        }
    }

}
