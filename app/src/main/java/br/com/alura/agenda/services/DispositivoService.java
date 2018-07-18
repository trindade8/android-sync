package br.com.alura.agenda.services;

import retrofit2.Call;
import retrofit2.http.Header;
import retrofit2.http.POST;
import retrofit2.http.Path;

public interface DispositivoService {

    @POST("firebase/dispositivo")
    Call<Void> enviaToken( @Header("token") String token);
}
