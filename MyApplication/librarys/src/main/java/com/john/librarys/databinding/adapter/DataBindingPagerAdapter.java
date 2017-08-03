package com.john.librarys.databinding.adapter;


import android.content.Context;
import android.databinding.ViewDataBinding;
import android.view.View;
import android.view.ViewGroup;


import com.john.librarys.uikit.adapter.DynamicPagerAdapter;

import java.util.Collection;

/**
 * 数据绑定 listAdapter</br>
 * 配合 DataBinding使用
 *
 * @param <T>
 */
public abstract class DataBindingPagerAdapter<T> extends DynamicPagerAdapter<T> {


    public DataBindingPagerAdapter(Context context) {
        super(context);
    }

    public DataBindingPagerAdapter(Context context, Collection<T> data) {
        super(context, data);
    }

    @Override
    public View instantiateView(ViewGroup container, int position) {
        ViewHolder viewHolder = onCreateViewHolder(position, container);
        onBind(viewHolder, position, viewHolder.getBinding());
        return viewHolder.getView();
    }

    /**
     * 创建viewholder
     * <i>此方法内生成viewhoder 生成view的时候 不要 把view 附加到 parent中</i>
     *
     * @param position
     * @param parent
     * @return
     */
    public abstract ViewHolder onCreateViewHolder(int position, ViewGroup parent);

    /**
     * 绑定数据
     *
     * @param holder
     * @param position
     * @param binding
     */
    public abstract void onBind(ViewHolder holder, int position, ViewDataBinding binding);

    /**
     * 绑定用holder
     */
    public static class ViewHolder {
        private View mRootView;
        private ViewDataBinding mViewDataBinding;

        public ViewHolder(ViewDataBinding binding) {
            mViewDataBinding = binding;
            mRootView = binding.getRoot();
        }

        public View getView() {
            return mRootView;
        }

        public ViewDataBinding getBinding() {
            return mViewDataBinding;
        }
    }

}
