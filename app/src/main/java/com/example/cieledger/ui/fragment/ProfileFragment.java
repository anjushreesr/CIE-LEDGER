package com.example.cieledger.ui.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;

import com.example.cieledger.databinding.FragmentProfileBinding;
import com.example.cieledger.ui.LoginActivity;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;

public class ProfileFragment extends Fragment {

    private FragmentProfileBinding binding;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        binding = FragmentProfileBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        populateProfile();
        setupClickListeners();
    }

    private void populateProfile() {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user == null) return;

        String name  = user.getDisplayName();
        String email = user.getEmail();

        binding.tvName.setText(name != null && !name.isEmpty() ? name : "Teacher");
        binding.tvEmail.setText(email != null ? email : "");
        binding.tvInfoName.setText(name != null && !name.isEmpty() ? name : "Teacher");
        binding.tvInfoEmail.setText(email != null ? email : "");

        // Avatar initial
        String initial = (name != null && !name.isEmpty())
                ? String.valueOf(name.charAt(0)).toUpperCase() : "T";
        binding.tvAvatarInitial.setText(initial);
    }

    private void setupClickListeners() {
        binding.btnEditName.setOnClickListener(v -> showEditNameDialog());

        binding.btnChangePassword.setOnClickListener(v -> {
            FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
            if (user == null || user.getEmail() == null) return;
            FirebaseAuth.getInstance().sendPasswordResetEmail(user.getEmail())
                    .addOnSuccessListener(unused ->
                            showSnackbar("Password reset email sent!"))
                    .addOnFailureListener(e ->
                            showSnackbar("Failed: " + e.getMessage()));
        });

        binding.btnLogout.setOnClickListener(v ->
                new AlertDialog.Builder(requireContext())
                        .setTitle("Logout")
                        .setMessage("Are you sure you want to logout?")
                        .setPositiveButton("Logout", (d, w) -> {
                            FirebaseAuth.getInstance().signOut();
                            startActivity(new Intent(requireContext(), LoginActivity.class));
                            requireActivity().finishAffinity();
                        })
                        .setNegativeButton("Cancel", null)
                        .show());
    }

    private void showEditNameDialog() {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user == null) return;

        EditText input = new EditText(requireContext());
        input.setHint("Enter your name");
        String current = user.getDisplayName();
        if (current != null) input.setText(current);
        input.setPadding(48, 32, 48, 32);

        new AlertDialog.Builder(requireContext())
                .setTitle("Edit Name")
                .setView(input)
                .setPositiveButton("Save", (d, w) -> {
                    String newName = input.getText().toString().trim();
                    if (newName.isEmpty()) return;
                    UserProfileChangeRequest req = new UserProfileChangeRequest.Builder()
                            .setDisplayName(newName).build();
                    user.updateProfile(req)
                            .addOnSuccessListener(unused -> {
                                binding.tvName.setText(newName);
                                binding.tvInfoName.setText(newName);
                                binding.tvAvatarInitial.setText(
                                        String.valueOf(newName.charAt(0)).toUpperCase());
                                showSnackbar("Name updated!");
                            })
                            .addOnFailureListener(e ->
                                    showSnackbar("Failed: " + e.getMessage()));
                })
                .setNegativeButton("Cancel", null)
                .show();
    }

    private void showSnackbar(String msg) {
        if (binding != null) {
            Snackbar.make(binding.getRoot(), msg, Snackbar.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}
