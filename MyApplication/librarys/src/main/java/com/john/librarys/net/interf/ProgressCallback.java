package com.john.librarys.net.interf;

/**
 * Created by LinYi.
 * 带进度的回调
 */
public interface ProgressCallback extends Callback{

    /**
     *
     * @param progress 0.0~1.0
     */
    void onProgress(float progress);

}
