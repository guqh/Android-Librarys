package com.john.librarys.utils.permissions;

import android.app.Activity;

/**
 * 要求权限的组件
 * 
 * 配合 {@link ActivityCompat#requestPermissions(Activity, String[], int)}调用起检测权限
 * 然后 activity 实现此接口 {@link ActivityCompat.OnRequestPermissionsResultCallback} 然后去获取权限的选择
 * 或者 activit 直接继承 {@link android.support.v7.app.AppCompatActivity}
 *
 * 
 */
public interface RequestPermissionsComponent {
    String[] getRequestPermissions();
}
