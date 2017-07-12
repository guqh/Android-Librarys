package com.john.librarys.uikit.adapter;

import android.content.Context;
import android.widget.BaseAdapter;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;


/**
 * 通用动态列表Adapter
 *
 * @author chenzipeng
 */
public abstract class DynamicListAdapter<T> extends BaseAdapter implements DynamicListDataSet<T>{
    private Context context;
    protected List<T> data;

    public void notifyDataSetChanged(List<T> data){
        this.data = data;
        super.notifyDataSetChanged();
    }


    public DynamicListAdapter(Context context) {
        super();
        this.context = context;
        this.data = new ArrayList<T>();

    }

    public DynamicListAdapter(Context context, List<T> data) {
        super();
        this.context = context;
        this.data = data;
    }

    @Override
    public int getCount() {
        return data.size();
    }

    @Override
    public T getItem(int position) {
        return data.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    protected Context getContext() {
        return this.context;
    }

    /**
     * 获取数据
     *
     * @return
     */
    protected List<T> getData() {
        return data;
    }

    /**
     * 添加数据
     *
     * @param item
     */
    public void addItem(T item) {
        data.add(item);
    }

    /**
     * 添加数据到 position 位置
     *
     * @param item
     * @param position
     */
    public void addItem(T item, int position) {
        data.add(position, item);
    }

    /**
     * 添加数据到 position 位置
     *
     * @param items
     * @param position
     */
    public void addItems(Collection<T> items, int position) {
        if (items != null)
            data.addAll(position, items);
    }

    /**
     * 添加数据到 最后
     *
     * @param items
     */
    public void addItems(Collection<T> items) {
        if (items != null)
            data.addAll(items);
    }

    /**
     * 删除数据
     *
     * @param item
     */
    public void removeItem(T item) {
        data.remove(item);
    }

    /**
     * 删除 position位置的数据
     *
     * @param position
     */
    public void removeItem(int position) {
        data.remove(position);
    }

    /**
     * 删除数据
     *
     * @param items
     */
    public void removeItems(Collection<T> items) {
        data.removeAll(items);
    }

    /**
     * 清除数据
     */
    public void clear() {
        data.clear();
    }
}
