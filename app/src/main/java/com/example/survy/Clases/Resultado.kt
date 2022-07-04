package com.example.survy.Clases

/**
 * Clase que representa los diferentes resultados de la aplicación.
 */
data class Resultado(
    val id : String,
    val idUsuario : String,
    val idAsignatura: String,
    val idEncuesta : String,
    val fecha : String,
    val nota : String
)
