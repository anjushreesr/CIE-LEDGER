package com.example.cieledger.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.example.cieledger.model.StudentMarks;
import com.example.cieledger.repository.MarksRepository;

import java.util.List;

public class MarksViewModel extends AndroidViewModel {

    private final MarksRepository repository;
    private int classId = -1;
    private String subjectType = "THEORY";

    public MarksViewModel(@NonNull Application application) {
        super(application);
        repository = new MarksRepository(application);
    }

    public void init(int classId, String subjectType) {
        this.classId     = classId;
        this.subjectType = subjectType;
    }

    public LiveData<List<StudentMarks>> getStudents() {
        return repository.getStudentsByClass(classId);
    }

    public void initRowsIfEmpty(int count) {
        repository.initRowsIfEmpty(classId, count, null);
    }

    public void saveAll(List<StudentMarks> students, Runnable onDone) {
        repository.saveAllMarks(students, onDone);
    }

    public void update(StudentMarks s) {
        repository.update(s);
    }

    public String getSubjectType() {
        return subjectType;
    }
}
