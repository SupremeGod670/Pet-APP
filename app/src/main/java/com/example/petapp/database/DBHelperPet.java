package com.example.petapp.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.example.petapp.database.model.RegistroPetModel;

public class DBHelperPet extends SQLiteOpenHelper {

    public static final String DATABASE_NAME = "pet.db";
    public static final int DATABASE_VERSION = 1;

    public DBHelperPet(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase dbPet) {
        dbPet.execSQL(RegistroPetModel.CREATE_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase dbPet, int oldVersion, int newVersion) {
        dbPet.execSQL(RegistroPetModel.DROP_TABLE);
        dbPet.execSQL(RegistroPetModel.CREATE_TABLE);
    }
}
