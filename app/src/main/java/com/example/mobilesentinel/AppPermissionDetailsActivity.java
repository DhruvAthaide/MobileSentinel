package com.example.mobilesentinel;

import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.os.Bundle;
import android.util.TypedValue;
import android.widget.LinearLayout;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import android.content.res.Configuration;

public class AppPermissionDetailsActivity extends AppCompatActivity {
    private LinearLayout layoutPermissions;
    private PackageManager packageManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.app_permission_details_activity);

        layoutPermissions = findViewById(R.id.layout_permissions);
        packageManager = getPackageManager();

        String packageName = getIntent().getStringExtra("packageName");
        try {
            PackageInfo packageInfo = packageManager.getPackageInfo(packageName, PackageManager.GET_PERMISSIONS);

            // Displaying the Application's Permissions
            if (packageInfo.requestedPermissions != null) {
                for (String permission : packageInfo.requestedPermissions) {
                    TextView textView = new TextView(this);
                    textView.setText(permission);

                    // Logic to determine if the permission is considered unnecessary
                    if (isUnnecessaryPermission(permission)) {
                        textView.setTextColor(Color.RED); // Set text color to RED for unnecessary permissions
                    } else {
                        // Check if the current theme is light or dark
                        boolean isLightTheme = isLightTheme();

                        // Set the text color based on the current theme
                        if (isLightTheme) {
                            textView.setTextColor(Color.parseColor("#000000")); // Light theme color
                        } else {
                            textView.setTextColor(Color.parseColor("#EBEBEB")); // Dark theme color
                        }
                    }

                    // Add the TextView to the layout for permissions
                    layoutPermissions.addView(textView);
                }
            }
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
    }

    // Method to determine if the permission is unnecessary
    private boolean isUnnecessaryPermission(String permission) {
        // Defining unnecessary permissions
        return permission.contains("CONTACTS") ||
                permission.contains("LOCATION") ||
                permission.contains("SMS") ||
                permission.contains("CALENDAR") ||
                permission.contains("CAMERA") ||
                permission.contains("MICROPHONE") ||
                permission.contains("STORAGE") ||
                permission.contains("PHONE") ||
                permission.contains("SENSORS") ||
                permission.contains("BLUETOOTH") ||
                permission.contains("BODY_SENSORS") ||
                permission.contains("CALL_LOG") ||
                permission.contains("READ_EXTERNAL_STORAGE") ||
                permission.contains("WRITE_EXTERNAL_STORAGE") ||
                permission.contains("RECORD_AUDIO");
    }

    // Method to determine if the current theme is light
    private boolean isLightTheme() {
        // Get the current night mode
        int nightModeFlags = getResources().getConfiguration().uiMode & Configuration.UI_MODE_NIGHT_MASK;
        // Return true if the current mode is not night (i.e., light mode)
        return nightModeFlags != Configuration.UI_MODE_NIGHT_YES;
    }
}
