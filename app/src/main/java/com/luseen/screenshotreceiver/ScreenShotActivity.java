package com.luseen.screenshotreceiver;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.File;

import static android.content.Intent.FLAG_ACTIVITY_NEW_TASK;

public class ScreenShotActivity extends AppCompatActivity {

    public static final String PATH_INTENT_KEY = "path_intent_key";
    public static final String FILENAME_INTENT_KEY = "filename_intent_key";

    public static void startScreenShotActivity(Context context, String path, String fileName) {
        Intent starter = new Intent(context, ScreenShotActivity.class);
        starter.setFlags(FLAG_ACTIVITY_NEW_TASK);
        starter.putExtra(PATH_INTENT_KEY, path);
        starter.putExtra(FILENAME_INTENT_KEY, fileName);
        context.startActivity(starter);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_screen_shot);

        String path = getIntent().getStringExtra(PATH_INTENT_KEY);
        String fileName = getIntent().getStringExtra(FILENAME_INTENT_KEY);

        File imgFile = new File(path);
        if (imgFile.exists()) {
            Bitmap myBitmap = BitmapFactory.decodeFile(imgFile.getAbsolutePath());
            ImageView screenShotImage = (ImageView) findViewById(R.id.screenshot_image);
            screenShotImage.setScaleX(0.7f);
            screenShotImage.setScaleY(0.7f);
            screenShotImage.setImageBitmap(myBitmap);
        }
    }
}
