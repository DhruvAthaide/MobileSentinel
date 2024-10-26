package com.example.mobilesentinel;

import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.os.Bundle;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class AppPermissionDetailsActivity extends AppCompatActivity {
    private LinearLayout layoutPermissions;
    private PackageManager packageManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_app_permission_details);

        layoutPermissions = findViewById(R.id.layout_permissions);
        packageManager = getPackageManager();

        String packageName = getIntent().getStringExtra("packageName");
        try {
            PackageInfo packageInfo = packageManager.getPackageInfo(packageName, PackageManager.GET_PERMISSIONS);

            // Display permissions
            if (packageInfo.requestedPermissions != null) {
                for (String permission : packageInfo.requestedPermissions) {
                    TextView textView = new TextView(this);
                    textView.setText(permission);

                    // Example logic to determine if a permission is unnecessary (customize as needed)
                    if (isUnnecessaryPermission(permission)) {
                        textView.setTextColor(Color.RED);
                    } else {
                        textView.setTextColor(Color.BLACK);
                    }
                    layoutPermissions.addView(textView);
                }
            }
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
    }

    private boolean isUnnecessaryPermission(String permission) {
        // Define your own criteria for unnecessary permissions
        // Example: Mark permissions related to contacts, location, or SMS as unnecessary
        return permission.contains("CONTACTS") || permission.contains("LOCATION") || permission.contains("SMS");
    }
}