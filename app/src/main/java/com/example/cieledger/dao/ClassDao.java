package com.example.cieledger.database;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import com.example.cieledger.model.ClassEntity;

import java.util.List;

@Dao
public interface ClassDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    long insert(ClassEntity classEntity);

    @Update
    void update(ClassEntity classEntity);

    @Delete
    void delete(ClassEntity classEntity);

    // CRITICAL: filter by teacherId so each teacher only sees their own classes
    @Query("SELECT * FROM classes WHERE teacherId = :teacherId ORDER BY createdAt DESC")
    LiveData<List<ClassEntity>> getClassesByTeacher(String teacherId);

    @Query("SELECT * FROM classes WHERE id = :id")
    ClassEntity getClassById(int id);

    @Query("SELECT COUNT(*) FROM classes WHERE teacherId = :teacherId")
    LiveData<Integer> getClassCount(String teacherId);

    @Query("DELETE FROM classes WHERE teacherId = :teacherId")
    void deleteAllByTeacher(String teacherId);
}
