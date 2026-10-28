package com.bernilla.denunciasapp.data.remote

import com.bernilla.denunciasapp.data.model.Denuncia
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path
import retrofit2.http.Query

/**
 * Interfaz de Retrofit que define las funciones suspendidas para realizar peticiones HTTP a la API.
 */
interface ApiService {

    /**
     * 1. POST denuncias/ -> Registra una nueva denuncia en la base de datos.
     * El servidor asigna el estado inicial 1 (Pendiente).
     */
    @POST("denuncias/")
    suspend fun registrarDenuncia(
        @Body denuncia: Denuncia
    ): Response<Denuncia>

    /**
     * 2. GET denuncias/?offset=0&limit=5 -> Obtiene la lista paginada de denuncias.
     * NOTA: Si tu API devuelve un objeto contenedor de paginación diferente (ej. PageResponse con campo 'content'),
     * cambia el tipo de retorno aquí a Response<PageResponse<Denuncia>>.
     */
    @GET("denuncias/")
    suspend fun getDenunciasPaginadas(
        @Query("offset") offset: Int,
        @Query("limit") limit: Int
    ): Response<List<Denuncia>>

    /**
     * 3. GET denuncias/{id} -> Obtiene el detalle de una denuncia específica por su ID.
     */
    @GET("denuncias/{id}")
    suspend fun getDenunciaPorId(
        @Path("id") id: Int
    ): Response<Denuncia>

    /**
     * 4. GET denuncias/ciudadano/{dni} -> Obtiene todas las denuncias registradas por un DNI.
     */
    @GET("denuncias/ciudadano/{dni}")
    suspend fun getDenunciasPorDni(
        @Path("dni") dni: String
    ): Response<List<Denuncia>>

    /**
     * 5. PUT denuncias/{id} -> Marca una denuncia como atendida (el estado pasa de 1 a 0).
     * No requiere body en la petición.
     */
    @PUT("denuncias/{id}")
    suspend fun atenderDenuncia(
        @Path("id") id: Int
    ): Response<Denuncia>
}
