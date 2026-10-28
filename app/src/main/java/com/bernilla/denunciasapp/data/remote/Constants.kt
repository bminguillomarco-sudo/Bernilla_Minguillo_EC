package com.bernilla.denunciasapp.data.remote

/**
 * Objeto de constantes globales para la configuración de red y API.
 */
object Constants {

    // OPCIÓN 1: Servidor en la nube (Render) - Activa por defecto
    const val BASE_URL = "https://denunciaservice-bernilla.onrender.com/"

    // OPCIÓN 2: Servidor Local ejecutado en la misma PC y probado desde Emulador Android
    // const val BASE_URL = "http://10.0.2.2:8080/"

    // OPCIÓN 3: Servidor Local probado desde Celular Físico en la misma red Wi-Fi
    // (Reemplazar IP_DE_MI_PC por la dirección IPv4 de tu computadora, ej: http://192.168.1.50:8080/)
    // const val BASE_URL = "http://192.168.1.50:8080/"
}
