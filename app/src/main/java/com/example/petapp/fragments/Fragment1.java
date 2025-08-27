package com.example.petapp.fragments;

import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.bumptech.glide.Glide;
import com.example.petapp.R;

public class Fragment1 extends Fragment {

    private ImageView petPhoto;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment1, container, false);

        petPhoto = view.findViewById(R.id.pet_photo);

        // Receber dados do bundle
        Bundle arguments = getArguments();
        if (arguments != null) {
            String imageUrl = arguments.getString("pet_image_url");

            // Carregar imagem se existir
            if (imageUrl != null && !imageUrl.isEmpty()) {
                try {
                    Uri imageUri = Uri.parse(imageUrl);
                    Glide.with(this)
                            .load(imageUri)
                            .placeholder(R.drawable.ic_launcher_background)
                            .error(R.drawable.ic_launcher_background)
                            .into(petPhoto);
                } catch (Exception e) {
                    e.printStackTrace();
                    // Usar imagem padrão em caso de erro
                    petPhoto.setImageResource(R.drawable.ic_launcher_background);
                }
            } else {
                // Usar imagem padrão se não tiver URL
                petPhoto.setImageResource(R.drawable.ic_launcher_background);
            }
        }

        return view;
    }
}