package com.luseen.screenshotreceiver;

import android.app.Service;
import android.content.Intent;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.IBinder;
import android.os.Message;
import android.provider.MediaStore;
import android.util.Log;

public class ScreenshotObserverService extends Service {

    private ScreenShotContentObserver screenShotContentObserver;

    public ScreenshotObserverService() {
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        Log.e("onCreate ", "onCreate");
        registerScreenShotObserver();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        return START_STICKY;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        unRegisterScreenShotObserver();
    }

    private void registerScreenShotObserver() {
        HandlerThread handlerThread = new HandlerThread("content_observer");
        handlerThread.start();
        final Handler handler = new Handler(handlerThread.getLooper()) {
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
            }
        };

        screenShotContentObserver = new ScreenShotContentObserver(handler, this) {
            @Override
            protected void onScreenShotTaken(String path, String fileName) {
                super.onScreenShotTaken(path, fileName);
                ScreenShotActivity.startScreenShotActivity(ScreenshotObserverService.this, path, fileName);
            }
        };

        getContentResolver().registerContentObserver(
                MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                true,
                screenShotContentObserver
        );
    }

    private void unRegisterScreenShotObserver() {
        getContentResolver().unregisterContentObserver(screenShotContentObserver);
    }
}
