package com.example.petapp.database.databaseUser.dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.util.Log;

import com.example.petapp.database.databaseUser.DBHelper;
import com.example.petapp.database.databaseUser.model.RegistroUserModel;

public class RegistroUserDAO extends AbstrataDAO {

    private String[] colunas = new String[]{
            RegistroUserModel.COLUNA_ID,
            RegistroUserModel.COLUNA_NOME,
            RegistroUserModel.COLUNA_EMAIL,
            RegistroUserModel.COLUNA_SENHA,
            RegistroUserModel.COLUNA_FOTO_PERFIL // Nova coluna
    };

    public RegistroUserDAO(Context context) {
        dbHelper = new DBHelper(context);
    }

    public boolean selectEmail(String email){
        try {
            Open();
            Cursor cursor = db.query(
                    RegistroUserModel.TABELA_USUARIO,
                    colunas,
                    RegistroUserModel.COLUNA_EMAIL + " = ? ",
                    new String[]{email}, null, null, null);
            cursor.moveToFirst();
            boolean exists = cursor.getCount() > 0;
            cursor.close();
            return exists;
        } catch (Exception e) {
            Log.e("RegistroUserDAO", "Erro ao verificar email: " + e.getMessage(), e);
            return false;
        } finally {
            try {
                Close();
            } catch (Exception e) {
                // Ignore
            }
        }
    }

    public boolean selectSenha(String senha){
        try {
            Open();
            Cursor cursor = db.query(
                    RegistroUserModel.TABELA_USUARIO,
                    colunas,
                    RegistroUserModel.COLUNA_SENHA + " = ? ",
                    new String[]{senha}, null, null, null);
            cursor.moveToFirst();
            boolean exists = cursor.getCount() > 0;
            cursor.close();
            return exists;
        } catch (Exception e) {
            Log.e("RegistroUserDAO", "Erro ao verificar senha: " + e.getMessage(), e);
            return false;
        } finally {
            try {
                Close();
            } catch (Exception e) {
                // Ignore
            }
        }
    }

    public boolean select(String email, String senha){
        try {
            Open();
            Log.d("RegistroUserDAO", "Verificando login - Email: " + email);

            Cursor cursor = db.query(
                    RegistroUserModel.TABELA_USUARIO,
                    colunas,
                    RegistroUserModel.COLUNA_EMAIL + " = ? AND " +
                            RegistroUserModel.COLUNA_SENHA + " = ? ",
                    new String[]{email, senha}, null, null, null);

            boolean exists = cursor.getCount() > 0;

            if (cursor.moveToFirst()) {
                String senhaDb = cursor.getString(cursor.getColumnIndexOrThrow(RegistroUserModel.COLUNA_SENHA));
                Log.d("RegistroUserDAO", "Senha no banco: " + senhaDb);
                Log.d("RegistroUserDAO", "Senha fornecida: " + senha);
                Log.d("RegistroUserDAO", "Senhas iguais: " + senhaDb.equals(senha));
            }

            cursor.close();
            Log.d("RegistroUserDAO", "Resultado da verificação: " + exists);
            return exists;
        } catch (Exception e) {
            Log.e("RegistroUserDAO", "Erro ao verificar login: " + e.getMessage(), e);
            return false;
        } finally {
            try {
                Close();
            } catch (Exception e) {
                // Ignore
            }
        }
    }

    public void insert(RegistroUserModel usuario) {
        try {
            Open();
            ContentValues values = new ContentValues();
            values.put(RegistroUserModel.COLUNA_NOME, usuario.getNome());
            values.put(RegistroUserModel.COLUNA_EMAIL, usuario.getEmail());
            values.put(RegistroUserModel.COLUNA_SENHA, usuario.getSenha());
            values.put(RegistroUserModel.COLUNA_FOTO_PERFIL, usuario.getFotoPerfil());

            db.insert(RegistroUserModel.TABELA_USUARIO, null, values);
        } catch (Exception e) {
            Log.e("RegistroUserDAO", "Erro ao inserir usuário: " + e.getMessage(), e);
            throw e;
        } finally {
            try {
                Close();
            } catch (Exception e) {
                // Ignore
            }
        }
    }

    public void update(RegistroUserModel usuario) {
        try {
            Open();
            ContentValues values = new ContentValues();
            values.put(RegistroUserModel.COLUNA_NOME, usuario.getNome());
            values.put(RegistroUserModel.COLUNA_EMAIL, usuario.getEmail());
            values.put(RegistroUserModel.COLUNA_SENHA, usuario.getSenha());
            values.put(RegistroUserModel.COLUNA_FOTO_PERFIL, usuario.getFotoPerfil());

            db.update(RegistroUserModel.TABELA_USUARIO, values,
                    RegistroUserModel.COLUNA_ID + " = ? ",
                    new String[]{usuario.getId().toString()});
        } catch (Exception e) {
            Log.e("RegistroUserDAO", "Erro ao atualizar usuário: " + e.getMessage(), e);
            throw e;
        } finally {
            try {
                Close();
            } catch (Exception e) {
                // Ignore
            }
        }
    }

    public void delete(RegistroUserModel usuario) {
        try {
            Open();
            db.delete(RegistroUserModel.TABELA_USUARIO,
                    RegistroUserModel.COLUNA_ID + " = ? ",
                    new String[]{usuario.getId().toString()});
        } catch (Exception e) {
            Log.e("RegistroUserDAO", "Erro ao deletar usuário: " + e.getMessage(), e);
            throw e;
        } finally {
            try {
                Close();
            } catch (Exception e) {
                // Ignore
            }
        }
    }

    // Método para buscar usuário por email
    public RegistroUserModel getUsuarioByEmail(String email) {
        try {
            Open();
            Log.d("RegistroUserDAO", "=== BUSCANDO USUÁRIO POR EMAIL ===");
            Log.d("RegistroUserDAO", "Email procurado: '" + email + "'");

            Cursor cursor = db.query(
                    RegistroUserModel.TABELA_USUARIO,
                    colunas,
                    RegistroUserModel.COLUNA_EMAIL + " = ?",
                    new String[]{email},
                    null,
                    null,
                    null
            );

            Log.d("RegistroUserDAO", "Resultados encontrados: " + cursor.getCount());

            RegistroUserModel usuario = null;
            if (cursor.moveToFirst()) {
                usuario = new RegistroUserModel();
                usuario.setId(cursor.getLong(cursor.getColumnIndexOrThrow(RegistroUserModel.COLUNA_ID)));
                usuario.setNome(cursor.getString(cursor.getColumnIndexOrThrow(RegistroUserModel.COLUNA_NOME)));
                usuario.setEmail(cursor.getString(cursor.getColumnIndexOrThrow(RegistroUserModel.COLUNA_EMAIL)));
                usuario.setSenha(cursor.getString(cursor.getColumnIndexOrThrow(RegistroUserModel.COLUNA_SENHA)));
                usuario.setFotoPerfil(cursor.getString(cursor.getColumnIndexOrThrow(RegistroUserModel.COLUNA_FOTO_PERFIL)));

                // Debug detalhado
                Log.d("RegistroUserDAO", "Usuário encontrado:");
                Log.d("RegistroUserDAO", "ID: " + usuario.getId());
                Log.d("RegistroUserDAO", "Nome: '" + usuario.getNome() + "'");
                Log.d("RegistroUserDAO", "Email: '" + usuario.getEmail() + "'");
                Log.d("RegistroUserDAO", "Senha: '" + usuario.getSenha() + "'");
                Log.d("RegistroUserDAO", "Foto: '" + usuario.getFotoPerfil() + "'");
            } else {
                Log.w("RegistroUserDAO", "Nenhum usuário encontrado com o email: " + email);

                // Debug: listar todos os emails no banco
                debugListarEmails();
            }

            cursor.close();
            return usuario;
        } catch (Exception e) {
            Log.e("RegistroUserDAO", "Erro ao buscar usuário por email: " + e.getMessage(), e);
            return null;
        } finally {
            try {
                Close();
            } catch (Exception e) {
                // Ignore
            }
        }
    }

    // Método auxiliar para debug - listar todos os emails
    private void debugListarEmails() {
        try {
            Cursor cursor = db.query(
                    RegistroUserModel.TABELA_USUARIO,
                    new String[]{RegistroUserModel.COLUNA_EMAIL, RegistroUserModel.COLUNA_SENHA},
                    null,
                    null,
                    null,
                    null,
                    null
            );

            Log.d("RegistroUserDAO", "=== TODOS OS EMAILS NO BANCO ===");
            if (cursor.moveToFirst()) {
                do {
                    String emailDb = cursor.getString(cursor.getColumnIndexOrThrow(RegistroUserModel.COLUNA_EMAIL));
                    String senhaDb = cursor.getString(cursor.getColumnIndexOrThrow(RegistroUserModel.COLUNA_SENHA));
                    Log.d("RegistroUserDAO", "Email: '" + emailDb + "', Senha: '" + senhaDb + "'");
                } while (cursor.moveToNext());
            } else {
                Log.d("RegistroUserDAO", "Nenhum usuário encontrado no banco!");
            }
            cursor.close();
        } catch (Exception e) {
            Log.e("RegistroUserDAO", "Erro ao listar emails: " + e.getMessage(), e);
        }
    }

    // Método para buscar usuário por ID
    public RegistroUserModel getUsuarioById(Long id) {
        try {
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
                usuario.setFotoPerfil(cursor.getString(cursor.getColumnIndexOrThrow(RegistroUserModel.COLUNA_FOTO_PERFIL)));
            }

            cursor.close();
            return usuario;
        } catch (Exception e) {
            Log.e("RegistroUserDAO", "Erro ao buscar usuário por ID: " + e.getMessage(), e);
            return null;
        } finally {
            try {
                Close();
            } catch (Exception e) {
                // Ignore
            }
        }
    }

    // Método para atualizar apenas o nome e foto
    public void updatePerfil(String email, String novoNome, String novaFotoPerfil) {
        try {
            Open();
            ContentValues values = new ContentValues();
            values.put(RegistroUserModel.COLUNA_NOME, novoNome);
            values.put(RegistroUserModel.COLUNA_FOTO_PERFIL, novaFotoPerfil);

            int rowsAffected = db.update(RegistroUserModel.TABELA_USUARIO, values,
                    RegistroUserModel.COLUNA_EMAIL + " = ?",
                    new String[]{email});

            Log.d("RegistroUserDAO", "Perfil atualizado. Linhas afetadas: " + rowsAffected);
        } catch (Exception e) {
            Log.e("RegistroUserDAO", "Erro ao atualizar perfil: " + e.getMessage(), e);
            throw e;
        } finally {
            try {
                Close();
            } catch (Exception e) {
                // Ignore
            }
        }
    }

    // Método para atualizar apenas a senha
    public void updateSenha(String email, String novaSenha) {
        try {
            Open();
            ContentValues values = new ContentValues();
            values.put(RegistroUserModel.COLUNA_SENHA, novaSenha);

            int rowsAffected = db.update(RegistroUserModel.TABELA_USUARIO, values,
                    RegistroUserModel.COLUNA_EMAIL + " = ?",
                    new String[]{email});

            Log.d("RegistroUserDAO", "Senha atualizada para email: " + email + ". Linhas afetadas: " + rowsAffected);
        } catch (Exception e) {
            Log.e("RegistroUserDAO", "Erro ao atualizar senha: " + e.getMessage(), e);
            throw e;
        } finally {
            try {
                Close();
            } catch (Exception e) {
                // Ignore
            }
        }
    }

    // Método para atualizar apenas a foto de perfil
    public void updateFotoPerfil(String email, String novaFotoPerfil) {
        try {
            Open();
            ContentValues values = new ContentValues();
            values.put(RegistroUserModel.COLUNA_FOTO_PERFIL, novaFotoPerfil);

            int rowsAffected = db.update(RegistroUserModel.TABELA_USUARIO, values,
                    RegistroUserModel.COLUNA_EMAIL + " = ?",
                    new String[]{email});

            Log.d("RegistroUserDAO", "Foto de perfil atualizada. Linhas afetadas: " + rowsAffected);
        } catch (Exception e) {
            Log.e("RegistroUserDAO", "Erro ao atualizar foto de perfil: " + e.getMessage(), e);
            throw e;
        } finally {
            try {
                Close();
            } catch (Exception e) {
                // Ignore
            }
        }
    }
}