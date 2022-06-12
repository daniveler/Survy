package com.example.survy.Clases

import android.net.Uri

data class Asignatura(
    private val id : String,
    private val idCurso : String,
    private val nombre : String,
    private val imagen : Uri
)
{
}