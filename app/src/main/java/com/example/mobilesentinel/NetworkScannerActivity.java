package com.example.mobilesentinel;

import android.os.Bundle;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public class NetworkScannerActivity extends AppCompatActivity {

    private TextView resultTextView;
    private ExecutorService executorService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_network_scanner);

        resultTextView = findViewById(R.id.resultTextView);
        executorService = Executors.newFixedThreadPool(10); // Thread pool for concurrent scanning

        // Start the network scan
        startNetworkScan("192.168.1.");
    }

    private void startNetworkScan(String baseIp) {
        List<Future<String>> futures = new ArrayList<>();

        for (int i = 1; i <= 255; i++) {
            String host = baseIp + i;
            futures.add(executorService.submit(new PortScanTask(host, 80, 200)));
        }

        for (Future<String> future : futures) {
            try {
                String result = future.get();  // Get scan result
                if (result != null) {
                    resultTextView.append(result + "\n");
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    // Inner class for the port scanning task
    private static class PortScanTask implements Callable<String> {
        private final String ip;
        private final int port;
        private final int timeout;

        public PortScanTask(String ip, int port, int timeout) {
            this.ip = ip;
            this.port = port;
            this.timeout = timeout;
        }

        @Override
        public String call() {
            if (isPortOpen(ip, port, timeout)) {
                return "Open port found at: " + ip;
            }
            return null;
        }

        private boolean isPortOpen(String ip, int port, int timeout) {
            try (Socket socket = new Socket()) {
                socket.connect(new InetSocketAddress(ip, port), timeout);
                return true;
            } catch (Exception e) {
                return false;
            }
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        executorService.shutdownNow(); // Shutdown executor to prevent memory leaks
    }
}