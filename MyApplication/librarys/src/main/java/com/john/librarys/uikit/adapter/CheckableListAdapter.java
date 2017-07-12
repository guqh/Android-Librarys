package com.john.librarys.uikit.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.Checkable;

import java.util.List;

public abstract class CheckableListAdapter<T> extends DynamicListAdapter<T> {
    AbsListView mListView;


    public CheckableListAdapter(Context context, AbsListView listview) {
        this(context, null, listview);
    }

    public CheckableListAdapter(Context context, List<T> data, AbsListView listview) {
        super(context, data);
        mListView = listview;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        convertView = getCheckableView(position, convertView, parent);

        boolean checked = mListView.isItemChecked(position);
        if (convertView instanceof Checkable) {
            ((Checkable) convertView).setChecked(checked);
        }

        setChecked(convertView, checked);
        return convertView;
    }

    /**
     * 生成view 用，这个方法就是封装了 BaseAdapter.getView 方法，里面的实现需要跟 BaseAdapter.getView 一样
     *
     * @param position
     * @param convertView
     * @param parent
     * @return
     */
    public abstract View getCheckableView(int position, View convertView, ViewGroup parent);

    /**
     * 设置check状态用
     * 如果 itemview 是 checkable 的话 就会自动设置，如果不是就要另外设置，比如这个item 下 还layout了其他多层的checkable的view
     *
     * @param view
     * @param checked
     */
    public abstract void setChecked(View view, boolean checked);

    //这个值涉及到 listview.getCheckedItemIds()的取值
    @Override
    public boolean hasStableIds() {
        return true;
    }
}
