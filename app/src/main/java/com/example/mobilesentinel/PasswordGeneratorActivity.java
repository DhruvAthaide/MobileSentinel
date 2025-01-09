package com.example.mobilesentinel;

import android.os.Bundle;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.SeekBar;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

public class PasswordGeneratorActivity extends AppCompatActivity {
    private EditText etPassword;
    private SeekBar seekBarLength;
    private TextView tvLength;
    private CheckBox cbIncludeLowercase, cbIncludeUppercase, cbIncludeNumbers, cbIncludeSymbols, cbExcludeDuplicates, cbIncludeSpaces;
    private Button btnGenerate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.password_generator_activity);

        // Initialize UI components
        etPassword = findViewById(R.id.etPassword);
        seekBarLength = findViewById(R.id.seekBarLength);
        tvLength = findViewById(R.id.tvLength);
        cbIncludeLowercase = findViewById(R.id.cbIncludeLowercase);
        cbIncludeUppercase = findViewById(R.id.cbIncludeUppercase);
        cbIncludeNumbers = findViewById(R.id.cbIncludeNumbers);
        cbIncludeSymbols = findViewById(R.id.cbIncludeSymbols);
        cbExcludeDuplicates = findViewById(R.id.cbExcludeDuplicates);
        cbIncludeSpaces = findViewById(R.id.cbIncludeSpaces);
        btnGenerate = findViewById(R.id.btnGenerate);

        // Update password length text as the slider moves
        seekBarLength.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                int length = Math.max(progress, 8);
                tvLength.setText("Password Length: " + length);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {}

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {}
        });

        // Generate Password on Button Click
        btnGenerate.setOnClickListener(v -> {
            int length = Math.max(seekBarLength.getProgress(), 8);
            boolean includeLowercase = cbIncludeLowercase.isChecked();
            boolean includeUppercase = cbIncludeUppercase.isChecked();
            boolean includeNumbers = cbIncludeNumbers.isChecked();
            boolean includeSymbols = cbIncludeSymbols.isChecked();
            boolean excludeDuplicates = cbExcludeDuplicates.isChecked();
            boolean includeSpaces = cbIncludeSpaces.isChecked();

            String password = generatePassword(length, includeLowercase, includeUppercase, includeNumbers, includeSymbols, excludeDuplicates, includeSpaces);
            etPassword.setText(password);
        });
    }

    // Generate Password Methods
    private String generatePassword(int length, boolean includeLowercase, boolean includeUppercase, boolean includeNumbers, boolean includeSymbols, boolean excludeDuplicates, boolean includeSpaces) {
        String lowercase = "abcdefghijklmnopqrstuvwxyz";
        String uppercase = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
        String numbers = "0123456789";
        String symbols = "!@#$%^&*()-_=+[]{}|;:'\",.<>?/`~";
        String spaces = " ";

        StringBuilder charPool = new StringBuilder();

        if (includeLowercase) charPool.append(lowercase);
        if (includeUppercase) charPool.append(uppercase);
        if (includeNumbers) charPool.append(numbers);
        if (includeSymbols) charPool.append(symbols);
        if (includeSpaces) charPool.append(spaces);

        if (charPool.length() == 0) {
            return "Please select at least one option.";
        }

        List<Character> characters = new ArrayList<>();
        for (char c : charPool.toString().toCharArray()) {
            characters.add(c);
        }

        if (excludeDuplicates) {
            Collections.shuffle(characters);
        }

        Random random = new Random();
        StringBuilder password = new StringBuilder();

        while (password.length() < length) {
            char nextChar = characters.get(random.nextInt(characters.size()));
            if (!excludeDuplicates || password.indexOf(String.valueOf(nextChar)) == -1) {
                password.append(nextChar);
            }
        }

        return password.toString();
    }
}