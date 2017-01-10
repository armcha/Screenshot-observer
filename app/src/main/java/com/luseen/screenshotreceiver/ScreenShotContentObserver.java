package com.luseen.screenshotreceiver;

import android.content.Context;
import android.database.ContentObserver;
import android.database.Cursor;
import android.net.Uri;
import android.os.Handler;
import android.provider.MediaStore;
import android.util.Log;

/**
 * Created by Chatikyan on 08.01.2017.
 */

public class ScreenShotContentObserver extends ContentObserver {

    public static final String TAG = ScreenShotContentObserver.class.getSimpleName();
    private Context context;

    public ScreenShotContentObserver(Handler handler, Context context) {
        super(handler);
        this.context = context;
    }

    @Override
    public boolean deliverSelfNotifications() {
        return super.deliverSelfNotifications();
    }

    @Override
    public void onChange(boolean selfChange) {
        super.onChange(selfChange);
    }

    @Override
    public void onChange(boolean selfChange, Uri uri) {
        // if (uri.toString().matches(MediaStore.Images.Media.EXTERNAL_CONTENT_URI.toString() + "/[0-9]+")) {
        Cursor cursor = null;
        try {
            cursor = context.getContentResolver().query(uri, new String[]{
                    MediaStore.Images.Media.DISPLAY_NAME,
                    MediaStore.Images.Media.DATA,
                    MediaStore.Images.Media.DATE_TAKEN,
                    MediaStore.Images.Media.DATE_ADDED,
                    MediaStore.Images.Media.TITLE,
                    MediaStore.Images.Media.BUCKET_DISPLAY_NAME
            }, null, null, null);
            if (cursor != null && cursor.moveToFirst()) {
                //  Log.e("onChange ", "getCount " + cursor.getCount());

                //for (int i = 0; i < cursor.getCount(); i++) {
                //cursor.move(i);
                if (cursor.moveToLast()) {

                    int displayNameColumnIndex = cursor.getColumnIndex(MediaStore.Images.Media.DISPLAY_NAME);
                    int dataColumnIndex = cursor.getColumnIndex(MediaStore.Images.Media.DATA);
                    int bucketDisplayNameColumnIndex = cursor.getColumnIndex(MediaStore.Images.Media.BUCKET_DISPLAY_NAME);
                    String fileName = cursor.getString(displayNameColumnIndex);
                    String path = cursor.getString(dataColumnIndex);
                    String bucketDisplayName = cursor.getString(bucketDisplayNameColumnIndex);
                    //Log.e("onChange ", "path " + path);
                    //Log.e("onChange ", "fileName " + fileName);
                    //Log.e("onChange ", "bucketDisplayName " + bucketDisplayName);

                    if (isScreenshot(fileName, path, bucketDisplayName)) {
                        onScreenShotTaken(path, fileName);
                        Log.e(TAG, " Screen shot added " + fileName + " " + path);
                        //Log.e(TAG, "DATE_TAKEN " + cursor.getColumnIndex(MediaStore.Images.Media.DATE_TAKEN));
                        //Log.e(TAG, "DATE_ADDED " + cursor.getColumnIndex(MediaStore.Images.Media.DATE_ADDED));
                        // Toast.makeText(context, "Is screenshot", Toast.LENGTH_SHORT).show();
                        //return;
                    } else {
                        // onScreenShotTaken(path, fileName);
                        // Toast.makeText(context, "Not screenshot", Toast.LENGTH_SHORT).show();
                        // Log.e(TAG, "Something changed, but it is not screenshot");
                    }
                }

                // }
            } else {
                Log.e("onChange ", "Cursor is null");
            }
        } catch (Throwable t) {
            Log.e("catch ", "Throwable " + t.getMessage());
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }
        super.onChange(selfChange, uri);
    }

    private boolean isScreenshot(String path, String filename) {
        return path != null && filename != null && (path.toLowerCase().contains("screenshot") ||
                filename.toLowerCase().contains("screenshot"));
    }

    private boolean isScreenshot(String... args) {
        for (String arg : args) {
            if (arg.toLowerCase().contains("screenshot")) {
                return true;
            }
        }
        return false;
    }

    protected void onScreenShotTaken(String path, String fileName) {
    }
}
