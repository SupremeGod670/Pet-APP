package com.example.petapp.database.databaseUser.dao;

import android.database.sqlite.SQLiteDatabase;

import com.example.petapp.database.databaseUser.DBHelper;

//Abre e Fecha o Banco
public abstract class AbstrataDAO {

    public SQLiteDatabase db;
    public DBHelper dbHelper;

    public final void Open(){
        //Abre o Banco de Dados
        db = dbHelper.getWritableDatabase();
    }

    public final void Close(){
        //Fecha o Banco de Dados
        dbHelper.close();
    }

}
