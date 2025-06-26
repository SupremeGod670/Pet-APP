package com.example.petapp.api.apicep.endpoint;

import com.example.petapp.api.apicep.model.Cep;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;

public interface CepEndpoint {

    @GET("/consulta/cep/{cep}")
    Call<List<Cep>> getCep(@Path("cep") String cep);

}
