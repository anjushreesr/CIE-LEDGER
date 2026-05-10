package com.example.cieledger.repository;

import android.app.Application;

import androidx.lifecycle.LiveData;

import com.example.cieledger.database.AppDatabase;
import com.example.cieledger.database.StudentMarksDao;
import com.example.cieledger.model.StudentMarks;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class MarksRepository {

    private final StudentMarksDao dao;
    private final ExecutorService executor;

    public MarksRepository(Application application) {
        AppDatabase db = AppDatabase.getInstance(application);
        dao = db.studentMarksDao();
        executor = Executors.newSingleThreadExecutor();
    }

    public LiveData<List<StudentMarks>> getStudentsByClass(int classId) {
        return dao.getStudentsByClass(classId);
    }

    public void insertAll(List<StudentMarks> students) {
        executor.execute(() -> dao.insertAll(students));
    }

    public void insert(StudentMarks student) {
        executor.execute(() -> dao.insert(student));
    }

    public void update(StudentMarks student) {
        executor.execute(() -> dao.update(student));
    }

    public void delete(StudentMarks student) {
        executor.execute(() -> dao.delete(student));
    }

    public void deleteAllByClass(int classId) {
        executor.execute(() -> dao.deleteAllByClass(classId));
    }

    public void initRowsIfEmpty(int classId, int count, Runnable onDone) {
        executor.execute(() -> {
            int existing = dao.getStudentCount(classId);
            if (existing == 0) {
                List<StudentMarks> rows = new java.util.ArrayList<>();
                for (int i = 0; i < count; i++) {
                    rows.add(new StudentMarks(classId));
                }
                dao.insertAll(rows);
            }
            if (onDone != null) onDone.run();
        });
    }

    public void saveAllMarks(List<StudentMarks> students, Runnable onDone) {
        executor.execute(() -> {
            dao.insertAll(students);
            if (onDone != null) onDone.run();
        });
    }
}
