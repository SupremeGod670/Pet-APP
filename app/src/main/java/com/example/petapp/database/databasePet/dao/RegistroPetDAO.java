package com.example.petapp.database.databasePet.dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.example.petapp.database.databasePet.DBHelper;
import com.example.petapp.database.databasePet.model.RegistroPetModel;

import java.util.ArrayList;
import java.util.List;

public class RegistroPetDAO extends AbstrataDAO {

    private String[] colunas = new String[]{
            RegistroPetModel.COLUNA_ID,
            RegistroPetModel.COLUNA_NOMEPET,
            RegistroPetModel.COLUNA_NASCIMENTO,
            RegistroPetModel.COLUNA_ESPECIE,
            RegistroPetModel.COLUNA_SEXO,
            RegistroPetModel.COLUNA_PAI,
            RegistroPetModel.COLUNA_MAE,
            RegistroPetModel.COLUNA_RACA,
            RegistroPetModel.COLUNA_NATURALIDADE,
            RegistroPetModel.COLUNA_COR,
            RegistroPetModel.COLUNA_ENDERECO,
            RegistroPetModel.COLUNA_BAIRRO,
            RegistroPetModel.COLUNA_CIDADE,
            RegistroPetModel.COLUNA_TELEFONERESD,
            RegistroPetModel.COLUNA_EMAIL,
            RegistroPetModel.COLUNA_CEP,
            RegistroPetModel.COLUNA_ESTADO,
            RegistroPetModel.COLUNA_TELEFONECEL,
            RegistroPetModel.COLUNA_DESCRICAO,
            RegistroPetModel.COLUNA_URL_IMAGEM
    };

    public RegistroPetDAO(Context context) {
        dbHelper = new DBHelper(context);
    }

    public boolean selectNotNull(String nome, String especie, String raca, String estado, String cidade) {
        Open();
        Cursor cursor = db.query(
                RegistroPetModel.TABELA_PET,
                colunas,
                RegistroPetModel.COLUNA_NOMEPET + " = ? AND " +
                        RegistroPetModel.COLUNA_ESPECIE + " = ? AND " +
                        RegistroPetModel.COLUNA_RACA + " = ? AND " +
                        RegistroPetModel.COLUNA_ESTADO + " = ? AND " +
                        RegistroPetModel.COLUNA_CIDADE + " = ? ",
                new String[]{nome, especie, raca, estado, cidade}, null, null, null);
        cursor.moveToFirst();
        Close();
        return cursor.getCount() > 0;
    }

    public boolean select(String nomepet, String nascimento, String especie, String sexo, String pai, String mae, String raca, String naturalidade, String cor, String endereco, String bairro, String cidade, String telefoneresd, String email, String cep, String estado, String telefonecel, String descricao) {
        Open();
        Cursor cursor = db.query(
                RegistroPetModel.TABELA_PET,
                colunas,
                RegistroPetModel.COLUNA_NOMEPET + " = ? AND " +
                        RegistroPetModel.COLUNA_NASCIMENTO + " = ? AND " +
                        RegistroPetModel.COLUNA_ESPECIE + " = ? AND " +
                        RegistroPetModel.COLUNA_SEXO + " = ? AND " +
                        RegistroPetModel.COLUNA_PAI + " = ? AND " +
                        RegistroPetModel.COLUNA_MAE + " = ? AND " +
                        RegistroPetModel.COLUNA_RACA + " = ? AND " +
                        RegistroPetModel.COLUNA_NATURALIDADE + " = ? AND " +
                        RegistroPetModel.COLUNA_COR + " = ? AND " +
                        RegistroPetModel.COLUNA_ENDERECO + " = ? AND " +
                        RegistroPetModel.COLUNA_BAIRRO + " = ? AND " +
                        RegistroPetModel.COLUNA_CIDADE + " = ? AND " +
                        RegistroPetModel.COLUNA_TELEFONERESD + " = ? AND " +
                        RegistroPetModel.COLUNA_EMAIL + " = ? AND " +
                        RegistroPetModel.COLUNA_CEP + " = ? AND " +
                        RegistroPetModel.COLUNA_ESTADO + " = ? AND " +
                        RegistroPetModel.COLUNA_TELEFONECEL + " = ? AND " +
                        RegistroPetModel.COLUNA_DESCRICAO + " = ? ",
                new String[]{nomepet, nascimento, especie, sexo, pai, mae, raca, naturalidade, cor, endereco, bairro, cidade, telefoneresd, email, cep, estado, telefonecel, descricao}, null, null, null);
        cursor.moveToFirst();
        Close();
        return cursor.getCount() > 0;
    }

    public void insert(RegistroPetModel pet) {
        Open();
        ContentValues values = new ContentValues();
        values.put(RegistroPetModel.COLUNA_NOMEPET, pet.getNomepet());
        values.put(RegistroPetModel.COLUNA_NASCIMENTO, pet.getNascimento());
        values.put(RegistroPetModel.COLUNA_ESPECIE, pet.getEspecie());
        values.put(RegistroPetModel.COLUNA_SEXO, pet.getSexo());
        values.put(RegistroPetModel.COLUNA_PAI, pet.getPai());
        values.put(RegistroPetModel.COLUNA_MAE, pet.getMae());
        values.put(RegistroPetModel.COLUNA_RACA, pet.getRaca());
        values.put(RegistroPetModel.COLUNA_NATURALIDADE, pet.getNaturalidade());
        values.put(RegistroPetModel.COLUNA_COR, pet.getCor());
        values.put(RegistroPetModel.COLUNA_ENDERECO, pet.getEndereco());
        values.put(RegistroPetModel.COLUNA_BAIRRO, pet.getBairro());
        values.put(RegistroPetModel.COLUNA_CIDADE, pet.getCidade());
        values.put(RegistroPetModel.COLUNA_TELEFONERESD, pet.getTelefoneresd());
        values.put(RegistroPetModel.COLUNA_EMAIL, pet.getEmail());
        values.put(RegistroPetModel.COLUNA_CEP, pet.getCep());
        values.put(RegistroPetModel.COLUNA_ESTADO, pet.getEstado());
        values.put(RegistroPetModel.COLUNA_TELEFONECEL, pet.getTelefonecel());
        values.put(RegistroPetModel.COLUNA_DESCRICAO, pet.getDescricao());
        values.put(RegistroPetModel.COLUNA_URL_IMAGEM, pet.getUrlImagem());

        db.insert(RegistroPetModel.TABELA_PET, null, values);
        Close();
    }

    public List<RegistroPetModel> getAllPets() {
        List<RegistroPetModel> petList = new ArrayList<>();
        Open();

        Cursor cursor = db.query(
                RegistroPetModel.TABELA_PET,
                colunas,
                null,
                null,
                null,
                null,
                RegistroPetModel.COLUNA_NOMEPET + " ASC"
        );

        if (cursor.moveToFirst()) {
            do {
                RegistroPetModel pet = new RegistroPetModel();
                pet.setId(cursor.getLong(cursor.getColumnIndexOrThrow(RegistroPetModel.COLUNA_ID)));
                pet.setNomepet(cursor.getString(cursor.getColumnIndexOrThrow(RegistroPetModel.COLUNA_NOMEPET)));

                int nascimentoIndex = cursor.getColumnIndexOrThrow(RegistroPetModel.COLUNA_NASCIMENTO);
                if (!cursor.isNull(nascimentoIndex)) {
                    pet.setNascimento(cursor.getDouble(nascimentoIndex));
                } else {
                    pet.setNascimento(null);
                }

                pet.setEspecie(cursor.getString(cursor.getColumnIndexOrThrow(RegistroPetModel.COLUNA_ESPECIE)));
                pet.setSexo(cursor.getString(cursor.getColumnIndexOrThrow(RegistroPetModel.COLUNA_SEXO)));
                pet.setPai(cursor.getString(cursor.getColumnIndexOrThrow(RegistroPetModel.COLUNA_PAI)));
                pet.setMae(cursor.getString(cursor.getColumnIndexOrThrow(RegistroPetModel.COLUNA_MAE)));
                pet.setRaca(cursor.getString(cursor.getColumnIndexOrThrow(RegistroPetModel.COLUNA_RACA)));
                pet.setNaturalidade(cursor.getString(cursor.getColumnIndexOrThrow(RegistroPetModel.COLUNA_NATURALIDADE)));
                pet.setCor(cursor.getString(cursor.getColumnIndexOrThrow(RegistroPetModel.COLUNA_COR)));
                pet.setEndereco(cursor.getString(cursor.getColumnIndexOrThrow(RegistroPetModel.COLUNA_ENDERECO)));
                pet.setBairro(cursor.getString(cursor.getColumnIndexOrThrow(RegistroPetModel.COLUNA_BAIRRO)));
                pet.setCidade(cursor.getString(cursor.getColumnIndexOrThrow(RegistroPetModel.COLUNA_CIDADE)));

                int telResdIndex = cursor.getColumnIndexOrThrow(RegistroPetModel.COLUNA_TELEFONERESD);
                if (!cursor.isNull(telResdIndex)) {
                    pet.setTelefoneresd(cursor.getDouble(telResdIndex));
                } else {
                    pet.setTelefoneresd(null);
                }

                pet.setEmail(cursor.getString(cursor.getColumnIndexOrThrow(RegistroPetModel.COLUNA_EMAIL)));
                pet.setCep(cursor.getDouble(cursor.getColumnIndexOrThrow(RegistroPetModel.COLUNA_CEP)));
                pet.setEstado(cursor.getString(cursor.getColumnIndexOrThrow(RegistroPetModel.COLUNA_ESTADO)));

                int telCelIndex = cursor.getColumnIndexOrThrow(RegistroPetModel.COLUNA_TELEFONECEL);
                if (!cursor.isNull(telCelIndex)) {
                    pet.setTelefonecel(cursor.getDouble(telCelIndex));
                } else {
                    pet.setTelefonecel(null);
                }

                pet.setDescricao(cursor.getString(cursor.getColumnIndexOrThrow(RegistroPetModel.COLUNA_DESCRICAO)));
                pet.setUrlImagem(cursor.getString(cursor.getColumnIndexOrThrow(RegistroPetModel.COLUNA_URL_IMAGEM)));

                petList.add(pet);
            } while (cursor.moveToNext());
        }

        cursor.close();
        Close();
        return petList;
    }

    public boolean update(RegistroPetModel pet) {
        SQLiteDatabase db = null;
        try {
            Open();
            db = this.db;

            ContentValues contentValues = new ContentValues();
            contentValues.put("nomepet", pet.getNomepet());
            contentValues.put("raca", pet.getRaca());
            contentValues.put("especie", pet.getEspecie());
            contentValues.put("sexo", pet.getSexo());
            contentValues.put("cor", pet.getCor());
            contentValues.put("cidade", pet.getCidade());
            contentValues.put("estado", pet.getEstado());
            contentValues.put("bairro", pet.getBairro());
            contentValues.put("endereco", pet.getEndereco());
            contentValues.put("email", pet.getEmail());
            contentValues.put("pai", pet.getPai());
            contentValues.put("mae", pet.getMae());
            contentValues.put("naturalidade", pet.getNaturalidade());
            contentValues.put("descricao", pet.getDescricao());
            contentValues.put("urlImagem", pet.getUrlImagem());

            if (pet.getCep() != null) {
                contentValues.put("cep", pet.getCep());
            } else {
                contentValues.putNull("cep");
            }

            if (pet.getTelefoneresd() != null) {
                contentValues.put("telefoneresd", pet.getTelefoneresd());
            } else {
                contentValues.putNull("telefoneresd");
            }

            if (pet.getTelefonecel() != null) {
                contentValues.put("telefonecel", pet.getTelefonecel());
            } else {
                contentValues.putNull("telefonecel");
            }

            if (pet.getNascimento() != null) {
                contentValues.put("nascimento", pet.getNascimento());
            } else {
                contentValues.putNull("nascimento");
            }

            int rowsAffected = db.update(
                    RegistroPetModel.TABELA_PET,
                    contentValues,
                    RegistroPetModel.COLUNA_ID + " = ?",
                    new String[]{String.valueOf(pet.getId())}
            );

            return rowsAffected > 0;

        } catch (Exception e) {
            Log.e("RegistroPetDAO", "Erro ao fazer update do pet: " + e.getMessage(), e);
            return false;
        } finally {
            Close();
        }
    }

    public void delete(Long id) {
        Open();
        db.delete(RegistroPetModel.TABELA_PET, RegistroPetModel.COLUNA_ID + " = ?", new String[]{id.toString()});
        Close();
    }

    public RegistroPetModel getPetById(Long id) {
        Open();
        Cursor cursor = db.query(
                RegistroPetModel.TABELA_PET,
                colunas,
                RegistroPetModel.COLUNA_ID + " = ?",
                new String[]{String.valueOf(id)},
                null,
                null,
                null
        );

        RegistroPetModel pet = null;
        if (cursor.moveToFirst()) {
            pet = new RegistroPetModel();
            pet.setId(cursor.getLong(cursor.getColumnIndexOrThrow(RegistroPetModel.COLUNA_ID)));
            pet.setNomepet(cursor.getString(cursor.getColumnIndexOrThrow(RegistroPetModel.COLUNA_NOMEPET)));

            int nascimentoIndex = cursor.getColumnIndexOrThrow(RegistroPetModel.COLUNA_NASCIMENTO);
            if (!cursor.isNull(nascimentoIndex)) {
                pet.setNascimento(cursor.getDouble(nascimentoIndex));
            }

            pet.setEspecie(cursor.getString(cursor.getColumnIndexOrThrow(RegistroPetModel.COLUNA_ESPECIE)));
            pet.setSexo(cursor.getString(cursor.getColumnIndexOrThrow(RegistroPetModel.COLUNA_SEXO)));
            pet.setPai(cursor.getString(cursor.getColumnIndexOrThrow(RegistroPetModel.COLUNA_PAI)));
            pet.setMae(cursor.getString(cursor.getColumnIndexOrThrow(RegistroPetModel.COLUNA_MAE)));
            pet.setRaca(cursor.getString(cursor.getColumnIndexOrThrow(RegistroPetModel.COLUNA_RACA)));
            pet.setNaturalidade(cursor.getString(cursor.getColumnIndexOrThrow(RegistroPetModel.COLUNA_NATURALIDADE)));
            pet.setCor(cursor.getString(cursor.getColumnIndexOrThrow(RegistroPetModel.COLUNA_COR)));
            pet.setEndereco(cursor.getString(cursor.getColumnIndexOrThrow(RegistroPetModel.COLUNA_ENDERECO)));
            pet.setBairro(cursor.getString(cursor.getColumnIndexOrThrow(RegistroPetModel.COLUNA_BAIRRO)));
            pet.setCidade(cursor.getString(cursor.getColumnIndexOrThrow(RegistroPetModel.COLUNA_CIDADE)));

            int telResdIndex = cursor.getColumnIndexOrThrow(RegistroPetModel.COLUNA_TELEFONERESD);
            if (!cursor.isNull(telResdIndex)) {
                pet.setTelefoneresd(cursor.getDouble(telResdIndex));
            }

            pet.setEmail(cursor.getString(cursor.getColumnIndexOrThrow(RegistroPetModel.COLUNA_EMAIL)));

            int cepIndex = cursor.getColumnIndexOrThrow(RegistroPetModel.COLUNA_CEP);
            if (!cursor.isNull(cepIndex)) {
                pet.setCep(cursor.getDouble(cepIndex));
            }

            pet.setEstado(cursor.getString(cursor.getColumnIndexOrThrow(RegistroPetModel.COLUNA_ESTADO)));

            int telCelIndex = cursor.getColumnIndexOrThrow(RegistroPetModel.COLUNA_TELEFONECEL);
            if (!cursor.isNull(telCelIndex)) {
                pet.setTelefonecel(cursor.getDouble(telCelIndex));
            }

            pet.setDescricao(cursor.getString(cursor.getColumnIndexOrThrow(RegistroPetModel.COLUNA_DESCRICAO)));
            pet.setUrlImagem(cursor.getString(cursor.getColumnIndexOrThrow(RegistroPetModel.COLUNA_URL_IMAGEM)));
        }

        cursor.close();
        Close();
        return pet;
    }
}