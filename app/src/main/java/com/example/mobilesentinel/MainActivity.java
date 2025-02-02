package com.example.mobilesentinel;

import android.os.Bundle;
import android.content.res.Configuration;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import android.content.Intent;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {

    private Button btnNetworkScanner;
    private Button btnDeviceVulnerability;
    private Button btnTrafficSniffing;
    private Button btnSSHClient;
    private Button btnAntiKeylogger;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Apply the Current Theme based on User Device theme (Dark/Light)
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM);
        setContentView(R.layout.main_activity);

        // Initialize buttons
        btnNetworkScanner = findViewById(R.id.btn_network_scanner);
        btnDeviceVulnerability = findViewById(R.id.btn_device_vulnerability);
        btnTrafficSniffing = findViewById(R.id.btn_traffic_sniffing);
        btnSSHClient = findViewById(R.id.btn_ssh_client);
        btnAntiKeylogger = findViewById(R.id.btn_antikeylogger);


        // Set OnClickListener for Network Scanner button
        btnNetworkScanner.setOnClickListener(view -> {
            Intent intent = new Intent(MainActivity.this, NetworkScannerActivity.class);
            startActivity(intent);
        });

        // Set OnClickListener for Device Vulnerability button
        btnNetworkScanner.setOnClickListener(view -> {
            Intent intent = new Intent(MainActivity.this, DeviceVulnerability.class);
            startActivity(intent);
        });

        // Set OnClickListener for Traffic Sniffing button
        btnTrafficSniffing.setOnClickListener(view -> {
            Intent intent = new Intent(MainActivity.this, TrafficSniffingActivity.class);
            startActivity(intent);
        });

        // Set OnClickListener for SSH Client button
        btnSSHClient.setOnClickListener(view -> {
            Intent intent = new Intent(MainActivity.this, SSHClientActivity.class);
            startActivity(intent);
        });

        // Set OnClickListener for Anti-Keylogger button
        btnAntiKeylogger.setOnClickListener(view -> {
            Intent intent = new Intent(MainActivity.this, AntiKeyloggerActivity.class);
            startActivity(intent);
        });
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        // Apply the Current Theme when configuration changes
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM);
    }
}