package com.john.librarys.utils.util;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.support.v4.content.FileProvider;
import android.util.Log;

import com.john.librarys.utils.download.DownloadHelper;
import com.john.librarys.utils.download.DownloadInfo;
import com.john.librarys.utils.download.DownloadListener;
import com.john.librarys.utils.permissions.RequestPermissionsComponent;

import java.io.File;

/**
 * 软件更新
 * <p/>
 * 需要权限来下载
 * "android.permission.WRITE_EXTERNAL_STORAGE"
 * "android.permission.READ_EXTERNAL_STORAGE"
 */
public class ApkUpgradeHelper implements RequestPermissionsComponent {
    private Context mContext;
    private String mUri;
    private String mStorePath;
    private DownloadInfo mDownloadInfo;

    private boolean isRunning = false;

    /**
     * 会自动根据uri 创建默认的 下载地址
     *
     * @param context
     * @param uri
     */
    public ApkUpgradeHelper(Context context, String uri) {
        mContext = context;
        mUri = uri;
        mStorePath = DownloadHelper.getDownloadFolder(mContext) + "/" + Uri.parse(uri).getLastPathSegment();
    }

    public ApkUpgradeHelper(Context context, String uri, String storePath) {
        mContext = context;
        mUri = uri;
        mStorePath = storePath;
    }


    /**
     * 检查权限
     *
     * @param context
     * @return
     */
    @Deprecated
    public static boolean checkPermission(Context context) {//检查权限
        return DownloadHelper.checkPermission(context);
    }

    public void upgrade() {
        if (!isRunning) {
            download();
        }
    }

    public void cancel() {
        DownloadHelper.getInstance(mContext).cancel(mDownloadInfo.getId());
    }

    /**
     * 安装
     */
    private void installApk() {
        Uri fileUri = null;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            fileUri = FileProvider.getUriForFile(mContext, mContext.getPackageName() + ".FileProvider", new File(mUri));
        } else {
            fileUri = Uri.fromFile(new File(mUri));
        }
        Intent installIntent = new Intent(Intent.ACTION_VIEW);
        installIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        installIntent.setFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        installIntent.setDataAndType(fileUri, "application/vnd.android.package-archive");
        mContext.startActivity(installIntent);
        android.os.Process.killProcess(android.os.Process.myPid());
    }

    /**
     * 开始下载
     */
    private void download() {
        String title = PackageHelper.getApplicationName(mContext) + "_" + PackageHelper.getVersionName(mContext);
        mDownloadInfo = new DownloadInfo(title, null, mUri, mStorePath, true);
        DownloadHelper downloadHelper = DownloadHelper.getInstance(mContext);
        mDownloadInfo = downloadHelper.download(mDownloadInfo);
        if (mDownloadInfo.getId() != -1) {
            isRunning = true;
            downloadHelper.addDownloadListener(mDownloadListener);
        } else {
            Log.w("ApkUpgradeHelper", "download apk faild " + mUri);
        }
    }


    private DownloadListener mDownloadListener = new DownloadListener() {

        @Override
        public void onSuccess(long downloadId) {
            DownloadHelper.getInstance(mContext).removeDownloadListenr(this);

            //由于 有可能 已经重名，downloadmanager 会自动改名下载，所以完成后需要 在这里重新拿真正的地址
            String storePath = DownloadHelper.getDownloadStorePath(mContext, mDownloadInfo.getId());
            mDownloadInfo.setStorePath(storePath);

            //下载成功开始更新
            installApk();
            isRunning = false;
        }

        @Override
        public void onFaild(long downloadId, int reason) {
            DownloadHelper.getInstance(mContext).removeDownloadListenr(this);
            isRunning = false;
        }

        @Override
        public void onPorgress(long downloadId, float progress) {

        }

        @Override
        public void onPause(long downloadId) {

        }

        @Override
        public void onCancel(long downloadId) {
            DownloadHelper.getInstance(mContext).removeDownloadListenr(this);
            isRunning = false;
        }
    };

    public static String[] getRequestPermissions(Context context) {
        return DownloadHelper.getInstance(context).getRequestPermissions();
    }

    @Override
    public String[] getRequestPermissions() {
        return DownloadHelper.getInstance(mContext).getRequestPermissions();
    }
}
