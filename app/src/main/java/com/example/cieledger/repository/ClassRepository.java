package com.example.cieledger.repository;

import android.app.Application;

import androidx.lifecycle.LiveData;

import com.example.cieledger.database.AppDatabase;
import com.example.cieledger.database.ClassDao;
import com.example.cieledger.model.ClassEntity;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ClassRepository {

    private final ClassDao classDao;
    private final ExecutorService executor;

    public ClassRepository(Application application) {
        AppDatabase db = AppDatabase.getInstance(application);
        classDao = db.classDao();
        executor = Executors.newSingleThreadExecutor();
    }

    // ── Read ─────────────────────────────────────────────────────
    public LiveData<List<ClassEntity>> getClassesByTeacher(String teacherId) {
        return classDao.getClassesByTeacher(teacherId);
    }

    public LiveData<Integer> getClassCount(String teacherId) {
        return classDao.getClassCount(teacherId);
    }

    // ── Write (background thread) ─────────────────────────────────
    public void insert(ClassEntity classEntity, InsertCallback callback) {
        executor.execute(() -> {
            long id = classDao.insert(classEntity);
            if (callback != null) callback.onInserted((int) id);
        });
    }

    public void update(ClassEntity classEntity) {
        executor.execute(() -> classDao.update(classEntity));
    }

    public void delete(ClassEntity classEntity) {
        executor.execute(() -> classDao.delete(classEntity));
    }

    public void getClassById(int id, SingleClassCallback callback) {
        executor.execute(() -> {
            ClassEntity c = classDao.getClassById(id);
            if (callback != null) callback.onResult(c);
        });
    }

    // ── Callbacks ─────────────────────────────────────────────────
    public interface InsertCallback {
        void onInserted(int newId);
    }

    public interface SingleClassCallback {
        void onResult(ClassEntity classEntity);
    }
}