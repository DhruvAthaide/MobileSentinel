package com.example.mobilesentinel;

import android.os.Bundle;
import android.content.res.Configuration;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import android.content.Intent;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {

    private Button btnNetworkScanner;
    private Button btnVulnerabilityScanner;
    private Button btnTrafficSniffing;
    private Button btnSSHClient;
    private Button btnPermissionAnalyzer;
    private Button btnPasswordGenerator;
    private Button btnPortScanner;
    private Button btnAntiKeylogger;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Apply the Current Theme based on User Device theme (Dark/Light)
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM);
        setContentView(R.layout.main_activity);

        // Initialize buttons
        btnNetworkScanner = findViewById(R.id.btn_network_scanner);
        btnVulnerabilityScanner = findViewById(R.id.btn_vulnerability_scanner);
        btnTrafficSniffing = findViewById(R.id.btn_traffic_sniffing);
        btnSSHClient = findViewById(R.id.btn_ssh_client);
        btnPermissionAnalyzer = findViewById(R.id.btn_permission_analyzer);
        btnPasswordGenerator = findViewById(R.id.btn_password_generator);
        btnPortScanner = findViewById(R.id.btn_port_scanner);
        btnAntiKeylogger = findViewById(R.id.btn_antikeylogger);

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

        // Set OnClickListener for Password Generator button
        btnPasswordGenerator.setOnClickListener(view -> {
            Intent intent = new Intent(MainActivity.this, PasswordGeneratorActivity.class);
            startActivity(intent);
        });

        // Set OnClickListener for Port Scanner button
        btnPortScanner.setOnClickListener(view -> {
            Intent intent = new Intent(MainActivity.this, PortScannerActivity.class);
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