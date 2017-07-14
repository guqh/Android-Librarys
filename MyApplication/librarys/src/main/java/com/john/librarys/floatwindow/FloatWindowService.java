package com.john.librarys.floatwindow;

import android.app.ActivityManager;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Handler;
import android.os.IBinder;
import android.text.TextUtils;

import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

/**
 * 1像素前台服务保活程序进程
 * <!-- 悬浮框权限 -->
 * <uses-permission android:name="android.permission.SYSTEM_ALERT_WINDOW" />
 */
public class FloatWindowService extends Service {

	/**
	 * 用于在线程中创建或移除悬浮窗。
	 */
	private Handler handler = new Handler();

	/**
	 * 定时器，定时进行检测当前应该创建还是移除悬浮窗。
	 */
	private Timer timer;

	@Override
	public IBinder onBind(Intent intent) {
		return null;
	}

	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		// 开启定时器，每隔0.5秒刷新一次
		if (timer == null) {
			timer = new Timer();
			timer.scheduleAtFixedRate(new RefreshTask(), 0, 500);
		}
		return super.onStartCommand(intent, flags, startId);
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
		// Service被终止的同时也停止定时器继续运行
		timer.cancel();
		timer = null;
	}

	class RefreshTask extends TimerTask {

		@Override
		public void run() {
			// 当前界面不是当前应用，且没有悬浮窗显示，则创建悬浮窗。
			if (!isHome() && !MyWindowManager.isWindowShowing()) {
				handler.post(new Runnable() {
					@Override
					public void run() {
						MyWindowManager.createSmallWindow(getApplicationContext());
					}
				});
			}
			// 当前界面是当前应用，且有悬浮窗显示，则移除悬浮窗。
			else if (isHome() && MyWindowManager.isWindowShowing()) {
				handler.post(new Runnable() {
					@Override
					public void run() {
						MyWindowManager.removeSmallWindow(getApplicationContext());
					}
				});
			}
		}

	}

	/**
	 * 判断当前界面是否是当前应用
	 */
	private boolean isHome() {
//		KLog.e("悬浮窗===",getPackageName().equals(getTopActivityInfo()));
//		KLog.e("悬浮窗===22222222",getTopActivityInfo());
		return getPackageName().equals(getTopActivityInfo()) ;
	}

	private String getTopActivityInfo() {
		ActivityManager manager = ((ActivityManager) getApplicationContext().getSystemService(Context.ACTIVITY_SERVICE));
		String packageName="悬浮窗";
		if (Build.VERSION.SDK_INT >= 21) {
			List<ActivityManager.RunningAppProcessInfo> pis = manager.getRunningAppProcesses();
			ActivityManager.RunningAppProcessInfo topAppProcess = pis.get(0);
			if (topAppProcess != null && topAppProcess.importance == ActivityManager.RunningAppProcessInfo.IMPORTANCE_FOREGROUND) {
				packageName = topAppProcess.processName;
			}
		} else {
			//getRunningTasks() is deprecated since API Level 21 (Android 5.0)
			List localList = manager.getRunningTasks(1);
			ActivityManager.RunningTaskInfo localRunningTaskInfo = (ActivityManager.RunningTaskInfo)localList.get(0);
			packageName = localRunningTaskInfo.topActivity.getPackageName();
		}
		if (TextUtils.isEmpty(packageName)) {
			packageName = "悬浮窗";
		}
		return packageName;
	}
}
