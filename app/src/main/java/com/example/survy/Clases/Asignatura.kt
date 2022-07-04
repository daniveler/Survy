package com.example.survy.Clases

import android.net.Uri

/**
 * Clase que representa las diferentes asignaturas de la aplicación.
 */
data class Asignatura(
    val id : String,
    val nombre : String,
    val idProfesor : String,
    val curso : String,
    val icono : String,
    val numAlumnos : Int
)