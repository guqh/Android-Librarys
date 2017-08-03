package com.john.librarys.databinding.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;


import com.john.librarys.uikit.adapter.DynamicListDataSet;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * 列表类型 adapter
 *
 * @param <T>
 */
public abstract class ListRecyclerAdapter<T> extends RecyclerView.Adapter implements DynamicListDataSet<T> {

    Context mContext;
    List<T> mItems = new ArrayList<T>();

    public void notifyDataSetChanged(List<T> data){
        this.mItems = data;
        super.notifyDataSetChanged();
    }

    public ListRecyclerAdapter(Context context) {
        super();
        mContext = context;
    }

    public ListRecyclerAdapter(Context context, List<T> items) {
        this(context);
        mItems.addAll(items);
    }

    @Override
    public T getItem(int position) {
        return mItems.get(position);
    }

    @Override
    public int getItemCount() {
        return mItems.size();
    }

    @Override
    public void addItem(T item) {
        mItems.add(item);
    }

    @Override
    public void addItem(T item, int position) {
        mItems.add(position, item);
    }

    @Override
    public void addItems(Collection<T> items) {
        if (items != null) {
            mItems.addAll(items);
        }
    }

    @Override
    public void removeItem(T item) {
        mItems.remove(item);
    }

    @Override
    public void removeItem(int position) {
        mItems.remove(position);
    }

    @Override
    public void removeItems(Collection<T> items) {
        if (items != null) {
            mItems.removeAll(items);
        }
    }

    public void clearItems() {
        mItems.clear();
    }

    @Override
    public int getCount() {
        return getItemCount();
    }

    @Override
    public void addItems(Collection<T> items, int position) {
        if (items != null) {
            mItems.addAll(position, items);
        }
    }

    @Override
    public void clear() {
        mItems.clear();
    }

    public Context getContext() {
        return mContext;
    }
}

