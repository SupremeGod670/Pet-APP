package com.example.petapp.adapter;

public class CriarExpModel {

    private String[] especie = {
            "Cachorro",
            "Gato",
            "Pássaro",
            "Outros"
    };

    public CriarExpModel(String[] especie){
        this.especie = especie;
    }

    public String[] getEspecie(){
        return especie;
    }

}
