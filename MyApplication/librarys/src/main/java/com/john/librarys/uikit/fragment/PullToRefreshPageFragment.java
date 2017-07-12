package com.john.librarys.uikit.fragment;


import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.john.librarys.R;
import com.john.librarys.pulltorefresh.PullToRefreshAdapterViewBase;
import com.john.librarys.net.interf.ServiceTask;
import com.john.librarys.uikit.adapter.DynamicListAdapter;
import com.john.librarys.uikit.entity.PageResult;
import com.john.librarys.uikit.helper.MessageHelper;
import com.john.librarys.uikit.helper.PullToRefreshPageHelper;

import java.util.Date;

/**
 * 下拉刷新fragment，提供分页功能的Fragment
 * <p/>
 * 必须 含有 R.id.pulltorefresh_view 的下拉刷新控件
 * 需要实现 {@link #startTask(Date, int, ServiceTask)} 即可，一般这里调用service 去获取分页数据
 * <p/>
 * 需要自定义错误的时候 可以重写 {@link #onTaskError(int, PageResult)} 来处理错误信息
 * <p/>
 * 处理最后一页时，可以重写{@link #onLastPage()}
 * <p/>
 * 提供自动加载功能，只需要 重写 {@link #isAutoLoad} 并 retrun true，即可
 *
 * @param <T> 具体modle 如User
 */
public abstract class PullToRefreshPageFragment<T> extends BaseFragment {
    protected PullToRefreshAdapterViewBase mPullToRefreshView;

    protected PullToRefreshPageHelper<T> mPageHelper = new PullToRefreshPageHelper<T>() {
        @Override
        public void startTask(Date timestamp, int pageNo, ServiceTask<PageResult<T>> task) {
            if (getLoadingDialog() != null) {
                getLoadingDialog().addServiceTask(task);
            }
            PullToRefreshPageFragment.this.startTask(timestamp, pageNo, task);
        }

        @Override
        public void onTaskError(int resultCode, PageResult<T> pageResult) {
            PullToRefreshPageFragment.this.onTaskError(resultCode, pageResult);
        }

        @Override
        public void onLastPage() {
            PullToRefreshPageFragment.this.onLastPage();
        }

        @Override
        public void postTaskResult(int resultCode, PageResult<T> pageResult) {
            PullToRefreshPageFragment.this.postTaskResult(resultCode, pageResult);
        }
    };

    @Override
    protected View createView(LayoutInflater inflater, ViewGroup container) {
        View rootView = obtainView(inflater, container);
        //找R.id.pulltorefresh_view

        mPullToRefreshView = (PullToRefreshAdapterViewBase) rootView.findViewById(R.id.pulltorefresh_view);
        mPageHelper.setUp(mPullToRefreshView, getAdapter());

        mRootView = rootView;//赋值给rootview，让autoLoad 能找到view
        autoLoad();

        return rootView;
    }

    /**
     * 生成view
     *
     * @param inflater
     * @param container
     * @return
     */
    protected abstract View obtainView(LayoutInflater inflater, ViewGroup container);

    /**
     * 获取adapter
     *
     * @return
     */
    public abstract DynamicListAdapter<T> getAdapter();

    /**
     * 启动任务
     *
     * @param timestamp
     * @param pageNo
     * @param task
     */
    public abstract void startTask(Date timestamp, int pageNo, ServiceTask<PageResult<T>> task);

    /**
     * 发生错误时
     *
     * @param resultCode
     * @param pageResult
     */
    protected void onTaskError(int resultCode, PageResult<T> pageResult) {
        //默认显示错误
        MessageHelper.getInstance(getActivity()).showErrorMessage(Integer.MAX_VALUE, resultCode, getView());
    }

    /**
     * 最后一页动作
     */
    protected void onLastPage() {
        //MessageHelper.getInstance().showMessage(R.string.already_last_page, getView());
    }

    /**
     * 结束加载后动作
     *
     * @param resultCode
     * @param pageResult
     */
    protected void postTaskResult(int resultCode, PageResult<T> pageResult) {
        //TODO 需要 使用 handler post 进行显示？由于listview 等加载数据需要时间
        showContentContainer();//显示内容，这里是为了autoload 中 hideContentContainer对应的显示
    }


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
    public PullToRefreshPageHelper getPullToRefreshPageHelper() {
        return mPageHelper;
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
