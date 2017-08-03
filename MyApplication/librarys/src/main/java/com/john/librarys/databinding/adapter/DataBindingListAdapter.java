package com.john.librarys.databinding.adapter;


import android.content.Context;
import android.databinding.ViewDataBinding;
import android.view.View;
import android.view.ViewGroup;


import com.john.librarys.R;
import com.john.librarys.uikit.adapter.DynamicListAdapter;

import java.util.List;

/**
 * 数据绑定 listAdapter</br>
 * 配合 DataBinding使用
 * @param <T>
 */
public abstract class DataBindingListAdapter<T> extends DynamicListAdapter<T> {

    public DataBindingListAdapter(Context context) {
        super(context);
    }

    public DataBindingListAdapter(Context context, List<T> data) {
        super(context, data);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder = null;
        if(convertView == null){
            holder = onCreateViewHolder(position,parent);
            convertView = holder.getView();
            convertView.setTag(R.id.dataBindingViewHolder,holder);
        }else{
            holder = (ViewHolder)convertView.getTag(R.id.dataBindingViewHolder);
        }
        onBind(holder,position,holder.getBinding());
        return holder.getView();
    }

    /**
     * 创建viewholder
     * @param position
     * @param parent
     * @return
     */
    public abstract ViewHolder onCreateViewHolder(int position, ViewGroup parent);

    /**
     * 绑定数据
     * @param holder
     * @param position
     * @param binding
     */
    public abstract void onBind(ViewHolder holder,int position,ViewDataBinding binding);

    /**
     * 绑定用holder
     */
    public static class ViewHolder{
        private View mRootView;
        private ViewDataBinding mViewDataBinding;

        public ViewHolder(ViewDataBinding binding){
            mViewDataBinding = binding;
            mRootView = binding.getRoot();
        }

        public View getView(){
            return mRootView;
        }

        public ViewDataBinding getBinding() {
            return mViewDataBinding;
        }
    }
}
