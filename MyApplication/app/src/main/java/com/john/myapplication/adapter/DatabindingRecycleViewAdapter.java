package com.john.myapplication.adapter;

import android.content.Context;
import android.databinding.ViewDataBinding;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import com.john.librarys.databinding.adapter.DataBindingRecyclerAdapter;
import com.john.myapplication.BR;
import com.john.myapplication.databinding.ItemDatabindingBinding;
import com.zhy.autolayout.utils.AutoUtils;

/**
 * Created by guqh on 2017/8/2.
 *  databinding RecycleAdapter
 */

public class DatabindingRecycleViewAdapter extends DataBindingRecyclerAdapter<String> {

    public DatabindingRecycleViewAdapter(Context context) {
        super(context);
    }


    @Override
    protected RecyclerView.ViewHolder onCreateDataBindingViewHolder(ViewGroup parent, int viewType) {
        ItemDatabindingBinding binding = ItemDatabindingBinding.inflate(LayoutInflater.from(getContext()), parent, false);
        AutoUtils.autoSize(binding.getRoot()); //自动适配
        return new ViewHolder(binding);
    }
    @Override
    protected void onBinding(RecyclerView.ViewHolder holder, int position) {
        ViewHolder mHolder= (ViewHolder) holder;
        mHolder.bindData(getItem(position));
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        ViewDataBinding binding;
        public ViewHolder(ViewDataBinding binding) {
            super(binding.getRoot());
            this.binding=binding;
        }
        public void bindData(String item){
            binding.setVariable(BR.str,item);
        }
    }
}
