package com.john.librarys.utils;

import android.content.Context;
import android.content.Intent;

import com.john.librarys.utils.dispatcher.IntentDispatcher;

import java.util.LinkedList;
import java.util.List;

/**
 * 管理启动context相关的操作，比如启动activity等
 * 通过这里启动的activity 可以根据 @RequireLogin 等注解判断拦截
 *
 * 通过调用 {@link #addIntentIntentDispatcher(IntentDispatcher)} 来增加拦截分发器，比如登录拦截器
 */
public class ContextManager {

    static List<IntentDispatcher> mIntentDispatcher = new LinkedList<>();

    public static void addIntentIntentDispatcher(IntentDispatcher intentDispatcher) {
        mIntentDispatcher.add(intentDispatcher);
    }

    public static void startActivity(Context context, Class activityClass){
        startActivity(context,new Intent(context,activityClass));
    }

    /**
     * 启动activity
     *
     * @param context
     * @param intent
     */
    public static void startActivity(Context context, Intent intent) {

        for (IntentDispatcher intentInterceptor : mIntentDispatcher) {
            if (intentInterceptor.dispatch(context, intent)) {
                return;
            }
        }
        //不用拦截处理
        context.startActivity(intent);
    }

}
