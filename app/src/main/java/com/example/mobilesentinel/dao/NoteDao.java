package com.example.mobilesentinel.dao;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.example.mobilesentinel.entity.EncryptedNote;

import java.util.List;

@Dao
public interface NoteDao {

    @Insert
    void insert(EncryptedNote note);

    @Update
    void update(EncryptedNote note);

    @Delete
    void delete(EncryptedNote note);

    @Query("SELECT * FROM encrypted_notes ORDER BY id DESC")
    List<EncryptedNote> getAllNotes();
}
