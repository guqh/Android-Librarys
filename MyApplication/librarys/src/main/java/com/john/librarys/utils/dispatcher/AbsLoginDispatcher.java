package com.john.librarys.utils.dispatcher;

import android.content.Context;
import android.content.Intent;

import com.john.librarys.utils.annotation.RequireLogin;

/**
 * 登录拦截器
 */
public abstract class AbsLoginDispatcher implements IntentDispatcher {

    @Override
    public boolean dispatch(Context context, Intent intent) {
        //检测 注解
        if (intent.getComponent() != null) {
            String className = intent.getComponent().getClassName();
            try {
                Class targetClass = Class.forName(className);

                //需要登录拦截
                if (targetClass.isAnnotationPresent(RequireLogin.class)) {
                    boolean isLogin = isLogin(context);

                    if (isLogin) {
                        return false;
                    } else {
                        dispatchToLoginActivity(context, intent);
                        return true;
                    }
                }

            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }
        }
        return false;
    }

    /**
     * 是否已经登录
     *
     * @return
     */
    public abstract boolean isLogin(Context context);

    /**
     * 跳转到登录activity ，{@link #isLogin(Context)}返false &&
     * 目标activity 含有 {@link RequireLogin} 时生效
     *
     * @param context
     * @param originIntent 原来想去的意图
     */
    public abstract void dispatchToLoginActivity(Context context, Intent originIntent);
}
