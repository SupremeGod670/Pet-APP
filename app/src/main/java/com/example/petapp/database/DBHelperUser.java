package com.example.petapp.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.example.petapp.database.model.RegistroUserModel;

public class DBHelperUser extends SQLiteOpenHelper {
    public static final String DATABASE_NAME = "registro.db";
    public static final int DATABASE_VERSION = 1;

    public DBHelperUser(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase dbUser) {
        dbUser.execSQL(RegistroUserModel.CREATE_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase dbUser, int oldVersion, int newVersion) {
        dbUser.execSQL(RegistroUserModel.DROP_TABLE);
        dbUser.execSQL(RegistroUserModel.CREATE_TABLE);
    }
}
