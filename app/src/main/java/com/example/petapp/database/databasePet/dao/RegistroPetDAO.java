package com.example.petapp.database.databasePet.dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;

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
            RegistroPetModel.COLUNA_DESCRICAO
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

        db.insert(RegistroPetModel.TABELA_PET, null, values);
        Close();
    }

    public List<RegistroPetModel> getAllPets() {
        List<RegistroPetModel> petList = new ArrayList<>();
        Open(); // Open the database connection

        Cursor cursor = db.query(
                RegistroPetModel.TABELA_PET, // The table name
                colunas,                     // The columns to return
                null,                        // The columns for the WHERE clause (null means all rows)
                null,                        // The values for the WHERE clause
                null,                        // don't group the rows
                null,                        // don't filter by row groups
                RegistroPetModel.COLUNA_NOMEPET + " ASC" // The sort order (order by name ascending)
        );

        // Loop through all rows and add to list
        if (cursor.moveToFirst()) {
            do {
                RegistroPetModel pet = new RegistroPetModel();
                pet.setId(cursor.getLong(cursor.getColumnIndexOrThrow(RegistroPetModel.COLUNA_ID)));
                pet.setNomepet(cursor.getString(cursor.getColumnIndexOrThrow(RegistroPetModel.COLUNA_NOMEPET)));

                // Handle possible null values for Double types
                int nascimentoIndex = cursor.getColumnIndexOrThrow(RegistroPetModel.COLUNA_NASCIMENTO);
                if (!cursor.isNull(nascimentoIndex)) {
                    pet.setNascimento(cursor.getDouble(nascimentoIndex));
                } else {
                    pet.setNascimento(null); // Or 0.0, or handle as needed
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
                pet.setCep(cursor.getDouble(cursor.getColumnIndexOrThrow(RegistroPetModel.COLUNA_CEP))); // Assuming CEP is String
                pet.setEstado(cursor.getString(cursor.getColumnIndexOrThrow(RegistroPetModel.COLUNA_ESTADO)));

                int telCelIndex = cursor.getColumnIndexOrThrow(RegistroPetModel.COLUNA_TELEFONECEL);
                if (!cursor.isNull(telCelIndex)) {
                    pet.setTelefonecel(cursor.getDouble(telCelIndex));
                } else {
                    pet.setTelefonecel(null);
                }

                pet.setDescricao(cursor.getString(cursor.getColumnIndexOrThrow(RegistroPetModel.COLUNA_DESCRICAO)));

                petList.add(pet);
            } while (cursor.moveToNext());
        }

        cursor.close(); // Always close the cursor
        Close();      // Close the database connection
        return petList;
    }

    public void update(RegistroPetModel pet) {
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

        db.update(RegistroPetModel.TABELA_PET, values, RegistroPetModel.COLUNA_ID + " = ?", new String[]{pet.getId().toString()});
        Close();
    }

    public void delete(Long id) {
        Open();
        db.delete(RegistroPetModel.TABELA_PET, RegistroPetModel.COLUNA_ID + " = ?", new String[]{id.toString()});
        Close();
    }

}
