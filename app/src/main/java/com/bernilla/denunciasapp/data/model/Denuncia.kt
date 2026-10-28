package com.bernilla.denunciasapp.data.model

import com.google.gson.annotations.SerializedName

/**
 * Data Class que mapea la entidad Denuncia para la API REST en Spring Boot.
 */
data class Denuncia(
    @SerializedName("id")
    val id: Int? = null,

    @SerializedName("dni")
    val dni: String = "",

    @SerializedName("tipo")
    val tipo: String = "",

    @SerializedName("titulo")
    val titulo: String = "",

    @SerializedName("descripcion")
    val descripcion: String = "",

    @SerializedName("direccion")
    val direccion: String = "",

    @SerializedName("referencia")
    val referencia: String = "",

    @SerializedName("fecha")
    val fecha: String = "",

    @SerializedName("usuario")
    val usuario: String = "",

    @SerializedName("estado")
    val estado: Int? = 1,
)
