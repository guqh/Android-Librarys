package com.john.librarys.uikit.helper;

import com.john.librarys.pulltorefresh.PullToRefreshAdapterViewBase;
import com.john.librarys.pulltorefresh.PullToRefreshBase;
import com.john.librarys.uikit.adapter.DynamicListAdapter;

import java.util.HashSet;
import java.util.Set;

/**
 * 拉动刷新工具类，提供自动绑定 上拉下来刷新动作，最后一个item显示自动调用上拉刷新（无视 刷新控件的mode类型）
 *
 * @author chenzipeng
 */
public abstract class PullToRefreshHelper implements PullToRefreshBase.OnRefreshListener2,PullToRefreshBase.OnLastItemVisibleListener {
    protected PullToRefreshAdapterViewBase mRefreshView;
    protected DynamicListAdapter mAdapter;

    Set<PullToRefreshBase.OnRefreshListener2> mOnRefreshListenerSet = new HashSet<>();

    public PullToRefreshHelper() {

    }

    public PullToRefreshHelper(PullToRefreshAdapterViewBase refreshView, DynamicListAdapter adapter) {
        setUp(refreshView, adapter);
    }

    /**
     * 绑定
     *
     * @param refreshView
     * @param adapter
     */
    public void setUp(PullToRefreshAdapterViewBase refreshView, DynamicListAdapter adapter) {
        mRefreshView = refreshView;
        mAdapter = adapter;
        mRefreshView.setOnRefreshListener(this);
        mRefreshView.setOnLastItemVisibleListener(this);
        mRefreshView.setAdapter(mAdapter);
    }

    /**
     * 更新数据</br>
     * 这里实现具体的更新数据方法
     * 1.启动更新数据任务
     * 2.adapter设置数据
     * 3.refreshView.onRefreshComplate();
     *
     * @param refreshView
     * @param adapter
     * @param clear       是否需要清空数据
     */
    public abstract void updateData(final PullToRefreshBase refreshView, DynamicListAdapter adapter, final boolean clear);


    @Override
    public void onPullDownToRefresh(final PullToRefreshBase refreshView) {
        updateData(refreshView, mAdapter, true);

        for(PullToRefreshBase.OnRefreshListener2 listener:mOnRefreshListenerSet){
            listener.onPullDownToRefresh(refreshView);
        }
    }

    @Override
    public void onPullUpToRefresh(final PullToRefreshBase refreshView) {
        updateData(refreshView, mAdapter, false);

        for(PullToRefreshBase.OnRefreshListener2 listener:mOnRefreshListenerSet){
            listener.onPullUpToRefresh(refreshView);
        }
    }

    @Override
    public void onLastItemVisible() {
        //强制上拉刷新
        mRefreshView.setRefreshing(true, PullToRefreshBase.Mode.PULL_FROM_END);
    }


    public PullToRefreshAdapterViewBase getRefreshView() {
        return mRefreshView;
    }

    public DynamicListAdapter getAdapter() {
        return mAdapter;
    }

    /**
     * 增加额外的刷新监听器
     * @param onRefreshListener
     */
    public void addOnRefreshListener(PullToRefreshBase.OnRefreshListener2 onRefreshListener){
        mOnRefreshListenerSet.add(onRefreshListener);
    };

    /**
     * 伤处额外的刷新监听器
     * @param onRefreshListener
     */
    public void removeOnRefreshListener(PullToRefreshBase.OnRefreshListener2 onRefreshListener){
        mOnRefreshListenerSet.remove(onRefreshListener);
    };
}