package com.john.librarys.net.core;

import com.apkfuns.logutils.LogUtils;
import com.john.librarys.net.Constants;
import com.john.librarys.net.interf.Callback;
import com.john.librarys.net.interf.ServiceTask;
import com.john.librarys.utils.util.ClassUtil;

import java.util.Collection;

/**
 * Api回调，用于ApiHttpClient回调
 * 这里提供不用关系当发生错误时的处理，只需要关注业务处理，
 * <p/>
 * 重写 {@link #onSuccess(Object, ServiceTask)}
 */
public class ApiCallback implements Callback {

    ServiceTask mServiceTask;
    boolean mIsCallbackJSONArray = false;

    public ApiCallback(ServiceTask serviceTask) {
        this.mServiceTask = serviceTask;
        Class typeClass = ClassUtil.getClassGenricType(serviceTask.getClass());
        //根据 task 的泛型参数是否 是集合类型的 去判断是否 在callback中使用 JSONARRAY
        mIsCallbackJSONArray = Collection.class.isAssignableFrom(typeClass);
    }

    @Override
    public void onCall(int resultCode, Object data) {
        //这里对回调进行错误处理
        if (resultCode == Constants.STATE_CODE_SUCCESS) {
            try {
                onSuccess(data, mServiceTask);
            } catch (Exception e) {
                LogUtils.e(e);
                onError(Constants.STATE_CODE_FAILED, data, mServiceTask);
            }
        } else {
            onError(resultCode, data, mServiceTask);
        }
    }

    /**
     * 成功
     * 这里处理成功后的数据
     * 一般只需要处理这里的数据
     * 完成以后需要调用 task.complete
     */
    public void onSuccess(Object data, ServiceTask task) throws Exception {
        task.complete(Constants.STATE_CODE_SUCCESS, data);
    }

    /**
     * 失败处理
     *
     * @param resultCode
     * @param data       一般这里会返回 jsonobject 或者 jsonarray类型
     */
    public void onError(int resultCode, Object data, ServiceTask task) {
        task.complete(resultCode, null);
    }

    public ServiceTask getServiceTask() {
        return mServiceTask;
    }

    public boolean isCallbackTypeJsonArray() {
        return mIsCallbackJSONArray;
    }


    //----debug 相关---
    protected String mTag;
    /**
     * 设置日志相关debugtag
     * @param tag
     */
    public void setDebugTag(String tag){
        mTag = tag;
    }
}
