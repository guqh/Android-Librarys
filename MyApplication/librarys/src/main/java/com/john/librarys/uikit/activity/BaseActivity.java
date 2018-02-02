package com.john.librarys.uikit.activity;


import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.Toast;

import com.jaeger.library.StatusBarUtil;
import com.john.librarys.R;
import com.john.librarys.net.interf.ServiceTask;
import com.john.librarys.utils.permissions.PermissionsHelper;
import com.john.librarys.uikit.dialog.LoadingDialogFragment;
import com.john.librarys.utils.util.ACache;
import com.john.librarys.utils.util.DialogHelper;
import com.john.librarys.utils.util.ExitHelper;
import com.zhy.autolayout.AutoLayoutActivity;

import java.util.HashMap;
import java.util.Map;

import de.greenrobot.event.EventBus;

public class BaseActivity extends AutoLayoutActivity {

    private EventBus mEventBus = EventBus.getDefault();
    protected Context mContext;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ExitHelper.getInstance().addActivity(this);
        this.mContext = this;
        registerEventBus();
        setBindingContentView();
//        setStatusBar();
        DialogHelper.init(this);
    }

    protected void setStatusBar() {
        StatusBarUtil.setColor(this, getResources().getColor(R.color.primary));
    }
    /**
     * 设置绑定view </br>
     * 如果是绑定类型的view 就需要重写该方法
     */
    protected void setBindingContentView() {
        //1.init bind data
        //2.DataBindingUtil.setContentView(this,layout);
        //3.set bindData to bindingView
        //4.other init
    }

    public EventBus getEventBus() {
        return mEventBus;
    }

    /**
     * 是否使用EventBus</br>
     * 默认不使用，如果使用，就会在 onAttachedToWindow 上注册 提供者、订阅者
     * 在 onDetachedFromWindow 上解除
     * 使用方式
     * 1. 重写 isUseEventBus return true
     * 2. 自定义消息Event
     * 3. 新建回调方法 加入注解 @Subscribe
     * 4. 在需要发送的地方 调用 getEventBus().post
     *
     * @return
     */
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
    protected void onDestroy() {
        super.onDestroy();
        unregisterEventBus();
        ExitHelper.getInstance().removeActivity(this);
    }


    // 通过返回键退出应用
    private long exitTime = 0;
    private boolean mWillExit = false;

    /**
     * icon_settings 是否快速双击退出应用程序
     *
     * @param exit
     */
    protected void setWillDoubleClickExitApp(boolean exit) {
        mWillExit = exit;
    }

    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (mWillExit) {
            if (keyCode == KeyEvent.KEYCODE_BACK && event.getAction() == KeyEvent.ACTION_DOWN) {
                if ((System.currentTimeMillis() - exitTime) > 2000) {
                    Toast.makeText(BaseActivity.this, "再按一次退出", Toast.LENGTH_SHORT).show();
                    exitTime =
                            System.currentTimeMillis();
                } else {
                    ExitHelper.getInstance().exit();
                    ACache.get(mContext).clear();
                }
                return true;
            }
        }
        return super.onKeyDown(keyCode, event);
    }

    private boolean mAutoHideKeyboard = true;

    /**
     * 设置时候启用点击关闭键盘
     *
     * @param autohide
     */
    public void setAutoHideKeyboard(boolean autohide) {
        mAutoHideKeyboard = autohide;
    }

    /**
     * icon_settings 点击Edittext外的地方就收起软键盘
     */
    public boolean dispatchTouchEvent(MotionEvent ev) {

        if (!mAutoHideKeyboard) {
            return super.dispatchTouchEvent(ev);
        }

        if (ev.getAction() == MotionEvent.ACTION_DOWN) {
            View v = getCurrentFocus();
            if (isShouldHideInput(v, ev)) {

                InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                if (imm != null) {
                    imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
                }
            }
            return super.dispatchTouchEvent(ev);
        }
        // 必不可少，否则所有的组件都不会有TouchEvent了
        if (getWindow().superDispatchTouchEvent(ev)) {
            return true;
        }
        return onTouchEvent(ev);
    }

    /**
     * 判断edittext的位置
     *
     * @param v
     * @param event
     * @return
     */
    public boolean isShouldHideInput(View v, MotionEvent event) {
        if (v != null && (v instanceof EditText)) {
            int[] leftTop = {0, 0};
            // 获取输入框当前的location位置
            v.getLocationInWindow(leftTop);
            int left = leftTop[0];
            int top = leftTop[1];
            int bottom = top + v.getHeight();
            int right = left + v.getWidth();
            if (event.getX() > left && event.getX() < right && event.getY() > top && event.getY() < bottom) {
                // 点击的是输入框区域，保留点击EditText的事件
                return false;
            } else {
                return true;
            }
        }
        return false;
    }

    private boolean outCancel = false;
    public boolean isOutCancel() {
        return outCancel;
    }

    public void setOutCancel(boolean outCancel) {
        this.outCancel = outCancel;
    }
    /**
     * 显示加载框
     *
     * @param task
     */
    public void showLoadingDialog(ServiceTask task) {
        LoadingDialogFragment.showLoading(this,task);
    }
    public void showLoadingDialog(String msg) {
        LoadingDialogFragment.showLoading(this, msg);
    }
    public void showLoadingDialog() {
        LoadingDialogFragment.showLoading(this);
    }
    public void dismissLoadingDialog() {
        LoadingDialogFragment.dismissDialog();

    }


    //------ 隐藏容器-------
    protected View mContentContainer;

    /**
     * 隐藏 内容容器，这里一般用与 必须加载后才显示的内容
     * 比如 支付，
     * 这里方法会去找 当前view 下的 R.id.content_container 的容器来隐藏
     */
    protected void hideContentContainer() {
        if (mContentContainer == null) {
            mContentContainer = findViewById(R.id.content_container);
        }
        mContentContainer.setVisibility(View.INVISIBLE);
    }

    /**
     * 显示 内容容器，这里一般用与 必须加载后才显示的内容
     * 比如 支付，
     * 这里方法会去找 当前view 下的 R.id.content_container 的容器来隐藏
     */
    protected void showContentContainer() {
        if (mContentContainer == null) {
            mContentContainer = findViewById(R.id.content_container);
        }
        mContentContainer.setVisibility(View.VISIBLE);
    }


    //-------fragment------

    /**
     * 设置主体fragment
     * 重置 activity contentview 为 R.layout.activity_content_fragment,并把fragment设置到里面
     * 这里自动会把 activity 的 intent extras 设置到fragment的arguments中，
     *
     * @param fragment
     * @param tag
     */
    public void setContentFragment(Fragment fragment, String tag) {

        setContentView(R.layout.activity_content_fragment);

        FragmentTransaction transaction = getSupportFragmentManager()
                .beginTransaction();

        fragment.setArguments(getIntent().getExtras());

        if (fragment.isAdded()) {
            transaction.show(fragment);
        } else {
            transaction.add(R.id.fragment_content, fragment, tag);
        }

        transaction.commitAllowingStateLoss();
    }

    //=======需要权限的action========
    int mCurrentPerminssionCodeIndex = 0;
    Map<Integer, Runnable> mExecuteActions = new HashMap<>();

    /**
     * 执行 需要权限的 操作
     *
     * @param permissions
     * @param action
     */
    public void executeRequestPermissionAction(String[] permissions, Runnable action) {

        if (PermissionsHelper.chcekPermissions(this, permissions)) {
            //已经有权限，直接运行
            action.run();
        } else {
            String[] requetPermissions = PermissionsHelper.getShouldShowRequestPermissionRationale(this, permissions);

            if (requetPermissions.length > 0) {
                //需要权限确认
                mCurrentPerminssionCodeIndex++;
                mExecuteActions.put(mCurrentPerminssionCodeIndex, action);
                ActivityCompat.requestPermissions(this, requetPermissions, mCurrentPerminssionCodeIndex);
            } else {
                //已经授权过，直接运行
                action.run();
            }
        }

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (mExecuteActions.containsKey(requestCode) && PermissionsHelper.chcekPermissionsResult(grantResults)) {
            mExecuteActions.get(requestCode).run();
        }
    }

    /**
     * 吐司 提示
     * @param showStr
     */
    private Toast toast;
    public void showToast(String showStr){
        if (toast==null){
            toast=Toast.makeText(this,showStr, Toast.LENGTH_SHORT);
        }else {
            toast.setText(showStr);
        }
        toast.show();
    }
}
