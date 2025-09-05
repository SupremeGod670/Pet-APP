package com.example.petapp.database.databasePet;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.example.petapp.database.databasePet.model.RegistroPetModel;

public class DBHelper extends SQLiteOpenHelper {

    public static final String DATABASE_NAME = "pet.db";
    public static final int DATABASE_VERSION = 4; // Incrementar versão para forçar nova migração

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
            // Adicionar colunas da versão 2
            addColumnIfNotExists(db, RegistroPetModel.TABELA_PET, RegistroPetModel.COLUNA_COR, "TEXT");
            addColumnIfNotExists(db, RegistroPetModel.TABELA_PET, RegistroPetModel.COLUNA_CEP, "REAL");
            addColumnIfNotExists(db, RegistroPetModel.TABELA_PET, RegistroPetModel.COLUNA_TELEFONERESD, "REAL");
            addColumnIfNotExists(db, RegistroPetModel.TABELA_PET, RegistroPetModel.COLUNA_TELEFONECEL, "REAL");
        }

        if (oldVersion < 3) {
            // Adicionar coluna URL da imagem
            addColumnIfNotExists(db, RegistroPetModel.TABELA_PET, RegistroPetModel.COLUNA_URL_IMAGEM, "TEXT");
        }

        if (oldVersion < 4) {
            // Garantir que todas as colunas existam (para casos onde a migração anterior falhou)
            addColumnIfNotExists(db, RegistroPetModel.TABELA_PET, RegistroPetModel.COLUNA_COR, "TEXT");
            addColumnIfNotExists(db, RegistroPetModel.TABELA_PET, RegistroPetModel.COLUNA_CEP, "REAL");
            addColumnIfNotExists(db, RegistroPetModel.TABELA_PET, RegistroPetModel.COLUNA_TELEFONERESD, "REAL");
            addColumnIfNotExists(db, RegistroPetModel.TABELA_PET, RegistroPetModel.COLUNA_TELEFONECEL, "REAL");
            addColumnIfNotExists(db, RegistroPetModel.TABELA_PET, RegistroPetModel.COLUNA_URL_IMAGEM, "TEXT");
        }
    }

    /**
     * Adiciona uma coluna apenas se ela não existir
     */
    private void addColumnIfNotExists(SQLiteDatabase db, String tableName, String columnName, String columnType) {
        try {
            db.execSQL("ALTER TABLE " + tableName + " ADD COLUMN " + columnName + " " + columnType + ";");
        } catch (Exception e) {
            // Coluna já existe, ignorar erro
            android.util.Log.d("DBHelper", "Coluna " + columnName + " já existe ou erro ao adicionar: " + e.getMessage());
        }
    }
}