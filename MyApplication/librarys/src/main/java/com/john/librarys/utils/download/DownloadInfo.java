package com.john.librarys.utils.download;

import android.net.ConnectivityManager;

/**
 * 下载信息
 */
public class DownloadInfo {
    /**
     * Bit flag for {@link #setAllowedNetworkTypes} corresponding to
     * {@link ConnectivityManager#TYPE_MOBILE}.
     */
    public static final int NETWORK_MOBILE = ConnectivityManager.TYPE_MOBILE;
    /**
     * 以太网
     */
    public static final int NETWORK_ETHERNET = ConnectivityManager.TYPE_ETHERNET;
    public static final int NETWORK_DUMMY = ConnectivityManager.TYPE_DUMMY;
    public static final int NETWORK_MOBILE_DUN = ConnectivityManager.TYPE_MOBILE_DUN;
    public static final int NETWORK_VPN = ConnectivityManager.TYPE_VPN;
    public static final int NETWORK_WIMAX = ConnectivityManager.TYPE_WIMAX;
    public static final int NETWORK_MOBILE_HIPRI = ConnectivityManager.TYPE_MOBILE_HIPRI;
    public static final int NETWORK_MOBILE_MMS = ConnectivityManager.TYPE_MOBILE_MMS;
    public static final int NETWORK_MOBILE_SUPL = ConnectivityManager.TYPE_MOBILE_SUPL;

    /**
     * Bit flag for {@link #setAllowedNetworkTypes} corresponding to
     * {@link ConnectivityManager#TYPE_WIFI}.
     */
    public static final int NETWORK_WIFI = ConnectivityManager.TYPE_WIFI;

    /**
     * Bit flag for {@link #setAllowedNetworkTypes} corresponding to
     * {@link ConnectivityManager#TYPE_BLUETOOTH}.
     *
     * @hide
     */
    public static final int NETWORK_BLUETOOTH = ConnectivityManager.TYPE_BLUETOOTH;


    long id = -1;
    String title;
    String description;
    String uri;
    String storePath;
    boolean showNotification = false;
    int mAllowedNetworkTypes = NETWORK_MOBILE | NETWORK_WIFI | NETWORK_BLUETOOTH | NETWORK_ETHERNET | NETWORK_DUMMY | NETWORK_MOBILE_DUN | NETWORK_VPN|NETWORK_WIMAX|NETWORK_MOBILE_HIPRI|NETWORK_MOBILE_MMS|NETWORK_MOBILE_SUPL;

    public DownloadInfo(String title, String description, String uri, String storePath, boolean showNotification) {
        this.title = title;
        this.description = description;
        this.uri = uri;
        this.storePath = storePath;
        this.showNotification = showNotification;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getUri() {
        return uri;
    }

    public void setUri(String uri) {
        this.uri = uri;
    }

    public String getStorePath() {
        return storePath;
    }

    public void setStorePath(String storePath) {
        this.storePath = storePath;
    }

    public boolean isShowNotification() {
        return showNotification;
    }

    public void setShowNotification(boolean showNotification) {
        this.showNotification = showNotification;
    }

    public int getAllowedNetworkTypes() {
        return mAllowedNetworkTypes;
    }

    /**
     * 设置允许的网络访问
     * <p/>
     * {@link #NETWORK_WIFI} {@link #NETWORK_MOBILE} {@link #NETWORK_BLUETOOTH}
     *
     * @param allowedNetworkTypes
     */
    public void setAllowedNetworkTypes(int allowedNetworkTypes) {
        mAllowedNetworkTypes = allowedNetworkTypes;
    }


    @Override
    public String toString() {
        return "DownloadInfo{" +
                "id=" + id +
                ", title='" + title + '\'' +
                ", description='" + description + '\'' +
                ", uri='" + uri + '\'' +
                ", storePath='" + storePath + '\'' +
                ", showNotification=" + showNotification +
                ", mAllowedNetworkTypes=" + mAllowedNetworkTypes +
                '}';
    }
}
