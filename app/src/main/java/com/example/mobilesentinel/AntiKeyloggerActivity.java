package com.example.mobilesentinel;

import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.provider.Settings;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;
import java.util.List;
import java.util.Arrays;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

public class AntiKeyloggerActivity extends AppCompatActivity {

    private static final String TAG = "AntiKeyloggerActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.antikeylogger_activity);

        // Detect Overlay Apps
        findViewById(R.id.btnDetectOverlays).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkForOverlays();
            }
        });

        // Scan Installed Apps
        findViewById(R.id.btnScanApps).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                scanInstalledApps();
            }
        });

        // Redirect to Accessibility Settings
        findViewById(R.id.btnEnableService).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openAccessibilitySettings();
            }
        });
    }

    private void checkForOverlays() {
        if (Settings.canDrawOverlays(this)) {
            Toast.makeText(this, "Warning: Overlay permission is active. Be cautious!", Toast.LENGTH_LONG).show();
        } else {
            Toast.makeText(this, "No overlay permissions detected.", Toast.LENGTH_SHORT).show();
        }
    }

    private void scanInstalledApps() {
        PackageManager packageManager = getPackageManager();
        StringBuilder suspiciousApps = new StringBuilder();

        List<String> trustedApps = Arrays.asList(
                "com.google.android.apps.accessibility",
                "com.facebook.messenger",
                "com.microsoft.teams"
        ); // Add package names of trusted apps here

        for (PackageInfo packageInfo : packageManager.getInstalledPackages(PackageManager.GET_PERMISSIONS)) {
            if (packageInfo.requestedPermissions != null) {
                boolean isSuspicious = false;

                for (String permission : packageInfo.requestedPermissions) {
                    if ((permission.equals("android.permission.BIND_ACCESSIBILITY_SERVICE") ||
                            permission.equals("android.permission.SYSTEM_ALERT_WINDOW")) &&
                            !trustedApps.contains(packageInfo.packageName)) {
                        isSuspicious = true;
                    }
                }

                if (isSuspicious) {
                    suspiciousApps.append(packageInfo.packageName)
                            .append(" (")
                            .append(packageInfo.applicationInfo.loadLabel(packageManager))
                            .append(")\n");
                    Log.d(TAG, "Suspicious app detected: " + packageInfo.packageName);
                }
            }
        }

        TextView tvResult = findViewById(R.id.tvResult);
        if (suspiciousApps.length() > 0) {
            tvResult.setText("Suspicious Apps Detected:\n" + suspiciousApps);
        } else {
            tvResult.setText("No suspicious apps detected.");
        }
    }

    private void openAccessibilitySettings() {
        Intent intent = new Intent(Settings.ACTION_ACCESSIBILITY_SETTINGS);
        startActivity(intent);
    }
}