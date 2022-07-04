package com.example.survy.Clases

import android.net.Uri
import androidx.core.net.toUri

/**
 * Clase que representa a los usuarios Profesor de la aplicación.
 */
data class Profesor(
    val nombre : String,
    val apellidos : String,
    val email : String,
    val fotoDePerfil : Uri
)
{
    constructor() : this ("", "", "", "".toUri())
}