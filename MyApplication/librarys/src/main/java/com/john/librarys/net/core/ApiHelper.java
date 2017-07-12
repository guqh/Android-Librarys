package com.john.librarys.net.core;

import android.content.Context;
import android.util.Log;

import com.google.gson.Gson;
import com.john.librarys.utils.util.DateHelper;
import com.john.librarys.utils.util.GsonHelper;

import java.util.Date;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * api工具类
 */
public class ApiHelper {

    public final static String DATE_FORMAT = "yyyyMMddHHmmssSSS";

    /**
     * 获取gson解析
     *
     * @return
     */
    public static Gson getGson() {
        return GsonHelper.getGsonForDateFormat(DATE_FORMAT);
    }

    public static ApiHttpClient getApiHttpClient(Context context) {
        return ApiHttpClient.getInstance(context);
    }

    //时间戳转换
    public static String getTimestamp(long timestamp) {
        return DateHelper.formatDate(DATE_FORMAT, new Date(timestamp));
    }

    //时间戳转换
    public static String getTimestamp(Date timestamp) {
        return DateHelper.formatDate(DATE_FORMAT, timestamp);
    }

    /**
     * 格式化 url，处理{xx}包括的url参数
     * 转换参数到url中，并把paramsmap中的参数删除
     *
     * @param url
     * @param paramsmap
     * @return
     */
    public static String formatUrl(String url, Map<String, String> paramsmap) {
        String resultUrl = url;
        Pattern regex = Pattern.compile("\\{([^/]+)\\}");//{xxxx}
        Matcher matcher = regex.matcher(url);
        while (matcher.find()) {//开始逐个匹配
            String matchParamName = matcher.group(1);//匹配具体名字
            if (paramsmap.containsKey(matchParamName)) {
                resultUrl = resultUrl.replaceAll("\\{" + matchParamName + "\\}", paramsmap.get(matchParamName));
                paramsmap.remove(matchParamName);
            } else {
                Log.w("ApiHelper", "formatUrl params can't find in paramsmap : " + matchParamName);
            }
        }
        return resultUrl;
    }


}
