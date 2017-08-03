package com.john.myapplication.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.john.librarys.databinding.adapter.ListRecyclerAdapter;
import com.john.librarys.uikit.widget.DraweeView;
import com.john.myapplication.R;
import com.zhy.autolayout.utils.AutoUtils;

/**
 * Created by guqh on 2017/8/3.
 * 普通 RecycleAdapter
 */

public class SimpleRecucleAdapter extends ListRecyclerAdapter<String> {

    public SimpleRecucleAdapter(Context context) {
        super(context);
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_databinding_2, parent, false);
        AutoUtils.autoSize(view);
        return new SimpleViewHolder(view);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        SimpleViewHolder mholder= (SimpleViewHolder) holder;
        mholder.dv.setUrl(getItem(position).toString());
    }

    class SimpleViewHolder extends RecyclerView.ViewHolder{
        public   DraweeView dv;

        public SimpleViewHolder(View itemView) {
            super(itemView);
            dv=itemView.findViewById(R.id.dv);
        }
    }
}
