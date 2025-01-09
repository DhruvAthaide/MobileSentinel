package com.example.mobilesentinel;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import androidx.core.content.ContextCompat;
import android.util.TypedValue;
import android.util.Log;

public class TrafficSniffingActivity extends AppCompatActivity {

    private static final String TAG = "TrafficSniffingActivity";
    private static final int PERMISSION_REQUEST_CODE = 1;

    private TextView outputTextView;
    private TextView textView;
    private Button startButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.traffic_sniffing_activity);

        outputTextView = findViewById(R.id.outputTextView);
        startButton = findViewById(R.id.startButton);

        // Check if the current mode is dark mode and set text color
        int nightModeFlags = getResources().getConfiguration().uiMode & android.content.res.Configuration.UI_MODE_NIGHT_MASK;
        if (nightModeFlags == android.content.res.Configuration.UI_MODE_NIGHT_YES) {
            // Dark mode
            textView = findViewById(R.id.outputTextView);
            TypedValue typedValue = new TypedValue();
            getTheme().resolveAttribute(android.R.attr.textColorPrimary, typedValue, true);
            int textColor =typedValue.data; // Access data field
            textView.setTextColor(textColor);
        } else {
            // Light mode
            textView = findViewById(R.id.outputTextView);
            TypedValue typedValue = new TypedValue();
            getTheme().resolveAttribute(android.R.attr.textColorPrimary, typedValue, true);
            int textColor = typedValue.data; // Access data field
            textView.setTextColor(textColor);
        }

        startButton.setOnClickListener(v -> startSniffing());
    }

    private void startSniffing() {
        // Request permissions at runtime
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.INTERNET) != PackageManager.PERMISSION_GRANTED ||
                ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_WIFI_STATE) != PackageManager.PERMISSION_GRANTED ||
                ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED ||
                ActivityCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {

            ActivityCompat.requestPermissions(this, new String[]{
                    Manifest.permission.INTERNET,
                    Manifest.permission.ACCESS_WIFI_STATE,
                    Manifest.permission.READ_EXTERNAL_STORAGE,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE
            }, PERMISSION_REQUEST_CODE);
        } else {
            // Permissions already granted, start capturing packets
            capturePackets();
        }
    }

    private void capturePackets() {
        new Thread(() -> {
            try {
                // Command to start tcpdump
                String command = "tcpdump -i any -s 0 -w /sdcard/capture.pcap";

                // Execute the command
                Process process = Runtime.getRuntime().exec(command);

                // Read output from the command
                BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
                String line;
                while ((line = reader.readLine()) != null) {
                    // Update UI with captured packets
                    String finalLine = line; // Make the line effectively final for lambda use
                    runOnUiThread(() -> outputTextView.append(finalLine + "\n"));
                }

                // Show a Toast message
                runOnUiThread(() -> Toast.makeText(TrafficSniffingActivity.this, "Packet capture started!", Toast.LENGTH_SHORT).show());
            } catch (Exception e) {
                Log.e(TAG, "Error capturing packets", e);
                runOnUiThread(() -> Toast.makeText(TrafficSniffingActivity.this, "Error: " + e.getMessage(), Toast.LENGTH_LONG).show());
            }
        }).start();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == PERMISSION_REQUEST_CODE) {
            boolean allPermissionsGranted = true;
            for (int result : grantResults) {
                if (result != PackageManager.PERMISSION_GRANTED) {
                    allPermissionsGranted = false;
                    break;
                }
            }
            if (allPermissionsGranted) {
                // All permissions granted, start capturing packets
                capturePackets();
            } else {
                // Permissions denied, show a message to the user
                Toast.makeText(this, "Permission denied. Packet sniffing requires all permissions.", Toast.LENGTH_SHORT).show();
            }
        }
    }
}