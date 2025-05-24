package com.example.petapp.adapter;

import androidx.appcompat.app.AppCompatActivity;

public class CriarPetsModel extends AppCompatActivity {

    private String perfilpet;
    private String nome;
    private String raca;

    public CriarPetsModel(String perfilpet, String nome, String raca) {
        this.perfilpet = perfilpet;
        this.nome = nome;
        this.raca = raca;
    }

    public String getPerfil(){
        return perfilpet;
    }

    public String getNome(){
        return nome;
    }

    public String getRaca(){
        return raca;
    }

}
