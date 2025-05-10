package com.example.petapp.database.dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;

import com.example.petapp.database.DBHelperUser;
import com.example.petapp.database.model.RegistroUserModel;

public class RegistroUserDAO extends AbstrataDAO {

    private String[] colunas = new String[]{
            RegistroUserModel.COLUNA_ID,
            RegistroUserModel.COLUNA_EMAIL,
            RegistroUserModel.COLUNA_SENHA
    };

    public RegistroUserDAO(Context context) {
        dbHelperUser = new DBHelperUser(context);
    }

    public boolean select(String email, String senha) {
        Cursor cursor = dbUser.query(
                RegistroUserModel.TABELA_USUARIO,
                colunas,
                RegistroUserModel.COLUNA_EMAIL + " = ? AND " +
                        RegistroUserModel.COLUNA_SENHA + " = ? ",
                new String[]{email, senha}, null, null, null);
        cursor.moveToFirst();
        return cursor.getCount() > 0;
    }

    public void insert(RegistroUserModel usuario) {

        Open();
        ContentValues values = new ContentValues();
        values.put(RegistroUserModel.COLUNA_EMAIL, usuario.getEmail());
        values.put(RegistroUserModel.COLUNA_SENHA, usuario.getSenha());

        dbUser.insert(RegistroUserModel.TABELA_USUARIO, null, values);
        Close();
    }

    public void update(RegistroUserModel usuario) {

        Open();
        ContentValues values = new ContentValues();
        values.put(RegistroUserModel.COLUNA_EMAIL, usuario.getEmail());
        values.put(RegistroUserModel.COLUNA_SENHA, usuario.getSenha());

        dbUser.update(RegistroUserModel.TABELA_USUARIO, values,
                RegistroUserModel.COLUNA_ID + " = ? ",
                new String[]{usuario.getId().toString()});
        Close();

    }

    public void delete(RegistroUserModel usuario) {

        Open();
        dbUser.delete(RegistroUserModel.TABELA_USUARIO,
                RegistroUserModel.COLUNA_ID + " = ? ",
                new String[]{usuario.getId().toString()});
        Close();

    }

}
