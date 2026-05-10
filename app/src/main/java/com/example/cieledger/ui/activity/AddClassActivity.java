package com.example.cieledger.ui;

import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import com.example.cieledger.R;
import com.example.cieledger.databinding.ActivityAddClassBinding;
import com.example.cieledger.model.ClassEntity;
import com.example.cieledger.viewmodel.ClassViewModel;
import com.google.android.material.snackbar.Snackbar;

public class AddClassActivity extends AppCompatActivity {

    private ActivityAddClassBinding binding;
    private ClassViewModel viewModel;
    private int editId = -1;

    private final String[] SEMESTERS = {"1","2","3","4","5","6","7","8"};
    private final String[] SECTIONS  = {"A","B","C","D","E"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityAddClassBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        viewModel = new ViewModelProvider(this).get(ClassViewModel.class);
        editId    = getIntent().getIntExtra("edit_id", -1);

        setupDropdowns();
        setupToggle();
        setupClickListeners();

        if (editId != -1) {
            binding.tvTitle.setText("Edit Class");
            loadExistingData();
        }
    }

    private void setupDropdowns() {
        ArrayAdapter<String> semAdapter = new ArrayAdapter<>(
                this, android.R.layout.simple_dropdown_item_1line, SEMESTERS);
        binding.actvSemester.setAdapter(semAdapter);
        binding.actvSemester.setText(SEMESTERS[0], false);

        ArrayAdapter<String> secAdapter = new ArrayAdapter<>(
                this, android.R.layout.simple_dropdown_item_1line, SECTIONS);
        binding.actvSection.setAdapter(secAdapter);
        binding.actvSection.setText(SECTIONS[0], false);
    }

    private void setupToggle() {
        binding.toggleType.check(R.id.btnTheory);
    }

    private void setupClickListeners() {
        binding.btnBack.setOnClickListener(v -> finish());
        binding.btnSave.setOnClickListener(v -> saveClass());
    }

    private void loadExistingData() {
        viewModel.getClassById(editId, classEntity -> runOnUiThread(() -> {
            if (classEntity == null) return;
            binding.etCourse.setText(classEntity.getCourseName());
            binding.actvSemester.setText(classEntity.getSemester(), false);
            binding.actvSection.setText(classEntity.getSection(), false);
            binding.etNumStudents.setText(String.valueOf(classEntity.getNumStudents()));
            if ("INTEGRATED".equals(classEntity.getSubjectType())) {
                binding.toggleType.check(R.id.btnIntegrated);
            } else {
                binding.toggleType.check(R.id.btnTheory);
            }
        }));
    }

    private void saveClass() {
        String course = binding.etCourse.getText().toString().trim();
        String sem    = binding.actvSemester.getText().toString().trim();
        String sec    = binding.actvSection.getText().toString().trim();
        String numStr = binding.etNumStudents.getText().toString().trim();

        if (course.isEmpty()) {
            binding.tilCourse.setError("Course name required");
            return;
        }
        binding.tilCourse.setError(null);

        if (numStr.isEmpty()) {
            binding.tilNumStudents.setError("Number of students required");
            return;
        }
        binding.tilNumStudents.setError(null);

        int num;
        try {
            num = Integer.parseInt(numStr);
            if (num < 1 || num > 200) throw new NumberFormatException();
        } catch (NumberFormatException e) {
            binding.tilNumStudents.setError("Enter valid number (1–200)");
            return;
        }

        boolean integrated = binding.toggleType.getCheckedButtonId() == R.id.btnIntegrated;
        String type = integrated ? "INTEGRATED" : "THEORY";

        binding.btnSave.setEnabled(false);

        if (editId == -1) {
            // Insert new
            ClassEntity entity = new ClassEntity(course, sem, sec, type, num,
                    viewModel.getCurrentUid());
            viewModel.insertClass(entity, newId -> runOnUiThread(() -> {
                showSnackbar("Class created!");
                finish();
            }));
        } else {
            // Update existing
            viewModel.getClassById(editId, existing -> runOnUiThread(() -> {
                if (existing == null) return;
                existing.setCourseName(course);
                existing.setSemester(sem);
                existing.setSection(sec);
                existing.setSubjectType(type);
                existing.setNumStudents(num);
                viewModel.updateClass(existing);
                showSnackbar("Class updated!");
                finish();
            }));
        }
    }

    private void showSnackbar(String msg) {
        Snackbar.make(binding.getRoot(), msg, Snackbar.LENGTH_SHORT).show();
    }
}
