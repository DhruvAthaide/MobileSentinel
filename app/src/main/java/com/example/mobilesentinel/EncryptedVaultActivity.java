package com.example.mobilesentinel;

import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import android.widget.Button;
import android.widget.EditText;

import com.example.mobilesentinel.database.NoteDatabase;
import com.example.mobilesentinel.entity.EncryptedNote;

import java.security.Key;
import java.security.SecureRandom;
import java.util.Base64;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

public class EncryptedVaultActivity extends AppCompatActivity {

    private NoteDatabase database;
    private static final String AES_ALGORITHM = "AES";
    private static SecretKey secretKey;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.encrypted_vault_activity);

        // Generate AES Key
        secretKey = generateAESKey();

        // UI Components
        EditText inputText = findViewById(R.id.input_text);
        EditText encryptedText = findViewById(R.id.encrypted_text);
        EditText decryptedText = findViewById(R.id.decrypted_text);
        Button encryptButton = findViewById(R.id.encrypt_button);
        Button decryptButton = findViewById(R.id.decrypt_button);

        // Initialize the database
        database = NoteDatabase.getInstance(this);

        // Encrypt Button
        encryptButton.setOnClickListener(v -> {
            String input = inputText.getText().toString();
            try {
                String encrypted = encrypt(input, secretKey);

                encryptedText.setText(encrypted);

                new Thread(() -> {
                    EncryptedNote note = new EncryptedNote();
                    note.title = "Example Title";
                    note.encryptedContent = encrypted;
                    database.noteDao().insert(note);
                }).start();
            } catch (Exception e) {
                e.printStackTrace();
            }
        });

        // Decrypt Button
        decryptButton.setOnClickListener(v -> {
            String encrypted = encryptedText.getText().toString();
            try {
                String decrypted = decrypt(encrypted, secretKey);
                decryptedText.setText(decrypted);
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }

    // Generate AES Key
    private SecretKey generateAESKey() {
        try {
            KeyGenerator keyGenerator = KeyGenerator.getInstance(AES_ALGORITHM);
            keyGenerator.init(256, new SecureRandom()); // Use 256-bit key
            return keyGenerator.generateKey();
        } catch (Exception e) {
            throw new RuntimeException("Error generating AES key", e);
        }
    }

    // AES Encryption
    private String encrypt(String data, SecretKey key) throws Exception {
        Cipher cipher = Cipher.getInstance(AES_ALGORITHM);
        cipher.init(Cipher.ENCRYPT_MODE, key);
        byte[] encryptedBytes = cipher.doFinal(data.getBytes());
        return Base64.getEncoder().encodeToString(encryptedBytes); // Encode to Base64 for safe storage
    }

    // AES Decryption
    private String decrypt(String encryptedData, SecretKey key) throws Exception {
        Cipher cipher = Cipher.getInstance(AES_ALGORITHM);
        cipher.init(Cipher.DECRYPT_MODE, key);
        byte[] decryptedBytes = cipher.doFinal(Base64.getDecoder().decode(encryptedData)); // Decode from Base64
        return new String(decryptedBytes);
    }
}
