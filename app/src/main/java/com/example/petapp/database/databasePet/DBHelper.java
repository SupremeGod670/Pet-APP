package com.example.petapp.database.databasePet;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.example.petapp.database.databasePet.model.RegistroPetModel;

public class DBHelper extends SQLiteOpenHelper {

    public static final String DATABASE_NAME = "pet.db";
    public static final int DATABASE_VERSION = 1;

    public DBHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(RegistroPetModel.CREATE_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL(RegistroPetModel.DROP_TABLE);
        db.execSQL(RegistroPetModel.CREATE_TABLE);
    }
}
