package com.example.petapp.database.databaseUser.dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;

import com.example.petapp.database.databaseUser.DBHelper;
import com.example.petapp.database.databaseUser.model.RegistroUserModel;

public class RegistroUserDAO extends AbstrataDAO {

    private String[] colunas = new String[]{
            RegistroUserModel.COLUNA_ID,
            RegistroUserModel.COLUNA_NOME,
            RegistroUserModel.COLUNA_EMAIL,
            RegistroUserModel.COLUNA_SENHA
    };

    public RegistroUserDAO(Context context) {
        dbHelper = new DBHelper(context);
    }

    public boolean selectEmail(String email){
        Open();
        Cursor cursor = db.query(
                RegistroUserModel.TABELA_USUARIO,
                colunas,
                RegistroUserModel.COLUNA_EMAIL + " = ? ",
                new String[]{email}, null, null, null);
        cursor.moveToFirst();
        boolean exists = cursor.getCount() > 0;
        cursor.close();
        Close();
        return exists;
    }

    public boolean selectSenha(String senha){
        Open();
        Cursor cursor = db.query(
                RegistroUserModel.TABELA_USUARIO,
                colunas,
                RegistroUserModel.COLUNA_SENHA + " = ? ",
                new String[]{senha}, null, null, null);
        cursor.moveToFirst();
        boolean exists = cursor.getCount() > 0;
        cursor.close();
        Close();
        return exists;
    }

    public boolean select(String email, String senha){
        Open();
        Cursor cursor = db.query(
                RegistroUserModel.TABELA_USUARIO,
                colunas,
                RegistroUserModel.COLUNA_EMAIL + " = ? AND " +
                        RegistroUserModel.COLUNA_SENHA + " = ? ",
                new String[]{email, senha}, null, null, null);
        cursor.moveToFirst();
        boolean exists = cursor.getCount() > 0;
        cursor.close();
        Close();
        return exists;
    }

    public void insert(RegistroUserModel usuario) {
        Open();
        ContentValues values = new ContentValues();
        values.put(RegistroUserModel.COLUNA_NOME, usuario.getNome());
        values.put(RegistroUserModel.COLUNA_EMAIL, usuario.getEmail());
        values.put(RegistroUserModel.COLUNA_SENHA, usuario.getSenha());

        db.insert(RegistroUserModel.TABELA_USUARIO, null, values);
        Close();
    }

    public void update(RegistroUserModel usuario) {
        Open();
        ContentValues values = new ContentValues();
        values.put(RegistroUserModel.COLUNA_NOME, usuario.getNome());
        values.put(RegistroUserModel.COLUNA_EMAIL, usuario.getEmail());
        values.put(RegistroUserModel.COLUNA_SENHA, usuario.getSenha());

        db.update(RegistroUserModel.TABELA_USUARIO, values,
                RegistroUserModel.COLUNA_ID + " = ? ",
                new String[]{usuario.getId().toString()});
        Close();
    }

    public void delete(RegistroUserModel usuario) {
        Open();
        db.delete(RegistroUserModel.TABELA_USUARIO,
                RegistroUserModel.COLUNA_ID + " = ? ",
                new String[]{usuario.getId().toString()});
        Close();
    }

    // Novo método para buscar usuário por email
    public RegistroUserModel getUsuarioByEmail(String email) {
        Open();
        Cursor cursor = db.query(
                RegistroUserModel.TABELA_USUARIO,
                colunas,
                RegistroUserModel.COLUNA_EMAIL + " = ?",
                new String[]{email},
                null,
                null,
                null
        );

        RegistroUserModel usuario = null;
        if (cursor.moveToFirst()) {
            usuario = new RegistroUserModel();
            usuario.setId(cursor.getLong(cursor.getColumnIndexOrThrow(RegistroUserModel.COLUNA_ID)));
            usuario.setNome(cursor.getString(cursor.getColumnIndexOrThrow(RegistroUserModel.COLUNA_NOME)));
            usuario.setEmail(cursor.getString(cursor.getColumnIndexOrThrow(RegistroUserModel.COLUNA_EMAIL)));
            usuario.setSenha(cursor.getString(cursor.getColumnIndexOrThrow(RegistroUserModel.COLUNA_SENHA)));
        }

        cursor.close();
        Close();
        return usuario;
    }

    // Novo método para buscar usuário por ID
    public RegistroUserModel getUsuarioById(Long id) {
        Open();
        Cursor cursor = db.query(
                RegistroUserModel.TABELA_USUARIO,
                colunas,
                RegistroUserModel.COLUNA_ID + " = ?",
                new String[]{String.valueOf(id)},
                null,
                null,
                null
        );

        RegistroUserModel usuario = null;
        if (cursor.moveToFirst()) {
            usuario = new RegistroUserModel();
            usuario.setId(cursor.getLong(cursor.getColumnIndexOrThrow(RegistroUserModel.COLUNA_ID)));
            usuario.setNome(cursor.getString(cursor.getColumnIndexOrThrow(RegistroUserModel.COLUNA_NOME)));
            usuario.setEmail(cursor.getString(cursor.getColumnIndexOrThrow(RegistroUserModel.COLUNA_EMAIL)));
            usuario.setSenha(cursor.getString(cursor.getColumnIndexOrThrow(RegistroUserModel.COLUNA_SENHA)));
        }

        cursor.close();
        Close();
        return usuario;
    }

    // Método para atualizar apenas o nome
    public void updateNome(String email, String novoNome) {
        Open();
        ContentValues values = new ContentValues();
        values.put(RegistroUserModel.COLUNA_NOME, novoNome);

        db.update(RegistroUserModel.TABELA_USUARIO, values,
                RegistroUserModel.COLUNA_EMAIL + " = ?",
                new String[]{email});
        Close();
    }

    // Método para atualizar apenas a senha
    public void updateSenha(String email, String novaSenha) {
        Open();
        ContentValues values = new ContentValues();
        values.put(RegistroUserModel.COLUNA_SENHA, novaSenha);

        db.update(RegistroUserModel.TABELA_USUARIO, values,
                RegistroUserModel.COLUNA_EMAIL + " = ?",
                new String[]{email});
        Close();
    }
}