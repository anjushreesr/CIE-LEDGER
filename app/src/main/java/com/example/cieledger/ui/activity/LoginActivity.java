package com.example.cieledger.ui;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

import com.example.cieledger.databinding.ActivityLoginBinding;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;

public class LoginActivity extends AppCompatActivity {

    private ActivityLoginBinding binding;
    private FirebaseAuth auth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityLoginBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        auth = FirebaseAuth.getInstance();

        binding.btnLogin.setOnClickListener(v -> attemptLogin());
        binding.tvGoRegister.setOnClickListener(v ->
                startActivity(new Intent(this, RegisterActivity.class)));
        binding.tvForgotPassword.setOnClickListener(v -> sendReset());
    }

    private void attemptLogin() {
        String email    = binding.etEmail.getText().toString().trim();
        String password = binding.etPassword.getText().toString().trim();

        if (email.isEmpty()) {
            binding.tilEmail.setError("Email required");
            return;
        }
        binding.tilEmail.setError(null);

        if (password.length() < 6) {
            binding.tilPassword.setError("Min 6 characters");
            return;
        }
        binding.tilPassword.setError(null);

        setLoading(true);

        auth.signInWithEmailAndPassword(email, password)
                .addOnSuccessListener(r -> {
                    setLoading(false);
                    startActivity(new Intent(this, MainActivity.class));
                    finishAffinity();
                })
                .addOnFailureListener(e -> {
                    setLoading(false);
                    showSnackbar(e.getMessage() != null ? e.getMessage() : "Login failed");
                });
    }

    private void sendReset() {
        String email = binding.etEmail.getText().toString().trim();
        if (email.isEmpty()) {
            binding.tilEmail.setError("Enter email first");
            return;
        }
        auth.sendPasswordResetEmail(email)
                .addOnSuccessListener(v -> showSnackbar("Reset email sent!"))
                .addOnFailureListener(e -> showSnackbar("Failed: " + e.getMessage()));
    }

    private void setLoading(boolean loading) {
        binding.btnLogin.setEnabled(!loading);
        binding.progressBar.setVisibility(loading ? View.VISIBLE : View.GONE);
        binding.btnLogin.setText(loading ? "Please wait…" : "Login");
    }

    private void showSnackbar(String msg) {
        Snackbar.make(binding.getRoot(), msg, Snackbar.LENGTH_LONG).show();
    }
}
