package com.example.mobilesentinel.entity;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "encrypted_notes")
public class EncryptedNote {

    @PrimaryKey(autoGenerate = true)
    public int id; // Unique identifier for each note

    @NonNull
    public String title; // The note title (e.g., "My Note")

    @NonNull
    public String encryptedContent; // The encrypted note content (e.g., "AES-encrypted data")

    // Default constructor for Room
    public EncryptedNote() {}
}
