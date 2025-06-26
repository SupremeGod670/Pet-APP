package com.example.petapp.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.viewpager2.adapter.FragmentStateAdapter;
import androidx.viewpager2.widget.ViewPager2;

import com.example.petapp.fragments.Fragment1;
import com.example.petapp.fragments.Fragment2;
import com.example.petapp.R;

public class MostrarPetActivity extends AppCompatActivity {

    private TextView voltar;
    private static final int NUM_PAGES = 2;
    private ViewPager2 viewPager;
    private FragmentStateAdapter pagerAdapter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.mostrar_pet);

        voltar = findViewById(R.id.voltar);
        viewPager = findViewById(R.id.rolar);

        voltar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MostrarPetActivity.this, MenuActivity.class);
                startActivity(intent);
                finish();
            }
        });

        pagerAdapter = new FragmentStateAdapter(this) {
            @Override
            public int getItemCount() {
                return NUM_PAGES;
            }

            @NonNull
            @Override
            public Fragment createFragment(int position) {
                switch (position) {
                    case 0:
                        return new Fragment1();
                    case 1:
                        return new Fragment2();
                    default:
                        return null;
                }
            }
        };
        viewPager.setAdapter(pagerAdapter);

    }
}
