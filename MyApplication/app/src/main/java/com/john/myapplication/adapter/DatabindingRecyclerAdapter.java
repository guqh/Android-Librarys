package com.john.myapplication.adapter;

import android.content.Context;
import android.databinding.BindingAdapter;
import android.databinding.ViewDataBinding;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.john.librarys.databinding.adapter.DataBindingListAdapter;
import com.john.librarys.databinding.adapter.DataBindingRecyclerAdapter;
import com.john.librarys.databinding.adapter.DataBindingRecyclerViewHodler;
import com.john.myapplication.R;
import com.john.myapplication.databinding.ItemDatabindingBinding;
import com.zhy.autolayout.utils.AutoUtils;

import java.util.List;

/**
 * Created by guqh on 2017/8/2.
 *  databinding RecycleAdapter
 */

public class DatabindingRecyclerAdapter extends DataBindingRecyclerAdapter<String> {

    public DatabindingRecyclerAdapter(Context context) {
        super(context);
    }

    @Override
    protected void onBinding(DataBindingRecyclerViewHodler holder, int position, ViewDataBinding binding) {
        ItemDatabindingBinding databindingBinding= (ItemDatabindingBinding) binding;
        databindingBinding.setStr(getItem(position).toString());
    }

    @Override
    protected DataBindingRecyclerViewHodler onCreateDataBindingViewHolder(ViewGroup parent, int viewType) {
        ItemDatabindingBinding binding=ItemDatabindingBinding.inflate(LayoutInflater.from(getContext()), parent, false);
        DataBindingRecyclerViewHodler holder =new DataBindingRecyclerViewHodler(binding);
        return holder;
    }

}
