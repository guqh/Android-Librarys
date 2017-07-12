package com.john.librarys.uikit.adapter;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 动态 viewpager 数据viewpager通用adapter
 *
 * @param <T>
 * @author chenzipeng
 */
public abstract class DynamicPagerAdapter<T> extends PagerAdapter implements DynamicListDataSet<T>{
    private Context mContext;
    private List<T> mData = new ArrayList<T>();
    private Map<Integer,View> mPositionViewMap = new HashMap<>();

    public DynamicPagerAdapter(Context context) {
        super();
        this.mContext = context;
    }

    public DynamicPagerAdapter(Context context, Collection<T> data) {
        super();
        this.mContext = context;
        this.mData.addAll(data);
    }

    @Override
    public int getCount() {
        return mData.size();
    }

    public T getItem(int position) {
        return mData.get(position);
    }

    protected Context getContext() {
        return this.mContext;
    }

    /**
     * 获取数据
     *
     * @return
     */
    protected List<T> getData() {
        return mData;
    }

    /**
     * 添加数据
     *
     * @param item
     */
    public void addItem(T item) {
        mData.add(item);
    }

    /**
     * 添加数据到 position 位置
     *
     * @param item
     * @param position
     */
    public void addItem(T item, int position) {
        mData.add(position, item);
    }

    /**
     * 添加数据到 position 位置
     *
     * @param items
     * @param position
     */
    public void addItems(Collection<T> items, int position) {
        if (items != null)
            mData.addAll(position, items);
    }

    /**
     * 添加数据到 最后
     *
     * @param items
     */
    public void addItems(Collection<T> items) {
        if (items != null)
            mData.addAll(items);
    }

    /**
     * 删除数据
     *
     * @param item
     */
    public void removeItem(T item) {
        mData.remove(item);
    }

    /**
     * 删除 position位置的数据
     *
     * @param position
     */
    public void removeItem(int position) {
        mData.remove(position);
    }

    /**
     * 删除数据
     *
     * @param items
     */
    public void removeItems(Collection<T> items) {
        mData.removeAll(items);
    }

    /**
     * 清除数据
     */
    public void clear() {
        mData.clear();
    }

    @Override
    public boolean isViewFromObject(View view, Object item) {
        return view == item;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        View v = mPositionViewMap.get(position);
        mPositionViewMap.remove(position);
        container.removeView(v);
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        View view = instantiateView(container, position);
        container.addView(view);
        mPositionViewMap.put(position,view);
        return view;
    }

    /**
     * 生成view
     * 这里不用 调用 container.addView();
     *
     * @param container
     * @param position
     * @return
     */
    public abstract View instantiateView(ViewGroup container, int position);

}
