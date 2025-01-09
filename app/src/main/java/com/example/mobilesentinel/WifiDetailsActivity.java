package com.example.mobilesentinel;

import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;
import android.content.Intent;
import android.widget.ScrollView;
import android.widget.Toast;
//import com.chaquo.python.Python;
//import com.chaquo.python.PyObject;
import androidx.appcompat.app.AppCompatActivity;

public class WifiDetailsActivity extends AppCompatActivity {

    private TextView wifiNameTextView;
    private TextView securityTypeTextView;
    private TextView networkModeTextView;
    private TextView terminalOutput;
    private Button startWifiCrackerButton;
    private ScrollView scrollView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.wifi_details_activity);

        // Initialize UI components
        wifiNameTextView = findViewById(R.id.wifiName);
        securityTypeTextView = findViewById(R.id.securityType); // To be Coded Later
        networkModeTextView = findViewById(R.id.networkMode); // To be Coded Later
        terminalOutput = findViewById(R.id.terminalOutput); // To be Coded Later
        startWifiCrackerButton = findViewById(R.id.startWifiCrackerButton); // To be Coded Later
        scrollView = findViewById(R.id.scrollView); // To be Coded Later

        // Get WiFi data from Intent
        String wifiName = getIntent().getStringExtra("WIFI_NAME");
        String capabilities = getIntent().getStringExtra("CAPABILITIES");

        // Display WiFi information
        displayWifiInfo(wifiName, capabilities);

        // Set up button to start WiFi cracking
        startWifiCrackerButton.setOnClickListener(view -> startWiFiCrackingProcess());
    }

    private void displayWifiInfo(String wifiName, String capabilities) {
        // Display WiFi name with fallback text
        wifiNameTextView.setText(wifiName != null && !wifiName.isEmpty() ? wifiName : "WiFi Name Unavailable");

        // Interpret and display security type and network mode
        if (capabilities != null) {
            if (capabilities.contains("WPA2")) {
                securityTypeTextView.setText("WPA2 (Secure)");
            } else if (capabilities.contains("WPA")) {
                securityTypeTextView.setText("WPA (Secure)");
            } else if (capabilities.contains("WEP")) {
                securityTypeTextView.setText("WEP (Less Secure)");
            } else {
                securityTypeTextView.setText("Open Network (No Security)");
            }

            if (capabilities.contains("ESS")) {
                networkModeTextView.setText("ESS (Extended Service Set)");
            } else {
                networkModeTextView.setText("Other Network Mode");
            }
        }
    }

    private void startWiFiCrackingProcess() {

    }

    private void sendCommandToTermux(String command) {
        Intent termuxIntent = new Intent("com.termux.RUN_COMMAND");
        termuxIntent.setPackage("com.termux");
        termuxIntent.putExtra("com.termux.RUN_COMMAND_PATH", "/data/data/com.termux/files/usr/bin/bash");
        termuxIntent.putExtra("com.termux.RUN_COMMAND_ARGUMENTS", new String[]{"-c", command});
        termuxIntent.putExtra("com.termux.RUN_COMMAND_BACKGROUND", true);

        try {
            startActivity(termuxIntent);
            updateTerminalOutput("Running command: " + command + "\n");
        } catch (Exception e) {
            Toast.makeText(this, "Error starting Termux: " + e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }

    private void updateTerminalOutput(String message) {
        terminalOutput.append(message);
        // Automatically scroll to the latest message
        scrollView.post(() -> scrollView.fullScroll(ScrollView.FOCUS_DOWN));
    }
}