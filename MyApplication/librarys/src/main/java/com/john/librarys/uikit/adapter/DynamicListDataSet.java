package com.john.librarys.uikit.adapter;

import java.util.Collection;

/**
 * 动态dynamicList
 */
public interface DynamicListDataSet<T> {

    int getCount();

    T getItem(int position);

    /**
     * 添加数据
     *
     * @param item
     */
    void addItem(T item);

    /**
     * 添加数据到 position 位置
     *
     * @param item
     * @param position
     */
    void addItem(T item, int position);

    /**
     * 添加数据到 position 位置
     *
     * @param items
     * @param position
     */
    void addItems(Collection<T> items, int position);

    /**
     * 添加数据到 最后
     *
     * @param items
     */
    void addItems(Collection<T> items);

    /**
     * 删除数据
     *
     * @param item
     */
    void removeItem(T item);

    /**
     * 删除 position位置的数据
     *
     * @param position
     */
    void removeItem(int position);

    /**
     * 删除数据
     *
     * @param items
     */
    void removeItems(Collection<T> items);

    /**
     * 清除数据
     */
    void clear();

    /**
     * 通知已经修改
     */
    void notifyDataSetChanged();
}
