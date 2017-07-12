package com.john.librarys.uikit.fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.john.librarys.R;
import com.john.librarys.pulltorefresh.PullToRefreshBase;
import com.john.librarys.utils.Constants;
import com.john.librarys.net.interf.ServiceTask;
import com.john.librarys.uikit.helper.MessageHelper;


/**
 * 自动加载 单体详细数据的fragment ，如果 加载一个用户详细信息
 * <strong>必须在 {@link #obtainView(LayoutInflater, ViewGroup)} } 时，生成的布局中 含有 R.id.pulltorefresh_view 的 PullToRefreshBase ,
 * 如 PullToRefreshFrameLayout、PullToRefreshScrollView</strong>
 * <i>可选 #obtainView 时，生成的布局中 含有 R.id.content_container 的Viewgroup，来包住，在loading中隐藏，loading后显示使用 </i>
 * <p/>
 * 1.{@link #startTask(ServiceTask)} 中使用service 启动servicetask
 * 2.实现 {@link #onTaskSuccess(int, Object)} 来处理成功后的数据
 * 3.（可选） 重写{@link #onTaskError(int)} 来处理失败后的错误显示
 *
 * @param <T> 数据 model
 */
public abstract class PullToRefreshDetailFragment<T> extends BaseFragment implements PullToRefreshBase.OnRefreshListener2, ServiceTask.OnCancelListener {

    protected PullToRefreshBase mPullToRefreshView;

    @Override
    protected View createView(LayoutInflater inflater, ViewGroup container) {
        View rootView = obtainView(inflater, container);

        //找R.id.pulltorefresh_view
        mPullToRefreshView = (PullToRefreshBase) rootView.findViewById(R.id.pulltorefresh_view);
        mPullToRefreshView.setOnRefreshListener(this);

        mRootView = rootView;//赋值给rootview，让autoLoad 能找到view

        if (isAutoLoad()) {
            autoLoad();
        }

        return rootView;
    }

    public PullToRefreshBase getPullToRefreshView() {
        return mPullToRefreshView;
    }

    private ServiceTask<T> startTask() {
        ServiceTask<T> task = new ServiceTask<T>() {
            @Override
            protected void onComplete(int resultCode, T data) {
                PullToRefreshDetailFragment.this.onComplete(resultCode, data);
            }
        };

        task.addOnCancelListener(this);

        startTask(task);
        return task;
    }

    @Override
    public void onPullDownToRefresh(PullToRefreshBase refreshView) {
        startTask();
    }

    @Override
    public void onPullUpToRefresh(PullToRefreshBase refreshView) {

    }

    @Override
    public void onCancel() {
        getPullToRefreshView().onRefreshComplete();
    }

    /**
     * 完成后的回调
     *
     * @param resultCode
     * @param data
     */
    protected void onComplete(int resultCode, T data) {
        if (resultCode == Constants.STATE_CODE_SUCCESS) {
            T model = null;
            if (data != null) {
                model = (T) data;
            }
            onTaskSuccess(resultCode, model);
        } else {
            onTaskError(resultCode);
        }

        getPullToRefreshView().onRefreshComplete();
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
     * 启动servicetask
     *
     * @param task
     */
    protected abstract void startTask(ServiceTask<T> task);

    /**
     * 成功
     *
     * @param resultCode
     * @param model
     */
    protected abstract void onTaskSuccess(int resultCode, T model);

    /**
     * 失败,可以通过重写此方法来提示错误
     *
     * @param resultCode
     */
    protected void onTaskError(int resultCode) {
        MessageHelper.getInstance(getActivity()).showErrorMessage(Integer.MAX_VALUE, resultCode, getView());;
    }

    //-------自动加载--------
    protected boolean isAutoLoad() {
        return false;
    }

    //自动加载 完成后 显示 content
    ServiceTask.OnCompleteListener<T> mAutoLoadCompleteListener = new ServiceTask.OnCompleteListener<T>() {

        @Override
        public void onComplete(int resultCode, T data) {
            if (resultCode == Constants.STATE_CODE_SUCCESS) {
                showContentContainer();
            }
        }
    };

    protected void autoLoad() {
        hideContentContainer();
        ServiceTask<T> task = startTask();
        showLoadingDialog(task);
        task.addOnCompleteListener(mAutoLoadCompleteListener);
    }

}
