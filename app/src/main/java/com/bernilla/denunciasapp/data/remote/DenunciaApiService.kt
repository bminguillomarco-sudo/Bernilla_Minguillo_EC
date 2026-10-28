package com.bernilla.denunciasapp.data.remote

import com.bernilla.denunciasapp.data.model.Denuncia
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST

/**
 * Interfaz de Retrofit que define las peticiones HTTP a la API REST de Spring Boot.
 */
interface DenunciaApiService {

    /**
     * Endpoint GET para obtener el listado de todas las denuncias.
     * Petición a: https://denunciaservice-bernilla.onrender.com/denuncias
     */
    @GET("denuncias")
    fun listarDenuncias(): Call<List<Denuncia>>

    /**
     * Endpoint POST para registrar una nueva denuncia.
     * Petición a: https://denunciaservice-bernilla.onrender.com/denuncias
     */
    @POST("denuncias")
    fun registrarDenuncia(@Body denuncia: Denuncia): Call<Denuncia>
}
