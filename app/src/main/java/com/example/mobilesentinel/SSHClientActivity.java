package com.example.mobilesentinel;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import com.jcraft.jsch.Channel;
import com.jcraft.jsch.ChannelExec;
import com.jcraft.jsch.JSch;
import com.jcraft.jsch.Session;
import java.io.ByteArrayOutputStream;

public class SSHClientActivity extends AppCompatActivity {

    private EditText edtHostname, edtUsername, edtPassword;
    private Button connectButton;
    private TextView outputTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.ssh_client_activity);

        // Initialize UI components
        edtHostname = findViewById(R.id.edtHostname);
        edtUsername = findViewById(R.id.edtUsername);
        edtPassword = findViewById(R.id.edtPassword);
        connectButton = findViewById(R.id.connectButton);
        outputTextView = findViewById(R.id.outputTextView);

        connectButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String hostname = edtHostname.getText().toString();
                String username = edtUsername.getText().toString();
                String password = edtPassword.getText().toString();

                connectSSH(hostname, username, password);
            }
        });
    }

    private void connectSSH(String hostname, String username, String password) {
        new Thread(() -> {
            try {
                // Initialize JSch
                JSch jsch = new JSch();
                Session session = jsch.getSession(username, hostname, 22);
                session.setPassword(password);

                // Set SSH configuration options
                session.setConfig("StrictHostKeyChecking", "no");
                session.connect();

                // Open an execution channel
                Channel channel = session.openChannel("exec");
                ((ChannelExec) channel).setCommand("ls"); // Example command

                ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
                channel.setOutputStream(outputStream);
                channel.connect();

                // Wait for command to finish
                while (!channel.isClosed()) {
                    Thread.sleep(100);
                }

                // Display the result
                runOnUiThread(() -> outputTextView.setText(outputStream.toString()));

                // Disconnect
                channel.disconnect();
                session.disconnect();
            } catch (Exception e) {
                runOnUiThread(() -> outputTextView.setText("Error: " + e.getMessage()));
            }
        }).start();
    }
}
