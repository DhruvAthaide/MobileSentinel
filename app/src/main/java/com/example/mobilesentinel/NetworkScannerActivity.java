package com.example.mobilesentinel;

import android.Manifest;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.os.Handler;
import android.widget.ListView;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import java.util.List;

public class NetworkScannerActivity extends AppCompatActivity {

    private ListView networkListView;
    private TextView resultTextView;
    private WifiManager wifiManager;
    private Handler handler;
    private Runnable wifiScanRunnable;
    private static final int SCAN_INTERVAL = 5000; // 5 seconds

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.network_scanner_activity);

        networkListView = findViewById(R.id.networkListView);
        resultTextView = findViewById(R.id.resultTextView);
        wifiManager = (WifiManager) getApplicationContext().getSystemService(Context.WIFI_SERVICE);
        handler = new Handler();

        // Register a broadcast receiver to listen for scan results
        IntentFilter filter = new IntentFilter(WifiManager.SCAN_RESULTS_AVAILABLE_ACTION);
        registerReceiver(wifiScanReceiver, filter);

        // Start the scanning process
        startWifiScanning();

        // Allows the WiFi Network to be Clickable to show the Wifi Details
        networkListView.setOnItemClickListener((parent, view, position, id) -> {
            ScanResult selectedNetwork = (ScanResult) parent.getItemAtPosition(position);

            Intent intent = new Intent(NetworkScannerActivity.this, WifiDetailsActivity.class);
            intent.putExtra("WIFI_NAME", selectedNetwork.SSID); // Updated key
            intent.putExtra("CAPABILITIES", selectedNetwork.capabilities); // Updated key
            startActivity(intent);
        });
    }

    private void startWifiScanning() {
        // Runnable to perform Wi-Fi scanning
        wifiScanRunnable = new Runnable() {
            @Override
            public void run() {
                if (!checkPermissions()) {
                    ActivityCompat.requestPermissions(NetworkScannerActivity.this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1);
                    return;
                }
                wifiManager.startScan();
                handler.postDelayed(this, SCAN_INTERVAL); // Schedule next scan
            }
        };
        handler.post(wifiScanRunnable); // Start the first scan
    }

    private boolean checkPermissions() {
        return ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED;
    }

    // BroadcastReceiver to handle the Wi-Fi scan results
    private final BroadcastReceiver wifiScanReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            boolean success = intent.getBooleanExtra(WifiManager.EXTRA_RESULTS_UPDATED, false);
            if (success) {
                displayScanResults();
            } else {
                resultTextView.setText(getString(R.string.wifi_scan_failed));
            }
        }
    };

    private void displayScanResults() {
        List<ScanResult> results = wifiManager.getScanResults();
        if (results.isEmpty()) {
            resultTextView.setText(getString(R.string.no_available_networks));
            networkListView.setAdapter(null);
        } else {
            resultTextView.setText(""); // Clear the text
            // Set the adapter to display the networks
            NetworkListAdapter adapter = new NetworkListAdapter(this, results);
            networkListView.setAdapter(adapter);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        handler.removeCallbacks(wifiScanRunnable); // Stop scanning
        unregisterReceiver(wifiScanReceiver); // Unregister the receiver
    }
}