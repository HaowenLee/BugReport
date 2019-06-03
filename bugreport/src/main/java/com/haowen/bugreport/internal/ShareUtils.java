package com.haowen.bugreport.internal;

import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.net.Uri;
import android.text.TextUtils;
import android.widget.ScrollView;

import com.haowen.bugreport.Config;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;

public class ShareUtils {

    /**
     * 目录
     */
    private static final String BUG_REPORT_DIR = "bugReport";

    /**
     * 截取scrollview的屏幕
     **/
    private static Bitmap view2Bitmap(ScrollView scrollView) {
        int h = 0;
        Bitmap bitmap;
        for (int i = 0; i < scrollView.getChildCount(); i++) {
            h += scrollView.getChildAt(i).getHeight();
        }
        // 创建对应大小的bitmap
        bitmap = Bitmap.createBitmap(scrollView.getWidth(), h,
                Bitmap.Config.ARGB_8888);
        final Canvas canvas = new Canvas(bitmap);
        canvas.drawColor(Color.WHITE);
        scrollView.draw(canvas);
        return bitmap;
    }

    /**
     * Save the bitmap.
     *
     * @param src      The source of bitmap.
     * @param filePath The path of file.
     * @param format   The format of the image.
     * @return {@code true}: success<br>{@code false}: fail
     */
    static boolean save(final Bitmap src,
                        final String filePath,
                        final Bitmap.CompressFormat format) {
        return save(src, getFileByPath(filePath), format, false);
    }

    /**
     * Save the bitmap.
     *
     * @param src    The source of bitmap.
     * @param file   The file.
     * @param format The format of the image.
     * @return {@code true}: success<br>{@code false}: fail
     */
    public static boolean save(final Bitmap src, final File file, final Bitmap.CompressFormat format) {
        return save(src, file, format, false);
    }

    /**
     * Save the bitmap.
     *
     * @param src      The source of bitmap.
     * @param filePath The path of file.
     * @param format   The format of the image.
     * @param recycle  True to recycle the source of bitmap, false otherwise.
     * @return {@code true}: success<br>{@code false}: fail
     */
    static boolean save(final Bitmap src,
                        final String filePath,
                        final Bitmap.CompressFormat format,
                        final boolean recycle) {
        return save(src, getFileByPath(filePath), format, recycle);
    }

    /**
     * Save the bitmap.
     *
     * @param src     The source of bitmap.
     * @param file    The file.
     * @param format  The format of the image.
     * @param recycle True to recycle the source of bitmap, false otherwise.
     * @return {@code true}: success<br>{@code false}: fail
     */
    private static boolean save(final Bitmap src,
                                final File file,
                                final Bitmap.CompressFormat format,
                                final boolean recycle) {
        if (isEmptyBitmap(src) || !createFileByDeleteOldFile(file)) return false;
        OutputStream os = null;
        boolean ret = false;
        try {
            os = new BufferedOutputStream(new FileOutputStream(file));
            ret = src.compress(format, 100, os);
            if (recycle && !src.isRecycled()) src.recycle();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (os != null) {
                    os.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return ret;
    }

    private static boolean createFileByDeleteOldFile(final File file) {
        if (file == null) return false;
        if (file.exists() && !file.delete()) return false;
        if (!createOrExistsDir(file.getParentFile())) return false;
        try {
            return file.createNewFile();
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }

    private static boolean createOrExistsDir(final File file) {
        return file != null && (file.exists() ? file.isDirectory() : file.mkdirs());
    }

    private static boolean isEmptyBitmap(final Bitmap src) {
        return src == null || src.getWidth() == 0 || src.getHeight() == 0;
    }

    private static File getFileByPath(final String filePath) {
        return isSpace(filePath) ? null : new File(filePath);
    }

    private static boolean isSpace(final String s) {
        if (s == null) return true;
        for (int i = 0, len = s.length(); i < len; ++i) {
            if (!Character.isWhitespace(s.charAt(i))) {
                return false;
            }
        }
        return true;
    }

    /**
     * 分享图片
     *
     * @param context    上下文
     * @param scrollView ScrollView
     */
    static void shareImage(Context context, ScrollView scrollView) {
        String filePath = getRootDir(context) + File.separator + System.currentTimeMillis() + ".jpg";
        save(view2Bitmap(scrollView), filePath, Bitmap.CompressFormat.JPEG);
        Uri imageUri = Uri.parse(filePath);
        Intent shareIntent = new Intent();
        shareIntent.setAction(Intent.ACTION_SEND);
        shareIntent.putExtra(Intent.EXTRA_STREAM, imageUri);
        shareIntent.setType("image/*");
        context.startActivity(Intent.createChooser(shareIntent, "分享到"));
    }

    /**
     * @return 存储目录
     */
    private static String getRootDir(Context context) {
        String cacheDir = getExternalCacheDir(context);
        if (TextUtils.isEmpty(cacheDir)) {
            cacheDir = getCacheDir(context);
        }
        return cacheDir + File.separator + BUG_REPORT_DIR;
    }

    /**
     * 不需要获取权限
     *
     * @return data/data/你的应用的包名/cache/目录
     */
    private static String getCacheDir(Context context) {
        File cacheDir = context.getCacheDir();
        if (cacheDir == null) {
            return "";
        }
        return cacheDir.getAbsolutePath();
    }

    /**
     * 不需要获取权限
     *
     * @return SDCard/Android/data/你的应用包名/cache/目录
     */
    private static String getExternalCacheDir(Context context) {
        File externalCacheDir = context.getExternalCacheDir();
        if (externalCacheDir == null) {
            return "";
        }
        return externalCacheDir.getAbsolutePath();
    }

    /**
     * 发送邮件给开发这
     *
     * @param context   上下文
     * @param crashInfo 崩溃信息
     */
    static void sendEmail(Context context, String crashInfo) {
        try {
            Intent sendIntent = new Intent(Intent.ACTION_SEND);
            sendIntent.putExtra(Intent.EXTRA_EMAIL, new String[]{Config.DEVELOPER_EMAIL});
            sendIntent.putExtra(Intent.EXTRA_TEXT, crashInfo);
            sendIntent.putExtra(Intent.EXTRA_SUBJECT, Utils.getAppInfo(context));
            sendIntent.setType("message/rfc822");
            context.startActivity(sendIntent);
        } catch (ActivityNotFoundException e) {
            // Empty
        }
    }
}