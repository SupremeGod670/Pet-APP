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

        if (view == null) {
            view = activity.getLayoutInflater().inflate(R.layout.pet, viewGroup, false);
        }

        PetModel pets = getPets.get(position);
        ImageView perfil = view.findViewById(R.id.perfil);

        TextView nome = view.findViewById(R.id.nome);
        nome.setText(pets.getNome());

        TextView raca = view.findViewById(R.id.raca);
        raca.setText(pets.getRaca());

        // Configurações do Glide para imagens redondas
        RequestOptions requestOptions = new RequestOptions()
                .placeholder(R.drawable.petpaw) // Imagem padrão específica para pets
                .error(R.drawable.petpaw) // Imagem de erro específica para pets
                .centerCrop()
                .circleCrop(); // Esta opção torna a imagem redonda

        // Carrega a imagem usando Glide
        String imageUrl = pets.getPerfil();

        if (imageUrl != null && !imageUrl.isEmpty()) {
            try {
                Uri imageUri = Uri.parse(imageUrl);

                Glide.with(activity)
                        .load(imageUri)
                        .apply(requestOptions)
                        .into(perfil);

                Log.d("PetAdapter", "Carregando imagem para pet: " + pets.getNome() + ", URI: " + imageUrl);

            } catch (Exception e) {
                Log.e("PetAdapter", "Erro ao analisar URI da imagem para pet: " + pets.getNome(), e);
                // Carrega imagem padrão em caso de erro
                Glide.with(activity)
                        .load(R.drawable.petpaw)
                        .apply(requestOptions)
                        .into(perfil);
            }
        } else {
            // Carrega imagem padrão se não houver URL
            Log.d("PetAdapter", "Nenhuma imagem fornecida para pet: " + pets.getNome() + ", usando imagem padrão");
            Glide.with(activity)
                    .load(R.drawable.petpaw)
                    .apply(requestOptions)
                    .into(perfil);
        }

        return view;
    }
}