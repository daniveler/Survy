package com.example.survy.Clases

import android.net.Uri
import androidx.core.net.toUri

/**
 * Clase que representa a los usuarios Alumno de la aplicación.
 */
data class Alumno(
    val id : String,
    val nombre : String,
    val apellidos : String,
    val email : String,
    val curso : String,
    val fotoDePerfil : Uri
)