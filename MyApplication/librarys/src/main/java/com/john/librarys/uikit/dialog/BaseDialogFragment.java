package com.john.librarys.uikit.dialog;

import android.content.DialogInterface;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;

import com.john.librarys.net.interf.ServiceTask;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * 提供绑定servicetask
 * 自动在service task 完成时，关闭dialog
 * 在关闭dialog时，自动取消service task
 */
public class BaseDialogFragment extends DialogFragment {
    private Set<ServiceTask> mTasks = new HashSet<>();//绑定的servicetask
    private Map<ServiceTask, ServiceTask.OnCompleteListener> mTaskCompleteMap = new HashMap<>();
    private Map<ServiceTask, ServiceTask.OnCancelListener> mTaskCancelMap = new HashMap<>();

    /**
     * 增加绑定的servicetask
     *
     * @param serviceTask
     */
    public void addServiceTask(final ServiceTask serviceTask) {

        ServiceTask.OnCancelListener onCancelListener = new ServiceTask.OnCancelListener() {
            @Override
            public void onCancel() {
                onServiceTaskFinish(serviceTask);
            }
        };

        ServiceTask.OnCompleteListener onCompleteListener = new ServiceTask.OnCompleteListener() {
            @Override
            public void onComplete(int resultCode, Object data) {
                onServiceTaskFinish(serviceTask);
            }
        };

        serviceTask.addOnCompleteListener(onCompleteListener);
        serviceTask.addOnCancelListener(onCancelListener);

        mTaskCancelMap.put(serviceTask, onCancelListener);
        mTaskCompleteMap.put(serviceTask, onCompleteListener);

        mTasks.add(serviceTask);
    }

    public void removeServiceTask(ServiceTask task) {
        task.removeOnCancelListener(mTaskCancelMap.get(task));
        task.removeOnCompleteListener(mTaskCompleteMap.get(task));
        mTasks.remove(task);
    }


    @Override
    public void onCancel(DialogInterface dialog) {
        super.onCancel(dialog);
        for (ServiceTask task : mTasks) {
            task.cancelTask();
        }
    }

    /**
     * 任务完成
     *
     * @param task
     */
    private void onServiceTaskFinish(ServiceTask task) {
        //解除绑定关系
        removeServiceTask(task);
        //关闭dialog
        if (mTasks.size() == 0) {
            BaseDialogFragment.this.dismissAllowingStateLoss();
        }
    }


    //为了解决  Can not perform this action after onSaveInstanceState
    public void show(FragmentManager manager, String tag) {

        if (isAdded() || isVisible() || isRemoving()) {
            return;
        }

        try {
            Field dismissedField = DialogFragment.class.getDeclaredField("mDismissed");
            Field shownByMeField = DialogFragment.class.getDeclaredField("mShownByMe");
            dismissedField.setAccessible(true);
            shownByMeField.setAccessible(true);

            dismissedField.set(this, false);
            shownByMeField.set(this, true);
            FragmentTransaction ft = manager.beginTransaction();
            ft.add(this, tag);
            ft.commitAllowingStateLoss();//关键。
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
    }

    //为了解决  Can not perform this action after onSaveInstanceState
    public int show(FragmentTransaction transaction, String tag) {
        if (isAdded() || isVisible() || isRemoving()) {
            return 0;
        }

        try {
            Field dismissedField = DialogFragment.class.getDeclaredField("mDismissed");
            Field shownByMeField = DialogFragment.class.getDeclaredField("mShownByMe");
            Field viewDestroyedField = DialogFragment.class.getDeclaredField("mViewDestroyed");
            Field backStackIdField = DialogFragment.class.getDeclaredField("mBackStackId");
            dismissedField.setAccessible(true);
            shownByMeField.setAccessible(true);
            viewDestroyedField.setAccessible(true);
            backStackIdField.setAccessible(true);

            dismissedField.set(this, false);
            shownByMeField.set(this, true);
            transaction.add(this, tag);

            viewDestroyedField.set(this, false);
            int backStackId = transaction.commitAllowingStateLoss();//关键。
            backStackIdField.set(this, backStackId);
            return backStackId;
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        return 0;
    }

}
