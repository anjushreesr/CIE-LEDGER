package com.example.cieledger.ui;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;

import androidx.appcompat.app.AppCompatActivity;

import com.example.cieledger.databinding.ActivitySplashBinding;
import com.google.firebase.auth.FirebaseAuth;

public class SplashActivity extends AppCompatActivity {

    private ActivitySplashBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivitySplashBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        // Animate logo + title
        binding.ivLogo.setAlpha(0f);
        binding.tvAppName.setAlpha(0f);
        binding.tvTagline.setAlpha(0f);

        binding.ivLogo.animate().alpha(1f).scaleX(1f).scaleY(1f)
                .setStartDelay(200).setDuration(700).start();
        binding.tvAppName.animate().alpha(1f).translationY(0f)
                .setStartDelay(500).setDuration(500).start();
        binding.tvTagline.animate().alpha(1f).translationY(0f)
                .setStartDelay(700).setDuration(500).start();

        new Handler(Looper.getMainLooper()).postDelayed(() -> {
            if (FirebaseAuth.getInstance().getCurrentUser() != null) {
                startActivity(new Intent(this, MainActivity.class));
            } else {
                startActivity(new Intent(this, LoginActivity.class));
            }
            finish();
        }, 2000);
    }
}
