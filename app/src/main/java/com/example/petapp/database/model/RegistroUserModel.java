package com.example.petapp.database.model;

public class RegistroUserModel {

    public static final String TABELA_USUARIO = "tb_user";

    public static final String COLUNA_ID = "_id";
    public static final String COLUNA_EMAIL = "email";
    public static final String COLUNA_SENHA = "senha";

    public static final String DROP_TABLE =
            "drop table if exists " + TABELA_USUARIO;

    public static final String CREATE_TABLE = "create table " + TABELA_USUARIO +
            " (" + COLUNA_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
            + COLUNA_EMAIL + " TEXT NOT NULL, "
            + COLUNA_SENHA + " TEXT NOT NULL)";

    private Long id;
    private String email;
    private String senha;

    public Long getId() {
        return id;
    }

    public String getEmail() {
        return email;
    }

    public String getSenha() {
        return senha;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setSenha(String senha) {
        this.senha = senha;
    }

}
