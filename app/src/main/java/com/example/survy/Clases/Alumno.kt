package com.example.survy.Clases

import android.net.Uri
import androidx.core.net.toUri

data class Alumno(
    val idAlumno : String,
    val nombre : String,
    val apellidos : String,
    val email : String,
    val curso : String,
    val fotoDePerfil : Uri
)