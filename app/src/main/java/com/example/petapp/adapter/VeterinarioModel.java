package com.example.petapp.adapter;

public class VeterinarioModel {
    private String nome;
    private String endereco;
    private String telefone;
    private String horarioFuncionamento;
    private String especialidades;
    private String linkMaps; // Link direto do Google Maps
    private String tipo; // "Clínica Veterinária", "Pet Shop", "Hospital Veterinário"
    private double avaliacao;
    private boolean atendeEmergencia;

    // Construtor vazio
    public VeterinarioModel() {}

    // Construtor completo
    public VeterinarioModel(String nome, String endereco, String telefone,
                            String horarioFuncionamento, String especialidades,
                            String linkMaps, String tipo,
                            double avaliacao, boolean atendeEmergencia) {
        this.nome = nome;
        this.endereco = endereco;
        this.telefone = telefone;
        this.horarioFuncionamento = horarioFuncionamento;
        this.especialidades = especialidades;
        this.linkMaps = linkMaps;
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

    public String getLinkMaps() {
        return linkMaps;
    }

    public void setLinkMaps(String linkMaps) {
        this.linkMaps = linkMaps;
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