package com.john.librarys.net.interf;

import com.android.volley.Request;

import java.util.HashSet;
import java.util.Set;

/**
 * UI层调service传入的回调
 * 可取消任务
 */
public abstract class ServiceTask<T> {

    /**
     * 请求集合
     */
    private Set<Request> mRequestSet;

    /**
     * Constructor
     */
    public ServiceTask() {
        this.mRequestSet = new HashSet<>();
    }

    /**
     * 处理完成
     *
     * @param resultCode
     * @param data
     */
    public void complete(int resultCode, T data){
        onComplete(resultCode,data);

        //防止，在遍历 listenners 的时候 这些OnCompleteListener里面做 removeListenner的操作,
        //把数据加到另外一个集合容器里面遍历
        Set<OnCompleteListener> iteraSet = new HashSet<>();
        iteraSet.addAll(mOnCompleteListeners);

        for (OnCompleteListener listener : iteraSet) {
            listener.onComplete(resultCode,data);
        }
    }

    /**
     * 完成以后处理回调
     * @param resultCode
     * @param data
     */
    protected abstract void onComplete(int resultCode, T data);

    /**
     * 取消任务
     */
    public void cancelTask() {
        for (Request request : mRequestSet) {
            request.cancel();
        }

        //防止，在遍历 listenners 的时候 这些OnCompleteListener里面做 removeListenner的操作,
        //把数据加到另外一个集合容器里面遍历
        Set<OnCancelListener> iteraSet = new HashSet<>();
        iteraSet.addAll(mOnCancelListeners);

        for (OnCancelListener listener : iteraSet) {
            listener.onCancel();
        }
    }

    /**
     * 更新进度
     * max = 100;
     *
     * @param progress
     */
    public void progress(int progress) {
        for (OnProgressListener listener : mOnProgressListeners) {
            listener.onProgress(progress);
        }
    }

    /**
     * 添加Request到set<br/>
     * 在service调ApiHttpClient方法时，必须调用此方法接受返回的request，以供在调用cancelTask()将该request取消
     */
    public void addRequest(Request request) {
        mRequestSet.add(request);
    }


    /**
     * 取消动作回调接口
     */
    public interface OnCancelListener {
        void onCancel();
    }

    /**
     * 进度更新
     */
    public interface OnProgressListener {
        /**
         * 进度更新，max = 100
         *
         * @param progress
         */
        void onProgress(int progress);
    }

    /**
     * 完成
     */
    public interface OnCompleteListener<T> {
        void onComplete(int resultCode, T data);
    }

    protected Set<OnCancelListener> mOnCancelListeners = new HashSet<>();
    protected Set<OnProgressListener> mOnProgressListeners = new HashSet<>();
    protected Set<OnCompleteListener> mOnCompleteListeners = new HashSet<>();

    public void addOnCancelListener(OnCancelListener onCancelListener) {
        mOnCancelListeners.add(onCancelListener);
    }

    public void removeOnCancelListener(OnCancelListener onCancelListener) {
        mOnCancelListeners.remove(onCancelListener);
    }

    public void addOnProgressListener(OnProgressListener onProgressListeners) {
        mOnProgressListeners.add(onProgressListeners);
    }

    public void removeOnProgressListener(OnProgressListener onProgressListeners) {
        mOnProgressListeners.remove(onProgressListeners);
    }

    public void addOnCompleteListener(OnCompleteListener onCompleteListener) {
        mOnCompleteListeners.add(onCompleteListener);
    }

    public void removeOnCompleteListener(OnCompleteListener onCompleteListener) {
        mOnCompleteListeners.remove(onCompleteListener);
    }
}

