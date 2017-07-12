package com.john.librarys.uikit.fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.john.librarys.R;
import com.john.librarys.pulltorefresh.PullToRefreshAdapterViewBase;
import com.john.librarys.pulltorefresh.PullToRefreshBase;
import com.john.librarys.net.interf.ServiceTask;
import com.john.librarys.uikit.adapter.DynamicListAdapter;
import com.john.librarys.uikit.helper.PullToRefreshHelper;

import java.util.List;

/**
 * 下拉刷新fragment,适合那种一次过拿到所有列表数据的类型
 * <p/>
 * 必须 含有 R.id.pulltorefresh_view 的下拉刷新控件
 * <p/>
 * 需要实现 {@link #updateData(ServiceTask, boolean)} {@link #getServiceTask(PullToRefreshBase, DynamicListAdapter)} 即可，一般这里调用service 去获取分页数据
 * <p/>
 * <p/>
 * 提供自动加载功能，只需要 重写 {@link #isAutoLoad} 并 retrun true，即可
 *
 * @param <T>
 */
public abstract class PullToRefreshListFragment<T> extends BaseFragment implements ServiceTask.OnCompleteListener {
    protected PullToRefreshAdapterViewBase mPullToRefreshView;

    protected PullToRefreshHelper mPullToRefreshHelper = new PullToRefreshHelper() {
        @Override
        public void updateData(PullToRefreshBase refreshView, DynamicListAdapter adapter, boolean clear) {
            ServiceTask task = PullToRefreshListFragment.this.getServiceTask(refreshView, adapter);
            task.addOnCompleteListener(PullToRefreshListFragment.this);
            if (getLoadingDialog() != null) {
                getLoadingDialog().addServiceTask(task);
            }
            PullToRefreshListFragment.this.updateData(task, clear);
        }

        @Override
        public void onLastItemVisible() {
            if(isAutoLoadOnLastItemVisible()) {
                super.onLastItemVisible();
            }
        }
    };

    /**
     * 是否启用最后一个item显示后 调用刷新？
     * 一般这里 是false 由于 一般使用这个list fargmgnet 都是一次就能拿到列表数据的，
     * 没必要启用此项
     * @return
     */
    public boolean isAutoLoadOnLastItemVisible(){
        return false;
    }

    @Override
    protected View createView(LayoutInflater inflater, ViewGroup container) {
        View rootView = obtainView(inflater, container);
        //找R.id.pulltorefresh_view

        mPullToRefreshView = (PullToRefreshAdapterViewBase) rootView.findViewById(R.id.pulltorefresh_view);
        mPullToRefreshHelper.setUp(mPullToRefreshView, getAdapter());

        mRootView = rootView;//赋值给rootview，让autoLoad 能找到view
        autoLoad();

        return rootView;
    }


    @Override
    public void onComplete(int resultCode, Object data) {
        showContentContainer();//显示内容，这里是为了autoload 中 hideContentContainer对应的显示
    }

    /**
     * 获取 更新用的servicetask,
     * 这里不要做更新的操作
     * @param refreshView
     * @param adapter
     * @return
     */
    public abstract ServiceTask<List<T>> getServiceTask(PullToRefreshBase refreshView, DynamicListAdapter adapter);

    /**
     * 更新数据
     * @param task
     */
    public abstract void updateData(ServiceTask<List<T>> task, boolean clear);


    /**
     * 获取adapter
     *
     * @return
     */
    public abstract DynamicListAdapter<T> getAdapter();

    /**
     * 生成view
     *
     * @param inflater
     * @param container
     * @return
     */
    protected abstract View obtainView(LayoutInflater inflater, ViewGroup container);

    /**
     * 获取下拉刷新view
     *
     * @return
     */
    public PullToRefreshAdapterViewBase getPullToRefreshView() {
        return mPullToRefreshView;
    }

    /**
     * 获取pagehelper
     *
     * @return
     */
    public PullToRefreshHelper getPullToRefreshHelper() {
        return mPullToRefreshHelper;
    }

    /**
     * 自动加载
     */
    public void autoLoad() {
        if (!isAutoLoad()) {
            return;
        }
        showLoadingDialog();
        hideContentContainer();
        getPullToRefreshView().setRefreshing(false);
    }

    /**
     * 是否启用自动加载?
     *
     * @return
     */
    public boolean isAutoLoad() {
        return false;
    }


}
