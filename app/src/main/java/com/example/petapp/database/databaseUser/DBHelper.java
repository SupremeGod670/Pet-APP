package com.example.petapp.database.databaseUser;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.example.petapp.database.databaseUser.model.RegistroUserModel;

public class DBHelper extends SQLiteOpenHelper {
    public static final String DATABASE_NAME = "registro.db";
    public static final int DATABASE_VERSION = 3; // Incrementado para adicionar coluna foto_perfil

    public DBHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(RegistroUserModel.CREATE_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        if (oldVersion < 2) {
            // Adicionar coluna nome para usuários existentes
            db.execSQL("ALTER TABLE " + RegistroUserModel.TABELA_USUARIO + " ADD COLUMN " + RegistroUserModel.COLUNA_NOME + " TEXT;");
        }
        if (oldVersion < 3) {
            // Adicionar coluna foto_perfil para usuários existentes
            db.execSQL("ALTER TABLE " + RegistroUserModel.TABELA_USUARIO + " ADD COLUMN " + RegistroUserModel.COLUNA_FOTO_PERFIL + " TEXT;");
        }
    }
}