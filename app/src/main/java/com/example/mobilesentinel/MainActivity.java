package com.example.mobilesentinel;

import android.content.res.Configuration;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import android.content.Intent;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {
    private Button btnNetworkScanner;
    private Button btnVulnerabilityScanner;
    private Button btnTrafficSniffing;
    private Button btnSSHClient;
    private Button btnPermissionAnalyzer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Apply the Current Theme (Dark/Light)
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM);
        setContentView(R.layout.activity_main);

        // Initialize buttons
        btnNetworkScanner = findViewById(R.id.btn_network_scanner);
        btnVulnerabilityScanner = findViewById(R.id.btn_vulnerability_scanner);
        btnTrafficSniffing = findViewById(R.id.btn_traffic_sniffing);
        btnSSHClient = findViewById(R.id.btn_ssh_client);
        btnPermissionAnalyzer = findViewById(R.id.btn_permission_analyzer); // New button

        // Set OnClickListener for Network Scanner button
        btnNetworkScanner.setOnClickListener(view -> {
            Intent intent = new Intent(MainActivity.this, NetworkScannerActivity.class);
            startActivity(intent);
        });

        // Set OnClickListener for Vulnerability Scanner button
        btnVulnerabilityScanner.setOnClickListener(view -> {
            Intent intent = new Intent(MainActivity.this, VulnerabilityScannerActivity.class);
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

        // Set OnClickListener for Permission Analyzer button
        btnPermissionAnalyzer.setOnClickListener(view -> {
            Intent intent = new Intent(MainActivity.this, PermissionAnalyzerActivity.class);
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