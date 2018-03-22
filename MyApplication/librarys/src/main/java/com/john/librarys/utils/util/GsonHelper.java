package com.john.librarys.utils.util;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import org.json.JSONArray;
import org.json.JSONObject;

import java.lang.reflect.Modifier;
import java.util.List;

/**
 * GSON工具类
 */
public class GsonHelper {

    /**
     * 获取GSON
     * 默认时间解析 为 {@link DateHelper#FORMAT_DATE_TIME}
     *
     * @return
     */
    public static Gson getGson() {
        return getGsonForDateFormat(DateHelper.FORMAT_DATE_TIME);
    }

    /**
     * 获取gson
     *
     * @param dateFormat 日期格式
     * @return
     */
    public static Gson getGsonForDateFormat(String dateFormat) {
        GsonBuilder builder = getGsonBuilder(dateFormat);
        builder.excludeFieldsWithModifiers(Modifier.FINAL, Modifier.TRANSIENT, Modifier.STATIC);
        return builder.create();
    }

    /**
     * 获取 gsonbuilder
     *
     * @param dateFormat 日期格式
     * @return
     */
    public static GsonBuilder getGsonBuilder(String dateFormat) {
        GsonBuilder builder = new GsonBuilder();
        builder.setDateFormat(dateFormat);
        return builder;
    }

    /**
     * 根据JSONobject 转换成实体类
     *
     * @param builder
     * @param json    json
     * @param clazz   实体类类
     * @return
     */
    public static Object parseJsonObject(GsonBuilder builder, JSONObject json, Class clazz) {
        return builder.create().fromJson(json.toString(), clazz);
    }

    /**
     * 根据JSONobject 转换成实体类
     *
     * @param json  json
     * @param clazz 实体类类
     * @return
     */
    public static Object parseJsonObject(JSONObject json, Class clazz) {
        return getGson().fromJson(json.toString(), clazz);
    }

    /**
     * 根据JSONArray 转换成 List
     *
     * @param builder
     * @param json    jsonarray
     * @param token   </br> 例如：new TypeToken<List<User>>() {}
     * @return
     */
    public static List parseJsonArray(GsonBuilder builder, JSONArray json, TypeToken token) {
        return builder.create().fromJson(json.toString(), token.getType());
    }

    /**
     * 根据JSONArray 转换成 List
     *
     * @param json  jsonarray
     * @param token </br> 例如：new TypeToken<List<User>>() {}
     * @return
     */
    public static List parseJsonArray(JSONArray json, TypeToken token) {
        return getGson().fromJson(json.toString(), token.getType());
    }

    /**
     * 实体类转json格式字符串
     * @param o
     * @return
     */
    public static String entity2JsonObject(Object o){
        return getGson().toJson(o);
    }

}
