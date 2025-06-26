package com.example.petapp.api.apicep;

import com.example.petapp.api.apicep.endpoint.CepEndpoint;
import com.example.petapp.api.apicep.model.Cep;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class Api {

    public static final String BASE_URL = "https://h-apigateway.conectagov.estaleiro.serpro.gov.br/api-cep/v1/consulta/cep/";

    private static Retrofit retrofit = new Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build();

    public static void getCep(String cep, Callback<List<Cep>> callback) {
        CepEndpoint endpoint = retrofit.create(CepEndpoint.class);
        Call<List<Cep>> call = endpoint.getCep(cep);
        call.enqueue(callback);
    }

}
