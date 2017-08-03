package com.john.librarys.databinding.adapter;


import android.content.Context;
import android.databinding.ViewDataBinding;
import android.support.v7.widget.RecyclerView;
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
    public DataBindingRecyclerViewHodler onCreateViewHolder(ViewGroup parent, int viewType) {
        DataBindingRecyclerViewHodler holder = onCreateDataBindingViewHolder(parent, viewType);
        //自动加入到parent
        View view = holder.getView();
        if (view != null) {
            if (view.getParent() != null) {
                ViewGroup parentView = (ViewGroup) view.getParent();
                parentView.removeView(view);
            }
            parent.addView(view);
        }

        return holder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof DataBindingRecyclerViewHodler) {
            DataBindingRecyclerViewHodler dataBindingHolder = (DataBindingRecyclerViewHodler) holder;
            onBinding(dataBindingHolder, position, dataBindingHolder.getBinding());
        } else {
            throw new IllegalArgumentException("DataBindingRecyclerAdapter holder must instanceof DataBindingRecyclerAdapter.ViewHolder!");
        }
    }

    /**
     * 数据绑定动作
     *
     * @param holder
     * @param position
     */
    protected abstract void onBinding(DataBindingRecyclerViewHodler holder, int position, ViewDataBinding binding);

    /**
     * 创建 databinding ViewHolder
     *
     * @param parent
     * @param viewType
     * @return
     */
    protected abstract DataBindingRecyclerViewHodler onCreateDataBindingViewHolder(ViewGroup parent, int viewType);


}
