package com.example.petapp.database.databaseUser.model;

public class RegistroUserModel {

    public static final String TABELA_USUARIO = "tb_user";

    public static final String COLUNA_ID = "_id";
    public static final String COLUNA_NOME = "nome";
    public static final String COLUNA_EMAIL = "email";
    public static final String COLUNA_SENHA = "senha";
    public static final String COLUNA_FOTO_PERFIL = "foto_perfil"; // Nova coluna

    public static final String DROP_TABLE =
            "drop table if exists " + TABELA_USUARIO;

    public static final String CREATE_TABLE = "create table " + TABELA_USUARIO +
            " (" + COLUNA_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
            + COLUNA_NOME + " TEXT NOT NULL, "
            + COLUNA_EMAIL + " TEXT NOT NULL UNIQUE, "
            + COLUNA_SENHA + " TEXT NOT NULL, "
            + COLUNA_FOTO_PERFIL + " TEXT" // Nova coluna para URL da foto
            + ")";

    private Long id;
    private String nome;
    private String email;
    private String senha;
    private String fotoPerfil; // Nova propriedade

    // Construtor vazio
    public RegistroUserModel() {
    }

    // Construtor com parâmetros
    public RegistroUserModel(String nome, String email, String senha) {
        this.nome = nome;
        this.email = email;
        this.senha = senha;
    }

    // Construtor completo
    public RegistroUserModel(String nome, String email, String senha, String fotoPerfil) {
        this.nome = nome;
        this.email = email;
        this.senha = senha;
        this.fotoPerfil = fotoPerfil;
    }

    // Getters
    public Long getId() {
        return id;
    }

    public String getNome() {
        return nome;
    }

    public String getEmail() {
        return email;
    }

    public String getSenha() {
        return senha;
    }

    public String getFotoPerfil() {
        return fotoPerfil;
    }

    // Setters
    public void setId(Long id) {
        this.id = id;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setSenha(String senha) {
        this.senha = senha;
    }

    public void setFotoPerfil(String fotoPerfil) {
        this.fotoPerfil = fotoPerfil;
    }

    @Override
    public String toString() {
        return "RegistroUserModel{" +
                "id=" + id +
                ", nome='" + nome + '\'' +
                ", email='" + email + '\'' +
                ", senha='[PROTECTED]'" +
                ", fotoPerfil='" + fotoPerfil + '\'' +
                '}';
    }
}