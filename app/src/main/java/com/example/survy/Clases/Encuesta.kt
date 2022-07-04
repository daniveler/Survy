package com.example.survy.Clases

import android.net.Uri

/**
 * Clase que representa las diferentes encuestas de la aplicación.
 */
data class Encuesta(
    val id : String,
    val idAsignatura : String,
    val nombre : String,
    val descripcion : String,
    val icono : String
    )