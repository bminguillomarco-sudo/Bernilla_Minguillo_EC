package com.bernilla.denunciasapp.ui.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.bernilla.denunciasapp.data.model.Denuncia
import com.bernilla.denunciasapp.data.repository.DenunciaRepository
import kotlinx.coroutines.launch

/**
 * ViewModel para gestionar el estado de la UI y las llamadas a la API.
 */
class DenunciaViewModel(
    private val repository: DenunciaRepository = DenunciaRepository()
) : ViewModel() {

    private val _denuncias = MutableLiveData<List<Denuncia>>()
    val denuncias: LiveData<List<Denuncia>> get() = _denuncias

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> get() = _isLoading

    private val _errorMessage = MutableLiveData<String?>()
    val errorMessage: LiveData<String?> get() = _errorMessage

    private val _operacionExitosa = MutableLiveData<Boolean>()
    val operacionExitosa: LiveData<Boolean> get() = _operacionExitosa

    fun listarDenuncias() {
        _isLoading.value = true
        _errorMessage.value = null

        viewModelScope.launch {
            val result = repository.listarDenuncias()
            _isLoading.value = false

            result.onSuccess { lista ->
                _denuncias.value = lista
            }.onFailure { exception ->
                _errorMessage.value = exception.message
            }
        }
    }

    fun registrarDenuncia(
        dni: String,
        tipo: String = "",
        descripcion: String = "",
        direccion: String,
        referencia: String,
        fecha: String,
        usuario: String,
        estado: Int? = 1,
        titulo: String = "",
    ) {
        _isLoading.value = true
        _errorMessage.value = null
        _operacionExitosa.value = false

        val fechaIso = formatearFechaIso(fecha)
        val tituloFinal = if (titulo.isNotEmpty()) {
            titulo
        } else if (descripcion.isNotEmpty()) {
            "$tipo - $descripcion"
        } else {
            tipo
        }

        val nuevaDenuncia = Denuncia(
            dni = dni,
            tipo = tipo,
            titulo = tituloFinal,
            descripcion = descripcion,
            direccion = direccion,
            referencia = referencia,
            fecha = fechaIso,
            usuario = usuario,
            estado = estado,
        )

        viewModelScope.launch {
            val result = repository.registrarDenuncia(nuevaDenuncia)
            _isLoading.value = false

            result.onSuccess {
                _operacionExitosa.value = true
            }.onFailure { exception ->
                _errorMessage.value = exception.message
            }
        }
    }

    /**
     * Garantiza que la fecha enviada al backend cumpla con el formato LocalDateTime de Spring Boot ("yyyy-MM-dd'T'HH:mm:ss").
     * Si recibe "2026-10-28", le concatena "T00:00:00".
     */
    private fun formatearFechaIso(fechaInput: String): String {
        val textoLimpio = fechaInput.trim()
        return when {
            textoLimpio.isEmpty() -> generarFechaActualIso()
            textoLimpio.contains("T") -> textoLimpio
            textoLimpio.contains(" ") -> textoLimpio.replace(" ", "T")
            textoLimpio.matches(Regex("^\\d{4}-\\d{2}-\\d{2}$")) -> "${textoLimpio}T00:00:00"
            textoLimpio.length == 10 -> "${textoLimpio}T00:00:00"
            else -> generarFechaActualIso()
        }
    }

    private fun generarFechaActualIso(): String {
        val sdf = java.text.SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss", java.util.Locale.getDefault())
        return sdf.format(java.util.Date())
    }

    fun clearErrorMessage() {
        _errorMessage.value = null
    }
}
