package com.example.petapp.api.apicep.model;

import java.io.Serializable;

public class Cep implements Serializable {

    private String nome;
    private String codigo;

    public Cep() {

    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getCodigo() {
        return codigo;
    }

    public void setCodigo(String codigo) {
        this.codigo = codigo;
    }
}
