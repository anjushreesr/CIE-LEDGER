package com.example.cieledger.ui.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.example.cieledger.databinding.FragmentHomeBinding;
import com.example.cieledger.model.ClassEntity;
import com.example.cieledger.ui.AddClassActivity;
import com.example.cieledger.ui.MarksEntryActivity;
import com.example.cieledger.ui.adapters.ClassAdapter;
import com.example.cieledger.viewmodel.ClassViewModel;

public class HomeFragment extends Fragment implements ClassAdapter.ClassClickListener {

    private FragmentHomeBinding binding;
    private ClassViewModel viewModel;
    private ClassAdapter adapter;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        binding = FragmentHomeBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        viewModel = new ViewModelProvider(this).get(ClassViewModel.class);

        setupHeader();
        setupRecyclerView();
        observeData();

        binding.fabAdd.setOnClickListener(v ->
                startActivity(new Intent(requireContext(), AddClassActivity.class)));
    }

    private void setupHeader() {
        binding.tvGreeting.setText("Hi, " + viewModel.getCurrentUserName() + " 👋");
    }

    private void setupRecyclerView() {
        adapter = new ClassAdapter(this);
        binding.rvClasses.setLayoutManager(new LinearLayoutManager(requireContext()));
        binding.rvClasses.setAdapter(adapter);
    }

    // ── CRITICAL: observe LiveData properly ──────────────────────
    private void observeData() {
        viewModel.getClasses().observe(getViewLifecycleOwner(), classes -> {
            adapter.setClasses(classes);

            boolean empty = classes == null || classes.isEmpty();
            binding.emptyState.setVisibility(empty ? View.VISIBLE : View.GONE);
            binding.rvClasses.setVisibility(empty ? View.GONE : View.VISIBLE);
            binding.tvTotalClasses.setText(String.valueOf(classes == null ? 0 : classes.size()));
        });
    }

    // ── ClassAdapter callbacks ────────────────────────────────────
    @Override
    public void onClassClick(ClassEntity classEntity) {
        Intent intent = new Intent(requireContext(), MarksEntryActivity.class);
        intent.putExtra("class_id",     classEntity.getId());
        intent.putExtra("subject_type", classEntity.getSubjectType());
        intent.putExtra("course_name",  classEntity.getCourseName());
        intent.putExtra("semester",     classEntity.getSemester());
        intent.putExtra("section",      classEntity.getSection());
        intent.putExtra("num_students", classEntity.getNumStudents());
        startActivity(intent);
    }

    @Override
    public void onEditClick(ClassEntity classEntity) {
        Intent intent = new Intent(requireContext(), AddClassActivity.class);
        intent.putExtra("edit_id", classEntity.getId());
        startActivity(intent);
    }

    @Override
    public void onDeleteClick(ClassEntity classEntity) {
        new AlertDialog.Builder(requireContext())
                .setTitle("Delete Class")
                .setMessage("Delete \"" + classEntity.getCourseName() + "\"?")
                .setPositiveButton("Delete", (d, w) -> viewModel.deleteClass(classEntity))
                .setNegativeButton("Cancel", null)
                .show();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}
