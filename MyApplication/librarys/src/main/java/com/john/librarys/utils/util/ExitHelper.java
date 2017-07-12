package com.john.librarys.utils.util;


import android.app.Activity;
import android.app.Application;
import android.os.Handler;

import java.util.LinkedList;
import java.util.List;

/**
 * 用来退出整个应用程序类
 * 
 * @author Administrator
 * 
 */
public class ExitHelper extends Application {
	private List<Activity> activitys = null;
	private static ExitHelper mInstance;

	private ExitHelper() {
		activitys = new LinkedList<Activity>();
	}

	/**
	 * 单例模式中获取唯一的MysApplication实例
	 * 
	 * @return
	 */
	public static ExitHelper getInstance() {
		if (null == mInstance) {
			mInstance = new ExitHelper();
		}
		return mInstance;

	}

	/**
	 * 把传过来的Activity添加到容器中
	 * 
	 * @param activity 当前Activity
	 */
	public void addActivity(Activity activity) {
		if (activitys != null && activitys.size() > 0) {
			if (!activitys.contains(activity)) {
				activitys.add(activity);
			}
		} else {
			activitys.add(activity);
		}

	}

	/**
	 * 遍历所有Activity并finish
	 * 这里必须要在主线程调用
	 * 在finshActivites执行过程中，遍历Activit关闭的时候，进程还没全部finish时，就执行了System.exit(0);这个，导致toast不能消失
	 * 所以把System.exit(0);这句话延迟执行，就能防止这种冲突
	 */
	public void exit() {
		finshActivites();
		new Handler().postDelayed(new Runnable() {
			@Override
			public void run() {
				System.exit(0);
			}
		}, 50);
	}
	
	/**
	 * finish 所有 activity
	 */
	public void finshActivites(){
		if (activitys != null && activitys.size() > 0) {
			for (Activity activity : activitys) {
				activity.finish();
			}
		}
	}
	/**
	 * 删除容器 中 activity 
	 * @param activity
	 */
	public void removeActivity(Activity activity){
		activitys.remove(activity);
	}
}

