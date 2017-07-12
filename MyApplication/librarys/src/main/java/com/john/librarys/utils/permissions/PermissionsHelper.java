package com.john.librarys.utils.permissions;

import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Build;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

/**
 * 权限帮助类
 */
public class PermissionsHelper {

    /**
     * 检测当前是否有此权限
     *
     * @param context
     * @param permissions
     * @return
     */
    public static boolean chcekPermissions(Context context, String[] permissions) {
        for (String permission : permissions) {
            if (context.checkCallingPermission(permission) != PackageManager.PERMISSION_GRANTED) {
                return false;
            }
        }
        return true;
    }

    /**
     * 获取那些权限需要弹出来
     *
     * @param context
     * @param permissions
     * @return
     */
    public static String[] getShouldShowRequestPermissionRationale(Context context, String[] permissions) {
        if (Build.VERSION.SDK_INT < 23) {
            return new String[0];
        }

        List<String> requetPermissions = new ArrayList<>();

        try {
            Method method = PackageManager.class.getMethod("shouldShowRequestPermissionRationale", String.class);
            for (String permission : permissions) {
                PackageManager packageManager = context.getPackageManager();
                if ((Boolean) method.invoke(packageManager, permission)) {
                    requetPermissions.add(permission);
                }
            }
            String[] ps = new String[requetPermissions.size()];
            requetPermissions.toArray(ps);
            return ps;
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }

        return new String[0];
    }

    /**
     * 检测权限获取结果
     *
     * @param grantResults
     * @return
     */
    public static boolean chcekPermissionsResult(int grantResults[]) {
        for (int result : grantResults) {
            if (result == PackageManager.PERMISSION_DENIED) {
                return false;
            }
        }
        return true;
    }

}
