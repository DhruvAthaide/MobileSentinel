package com.example.mobilesentinel;

import android.content.Intent;
import android.os.Bundle;
import android.provider.Settings;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

public class DebuggingDevelopmentActivity extends AppCompatActivity {

    private TextView developerOptionsStatus;
    private TextView usbDebuggingStatus;
    private Button openSettingsButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.debugging_development_activity);

        // Initialize UI components
        developerOptionsStatus = findViewById(R.id.developerOptionsStatus);
        usbDebuggingStatus = findViewById(R.id.usbDebuggingStatus);
        openSettingsButton = findViewById(R.id.openSettingsButton);

        // Check Developer Options and USB Debugging status
        checkDebuggingSettings();

        // Set up button to open settings
        openSettingsButton.setOnClickListener(view -> openDeveloperOptionsSettings());
    }

    private void checkDebuggingSettings() {
        boolean isDeveloperOptionsEnabled = Settings.Global.getInt(
                getContentResolver(),
                Settings.Global.DEVELOPMENT_SETTINGS_ENABLED,
                0
        ) == 1;

        boolean isUsbDebuggingEnabled = Settings.Global.getInt(
                getContentResolver(),
                Settings.Global.ADB_ENABLED,
                0
        ) == 1;

        // Update UI with status
        developerOptionsStatus.setText(isDeveloperOptionsEnabled ? "Enabled" : "Disabled");
        developerOptionsStatus.setTextColor(isDeveloperOptionsEnabled ? 0xFF8CFF98 : 0xFFE01C15);

        usbDebuggingStatus.setText(isUsbDebuggingEnabled ? "Enabled" : "Disabled");
        usbDebuggingStatus.setTextColor(isUsbDebuggingEnabled ? 0xFF8CFF98 : 0xFFE01C15);

        // Notify user if settings are not enabled
        if (!isDeveloperOptionsEnabled || !isUsbDebuggingEnabled) {
            Toast.makeText(this, "Please enable Developer Options and USB Debugging.", Toast.LENGTH_LONG).show();
        }
    }

    private void openDeveloperOptionsSettings() {
        Intent intent = new Intent(Settings.ACTION_APPLICATION_DEVELOPMENT_SETTINGS);
        startActivity(intent);
    }
}