package com.example.cieledger.database;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import com.example.cieledger.model.StudentMarks;

import java.util.List;

@Dao
public interface StudentMarksDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    long insert(StudentMarks student);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertAll(List<StudentMarks> students);

    @Update
    void update(StudentMarks student);

    @Delete
    void delete(StudentMarks student);

    @Query("SELECT * FROM student_marks WHERE classId = :classId ORDER BY id ASC")
    LiveData<List<StudentMarks>> getStudentsByClass(int classId);

    @Query("SELECT * FROM student_marks WHERE classId = :classId ORDER BY id ASC")
    List<StudentMarks> getStudentsByClassSync(int classId);

    @Query("DELETE FROM student_marks WHERE classId = :classId")
    void deleteAllByClass(int classId);

    @Query("SELECT COUNT(*) FROM student_marks WHERE classId = :classId")
    int getStudentCount(int classId);
}
