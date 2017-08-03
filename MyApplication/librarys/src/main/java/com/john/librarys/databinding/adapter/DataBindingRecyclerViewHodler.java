package com.john.librarys.databinding.adapter;

import android.databinding.ViewDataBinding;
import android.support.v7.widget.RecyclerView;
import android.view.View;

/**
 * databinding recyclerViewhodler
 */
public class DataBindingRecyclerViewHodler extends RecyclerView.ViewHolder{
    private ViewDataBinding mViewDataBinding;

    public DataBindingRecyclerViewHodler(ViewDataBinding binding) {
        super(binding.getRoot());
        mViewDataBinding = binding;
    }

    public ViewDataBinding getBinding() {
        return mViewDataBinding;
    }

    public View getView(){
        return getBinding().getRoot();
    };
}
