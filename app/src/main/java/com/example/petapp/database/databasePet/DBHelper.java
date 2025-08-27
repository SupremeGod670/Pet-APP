package com.example.petapp.database.databasePet;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.example.petapp.database.databasePet.model.RegistroPetModel;

public class DBHelper extends SQLiteOpenHelper {

    public static final String DATABASE_NAME = "pet.db";
    public static final int DATABASE_VERSION = 3; // Incrementar versão

    public DBHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(RegistroPetModel.CREATE_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        if (oldVersion < 2) {
            db.execSQL("ALTER TABLE " + RegistroPetModel.TABELA_PET + " ADD COLUMN " + RegistroPetModel.COLUNA_COR + " TEXT;");
            db.execSQL("ALTER TABLE " + RegistroPetModel.TABELA_PET + " ADD COLUMN " + RegistroPetModel.COLUNA_CEP + " TEXT;");
            db.execSQL("ALTER TABLE " + RegistroPetModel.TABELA_PET + " ADD COLUMN " + RegistroPetModel.COLUNA_TELEFONERESD + " REAL;");
            db.execSQL("ALTER TABLE " + RegistroPetModel.TABELA_PET + " ADD COLUMN " + RegistroPetModel.COLUNA_TELEFONECEL + " REAL;");
        }

        if (oldVersion < 3) {
            db.execSQL("ALTER TABLE " + RegistroPetModel.TABELA_PET + " ADD COLUMN " + RegistroPetModel.COLUNA_URL_IMAGEM + " TEXT;");
            db.execSQL("ALTER TABLE " + RegistroPetModel.TABELA_PET + " ADD COLUMN " + RegistroPetModel.COLUNA_COR + " TEXT;");
        }
    }
}
