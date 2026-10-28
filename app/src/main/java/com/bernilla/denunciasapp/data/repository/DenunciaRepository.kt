package com.bernilla.denunciasapp.data.repository

import com.bernilla.denunciasapp.data.model.Denuncia
import com.bernilla.denunciasapp.data.remote.DenunciaApiService
import com.bernilla.denunciasapp.data.remote.RetrofitClient
import retrofit2.awaitResponse
import java.io.IOException

/**
 * Repositorio que maneja el consumo de la API REST de Denuncias.
 */
class DenunciaRepository(
    private val apiService: DenunciaApiService = RetrofitClient.instance
) {

    suspend fun listarDenuncias(): Result<List<Denuncia>> {
        return try {
            val response = apiService.listarDenuncias().awaitResponse()
            if (response.isSuccessful) {
                Result.success(response.body() ?: emptyList())
            } else {
                Result.failure(Exception("Error al listar denuncias: Código ${response.code()}"))
            }
        } catch (e: IOException) {
            Result.failure(Exception("No se pudo conectar con el servidor en Render..."))
        } catch (e: Exception) {
            Result.failure(Exception("Error inesperado: ${e.message}"))
        }
    }

    suspend fun registrarDenuncia(denuncia: Denuncia): Result<Denuncia> {
        return try {
            val response = apiService.registrarDenuncia(denuncia).awaitResponse()
            if (response.isSuccessful && response.body() != null) {
                Result.success(response.body()!!)
            } else if (response.isSuccessful) {
                Result.success(denuncia)
            } else {
                Result.failure(Exception("Error al registrar: Código ${response.code()}"))
            }
        } catch (e: IOException) {
            Result.failure(Exception("No se pudo conectar con el servidor en Render..."))
        } catch (e: Exception) {
            Result.failure(Exception("Error inesperado: ${e.message}"))
        }
    }
}
