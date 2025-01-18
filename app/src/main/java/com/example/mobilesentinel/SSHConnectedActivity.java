package com.example.mobilesentinel;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import org.apache.sshd.client.SshClient;
import org.apache.sshd.client.channel.ClientChannel;
import org.apache.sshd.client.session.ClientSession;

import java.io.ByteArrayOutputStream;
import java.io.OutputStream;
import java.util.concurrent.TimeUnit;

public class SSHConnectedActivity extends AppCompatActivity {

    private SshClient client;
    private ClientSession session;
    private ClientChannel channel;

    private TextView shellOutput;
    private EditText commandInput;
    private Button sendButton;

    private ByteArrayOutputStream responseStream;

    private String host, username, password;
    private int port;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.ssh_connected_activity);

        // UI Elements
        shellOutput = findViewById(R.id.textView);
        commandInput = findViewById(R.id.commandInput); // Add this in your XML layout
        sendButton = findViewById(R.id.sendButton);     // Add this in your XML layout

        // Retrieve data from Intent
        Intent intent = getIntent();
        host = intent.getStringExtra("host");
        port = Integer.parseInt(intent.getStringExtra("port"));
        username = intent.getStringExtra("username");
        password = intent.getStringExtra("password");

        // Start the SSH connection in a background thread
        new Thread(this::startSSHConnection).start();

        // Set up the send button to send commands
        sendButton.setOnClickListener(v -> {
            String command = commandInput.getText().toString();
            if (!command.isEmpty()) {
                sendCommand(command);
            }
        });
    }

    private void startSSHConnection() {
        try {
            // Initialize the SSH client
            client = SshClient.setUpDefaultClient();
            client.start();

            // Establish the connection
            session = client.connect(username, host, port).verify(10000).getSession();
            session.addPasswordIdentity(password);
            session.auth().verify(5000);

            // Set up the channel for the shell
            channel = session.createChannel(ClientChannel.CHANNEL_SHELL);
            responseStream = new ByteArrayOutputStream();
            channel.setOut(responseStream);
            channel.open().verify(5, TimeUnit.SECONDS);

            runOnUiThread(() -> shellOutput.append("Connection established.\n"));

        } catch (Exception e) {
            e.printStackTrace();
            runOnUiThread(() -> shellOutput.append("Error: " + e.getMessage() + "\n"));
        }
    }

    private void sendCommand(String command) {
        new Thread(() -> {
            try (OutputStream pipedIn = channel.getInvertedIn()) {
                // Write the command to the SSH input
                pipedIn.write((command + "\n").getBytes());
                pipedIn.flush();

                // Wait for the response and update the output
                Thread.sleep(500); // Allow time for the command to execute
                String response = responseStream.toString();
                responseStream.reset(); // Clear the stream for the next command

                runOnUiThread(() -> {
                    shellOutput.append("> " + command + "\n"); // Show the command
                    shellOutput.append(response + "\n");       // Show the response
                });
            } catch (Exception e) {
                e.printStackTrace();
                runOnUiThread(() -> shellOutput.append("Error: " + e.getMessage() + "\n"));
            }
        }).start();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        try {
            if (channel != null) {
                channel.close();
            }
            if (session != null) {
                session.close();
            }
            if (client != null) {
                client.stop();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
