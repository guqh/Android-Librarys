package com.john.librarys.utils.util;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;

/**
 * 使用ImageHelper
 */
@Deprecated
public class BitmapHelper {

    /**
     * 缩放至固定大小
     *
     * @param bitmap
     * @param w
     * @param h
     * @return
     */
    public static Bitmap resizeImage(Bitmap bitmap, int w, int h) {
        Bitmap BitmapOrg = bitmap;
        int width = BitmapOrg.getWidth();
        int height = BitmapOrg.getHeight();
        int newWidth = w;
        int newHeight = h;

        float scaleWidth = ((float) newWidth) / width;
        float scaleHeight = ((float) newHeight) / height;

        Matrix matrix = new Matrix();
        matrix.postScale(scaleWidth, scaleHeight);
        // if you want to rotate the Bitmap
        // matrix.postRotate(45);
        Bitmap resizedBitmap = Bitmap.createBitmap(BitmapOrg, 0, 0, width, height, matrix, true);
        return resizedBitmap;
    }

    /**
     * 比例缩放
     *
     * @param bitmap
     * @param scale
     * @return
     */
    public static Bitmap resizeImage(Bitmap bitmap, float scale) {
        Bitmap BitmapOrg = bitmap;
        int width = BitmapOrg.getWidth();
        int height = BitmapOrg.getHeight();
        int newWidth = (int) (width * scale);
        int newHeight = (int) (height * scale);

        return resizeImage(bitmap, newWidth, newHeight);
    }

    /**
     * 将图片按照某个角度进行旋转
     *
     * @param bm     需要旋转的图片
     * @param degree 旋转角度
     * @return 旋转后的图片
     */
    public static Bitmap rotateBitmapByDegree(Bitmap bm, int degree) {
        Bitmap returnBm = null;

        // 根据旋转角度，生成旋转矩阵
        Matrix matrix = new Matrix();
        matrix.postRotate(degree);
        try {
            // 将原始图片按照旋转矩阵进行旋转，并得到新的图片
            returnBm = Bitmap.createBitmap(bm, 0, 0, bm.getWidth(), bm.getHeight(), matrix, true);
        } catch (OutOfMemoryError e) {
        }
        if (returnBm == null) {
            returnBm = bm;
        }
        if (bm != returnBm) {
            bm.recycle();
        }
        return returnBm;
    }

    /**
     * 压缩图片，压缩成 jpeg格式，
     * 注意压缩以后 如果有需要的话 需要 自己释放原来的bitmap
     *
     * @param bitmap
     * @param quality 质量，如果= 30 30 是压缩率，表示压缩70%;
     * @return
     */
    public static Bitmap compress(Bitmap bitmap, int quality) {
        ByteArrayOutputStream ots = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, quality, ots);
        ByteArrayInputStream its = new ByteArrayInputStream(ots.toByteArray());
        return BitmapFactory.decodeStream(its);
    }

}
