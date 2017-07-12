package com.john.librarys.utils.download;

/**
 * 下载回调
 */
public interface DownloadListener {
    void onSuccess(long downloadId);

    /**
     * 发生错误，
     * @param downloadId
     * @param reason  具体返回 android.app.DownloadManager#ERROR_*
     */
    void onFaild(long downloadId, int reason);

    /**
     * 进度回调
     *
     * @param downloadId
     * @param progress     进度 0~100
     */
    void onPorgress(long downloadId, float progress);

    void onPause(long downloadId);

    void onCancel(long downloadId);
}
