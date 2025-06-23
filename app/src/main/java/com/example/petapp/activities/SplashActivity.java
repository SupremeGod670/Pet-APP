package com.example.petapp.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.petapp.R;

public class SplashActivity extends AppCompatActivity {

    private ImageView pets;
    private Animation anim;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.splash);

        pets = findViewById(R.id.pets);
        anim = AnimationUtils.loadAnimation(SplashActivity.this, R.anim.animacao_splash);
        anim.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {

                Intent it = new Intent(SplashActivity.this, LoginActivity.class);
                startActivity(it);

            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
        pets.setAnimation(anim);
        anim.start();

    }

}
