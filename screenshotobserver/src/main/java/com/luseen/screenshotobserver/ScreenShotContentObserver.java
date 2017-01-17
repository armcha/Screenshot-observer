package com.luseen.screenshotobserver;

import android.content.Context;
import android.database.ContentObserver;
import android.database.Cursor;
import android.net.Uri;
import android.os.Handler;
import android.provider.MediaStore;
import android.util.Log;

import java.io.File;

/**
 * Created by Chatikyan on 08.01.2017.
 */

class ScreenShotContentObserver extends ContentObserver {

    private static final String TAG = ScreenShotContentObserver.class.getSimpleName();
    private Context context;

    ScreenShotContentObserver(Handler handler, Context context) {
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
        Cursor cursor = null;
        try {
            cursor = context.getContentResolver().query(uri, new String[]{
                    MediaStore.Images.Media.DISPLAY_NAME,
                    MediaStore.Images.Media.DATA
            }, null, null, null);
            if (cursor != null && cursor.moveToLast()) {
                int displayNameColumnIndex = cursor.getColumnIndex(MediaStore.Images.Media.DISPLAY_NAME);
                int dataColumnIndex = cursor.getColumnIndex(MediaStore.Images.Media.DATA);
                String fileName = cursor.getString(displayNameColumnIndex);
                String path = cursor.getString(dataColumnIndex);

                if (new File(path).lastModified() >= System.currentTimeMillis() - 10000) {
                    if (isScreenshot(path)) {
                        onScreenShot(path, fileName);
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
                } else {
                    cursor.close();
                    return;
                }
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

    private boolean isScreenshot(String path) {
        return path != null && path.toLowerCase().contains("screenshot");
    }

    protected void onScreenShot(String path, String fileName) {
    }
}
