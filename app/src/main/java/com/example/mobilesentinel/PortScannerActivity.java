package com.example.mobilesentinel;

import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.ProgressBar;
import androidx.appcompat.app.AppCompatActivity;

import java.io.IOException;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class PortScannerActivity extends AppCompatActivity {
    private Button buttonScanPorts;
    private ProgressBar progressBar;
    private ListView listViewPorts;
    private ArrayAdapter<String> adapter;
    private List<String> portStatusList;
    private ExecutorService executorService;
    private Handler mainHandler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.port_scanner_activity);

        // Initialize UI elements
        buttonScanPorts = findViewById(R.id.buttonScanPorts);
        progressBar = findViewById(R.id.progressBar);
        listViewPorts = findViewById(R.id.listViewPorts);
        portStatusList = new ArrayList<>();

        // Use the custom layout for each ListView item
        adapter = new ArrayAdapter<>(this, R.layout.port_scanner_list_item, portStatusList);
        listViewPorts.setAdapter(adapter);

        // Executor service for running the port scan in background threads
        executorService = Executors.newFixedThreadPool(4);
        mainHandler = new Handler(Looper.getMainLooper());

        // Set click listener for the button
        buttonScanPorts.setOnClickListener(v -> startPortScan());
    }

    private void startPortScan() {
        // Clear previous results and show progress bar
        progressBar.setVisibility(ProgressBar.VISIBLE);
        portStatusList.clear();
        adapter.notifyDataSetChanged();

        // Run port scanning in the executor service
        executorService.execute(() -> scanPorts(1, 1024)); // Scans ports 1-1024
    }

    private void scanPorts(int startPort, int endPort) {
        // Iterate through ports and scan each one
        for (int port = startPort; port <= endPort; port++) {
            // Declare the portStatus variable inside the loop (no conflicting declarations)
            final String portStatus;
            String portStatus1;

            try (Socket socket = new Socket("127.0.0.1", port)) {
                portStatus1 = "Port " + port + ": OPEN";  // Assign value if the port is open
            } catch (IOException e) {
                portStatus1 = "Port " + port + ": CLOSED";  // Assign value if the port is closed
            }

            // Update the UI with the result on the main thread
            portStatus = portStatus1;
            mainHandler.post(() -> {
                portStatusList.add(portStatus); // Add port status to the list
                adapter.notifyDataSetChanged(); // Notify adapter that the data has changed
            });
        }

        // Hide the progress bar after scanning is complete
        mainHandler.post(() -> progressBar.setVisibility(ProgressBar.GONE));
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        executorService.shutdown(); // Shutdown executor service to avoid memory leaks
    }
}
