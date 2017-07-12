package com.john.librarys.uikit.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.jaeger.library.StatusBarUtil;
import com.john.librarys.R;
import com.john.librarys.net.interf.ServiceTask;
import com.john.librarys.uikit.dialog.LoadingDialogFragment;

import de.greenrobot.event.EventBus;

/**
 * 基础Fragment
 * 提供不用每次fragment onCreateView 复用View
 * <p/>
 * <i>注意</i>如果你的fragment 需要重复多次被调用，把绑定操作(ButterKnife.bind)放到
 *
 * @see #createView(LayoutInflater inflater, ViewGroup container) 然后不用去释放，或者自己
 * 控制释放的时机
 */
public abstract class BaseFragment extends Fragment {
    protected View mRootView;
    private EventBus mEventBus = EventBus.getDefault();

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        registerEventBus();
//        setStatusBar();
    }
    protected void setStatusBar() {
        StatusBarUtil.setColor(getActivity(), getResources().getColor(R.color.primary));
    }

    public EventBus getEventBus() {
        return mEventBus;
    }

    protected boolean isUseEventBus() {
        return false;
    }

    /**
     * 注册EventBus
     */
    protected void registerEventBus() {
        if (isUseEventBus()) {
            mEventBus.register(this);
        }
    }

    /**
     * 解除注册
     */
    protected void unregisterEventBus() {
        if (isUseEventBus()) {
            mEventBus.unregister(this);
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        unregisterEventBus();
    }


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        if (null == mRootView) {
            mRootView = createView(inflater, container);
        }
        ViewGroup parent = (ViewGroup) mRootView.getParent();
        if (parent != null) {
            parent.removeView(mRootView);
        }

        onResumeView(mRootView);

        return mRootView;
    }

    /**
     * 创建view，使用此方法创建view，
     * 只有第一次创建view的时候使用，如果此fargment 被复用，Detach 后重新 Attach，就不会再调用此方法，而是会返回之前的已经创建好的View
     *
     * @param inflater
     * @param container
     * @return
     * @see #getView()
     */
    protected abstract View createView(LayoutInflater inflater, ViewGroup container);

    /***
     * 会在每次 Fragment onCreateView 以后 调用，
     * 一般用于 Fragment 被销毁后，重新 create 后调用
     * 恢复view的状态，
     *
     * @param view
     */
    public void onResumeView(View view) {
        //各种状态处理
    }

    protected LoadingDialogFragment mLoadingDialog;

    public LoadingDialogFragment showLoadingDialog() {
        return showLoadingDialog(null);
    }

    public LoadingDialogFragment getLoadingDialog() {
        return mLoadingDialog;
    }

    /**
     * 显示加载框
     *
     * @param task
     */
    public LoadingDialogFragment showLoadingDialog(ServiceTask task) {
        if (mLoadingDialog == null) {
            mLoadingDialog = generateLoadingDialogFragment();
        }
        if (task != null) {
            mLoadingDialog.addServiceTask(task);
        }
        mLoadingDialog.show(getFragmentManager());
        return mLoadingDialog;
    }


    /**
     * 生成新的loadingdialog
     * @return
     */
    protected LoadingDialogFragment generateLoadingDialogFragment(){
        return  new LoadingDialogFragment();
    }


    //------ 隐藏容器-------
    protected View mContentContainer;

    /**
     * 隐藏 内容容器，这里一般用与 必须加载后才显示的内容
     * 这里方法会去找 当前view 下的 R.id.content_container 的容器来隐藏
     *
     * 必须在 {@link #onCreateView} 后调用
     */
    protected void hideContentContainer() {
        if (mContentContainer == null) {
            mContentContainer = mRootView.findViewById(R.id.content_container);
        }
        if(mContentContainer != null) {
            mContentContainer.setVisibility(View.INVISIBLE);
        }
    }

    /**
     * 显示 内容容器，这里一般用与 必须加载后才显示的内容
     * 这里方法会去找 当前view 下的 R.id.content_container 的容器来隐藏
     *
     * 必须在 {@link #onCreateView} 后调用
     */
    protected void showContentContainer() {
        if (mContentContainer == null) {
            mContentContainer = mRootView.findViewById(R.id.content_container);
        }
        if(mContentContainer != null) {
            mContentContainer.setVisibility(View.VISIBLE);
        }
    }

    /**
     * 吐司 提示
     * @param showStr
     */
    public void showToast(String showStr){
        Toast.makeText(getActivity(),showStr, Toast.LENGTH_SHORT).show();
    }
}
