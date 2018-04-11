package com.john.librarys.utils.util;

import android.content.Context;
import android.os.Environment;
import com.apkfuns.logutils.LogUtils;

import java.io.File;
import java.io.FilenameFilter;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * @author John Gu
 * @date 2017/10/25.
 *
 * 设置logutil
 */

public class LogUtilHelper {
    private String appName;
    private Context mContext;

    private static LogUtilHelper instance = new LogUtilHelper();

    /** 保证只有一个LogUtilHelper实例 */
    private LogUtilHelper() {
    }

    /** 获取LogUtilHelper实例 ,单例模式 */
    public static LogUtilHelper getInstance() {
        return instance;
    }

    // 用于格式化日期,作为日志文件名的一部分
    private DateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");

    /**
     * 初始化
     *
     * @param context
     */
    public void init(Context context, String appStr) {
        mContext = context;
        appName=appStr;

        // 支持写入日志到文件
        LogUtils.getLog2FileConfig()
                .configLog2FileEnable(true) //是否支持写入文件
                // targetSdkVersion >= 23 需要确保有写sdcard权限 .
                .configLog2FilePath(getFilePath())//写入日志路径
                .configLog2FileNameFormat(getFileName())//写入日志文件名
                .configLogFileEngine(new LogFileEngineFactory());//写入日志实现

        autoClear(10);
        LogUtils.d("日志写入文件初始化完成");
    }

    /**
     * 获取项目地址目录
     * @return
     */
    public String getGlobalpath() {
        return Environment.getExternalStorageDirectory().getAbsolutePath()
                + File.separator + appName + File.separator+ "log" + File.separator;
    }

    /**
     * 获取文件 名称
     * @return
     */
    private String getFileName() {
        String time = formatter.format(new Date());
        String fileName = "log-" + time + ".txt";
        return fileName;
    }

    /**
     * 获取 文件路径
     * @return
     */
    private String getFilePath() {
        String path;
        if (FileUtil.hasSdcard()) {
            path = getGlobalpath();
        } else {// 如果SD卡不存在，就保存到本应用的目录下
            path = mContext.getFilesDir().getAbsolutePath() + File.separator + "log";
        }
        return path;
    }

    /**
     * 文件删除
     * @param autoClearDay 文件保存天数
     */
    public void autoClear(final int autoClearDay) {
        FileUtil.delete(getGlobalpath(), new FilenameFilter() {

            @Override
            public boolean accept(File file, String filename) {
                String s = FileUtil.getFileNameWithoutExtension(filename);
                int day = autoClearDay < 0 ? autoClearDay : -1 * autoClearDay;
                String date = "log-" + DateUtil.getOtherDay(day);
                return date.compareTo(s) >= 0;
            }
        });
    }
}
