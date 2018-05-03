package com.john.librarys.utils.download;

import android.app.DownloadManager;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.content.pm.PackageManager;
import android.database.ContentObserver;
import android.database.Cursor;
import android.net.Uri;
import android.os.Environment;
import android.os.Handler;
import android.text.TextUtils;
import android.util.Log;

import com.john.librarys.utils.permissions.RequestPermissionsComponent;
import com.john.librarys.utils.util.PackageHelper;

import java.io.File;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.HashSet;
import java.util.Set;

/**
 * 下载工具类
 * 可以通过 addDownloadListener 来增加下载监听
 *
 * 需要权限
 * "android.permission.WRITE_EXTERNAL_STORAGE",
 * "android.permission.READ_EXTERNAL_STORAGE"
 */
public class DownloadHelper implements RequestPermissionsComponent {
    private Context mContext;
    private DownloadManager mDownloadManager;
    private Uri mDownloadUri = Uri.parse("content://downloads/my_downloads");

    private Set<DownloadListener> mDownloadListeners = new HashSet<>();

    private static DownloadHelper mInstance;

    private DownloadHelper(Context context) {
        mContext = context;
        mDownloadManager = (DownloadManager) mContext.getSystemService(Context.DOWNLOAD_SERVICE);
        mContext.getContentResolver().registerContentObserver(mDownloadUri, true, mDownloadObserver);
    }

    public static DownloadHelper getInstance(Context context) {
        if (mInstance == null) {
            mInstance = new DownloadHelper(context);
        }
        return mInstance;
    }

    /**
     * 检查权限
     *
     * @param context
     * @return
     */
    @Deprecated
    public static boolean checkPermission(Context context) {//检查权限
        if (context.checkCallingPermission("android.permission.WRITE_EXTERNAL_STORAGE") == PackageManager.PERMISSION_DENIED
                || context.checkCallingPermission("android.permission.READ_EXTERNAL_STORAGE") == PackageManager.PERMISSION_DENIED) {
            return false;
        } else {
            return true;
        }
    }

    /**
     * 下载，当 downloadInfo.getId() == -1 时，会启动一个新的下载，
     * 如果 !=-1 那么，会找对应的id，重新开始下载
     * <p/>
     * <strong>注意：这里 如果downloadInfo 的 storepath 已经存在文件，
     * downloadManager 会自动改名然后去下载，导致 storepath 改变，一般在onsuuces的回调后
     * 调用 {@link #getDownloadStorePath(Context, long)}获取真正的路径</strong>
     *
     * @param downloadInfo
     * @return 启动download任务后的id, 如果返回 -1代表没有开始下载
     */
    public DownloadInfo download(DownloadInfo downloadInfo) {
        long downloadId = -1;
        if (downloadInfo.getId() == -1) {
            DownloadManager.Request request = obtainDownloadRequest(downloadInfo);
            downloadId = mDownloadManager.enqueue(request);
        } else {
            ContentValues cv = new ContentValues();
            cv.put(DownloadManager.COLUMN_STATUS, DownloadManager.STATUS_RUNNING);
            Uri updateUri = ContentUris.withAppendedId(mDownloadUri, downloadId);
            int updateCount = mContext.getContentResolver().update(updateUri, cv, null, null);
            if (updateCount > 0) {
                downloadId = downloadInfo.getId();
            }
        }

        downloadInfo.setId(downloadId);
        return downloadInfo;
    }

    /**
     * 构建 一个下载请求
     *
     * @param downloadInfo
     * @return
     */
    private DownloadManager.Request obtainDownloadRequest(DownloadInfo downloadInfo) {
        Uri uri = null;
        //中文路径问题
        try {
            String url = URLEncoder.encode(downloadInfo.getUri(), "utf-8").replaceAll("%3A", ":")
                    .replaceAll("%2F", "/").replaceAll("%3F", "?").replaceAll("%26", "&");
            uri = Uri.parse(url);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
            uri = Uri.parse(downloadInfo.getUri());
        }

        DownloadManager.Request request = new DownloadManager.Request(uri);
        request.setTitle(downloadInfo.getTitle());
        request.setNotificationVisibility(downloadInfo.isShowNotification() ?
                DownloadManager.Request.VISIBILITY_VISIBLE : DownloadManager.Request.VISIBILITY_HIDDEN);
        request.setDescription(downloadInfo.getDescription());
        request.setAllowedNetworkTypes(downloadInfo.getAllowedNetworkTypes());

        Uri storeUri = Uri.parse(downloadInfo.getStorePath());
        if (TextUtils.isEmpty(storeUri.getScheme())) {
            File pathFile = new File(downloadInfo.getStorePath());
            if(pathFile.exists()) {
                pathFile.delete();
            }
            storeUri = Uri.fromFile(pathFile);
        }
        request.setDestinationUri(storeUri);
        return request;
    }

    /**
     * 取消
     *
     * @param downloadId
     */
    public void cancel(long downloadId) {
        mDownloadManager.remove(downloadId);
        onCencel(downloadId);
    }

    /**
     * 暂停
     *
     * @param downloadId
     */
    public void pause(long downloadId) {
        ContentValues cv = new ContentValues();
        cv.put(DownloadManager.COLUMN_STATUS, DownloadManager.STATUS_PAUSED);
        Uri updateUri = ContentUris.withAppendedId(mDownloadUri, downloadId);
        int updateCount = mContext.getContentResolver().update(updateUri, cv, null, null);
        if (updateCount > 0) {
            onPause(downloadId);
        }
    }

    private void onSuccess(long downloadId) {
        for (DownloadListener listener : mDownloadListeners) {
            listener.onSuccess(downloadId);
        }
    }

    private void onFaild(long downloadId, int reason) {
        for (DownloadListener listener : mDownloadListeners) {
            listener.onFaild(downloadId, reason);
        }
    }

    private void onPause(long downloadId) {
        for (DownloadListener listener : mDownloadListeners) {
            listener.onPause(downloadId);
        }
    }

    private void onProgress(long downloadId, float progress) {
        for (DownloadListener listener : mDownloadListeners) {
            listener.onPorgress(downloadId, progress);
        }
    }

    private void onCencel(long downloadId) {
        for (DownloadListener listener : mDownloadListeners) {
            listener.onCancel(downloadId);
        }
    }

    public void addDownloadListener(DownloadListener listener) {
        mDownloadListeners.add(listener);
    }

    public void removeDownloadListenr(DownloadListener listener) {
        mDownloadListeners.remove(listener);
    }


    private ContentObserver mDownloadObserver = new ContentObserver(new Handler()) {

        @Override
        public boolean deliverSelfNotifications() {
            return super.deliverSelfNotifications();
        }

        @Override
        public void onChange(boolean selfChange) {
            super.onChange(selfChange);
        }

        @Override
        public void onChange(boolean selfChange, Uri uri) {
            super.onChange(selfChange, uri);

            if (selfChange) {
                return;
            }

            //监听进度
            long id = -1;
            try {
                id = ContentUris.parseId(uri);
            } catch (Exception e) {

            }
            if (id == -1) {
                return;
            }
            //fuck。。DownloadManager 封了一层 导致 status_running 跟 实际 downloads的 status 对不上号，
            //必须使用DownloadManager的查询才能查到
            Cursor cursor = mDownloadManager.query(new DownloadManager.Query().setFilterById(id));
            if (cursor != null) {
                try {
                    if (cursor.moveToFirst()) {
                        int status = cursor.getInt(cursor.getColumnIndex(DownloadManager.COLUMN_STATUS));
                        switch (status) {
                            case DownloadManager.STATUS_RUNNING:
                                float progress = getProgress(cursor);
                                onProgress(id, progress);
                                break;
                            case DownloadManager.STATUS_FAILED:
                                int reason = cursor.getInt(cursor.getColumnIndex(DownloadManager.COLUMN_REASON));
                                onFaild(id, reason);
                                break;
                            case DownloadManager.STATUS_SUCCESSFUL:
                                onSuccess(id);
                            default:
                                break;
                        }
                    }

                } catch (Exception e) {
                    Log.e("DownloadHelper", "onchange " + uri.toString(), e);
                } finally {
                    cursor.close();
                }
            }
        }

    };

    private float getProgress(Cursor cursor) {
        long downloadedByte = cursor.getLong(cursor.getColumnIndex(DownloadManager.COLUMN_BYTES_DOWNLOADED_SO_FAR));
        long totalByte = cursor.getLong(cursor.getColumnIndex(DownloadManager.COLUMN_TOTAL_SIZE_BYTES));
        return ((float) downloadedByte / totalByte) * 100;
    }

    /**
     * 获取通用下载文件夹
     * 会自动创建该文件夹
     *
     * @param context
     * @return
     */
    public static String getDownloadFolder(Context context) {
        String path = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS).getPath() +
                "/" + PackageHelper.getPackageInfo(context).packageName;
        File folder = new File(path);
        if (!folder.exists()) {
            folder.mkdirs();
        }
        return path;
    }

    /**
     * 获取 下载的路径
     * 一般在下载完成后需要更新  downloadinfo的数据
     *
     * @param context
     * @param downloadId
     * @return
     */
    public static String getDownloadStorePath(Context context, long downloadId) {
        DownloadManager downloadManager = (DownloadManager) context.getSystemService(Context.DOWNLOAD_SERVICE);

        Cursor cursor = downloadManager.query(new DownloadManager.Query().setFilterById(downloadId));
        String dest = null;
        if (cursor != null) {
            try {
                if (cursor.moveToFirst()) {
                    dest = cursor.getString(cursor.getColumnIndex(DownloadManager.COLUMN_LOCAL_URI));
                }
            } catch (Exception e) {
                Log.w("DownloadHelper", "download :" + downloadId, e);
            } finally {
                cursor.close();
            }
        }
        return dest;
    }

    @Override
    public String[] getRequestPermissions() {
        return new String[]{"android.permission.WRITE_EXTERNAL_STORAGE", "android.permission.READ_EXTERNAL_STORAGE"};
    }
}
