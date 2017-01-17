package com.luseen.screenshotreceiver;

import android.app.ActivityManager;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private TextView serviceStatus;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        findViewById(R.id.start_service).setOnClickListener(this);
        findViewById(R.id.stop_service).setOnClickListener(this);

        serviceStatus = (TextView) findViewById(R.id.service_status);
        isServiceRunning();
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.start_service:
                startService(new Intent(this, ScreenShotService.class));
                Toast.makeText(this, "ScreenShotService started", Toast.LENGTH_SHORT).show();
                isServiceRunning();
                break;
            case R.id.stop_service:
                stopService(new Intent(this, ScreenShotService.class));
                Toast.makeText(this, "ScreenShotService stopped", Toast.LENGTH_SHORT).show();
                isServiceRunning();
                break;
        }
    }

    private boolean isServiceRunning() {
        ActivityManager manager = (ActivityManager) getSystemService(ACTIVITY_SERVICE);
        for (ActivityManager.RunningServiceInfo service : manager.getRunningServices(Integer.MAX_VALUE)) {
            if ((getPackageName() + "." + ScreenShotService.class.getSimpleName()).equals(service.service.getClassName())) {
                serviceStatus.setText("ScreenShotService is running - " + true);
                return true;
            }
        }
        serviceStatus.setText("ScreenShotService is running - " + false);
        return false;
    }
}
