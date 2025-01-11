package com.example.mobilesentinel;

import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;
import android.content.Intent;
import android.widget.ScrollView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.chaquo.python.PyObject;
import com.chaquo.python.Python;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

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
        securityTypeTextView = findViewById(R.id.securityType);
        networkModeTextView = findViewById(R.id.networkMode);
        terminalOutput = findViewById(R.id.terminalOutput);
        startWifiCrackerButton = findViewById(R.id.startWifiCrackerButton);
        scrollView = findViewById(R.id.scrollView);

        // Get WiFi data from Intent
        String wifiName = getIntent().getStringExtra("WIFI_NAME");
        String capabilities = getIntent().getStringExtra("CAPABILITIES");

        // Display WiFi information
        displayWifiInfo(wifiName, capabilities);

        // Set up button to start WiFi cracking
        startWifiCrackerButton.setOnClickListener(view -> startWiFiCrackingProcess(wifiName));
    }

    private void displayWifiInfo(String wifiName, String capabilities) {
        wifiNameTextView.setText(wifiName != null && !wifiName.isEmpty() ? wifiName : "WiFi Name Unavailable");

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

    private void startWiFiCrackingProcess(String wifiName) {
        if (wifiName == null || wifiName.isEmpty()) {
            Toast.makeText(this, "WiFi name is missing. Unable to start cracking process.", Toast.LENGTH_SHORT).show();
            return;
        }

        Python py = Python.getInstance();
        PyObject wifiCrackModule = py.getModule("wifi_crack");

        String wordlistPath;
        try {
            // Open the wordlist file from assets
            InputStream inputStream = getAssets().open("wordlist.txt");
            BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
            StringBuilder content = new StringBuilder();
            String line;

            // Read the wordlist file line by line
            while ((line = reader.readLine()) != null) {
                content.append(line).append("\n");
            }

            reader.close();
            wordlistPath = content.toString();
        } catch (IOException e) {
            // Handle the exception and notify the user
            updateTerminalOutput("Error reading wordlist file: " + e.getMessage() + "\n");
            return;
        }

        updateTerminalOutput("Starting WiFi cracking for network: " + wifiName + "\n");

        // Run the cracking process
        try {
            PyObject result = wifiCrackModule.callAttr("crack_wifi_password", wifiName, wordlistPath);
            if (result != null) {
                String successMessage = "Success! Password for network '" + wifiName + "' is: " + result.toString() + "\n";
                updateTerminalOutput(successMessage);
            } else {
                updateTerminalOutput("Failed to crack the password for network: " + wifiName + "\n");
            }
        } catch (Exception e) {
            updateTerminalOutput("Error during WiFi cracking: " + e.getMessage() + "\n");
        }
    }

    private String getWordlistFromAssets() {
        try {
            InputStream inputStream = getAssets().open("wordlist.txt");
            BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
            StringBuilder content = new StringBuilder();
            String line;

            while ((line = reader.readLine()) != null) {
                content.append(line).append("\n");
            }

            reader.close();
            return content.toString();
        } catch (Exception e) {
            updateTerminalOutput("Error reading wordlist: " + e.getMessage() + "\n");
            e.printStackTrace();
            return null;
        }
    }

    private void updateTerminalOutput(String message) {
        terminalOutput.append(message);
        scrollView.post(() -> scrollView.fullScroll(ScrollView.FOCUS_DOWN));
    }
}
