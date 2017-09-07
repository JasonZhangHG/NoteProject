package com.steven.android_notelog.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.steven.android_notelog.R;
import com.steven.android_notelog.helper.BaseAdapterHelper;
import com.steven.greendao.NoteEntity;

import java.util.List;

/**
 * Created by StevenWang on 16/6/5.
 */
public class MyAdapter extends BaseAdapterHelper<NoteEntity> {

    public MyAdapter(Context context, List<NoteEntity> list) {
        super(context, list);
    }

    @Override
    public View getItemView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder = null;
        if (convertView == null) {
            convertView = mInflater.inflate(R.layout.item_listview_main , parent ,false);
            viewHolder = new ViewHolder(convertView);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        viewHolder.textView_item_title.setText(mList.get(position).getTitle());
        viewHolder.textView_item_time.setText(mList.get(position).getDate().toString());
        return convertView;
    }

    class ViewHolder {
        private TextView textView_item_title;
        private TextView textView_item_time;

        public ViewHolder(View itemView) {
            textView_item_title = (TextView) itemView.findViewById(R.id.textView_item_title);
            textView_item_time = (TextView) itemView.findViewById(R.id.textView_item_time);
        }
    }

    /**
     * 重新加载适配器中的数据，刷新ListView的方法
     *
     * @param _list   适配器中重新加载的数据集合
     * @param isClear 是否将适配器中之前的数据进行清除
     */
    public void reloadListView(List<NoteEntity> _list, boolean isClear) {
        if (isClear) {
            mList.clear();
        }
        mList.addAll(_list);
        notifyDataSetChanged();
    }
}
