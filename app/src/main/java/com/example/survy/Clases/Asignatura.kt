package com.example.survy.Clases

import android.net.Uri

data class Asignatura(
    val curso : String,
    val nombre : String,
    val numAlumnos: Int,
    val imagen : Uri
)
{
}