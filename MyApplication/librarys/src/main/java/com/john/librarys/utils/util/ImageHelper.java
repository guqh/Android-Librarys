package com.john.librarys.utils.util;

import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.AsyncTask;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.util.Base64;
import android.util.Log;
import android.widget.ImageView;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.lang.ref.WeakReference;

/**
 * 图片工具
 */
public class ImageHelper {
    private static String TAG = ImageHelper.class.getSimpleName();

    public static int[] getImageSize(Uri uri, Context context) {
        String path = null;
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;

        if (uri.toString().contains("content:")) {
            String[] projection = new String[]{MediaStore.Images.Media._ID, MediaStore.Images.Media.DATA};
            Cursor cur = null;
            try {
                cur = context.getContentResolver().query(uri, projection, null, null, null);
                if (cur != null && cur.moveToFirst()) {
                    int dataColumn = cur.getColumnIndex(MediaStore.Images.Media.DATA);
                    path = cur.getString(dataColumn);
                }
            } catch (IllegalStateException stateException) {
                Log.d(ImageHelper.class.getName(), "IllegalStateException querying content:" + uri);
            } finally {
                if (cur != null) {
                    cur.close();
                }
            }
        }

        if (TextUtils.isEmpty(path)) {
            //The file isn't ContentResolver, or it can't be access by ContentResolver. Try to access the file directly.
            path = uri.toString().replace("content://media", "");
            path = path.replace("file://", "");
        }

        BitmapFactory.decodeFile(path, options);
        int imageHeight = options.outHeight;
        int imageWidth = options.outWidth;
        return new int[]{imageWidth, imageHeight};
    }

    /**
     * 读取图片方向
     * Read the orientation from ContentResolver. If it fails, read from EXIF.
     *
     * @param ctx
     * @param filePath
     * @return
     */
    public static int getImageOrientation(Context ctx, String filePath) {
        Uri curStream;
        int orientation = 0;

        // Remove file protocol
        filePath = filePath.replace("file://", "");

        if (!filePath.contains("content://"))
            curStream = Uri.parse("content://media" + filePath);
        else
            curStream = Uri.parse(filePath);

        try {
            Cursor cur = ctx.getContentResolver().query(curStream, new String[]{MediaStore.Images.Media.ORIENTATION}, null, null, null);
            if (cur != null) {
                if (cur.moveToFirst()) {
                    orientation = cur.getInt(cur.getColumnIndex(MediaStore.Images.Media.ORIENTATION));
                }
                cur.close();
            }
        } catch (Exception errReadingContentResolver) {
            Log.e(TAG, "getImageOrientation", errReadingContentResolver);
        }

        if (orientation == 0) {
            orientation = getExifOrientation(filePath);
        }

        return orientation;
    }


    public static int getExifOrientation(String path) {
        ExifInterface exif;
        try {
            exif = new ExifInterface(path);
        } catch (IOException e) {
            Log.e(TAG, "getExifOrientation", e);
            return 0;
        }

        int exifOrientation = exif.getAttributeInt(ExifInterface.TAG_ORIENTATION, 0);

        switch (exifOrientation) {
            case ExifInterface.ORIENTATION_NORMAL:
                return 0;
            case ExifInterface.ORIENTATION_ROTATE_90:
                return 90;
            case ExifInterface.ORIENTATION_ROTATE_180:
                return 180;
            case ExifInterface.ORIENTATION_ROTATE_270:
                return 270;
            default:
                return 0;
        }
    }


    /** From http://developer.android.com/training/displaying-bitmaps/load-bitmap.html **/
    /**
     * 计算最合适的比例
     *
     * @param options
     * @param reqWidth
     * @param reqHeight
     * @return
     */
    public static int calculateInSampleSize(BitmapFactory.Options options, int reqWidth, int reqHeight) {
        // Raw height and width of image
        final int height = options.outHeight;
        final int width = options.outWidth;
        int inSampleSize = 1;

        if (height > reqHeight || width > reqWidth) {
            // Calculate ratios of height and width to requested height and width
            final int heightRatio = Math.round((float) height / (float) reqHeight);
            final int widthRatio = Math.round((float) width / (float) reqWidth);

            // Choose the smallest ratio as inSampleSize value, this will guarantee
            // a final image with both dimensions larger than or equal to the
            // requested height and width.
            inSampleSize = heightRatio < widthRatio ? heightRatio : widthRatio;
        }

        return inSampleSize;
    }


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
     * @param quality 质量，如果= 30 30 是质量，表示压缩70%;
     * @return
     */
    public static Bitmap compress(Bitmap bitmap, int quality) {
        return compress(bitmap, quality, Bitmap.CompressFormat.JPEG);
    }

    /**
     * 压缩图片，压缩成 jpeg格式，
     * 注意压缩以后 如果有需要的话 需要 自己释放原来的bitmap
     *
     * @param bitmap
     * @param quality 质量，如果= 30 30 是质量，表示压缩70%;
     * @param format  格式
     * @return
     */
    public static Bitmap compress(Bitmap bitmap, int quality, Bitmap.CompressFormat format) {
        ByteArrayOutputStream ots = new ByteArrayOutputStream();
        bitmap.compress(format, quality, ots);
        ByteArrayInputStream its = new ByteArrayInputStream(ots.toByteArray());
        return BitmapFactory.decodeStream(its);
    }

    public interface BitmapWorkerCallback {
        public void onBitmapReady(String filePath, ImageView imageView, Bitmap bitmap);
    }

    /**
     * 加载图片到imageView
     * execut()
     * 可以传入 uri
     */
    public static class BitmapWorkerTask extends AsyncTask<String, Void, Bitmap> {
        private final WeakReference<ImageView> imageViewReference;
        private final BitmapWorkerCallback callback;
        private int targetWidth;
        private int targetHeight;
        private String path;

        public BitmapWorkerTask(ImageView imageView, int width, int height, BitmapWorkerCallback callback) {
            // Use a WeakReference to ensure the ImageView can be garbage collected
            imageViewReference = new WeakReference<ImageView>(imageView);
            this.callback = callback;
            targetWidth = width;
            targetHeight = height;
        }

        // Decode image in background.
        @Override
        protected Bitmap doInBackground(String... params) {
            path = params[0];

            BitmapFactory.Options bfo = new BitmapFactory.Options();
            bfo.inJustDecodeBounds = true;
            BitmapFactory.decodeFile(path, bfo);

            bfo.inSampleSize = calculateInSampleSize(bfo, targetWidth, targetHeight);
            bfo.inJustDecodeBounds = false;

            // get proper rotation
            int bitmapWidth = 0;
            int bitmapHeight = 0;
            try {
                File f = new File(path);
                ExifInterface exif = new ExifInterface(f.getPath());
                int orientation = exif.getAttributeInt(ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_NORMAL);
                int angle = 0;
                if (orientation == ExifInterface.ORIENTATION_NORMAL) { // no need to rotate
                    return BitmapFactory.decodeFile(path, bfo);
                } else if (orientation == ExifInterface.ORIENTATION_ROTATE_90) {
                    angle = 90;
                } else if (orientation == ExifInterface.ORIENTATION_ROTATE_180) {
                    angle = 180;
                } else if (orientation == ExifInterface.ORIENTATION_ROTATE_270) {
                    angle = 270;
                }

                Matrix mat = new Matrix();
                mat.postRotate(angle);

                try {
                    Bitmap bmp = BitmapFactory.decodeStream(new FileInputStream(f), null, bfo);
                    if (bmp == null) {
                        Log.e(TAG, "can't decode bitmap: " + f.getPath());
                        return null;
                    }
                    bitmapWidth = bmp.getWidth();
                    bitmapHeight = bmp.getHeight();
                    return Bitmap.createBitmap(bmp, 0, 0, bmp.getWidth(), bmp.getHeight(), mat, true);
                } catch (OutOfMemoryError oom) {
                    Log.e(TAG, "OutOfMemoryError Error in setting image: " + oom);
                }
            } catch (IOException e) {
                Log.e(TAG, "Error in setting image", e);
            }

            return null;
        }

        // Once complete, see if ImageView is still around and set bitmap.
        @Override
        protected void onPostExecute(Bitmap bitmap) {
            if (imageViewReference == null || bitmap == null)
                return;

            final ImageView imageView = imageViewReference.get();

            if (callback != null)
                callback.onBitmapReady(path, imageView, bitmap);

        }
    }

    /**
     * 获取略缩图
     *
     * @param context
     * @param filePath
     * @param targetWidth
     * @return
     */
    public static Bitmap getThumbnailFromFilePath(Context context, String filePath, int targetWidth) {
        if (filePath == null || context == null) {
            return null;
        }

        Uri curUri;
        if (!filePath.contains("content://")) {
            curUri = Uri.parse("content://media" + filePath);
        } else {
            curUri = Uri.parse(filePath);
        }

        if (filePath.contains("video")) {
            // Load the video thumbnail from the MediaStore
            int videoId = 0;
            try {
                videoId = Integer.parseInt(curUri.getLastPathSegment());
            } catch (NumberFormatException e) {
            }
            ContentResolver crThumb = context.getContentResolver();
            BitmapFactory.Options options = new BitmapFactory.Options();
            options.inSampleSize = 1;
            Bitmap videoThumbnail = MediaStore.Video.Thumbnails.getThumbnail(crThumb, videoId, MediaStore.Video.Thumbnails.MINI_KIND,
                    options);
            if (videoThumbnail != null) {
                return getScaledBitmapAtLongestSide(videoThumbnail, targetWidth);
            } else {
                return null;
            }
        } else {
            // Create resized bitmap
            int rotation = getImageOrientation(context, filePath);
            byte[] bytes = createThumbnailFromUri(context, curUri, targetWidth, null, rotation);

            if (bytes != null && bytes.length > 0) {
                try {
                    Bitmap resizedBitmap = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
                    if (resizedBitmap != null) {
                        return getScaledBitmapAtLongestSide(resizedBitmap, targetWidth);
                    }
                } catch (OutOfMemoryError e) {
                    Log.e(TAG, "OutOfMemoryError Error in setting image: " + e);
                    return null;
                }
            }
        }

        return null;
    }

    /**
     * 转换图大小,只能往小转
     *
     * @param bitmap
     * @param targetSize
     * @return
     */
    public static Bitmap getScaledBitmapAtLongestSide(Bitmap bitmap, int targetSize) {
        if (bitmap.getWidth() <= targetSize && bitmap.getHeight() <= targetSize) {
            // Do not resize.
            return bitmap;
        }

        int targetWidth, targetHeight;
        if (bitmap.getHeight() > bitmap.getWidth()) {
            // Resize portrait bitmap
            targetHeight = targetSize;
            float percentage = (float) targetSize / bitmap.getHeight();
            targetWidth = (int) (bitmap.getWidth() * percentage);
        } else {
            // Resize landscape or square image
            targetWidth = targetSize;
            float percentage = (float) targetSize / bitmap.getWidth();
            targetHeight = (int) (bitmap.getHeight() * percentage);
        }

        return Bitmap.createScaledBitmap(bitmap, targetWidth, targetHeight, true);
    }

    public static Bitmap getImage(Context context, Uri uri) throws IOException {
        return MediaStore.Images.Media.getBitmap(context.getContentResolver(), uri);
    }

    /**
     * 根据uri获取图片,并根据目标宽度进行缩放,
     *
     * @param uri     可以是 media content://  file://
     * @param toWidth 目标宽度 （会等比例转换）当为-1时不做转换
     * @param quality 压缩质量
     * @return
     */
    public static Bitmap getImage(Context context, Uri uri, int toWidth, int quality) throws IOException {
        Bitmap bitmap = MediaStore.Images.Media.getBitmap(context.getContentResolver(), uri);

        //转换大小
        if (toWidth != -1) {
            Bitmap tmpBitmap = bitmap;
            float scale = (float) toWidth / tmpBitmap.getWidth();
            bitmap = resizeImage(tmpBitmap, scale);
            tmpBitmap.recycle();
        }

        //转角度
        int imageOrientation = getImageOrientation(context, uri.getPath());
        if (imageOrientation != 0) {
            Bitmap tmpBitmap = bitmap;
            bitmap = rotateBitmapByDegree(tmpBitmap, imageOrientation);
            tmpBitmap.recycle();
        }

        Bitmap compressBitmap = bitmap;
        bitmap = compress(compressBitmap, quality);
        compressBitmap.recycle();

        return bitmap;
    }

    /**
     * nbradbury - 21-Feb-2014 - similar to createThumbnail but more efficient since it doesn't
     * require passing the full-size image as an array of bytes[]
     */
    public static byte[] createThumbnailFromUri(Context context,
                                                Uri imageUri,
                                                int maxWidth,
                                                String fileExtension,
                                                int rotation) {
        if (context == null || imageUri == null || maxWidth <= 0)
            return null;

        String filePath = null;
        if (imageUri.toString().contains("content:")) {
            String[] projection = new String[]{MediaStore.Images.Media.DATA};
            Cursor cur = null;
            try {
                cur = context.getContentResolver().query(imageUri, projection, null, null, null);
                if (cur != null && cur.moveToFirst()) {
                    int dataColumn = cur.getColumnIndex(MediaStore.Images.Media.DATA);
                    filePath = cur.getString(dataColumn);
                }
            } catch (IllegalStateException stateException) {
                Log.d(ImageHelper.class.getName(), "IllegalStateException querying content:" + imageUri);
            } finally {
                if (cur != null) {
                    cur.close();
                }
            }
        }

        if (TextUtils.isEmpty(filePath)) {
            //access the file directly
            filePath = imageUri.toString().replace("content://media", "");
            filePath = filePath.replace("file://", "");
        }

        // get just the image bounds
        BitmapFactory.Options optBounds = new BitmapFactory.Options();
        optBounds.inJustDecodeBounds = true;

        try {
            BitmapFactory.decodeFile(filePath, optBounds);
        } catch (OutOfMemoryError e) {
            Log.e(TAG, "OutOfMemoryError Error in setting image: " + e);
            return null;
        }

        // determine correct scale value (should be power of 2)
        // http://stackoverflow.com/questions/477572/android-strange-out-of-memory-issue/3549021#3549021
        int scale = 1;
        if (maxWidth > 0 && optBounds.outWidth > maxWidth) {
            double d = Math.pow(2, (int) Math.round(Math.log(maxWidth / (double) optBounds.outWidth) / Math.log(0.5)));
            scale = (int) d;
        }

        BitmapFactory.Options optActual = new BitmapFactory.Options();
        optActual.inSampleSize = scale;

        // Get the roughly resized bitmap
        final Bitmap bmpResized;
        try {
            bmpResized = BitmapFactory.decodeFile(filePath, optActual);
        } catch (OutOfMemoryError e) {
            Log.e(TAG, "OutOfMemoryError Error in setting image: " + e);
            return null;
        }

        if (bmpResized == null) {
            return null;
        }

        ByteArrayOutputStream stream = new ByteArrayOutputStream();

        // Now calculate exact scale in order to resize accurately
        float percentage = (float) maxWidth / bmpResized.getWidth();
        float proportionateHeight = bmpResized.getHeight() * percentage;
        int finalHeight = (int) Math.rint(proportionateHeight);

        float scaleWidth = ((float) maxWidth) / bmpResized.getWidth();
        float scaleHeight = ((float) finalHeight) / bmpResized.getHeight();

        float scaleBy = Math.min(scaleWidth, scaleHeight);

        // Resize the bitmap to exact size
        Matrix matrix = new Matrix();
        matrix.postScale(scaleBy, scaleBy);

        // apply rotation
        if (rotation != 0) {
            matrix.setRotate(rotation);
        }

        Bitmap.CompressFormat fmt;
        if (fileExtension != null && fileExtension.equalsIgnoreCase("png")) {
            fmt = Bitmap.CompressFormat.PNG;
        } else {
            fmt = Bitmap.CompressFormat.JPEG;
        }

        final Bitmap bmpRotated;
        try {
            bmpRotated = Bitmap.createBitmap(bmpResized, 0, 0, bmpResized.getWidth(), bmpResized.getHeight(), matrix,
                    true);
        } catch (OutOfMemoryError e) {
            Log.e(TAG, "OutOfMemoryError Error in setting image: " + e);
            return null;
        } catch (NullPointerException e) {
            // See: https://github.com/wordpress-mobile/WordPress-Android/issues/1844
            Log.e(TAG, "Bitmap.createBitmap has thrown a NPE internally. This should never happen: " + e);
            return null;
        }

        if (bmpRotated == null) {
            // Fix an issue where bmpRotated is null even if the documentation doesn't say Bitmap.createBitmap can return null.
            // See: https://github.com/wordpress-mobile/WordPress-Android/issues/1848
            return null;
        }

        bmpRotated.compress(fmt, 100, stream);
        bmpResized.recycle();
        bmpRotated.recycle();

        return stream.toByteArray();
    }

    /**
     * 获取圆形图片
     *
     * @param bitmap
     * @return
     */
    public static Bitmap getCircularBitmap(final Bitmap bitmap) {
        if (bitmap == null)
            return null;

        final Bitmap output = Bitmap.createBitmap(bitmap.getWidth(), bitmap.getHeight(), Bitmap.Config.ARGB_8888);
        final Canvas canvas = new Canvas(output);
        final Paint paint = new Paint();
        final Rect rect = new Rect(0, 0, bitmap.getWidth(), bitmap.getHeight());
        final RectF rectF = new RectF(rect);

        paint.setAntiAlias(true);
        canvas.drawARGB(0, 0, 0, 0);
        paint.setColor(Color.RED);
        canvas.drawOval(rectF, paint);

        paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));
        canvas.drawBitmap(bitmap, rect, rect, paint);

        return output;
    }

    /**
     * 获取圆角图片
     *
     * @param bitmap
     * @param radius
     * @return
     */
    public static Bitmap getRoundedEdgeBitmap(final Bitmap bitmap, int radius) {
        if (bitmap == null) {
            return null;
        }

        final Bitmap output = Bitmap.createBitmap(bitmap.getWidth(), bitmap.getHeight(), Bitmap.Config.ARGB_8888);
        final Canvas canvas = new Canvas(output);
        final Paint paint = new Paint();
        final Rect rect = new Rect(0, 0, bitmap.getWidth(), bitmap.getHeight());
        final RectF rectF = new RectF(rect);

        paint.setAntiAlias(true);
        canvas.drawARGB(0, 0, 0, 0);
        paint.setColor(Color.RED);
        canvas.drawRoundRect(rectF, radius, radius, paint);

        paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));
        canvas.drawBitmap(bitmap, rect, rect, paint);

        paint.setStyle(Paint.Style.STROKE);
        paint.setStrokeWidth(1f);
        paint.setColor(Color.DKGRAY);
        canvas.drawRoundRect(rectF, radius, radius, paint);

        return output;
    }

    /**
     * 保存bitmap到file
     * <uses-permission android:name="android.permission.WRITE_EXTERNAL_STORAGE"/>
     *
     * @param bitmap
     * @param f
     */
    public static void saveBitmap(Bitmap bitmap, File f) throws IOException {
        if (!f.isDirectory() && f.exists()) {
            f.delete();
        }

        if (!f.getParentFile().exists()) {
            f.mkdirs();
        }

        FileOutputStream out = new FileOutputStream(f);
        try {
            Bitmap.CompressFormat fmt;
            if (f.getPath().toLowerCase().endsWith(".png")) {
                fmt = Bitmap.CompressFormat.PNG;
            } else {
                fmt = Bitmap.CompressFormat.JPEG;
            }

            bitmap.compress(fmt, 100, out);
            out.flush();
        } catch (IOException e) {
            throw e;
        } finally {
            out.close();
        }
    }

    /**
     * bitmap转为base64
     * @param bitmap
     * @return
     */
    public static String bitmapToBase64(Bitmap bitmap) {

        String result = null;
        ByteArrayOutputStream baos = null;
        try {
            if (bitmap != null) {
                baos = new ByteArrayOutputStream();
                bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);

                baos.flush();
                baos.close();

                byte[] bitmapBytes = baos.toByteArray();
                result = Base64.encodeToString(bitmapBytes, Base64.DEFAULT);
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (baos != null) {
                    baos.flush();
                    baos.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return result;
    }

    /**
     * base64转为bitmap
     * @param base64Data
     * @return
     */
    public static Bitmap base64ToBitmap(String base64Data) {
        byte[] bytes = Base64.decode(base64Data, Base64.DEFAULT);
        return BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
    }

    /**
     * 把Bitmap转Byte
     */
    public static byte[] bitmap2Bytes(Bitmap bm){
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bm.compress(Bitmap.CompressFormat.PNG, 100, baos);
        return baos.toByteArray();
    }
}

