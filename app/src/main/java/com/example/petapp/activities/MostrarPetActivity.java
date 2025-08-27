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
import com.example.petapp.database.databasePet.dao.RegistroPetDAO;
import com.example.petapp.database.databasePet.model.RegistroPetModel;

public class MostrarPetActivity extends AppCompatActivity {

    private TextView voltar;
    private static final int NUM_PAGES = 2;
    private ViewPager2 viewPager;
    private FragmentStateAdapter pagerAdapter;
    private RegistroPetModel petAtual;

    public static final String EXTRA_PET_ID = "EXTRA_PET_ID";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.mostrar_pet);

        voltar = findViewById(R.id.voltar);
        viewPager = findViewById(R.id.rolar);

        // Buscar dados do pet
        Long petId = getIntent().getLongExtra("PET_ID", -1L);
        if (petId != -1L) {
            RegistroPetDAO dao = new RegistroPetDAO(this);
            petAtual = dao.getPetById(petId);
        }

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
                        Fragment1 fragment1 = new Fragment1();
                        Bundle bundle1 = new Bundle();
                        if (petAtual != null) {
                            bundle1.putString("pet_name", petAtual.getNomepet());
                            bundle1.putString("pet_image_url", petAtual.getUrlImagem());
                        }
                        fragment1.setArguments(bundle1);
                        return fragment1;
                    case 1:
                        Fragment2 fragment2 = new Fragment2();
                        Bundle bundle2 = new Bundle();
                        if (petAtual != null) {
                            bundle2.putString("pet_name", petAtual.getNomepet());
                            bundle2.putString("pet_race", petAtual.getRaca());
                            bundle2.putString("pet_species", petAtual.getEspecie());
                            bundle2.putString("pet_sex", petAtual.getSexo());
                            bundle2.putString("pet_color", petAtual.getCor());
                            bundle2.putString("pet_city", petAtual.getCidade());
                            bundle2.putString("pet_state", petAtual.getEstado());
                            bundle2.putString("pet_neighborhood", petAtual.getBairro());
                            bundle2.putString("pet_address", petAtual.getEndereco());
                            bundle2.putString("pet_email", petAtual.getEmail());
                            bundle2.putString("pet_father", petAtual.getPai());
                            bundle2.putString("pet_mother", petAtual.getMae());
                            bundle2.putString("pet_birthplace", petAtual.getNaturalidade());
                            bundle2.putString("pet_description", petAtual.getDescricao());

                            if (petAtual.getCep() != null) {
                                bundle2.putString("pet_cep", String.valueOf(petAtual.getCep().longValue()));
                            }
                            if (petAtual.getTelefoneresd() != null) {
                                bundle2.putString("pet_phone", String.valueOf(petAtual.getTelefoneresd().longValue()));
                            }
                            if (petAtual.getTelefonecel() != null) {
                                bundle2.putString("pet_cell", String.valueOf(petAtual.getTelefonecel().longValue()));
                            }
                            if (petAtual.getNascimento() != null) {
                                bundle2.putString("pet_birth", String.valueOf(petAtual.getNascimento().longValue()));
                            }
                        }
                        fragment2.setArguments(bundle2);
                        return fragment2;
                    default:
                        return null;
                }
            }
        };
        viewPager.setAdapter(pagerAdapter);
    }

    public RegistroPetModel getPetAtual() {
        return petAtual;
    }
}