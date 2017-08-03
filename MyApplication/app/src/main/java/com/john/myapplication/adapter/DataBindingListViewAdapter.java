package com.john.myapplication.adapter;

import android.content.Context;
import android.databinding.ViewDataBinding;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import com.john.librarys.databinding.adapter.DataBindingListAdapter;
import com.john.myapplication.databinding.ItemListviewBinding;
import com.zhy.autolayout.utils.AutoUtils;

/**
 * Created by guqh on 2017/8/3.
 */

public class DataBindingListViewAdapter extends DataBindingListAdapter<String> {
    public DataBindingListViewAdapter(Context context) {
        super(context);
    }

    @Override
    public ViewHolder onCreateViewHolder(int position, ViewGroup parent) {
        ItemListviewBinding binding=ItemListviewBinding.inflate(LayoutInflater.from(getContext()),parent,false);
        AutoUtils.autoSize(binding.getRoot());
        return new ViewHolder(binding);
    }

    @Override
    public void onBind(ViewHolder holder, int position, ViewDataBinding binding) {
        ItemListviewBinding dataBinding= (ItemListviewBinding) binding;
        dataBinding.setData(getItem(position));
    }
}
