package com.john.librarys.net.core;

import android.content.Context;

import android.text.TextUtils;
import com.android.volley.Request;
import com.apkfuns.logutils.LogUtils;
import com.john.librarys.net.interf.ProgressCallback;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * Api 客户端
 */
public class ApiClient {
    private final boolean DEBUG = false;
    String mUrl;
    Map<String, String> mParams = new HashMap<>();
    String mJsonParams;
    String mDir;
    ApiCallback mApiCallback;
    ProgressCallback mProgressCallback;
    Context mContext;

    boolean mDoGet = true;//是否使用get 方式请求，
    boolean isJsonParams = false;//参数是否为json格式string
    private String value;
    private String key;

    /**
     * 构建标准的 map参数的 client
     *
     * @param context
     * @param url
     * @param params
     * @param apiCallback
     */
    public ApiClient(Context context, String url, Map<String, ? extends Object> params, ApiCallback apiCallback) {
        mParams = convertParamsMap(params);
        mUrl = ApiHelper.formatUrl(url, mParams);
        mApiCallback = apiCallback;
        mContext = context;

        //设置debug相关的tag
        mApiCallback.setDebugTag(mUrl + "?" + mParams);
    }

    /**
     * 构建 以Json 传输的jsonrequest 的client
     *
     * @param context
     * @param url
     * @param jsonParams
     * @param apiCallback
     */
    public ApiClient(Context context, String url, String jsonParams, ApiCallback apiCallback) {
        mJsonParams = jsonParams;
        mUrl = url;
        mApiCallback = apiCallback;
        mContext = context;
        isJsonParams = true;
    }
    /**
     * 构建下载服务
     *
     * @param context
     * @param url
     * @param dir
     * @param progressCallback
     */
    public ApiClient(Context context, String url, String dir, ProgressCallback progressCallback) {
        mDir = dir;
        mUrl = url;
        mProgressCallback = progressCallback;
        mContext = context;
    }
    /**
     * 下载功能
     */
    public void doDownload() {
        ApiHttpClient apiHttpClient = ApiHelper.getApiHttpClient(mContext);
        apiHttpClient.download(mUrl,mDir,mProgressCallback);
    }

    /**
     * 转换参数类型转成map<String,String>
     *
     * @param params
     * @return
     */
    private Map<String, String> convertParamsMap(Map<String, ? extends Object> params) {
        Map<String, String> result = new HashMap<>();
        if (params != null) {
            Set<String> keySet = params.keySet();
            for (String key : keySet) {
                Object value = params.get(key);
                if (value instanceof String) {
                    result.put(key, (String) value);
                } else if (value instanceof Date) {
                    //时间做转换
                    result.put(key, ApiHelper.getTimestamp((Date) value));
                } else {
                    result.put(key, String.valueOf(value));
                }
            }
        }
        return result;
    }

    /**
     * 使用get
     */
    public void doGet() {
        mDoGet = true;
        execute();
    }
    /**
     * 使用get
     */
    public void doGetAddHeaders(String key,String value) {
        mDoGet = true;
        this.key=key;
        this.value=value;
        execute();
    }
    /**
     * 使用post
     */
    public void doPostAddHeaders(String key,String value) {
        mDoGet = false;
        this.key=key;
        this.value=value;
        execute();
    }

    /**
     * 使用post
     */
    public void doPost() {
        mDoGet = false;
        execute();
    }

    /**
     * 获取回调的类型是否是jsonarray
     *
     * @return
     */
    private boolean isCallbackTypeJsonArray() {
        //根据callback的 泛型判断
        return mApiCallback.isCallbackTypeJsonArray();
    }

    /**
     * 开始执行，默认使用 get方法
     */
    public void execute() {

        ApiHttpClient apiHttpClient = ApiHelper.getApiHttpClient(mContext);
        Request request;

        if (DEBUG) {
            LogUtils.d("access : " + mUrl + "?" + mParams);
        }

        if (mDoGet) {
            //使用Get方式
            if (!isJsonParams) {
               if (!TextUtils.isEmpty(key)&&!TextUtils.isEmpty(value)){
                    request = apiHttpClient.doGetAddHeaders(mUrl, mParams,isCallbackTypeJsonArray(), getApiCallback(),key,value);
                }else {
                   request = apiHttpClient.doGet(mUrl, mParams, isCallbackTypeJsonArray(), getApiCallback());
               }
            } else {
                request = apiHttpClient.doGet(mUrl, mJsonParams,isCallbackTypeJsonArray(), getApiCallback());
            }
        } else {
            //使用post方式
            if (!isJsonParams) {
                request = apiHttpClient.doPost(mUrl, mParams, isCallbackTypeJsonArray(), getApiCallback());
            } else {
                if (!TextUtils.isEmpty(key)&&!TextUtils.isEmpty(value)){
                    request = apiHttpClient.doPostAddHeaders(mUrl, mJsonParams,isCallbackTypeJsonArray(), getApiCallback(),key,value);
                }else {
                    request = apiHttpClient.doPost(mUrl, mJsonParams,isCallbackTypeJsonArray(), getApiCallback());
                }
            }
        }

        getApiCallback().getServiceTask().addRequest(request);
    }

    protected ApiCallback getApiCallback() {
        return mApiCallback;
    }


}
