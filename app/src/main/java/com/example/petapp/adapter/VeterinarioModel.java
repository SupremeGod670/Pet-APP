package com.example.petapp.adapter;

public class VeterinarioModel {
    private String nome;
    private String endereco;
    private String telefone;
    private String horarioFuncionamento;
    private String especialidades;
    private double latitude;
    private double longitude;
    private String tipo; // "Clínica Veterinária", "Pet Shop", "Hospital Veterinário"
    private double avaliacao;
    private boolean atendeEmergencia;

    // Construtor vazio
    public VeterinarioModel() {}

    // Construtor completo
    public VeterinarioModel(String nome, String endereco, String telefone,
                            String horarioFuncionamento, String especialidades,
                            double latitude, double longitude, String tipo,
                            double avaliacao, boolean atendeEmergencia) {
        this.nome = nome;
        this.endereco = endereco;
        this.telefone = telefone;
        this.horarioFuncionamento = horarioFuncionamento;
        this.especialidades = especialidades;
        this.latitude = latitude;
        this.longitude = longitude;
        this.tipo = tipo;
        this.avaliacao = avaliacao;
        this.atendeEmergencia = atendeEmergencia;
    }

    // Getters e Setters
    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getEndereco() {
        return endereco;
    }

    public void setEndereco(String endereco) {
        this.endereco = endereco;
    }

    public String getTelefone() {
        return telefone;
    }

    public void setTelefone(String telefone) {
        this.telefone = telefone;
    }

    public String getHorarioFuncionamento() {
        return horarioFuncionamento;
    }

    public void setHorarioFuncionamento(String horarioFuncionamento) {
        this.horarioFuncionamento = horarioFuncionamento;
    }

    public String getEspecialidades() {
        return especialidades;
    }

    public void setEspecialidades(String especialidades) {
        this.especialidades = especialidades;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    public double getAvaliacao() {
        return avaliacao;
    }

    public void setAvaliacao(double avaliacao) {
        this.avaliacao = avaliacao;
    }

    public boolean isAtendeEmergencia() {
        return atendeEmergencia;
    }

    public void setAtendeEmergencia(boolean atendeEmergencia) {
        this.atendeEmergencia = atendeEmergencia;
    }

    @Override
    public String toString() {
        return "VeterinarioModel{" +
                "nome='" + nome + '\'' +
                ", endereco='" + endereco + '\'' +
                ", tipo='" + tipo + '\'' +
                ", avaliacao=" + avaliacao +
                '}';
    }
}