package com.example.petapp.adapter;

import androidx.appcompat.app.AppCompatActivity;

public class PetModel extends AppCompatActivity {

    private String perfilpet;
    private String nome;
    private String raca;
    private Long id;

    public PetModel(String perfilpet, String nome, String raca, Long id) {
        this.perfilpet = perfilpet;
        this.nome = nome;
        this.raca = raca;
        this.id = id;
    }

    public String getPerfil() {
        return perfilpet;
    }

    public String getNome() {
        return nome;
    }

    public String getRaca() {
        return raca;
    }

    public long getId() {
        return id;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public void setRaca(String raca) {
        this.raca = raca;
    }

    public void setId(Long id) { // New setter for ID
        this.id = id;
    }
}
