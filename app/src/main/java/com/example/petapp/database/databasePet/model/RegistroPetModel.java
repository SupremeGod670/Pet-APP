package com.example.petapp.database.databasePet.model;

public class RegistroPetModel {

    public static final String TABELA_PET = "tb_pet";
    public static final String COLUNA_ID = "_id";
    public static final String COLUNA_NOMEPET = "nomepet";
    public static final String COLUNA_NASCIMENTO = "nascimento";
    public static final String COLUNA_ESPECIE = "especie";
    public static final String COLUNA_SEXO = "sexo";
    public static final String COLUNA_PAI = "pai";
    public static final String COLUNA_MAE = "mae";
    public static final String COLUNA_RACA = "raca";
    public static final String COLUNA_NATURALIDADE = "naturalidade";
    public static final String COLUNA_COR = "cor";
    public static final String COLUNA_ENDERECO = "endereco";
    public static final String COLUNA_BAIRRO = "bairro";
    public static final String COLUNA_CIDADE = "cidade";
    public static final String COLUNA_TELEFONERESD = "telefoneresd";
    public static final String COLUNA_EMAIL = "email";
    public static final String COLUNA_CEP = "cep";
    public static final String COLUNA_ESTADO = "estado";
    public static final String COLUNA_TELEFONECEL = "telefonecel";
    public static final String COLUNA_DESCRICAO = "descricao";

    public static final String DROP_TABLE =
            "drop table if exists " + TABELA_PET;

    public static final String CREATE_TABLE = "create table " + TABELA_PET +
            " (" + COLUNA_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
            + COLUNA_NOMEPET + " TEXT NOT NULL UNIQUE, "
            + COLUNA_NASCIMENTO + " REAL, "
            + COLUNA_ESPECIE + " TEXT NOT NULL, "
            + COLUNA_RACA + " TEXT NOT NULL, "
            + COLUNA_SEXO + " TEXT, "
            + COLUNA_PAI + " TEXT, "
            + COLUNA_MAE + " TEXT, "
            + COLUNA_NATURALIDADE + " TEXT, "
            + COLUNA_COR + " TEXT, "
            + COLUNA_CEP + " REAL, "
            + COLUNA_ESTADO + " TEXT NOT NULL, "
            + COLUNA_CIDADE + " TEXT NOT NULL, "
            + COLUNA_ENDERECO + " TEXT, "
            + COLUNA_BAIRRO + " TEXT, "
            + COLUNA_TELEFONERESD + " REAL, "
            + COLUNA_EMAIL + " TEXT, "
            + COLUNA_TELEFONECEL + " REAL, "
            + COLUNA_DESCRICAO + " TEXT"
            + ")";

    private Long id;
    private String nomepet;
    private Double nascimento;
    private String especie;
    private String sexo;
    private String pai;
    private String mae;
    private String raca;
    private String naturalidade;
    private String cor;
    private String endereco;
    private String bairro;
    private String cidade;
    private Double telefoneresd;
    private String email;
    private Double cep;
    private String estado;
    private Double telefonecel;
    private String descricao;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNomepet() {
        return nomepet;
    }

    public void setNomepet(String nomepet) {
        this.nomepet = nomepet;
    }

    public Double getNascimento() {
        return nascimento;
    }

    public void setNascimento(Double nascimento) {
        this.nascimento = nascimento;
    }

    public String getEspecie() {
        return especie;
    }

    public void setEspecie(String especie) {
        this.especie = especie;
    }

    public String getSexo() {
        return sexo;
    }

    public void setSexo(String sexo) {
        this.sexo = sexo;
    }

    public String getPai() {
        return pai;
    }

    public void setPai(String pai) {
        this.pai = pai;
    }

    public String getMae() {
        return mae;
    }

    public void setMae(String mae) {
        this.mae = mae;
    }

    public String getRaca() {
        return raca;
    }

    public void setRaca(String raca) {
        this.raca = raca;
    }

    public String getNaturalidade() {
        return naturalidade;
    }

    public void setNaturalidade(String naturalidade) {
        this.naturalidade = naturalidade;
    }

    public String getCor() {
        return cor;
    }

    public void setCor(String cor) {
        this.cor = cor;
    }

    public String getEndereco() {
        return endereco;
    }

    public void setEndereco(String endereco) {
        this.endereco = endereco;
    }

    public String getBairro() {
        return bairro;
    }

    public void setBairro(String bairro) {
        this.bairro = bairro;
    }

    public String getCidade() {
        return cidade;
    }

    public void setCidade(String cidade) {
        this.cidade = cidade;
    }

    public Double getTelefoneresd() {
        return telefoneresd;
    }

    public void setTelefoneresd(Double telefoneresd) {
        this.telefoneresd = telefoneresd;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Double getCep() {
        return cep;
    }

    public void setCep(Double cep) {
        this.cep = cep;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    public Double getTelefonecel() {
        return telefonecel;
    }

    public void setTelefonecel(Double telefonecel) {
        this.telefonecel = telefonecel;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }
}
