package com.example.survy.Clases

data class Pregunta(
    val id : String,
    val titulo : String,
    val tiempo : Int,
    val respuestaA : String,
    val respuestaB : String,
    val respuestaC : String,
    val respuestaD : String,
    val respuestaCorrecta : String,
)