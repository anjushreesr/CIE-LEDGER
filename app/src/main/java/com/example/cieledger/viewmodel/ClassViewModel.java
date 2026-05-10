package com.example.cieledger.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.example.cieledger.model.ClassEntity;
import com.example.cieledger.repository.ClassRepository;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.List;

public class ClassViewModel extends AndroidViewModel {

    private final ClassRepository repository;
    private LiveData<List<ClassEntity>> classes;
    private LiveData<Integer> classCount;

    public ClassViewModel(@NonNull Application application) {
        super(application);
        repository = new ClassRepository(application);
        String uid = getCurrentUid();
        classes    = repository.getClassesByTeacher(uid);
        classCount = repository.getClassCount(uid);
    }

    public String getCurrentUid() {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        return user != null ? user.getUid() : "";
    }

    public String getCurrentUserName() {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user == null) return "Teacher";
        String name = user.getDisplayName();
        if (name != null && !name.isEmpty()) return name;
        String email = user.getEmail();
        return email != null ? email.split("@")[0] : "Teacher";
    }

    public String getCurrentEmail() {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        return user != null && user.getEmail() != null ? user.getEmail() : "";
    }

    // ── LiveData ─────────────────────────────────────────────────
    public LiveData<List<ClassEntity>> getClasses() {
        return classes;
    }

    public LiveData<Integer> getClassCount() {
        return classCount;
    }

    // ── Operations ───────────────────────────────────────────────
    public void insertClass(ClassEntity c, ClassRepository.InsertCallback cb) {
        c.setTeacherId(getCurrentUid());
        repository.insert(c, cb);
    }

    public void updateClass(ClassEntity c) {
        repository.update(c);
    }

    public void deleteClass(ClassEntity c) {
        repository.delete(c);
    }

    public void getClassById(int id, ClassRepository.SingleClassCallback cb) {
        repository.getClassById(id, cb);
    }
}
