package com.example.survy.Clases

import android.net.Uri
import androidx.core.net.toUri

data class Profesor(
    val nombre : String,
    val apellidos : String,
    val email : String,
    val fotoDePerfil : Uri
)
{
    constructor() : this ("", "", "", "".toUri())
}