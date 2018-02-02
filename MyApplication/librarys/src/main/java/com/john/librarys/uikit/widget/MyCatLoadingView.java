package com.john.librarys.uikit.widget;

import android.content.DialogInterface;

import com.john.librarys.net.interf.ServiceTask;
import com.roger.catloadinglibrary.CatLoadingView;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * @author John Gu
 * @date 2017/11/22.
 */

public class MyCatLoadingView extends CatLoadingView {

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
            this.dismissAllowingStateLoss();
        }
    }
}
