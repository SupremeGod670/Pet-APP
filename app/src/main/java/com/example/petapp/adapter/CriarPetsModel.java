package com.example.petapp.adapter;

import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

import com.example.petapp.R;

public class CriarPetsModel extends AppCompatActivity {

    private String perfilpet;
    private String nome;
    private String especie;

    public CriarPetsModel(String perfilpet, String nome, String especie) {
        this.perfilpet = perfilpet;
        this.nome = nome;
        this.especie = especie;
    }

    public String getPerfil(){
        return perfilpet;
    }

    public String getNome(){
        return nome;
    }

    public String getEspecie(){
        return especie;
    }

}
