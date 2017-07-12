package com.john.librarys.net.interf;

/**
 * Created by LinYi.
 * 处理后的callback
 * @param <T> 确定onCall返回的类型（JSONObject/JSONArray..）
 */
public interface Callback<T> {

    void onCall(int resultCode, T t);

}
