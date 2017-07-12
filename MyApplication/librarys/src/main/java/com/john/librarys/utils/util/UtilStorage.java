package com.john.librarys.utils.util;

import android.content.Context;
import android.os.Environment;
import android.util.Log;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

/**
 * 	public method
 *  <li>getRootDir(Context)				获取可用存储空间的根目录 </li>
 *  <li>getFileType(String) 			获取文件类型</li>
 *  <li>getFileDir(Context, int) 		根据文件类型获取文件目录名</li>
 *  <li>getFileDir(Context, String) 	根据文件名获取文件目录名</li>
 *  <li>createFileDir(Context, int) 	根据文件名创建文件目录</li>
 *  <li>createFileDir(Context, String) 	根据文件名创建文件目录</li>
 *  <li>getFilePath(Context, String) 	获取文件的绝对路径</li>
 *  <li>createFilePath(Context, String) 创建文件</li>
 *  <li>getLeftSpace(String) 			获取指定目录剩余存储空间</li>
 *  <li>getTotalSpace(String) 			获取指定目录所有存储空间</li>
 *  
 *  private method
 *  
 */
public class UtilStorage {
	
	/**
	 * 获取可用存储空间的根目录，一般先获取外置存储空间，如果没有，再去获取内置存储空间，
	 * 如果都没有，则获取本地应用的可用目录
	 * @param context
	 * @return
	 */
	public static String getRootDir(Context context) {
		//先找外置存储路径
		String state = Environment.getExternalStorageState();
		if(Environment.MEDIA_MOUNTED.equals(state)) {
			String rootDir = Environment.getExternalStorageDirectory().getAbsolutePath();
			//检测存储卡是否可用创建文件/目录
			if(mExternalStroageState == STORAGE_STATE_INIT) {
				StringBuilder path = new StringBuilder(rootDir)
					.append(File.separator).append("yyjg")
					.append(File.separator).append(System.currentTimeMillis());
				File file = new File(path.toString());
				if(file.mkdirs()) {
					file.delete();
					mExternalStroageState = STORAGE_STATE_ENABLED;
				} else {
					mExternalStroageState = STORAGE_STATE_DISABLED;
				}
			} 
			
			if(mExternalStroageState == STORAGE_STATE_ENABLED) {
				return rootDir;
			}
		}
			
		//再找内置SDCard
		for(VoldFstab vold : mVolds) {
			File mount = new File(vold.mMountPoint);
			if(mount.exists()
					&& mount.canRead()
					&& mount.canWrite()
					&& mount.canExecute()) {
				String rootDir = mount.getAbsolutePath();
				//检测存储卡是否可用创建文件/目录
				if(mInternalStroageState == STORAGE_STATE_INIT) {
					StringBuilder path = new StringBuilder(rootDir)
						.append(File.separator).append("yyjg")
						.append(File.separator).append(System.currentTimeMillis());
					File file = new File(path.toString());
					if(file.mkdirs()) {
						file.delete();
						mInternalStroageState = STORAGE_STATE_ENABLED;
					} else {
						mInternalStroageState = STORAGE_STATE_DISABLED;
					}
				} 
				
				if(mInternalStroageState == STORAGE_STATE_ENABLED) {
					return rootDir;
				}
			}
		}
				
		//再找本地应用内存路径
		if(context != null) {
			return context.getFilesDir().getAbsolutePath();
		} else {
			Log.e("TAG","Context is null");
		}

		return null;
	}


	private static final int STORAGE_STATE_INIT 		= 0;
	private static final int STORAGE_STATE_ENABLED 		= STORAGE_STATE_INIT + 1;
	private static final int STORAGE_STATE_DISABLED 	= STORAGE_STATE_ENABLED + 1;
	
	
	private static int mExternalStroageState;
	private static int mInternalStroageState;
	
	private static final String DEV_MOUNT = "dev_mount";
	private static ArrayList<VoldFstab> mVolds;

	static {
		mExternalStroageState = STORAGE_STATE_INIT;
		mInternalStroageState = STORAGE_STATE_INIT;

		mVolds = new ArrayList<VoldFstab>();
		BufferedReader reader = null;

		try {
			//vold.fstab文件
			File file = new File(Environment.getRootDirectory().getAbsoluteFile()
					+ File.separator
					+ "etc"
					+ File.separator
					+ "vold.fstab");
			reader = new BufferedReader(new FileReader(file));
			String line = null;

			while ((line = reader.readLine()) != null) {
				line = line.trim();
				if (line.startsWith(DEV_MOUNT)) {
					String[] infos = line.split(" ");
//					if(UtilArray.getCount(infos) >= 3) {
					if(infos.length >= 3) {
						VoldFstab vold = new VoldFstab();
						//vold.mLabel = infos[1];  //设置标签
						vold.mMountPoint = infos[2].split(":")[0];//设置挂载点
						//vold.mPart = infos[3];//设置子分区个数
						//vold.mSysfs = infos[4].split(":");//设置设备在sysfs文件系统下的路径
						mVolds.add(vold);
					}
				}
			}
		} catch (Exception ex) {
			ex.printStackTrace();
		} finally {
			try {
				if(reader != null) reader.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
	
	/**
	 * @author : Zhenshui.Xia
	 * @date   : 2013-11-1
	 * @desc   : vold配置文件， 文件位置/etc/vold.fstab
	 * 			 example:
	 * 			 # external sd card
	 *			 dev_mount sdcard-ext /mnt/sdcard-ext:none:lun1 auto /devices/platform/goldfish_mmc.0 /devices/platform/mmci-omap-hs.0/mmc_host/mmc0
	 *			 # internal eMMC
	 *			 dev_mount sdcard /mnt/sdcard 25 /devices/platform/mmci-omap-hs.1/mmc_host/mmc1
	 *
	 *			 ## Example of a dual card setup
	 *			 # dev_mount left_sdcard  /sdcard1  auto /devices/platform/goldfish_mmc.0 /devices/platform/msm_sdcc.2/mmc_host/mmc1
	 *	         # dev_mount right_sdcard /sdcard2  auto /devices/platform/goldfish_mmc.1 /devices/platform/msm_sdcc.3/mmc_host/mmc1
	 *
	 *			 ## Example of specifying a specific partition for mounts
	 *			 # dev_mount sdcard /sdcard 2 /devices/platform/goldfish_mmc.0 /devices/platform/msm_sdcc.2/mmc_host/mmc1
	 *
	 *			 # flash drive connection through hub connected to USB3
	 *			 dev_mount usbdisk_1.1 /mnt/usbdisk_1.1 auto /devices/platform/musb_hdrc/usb3/3-1/3-1.1
	 *
	 */
	private static class VoldFstab {
		//标签
		public String mLabel;
		//挂载点
		public String mMountPoint;
		//子分区个数
		public String mPart;
		//设备在sysfs文件系统下的路径
		public String[] mSysfs;
	}
}
