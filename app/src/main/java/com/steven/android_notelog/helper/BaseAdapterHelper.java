package com.steven.android_notelog.helper;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import java.util.List;

public abstract class BaseAdapterHelper<T> extends BaseAdapter {
    public Context mContext = null;
    public List<T> mList = null;
    public LayoutInflater mInflater = null;

    public BaseAdapterHelper(Context context, List<T> list) {
        this.mContext = context;
        this.mList = list;
        mInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        return mList.size();
    }

    @Override
    public T getItem(int position) {
        return mList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    public void reloadListView(List<T> data, boolean isClear) {
        if (isClear) {
            mList.clear();
        }
        mList.addAll(data);
        notifyDataSetChanged();
    }

    public void clearAll() {
        mList.clear();
        notifyDataSetChanged();
    }

    //根据list的position删除单条数据
    public void removeItem(int position) {
        mList.remove(position);
        notifyDataSetChanged();
    }

    //删除多条数据
    public void removeItems(List<T> _list) {
        mList.removeAll(_list);
        notifyDataSetChanged();
    }


    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        return getItemView(position, convertView, parent);
    }

    public abstract View getItemView(int position, View convertView, ViewGroup parent);

}
