package com.john.librarys.databinding.adapter;


import android.content.Context;
import android.databinding.ViewDataBinding;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.RecyclerView.ViewHolder;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

/**
 * RecyclerAdapter</br>
 * 配合 DataBinding使用
 *
 * @param <T>
 */
public abstract class DataBindingRecyclerAdapter<T> extends ListRecyclerAdapter<T> {

    public DataBindingRecyclerAdapter(Context context) {
        super(context);
    }

    public DataBindingRecyclerAdapter(Context context, List items) {
        super(context, items);
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        ViewHolder holder = onCreateDataBindingViewHolder(parent, viewType);
        return holder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        onBinding(holder, position);
    }

    /**
     * 数据绑定动作
     *
     * @param holder
     * @param position
     */
    protected abstract void onBinding(ViewHolder holder, int position);

    /**
     * 创建 databinding ViewHolder
     *
     * @param parent
     * @param viewType
     * @return
     */
    protected abstract ViewHolder onCreateDataBindingViewHolder(ViewGroup parent, int viewType);
}
