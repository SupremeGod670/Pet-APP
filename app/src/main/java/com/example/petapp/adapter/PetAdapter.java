package com.example.petapp.adapter;

import android.app.Activity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

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
        return 0;
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

        return view;
    }
}
