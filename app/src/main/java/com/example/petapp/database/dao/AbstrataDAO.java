package com.example.petapp.database.dao;

import android.database.sqlite.SQLiteDatabase;

import com.example.petapp.database.DBHelperPet;
import com.example.petapp.database.DBHelperUser;

//Abre e Fecha o Banco
public abstract class AbstrataDAO {

    public SQLiteDatabase dbUser, dbPet;
    public DBHelperUser dbHelperUser;
    public DBHelperPet dbHelperPet;

    public final void Open(){
        //Abre o Banco de Dados
        dbUser = dbHelperUser.getWritableDatabase();
        dbPet = dbHelperPet.getWritableDatabase();
    }

    public final void Close(){
        //Fecha o Banco de Dados
        dbHelperUser.close();
        dbHelperPet.close();
    }

}
