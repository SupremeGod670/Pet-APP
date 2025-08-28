package com.example.petapp.adapter;

import android.app.Activity;
import android.net.Uri;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.example.petapp.R;

import java.util.ArrayList;

public class PetAdapter extends BaseAdapter {

    private ArrayList<PetModel> getPets;
    private Activity activity;

    public PetAdapter(Activity activity, ArrayList<PetModel> getPets) {
        this.activity = activity;
        this.getPets = getPets;
    }

    @Override
    public int getCount() {
        return getPets.size();
    }

    @Override
    public Object getItem(int position) {
        return getPets.get(position);
    }

    @Override
    public long getItemId(int position) {
        return getPets.get(position).getId();
    }

    @Override
    public View getView(int position, View view, ViewGroup viewGroup) {

        if (view == null ){
            view = activity.getLayoutInflater().inflate(R.layout.pet, viewGroup, false);
        }

        PetModel pets = getPets.get(position);
        ImageView perfil = view.findViewById(R.id.perfil);

        TextView nome = view.findViewById(R.id.nome);
        nome.setText(pets.getNome());

        TextView raca = view.findViewById(R.id.raca);
        raca.setText(pets.getRaca());

        // Load image using Glide
        String imageUrl = pets.getPerfil();

        RequestOptions requestOptions = new RequestOptions()
                .placeholder(R.drawable.ic_launcher_background)
                .error(R.drawable.ic_launcher_background)
                .centerCrop();

        if (imageUrl != null && !imageUrl.isEmpty()) {
            try {
                Uri imageUri = Uri.parse(imageUrl);

                Glide.with(activity)
                        .load(imageUri)
                        .apply(requestOptions)
                        .into(perfil);

                Log.d("PetAdapter", "Loading image for pet: " + pets.getNome() + ", URI: " + imageUrl);

            } catch (Exception e) {
                Log.e("PetAdapter", "Error parsing image URI for pet: " + pets.getNome(), e);
                // Load default image on error
                Glide.with(activity)
                        .load(R.drawable.ic_launcher_background)
                        .apply(requestOptions)
                        .into(perfil);
            }
        } else {
            // Load default image if no URL
            Glide.with(activity)
                    .load(R.drawable.ic_launcher_background)
                    .apply(requestOptions)
                    .into(perfil);
        }

        return view;
    }
}