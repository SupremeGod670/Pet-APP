package com.example.petapp.fragments;

import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
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
            String petName = arguments.getString("pet_name", "Pet");

            Log.d("Fragment1", "Loading image for pet: " + petName + ", URL: " + imageUrl);

            // Configure Glide request options
            RequestOptions requestOptions = new RequestOptions()
                    .placeholder(R.drawable.ic_launcher_background)
                    .error(R.drawable.ic_launcher_background)
                    .centerCrop()
                    .fitCenter();

            // Carregar imagem se existir
            if (imageUrl != null && !imageUrl.isEmpty()) {
                try {
                    Uri imageUri = Uri.parse(imageUrl);

                    Glide.with(this)
                            .load(imageUri)
                            .apply(requestOptions)
                            .into(petPhoto);

                    Log.d("Fragment1", "Successfully loaded image from URI: " + imageUrl);

                } catch (Exception e) {
                    Log.e("Fragment1", "Error loading image from URI: " + imageUrl, e);

                    // Usar imagem padrão em caso de erro
                    Glide.with(this)
                            .load(R.drawable.ic_launcher_background)
                            .apply(requestOptions)
                            .into(petPhoto);
                }
            } else {
                Log.d("Fragment1", "No image URL provided, using default image");

                // Usar imagem padrão se não tiver URL
                Glide.with(this)
                        .load(R.drawable.ic_launcher_background)
                        .apply(requestOptions)
                        .into(petPhoto);
            }
        } else {
            Log.d("Fragment1", "No arguments received, using default image");

            // Use default image if no arguments
            Glide.with(this)
                    .load(R.drawable.ic_launcher_background)
                    .apply(new RequestOptions().centerCrop())
                    .into(petPhoto);
        }

        return view;
    }
}