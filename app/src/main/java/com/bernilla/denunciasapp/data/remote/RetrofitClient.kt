package com.bernilla.denunciasapp.data.remote

import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

/**
 * Objeto Singleton que provee la instancia única de Retrofit configurada para Render.com.
 */
object RetrofitClient {

    // URL Base del servidor Spring Boot en Render
    private const val BASE_URL = "https://denunciaservice-bernilla.onrender.com/"

    // Interceptor para visualizar las peticiones y respuestas HTTP en la consola de Logcat
    private val loggingInterceptor = HttpLoggingInterceptor().apply {
        level = HttpLoggingInterceptor.Level.BODY
    }

    // Configuración del cliente HTTP con timeouts de 90 segundos por si la instancia de Render está dormida
    private val okHttpClient = OkHttpClient.Builder()
        .connectTimeout(90, TimeUnit.SECONDS)
        .readTimeout(90, TimeUnit.SECONDS)
        .writeTimeout(90, TimeUnit.SECONDS)
        .addInterceptor(loggingInterceptor)
        .build()

    // Instancia del servicio API mediante Retrofit y GsonConverterFactory
    val instance: DenunciaApiService by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(DenunciaApiService::class.java)
    }
}
