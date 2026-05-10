package com.example.cieledger.ui;

import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.example.cieledger.databinding.ActivityMarksEntryBinding;
import com.example.cieledger.model.StudentMarks;
import com.example.cieledger.ui.adapter.MarksAdapter;
import com.example.cieledger.viewmodel.MarksViewModel;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.WriteBatch;
import com.google.firebase.firestore.DocumentReference;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MarksEntryActivity extends AppCompatActivity {

    private ActivityMarksEntryBinding binding;
    private MarksViewModel viewModel;
    private MarksAdapter adapter;

    private int classId;
    private String subjectType;
    private String courseName;
    private String semester;
    private String section;
    private int numStudents;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMarksEntryBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        classId     = getIntent().getIntExtra("class_id", -1);
        subjectType = getIntent().getStringExtra("subject_type");
        courseName  = getIntent().getStringExtra("course_name");
        semester    = getIntent().getStringExtra("semester");
        section     = getIntent().getStringExtra("section");
        numStudents = getIntent().getIntExtra("num_students", 0);

        if (subjectType == null) subjectType = "THEORY";

        viewModel = new ViewModelProvider(this).get(MarksViewModel.class);
        viewModel.init(classId, subjectType);

        setupHeader();
        setupColumnHeader();
        setupRecyclerView();
        observeStudents();
        setupClickListeners();
        syncScroll();
    }

    private void setupHeader() {
        binding.tvTitle.setText(courseName != null ? courseName : "Marks Entry");
        String sub = "Sem " + semester + "  ·  Sec " + section + "  ·  " + subjectType;
        binding.tvSubtitle.setText(sub);
    }

    private void setupColumnHeader() {
        if ("INTEGRATED".equals(subjectType)) {
            binding.tvHeaderLab.setVisibility(View.VISIBLE);
        } else {
            binding.tvHeaderLab.setVisibility(View.GONE);
        }
    }

    private void setupRecyclerView() {
        adapter = new MarksAdapter(subjectType);
        binding.rvMarks.setLayoutManager(new LinearLayoutManager(this));
        binding.rvMarks.setAdapter(adapter);
        binding.rvMarks.setItemAnimator(null);

        viewModel.initRowsIfEmpty(numStudents);
    }

    private void observeStudents() {
        viewModel.getStudents().observe(this, students -> {
            if (students != null && !students.isEmpty()) {
                adapter.setStudents(students);
            }
        });
    }

    private void setupClickListeners() {
        binding.btnBack.setOnClickListener(v -> finish());

        binding.btnSaveAll.setOnClickListener(v -> {
            List<StudentMarks> current = adapter.getCurrentList();

            if (current.isEmpty()) {
                showSnackbar("No data to save");
                return;
            }

            binding.btnSaveAll.setEnabled(false);

            // Save locally
            viewModel.saveAll(current, () -> runOnUiThread(() -> {
                binding.btnSaveAll.setEnabled(true);
                showSnackbar("✓ Marks saved!");
            }));

            // Save to Firebase
            saveMarksToFirestore(current);
        });
    }

    private void syncScroll() {
        binding.scrollHeader.setOnScrollChangeListener(
                (View v, int x, int y, int ox, int oy) ->
                        binding.scrollContent.scrollTo(x, 0));

        binding.scrollContent.setOnScrollChangeListener(
                (View v, int x, int y, int ox, int oy) ->
                        binding.scrollHeader.scrollTo(x, 0));
    }

    private void showSnackbar(String msg) {
        Snackbar.make(binding.getRoot(), msg, Snackbar.LENGTH_SHORT).show();
    }

    // ✅ FIXED: METHOD PLACED CORRECTLY OUTSIDE finish()
    private void saveMarksToFirestore(List<StudentMarks> students) {
        FirebaseFirestore db = FirebaseFirestore.getInstance();

        String teacherId = FirebaseAuth.getInstance().getCurrentUser() != null
                ? FirebaseAuth.getInstance().getCurrentUser().getUid()
                : "unknown";

        WriteBatch batch = db.batch();

        for (StudentMarks s : students) {
            Map<String, Object> data = new HashMap<>();
            data.put("name", s.getName());
            data.put("usn", s.getUsn());
            data.put("internal1", s.getInternal1());
            data.put("internal2", s.getInternal2());
            data.put("la1", s.getLa1());
            data.put("la2", s.getLa2());
            data.put("attendance", s.getAttendance());
            data.put("cie", s.getFinalCIE(subjectType));
            data.put("eligible", s.isEligible(subjectType));
            data.put("classId", String.valueOf(classId));
            data.put("teacherId", teacherId);
            data.put("savedAt", System.currentTimeMillis());

            DocumentReference ref = db
                    .collection("marks")
                    .document(teacherId)
                    .collection(String.valueOf(classId))
                    .document(s.getUsn().isEmpty()
                            ? db.collection("marks").document().getId()
                            : s.getUsn());

            batch.set(ref, data);
        }

        batch.commit()
                .addOnSuccessListener(unused ->
                        Snackbar.make(binding.getRoot(), "✓ Saved to cloud!", Snackbar.LENGTH_SHORT).show())
                .addOnFailureListener(e ->
                        Snackbar.make(binding.getRoot(), "Cloud save failed: " + e.getMessage(),
                                Snackbar.LENGTH_LONG).show());
    }

    @Override
    public void finish() {
        super.finish();
        overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
    }
}