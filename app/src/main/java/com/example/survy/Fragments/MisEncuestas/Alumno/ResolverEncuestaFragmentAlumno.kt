package com.example.survy.Fragments.MisEncuestas.Alumno

import android.graphics.Color
import android.os.Bundle
import android.os.CountDownTimer
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import com.example.survy.R
import com.google.firebase.firestore.FirebaseFirestore
import org.w3c.dom.Text
import java.util.*
import kotlin.concurrent.schedule


class ResolverEncuestaFragmentAlumno : Fragment()
{
    private val db = FirebaseFirestore.getInstance()

    private lateinit var timer : CountDownTimer
    private var segundosRestantes = 0L

    lateinit var tvPregunta : TextView
    lateinit var tvTiempo : TextView

    lateinit var btA : Button
    lateinit var btB : Button
    lateinit var btC : Button
    lateinit var btD : Button

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View?
    {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_resolver_encuesta_alumno, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?)
    {
        super.onViewCreated(view, savedInstanceState)

        tvPregunta = view.findViewById<TextView>(R.id.tvTituloPreguntaResolverEncuestaAlumno)
        tvTiempo = view.findViewById<TextView>(R.id.tvTiempoPreguntaResolverEncuestaAlumno)

        btA = view.findViewById<Button>(R.id.btRespuestaAResolverEncuestaAlumno)
        btB = view.findViewById<Button>(R.id.btRespuestaBResolverEncuestaAlumno)
        btC = view.findViewById<Button>(R.id.btRespuestaCResolverEncuestaAlumno)
        btD = view.findViewById<Button>(R.id.btRespuestaDResolverEncuestaAlumno)

        val idUsuario = arguments?.getString("idUsuario") ?: ""
        val idAsignatura = arguments?.getString("idAsignatura") ?: ""
        val idEncuesta = arguments?.getString("idEncuesta") ?: ""

        val numPreguntas = arguments?.getInt("numPreguntas") ?: -1

        val indice = 0

        cargarPregunta(indice, idEncuesta, numPreguntas)
    }

    private fun cargarPregunta(index: Int, idEncuesta: String, numPreguntas: Int)
    {
        if (index < numPreguntas)
        {
            db.collection("preguntas").whereEqualTo("idEncuesta", idEncuesta)
                .get().addOnSuccessListener {
                    val titulo = it.documents.get(index).data?.get("titulo").toString()
                    val tiempo = it.documents.get(index).data?.get("tiempo").toString().toLong()

                    val respuestaA = it.documents.get(index).data?.get("respuestaA").toString()
                    val respuestaB = it.documents.get(index).data?.get("respuestaB").toString()
                    val respuestaC = it.documents.get(index).data?.get("respuestaC").toString()
                    val respuestaD = it.documents.get(index).data?.get("respuestaD").toString()

                    val correcta = it.documents.get(index).data?.get("correcta").toString()

                    tvPregunta.setText(titulo)

                    if (tiempo in 1..5)
                    {
                        segundosRestantes = tiempo * 60L
                    }
                    else if (tiempo in 20..50)
                    {
                        segundosRestantes = tiempo
                    }

                    btA.setText(respuestaA)
                    btB.setText(respuestaB)
                    btC.setText(respuestaC)
                    btD.setText(respuestaD)

                    btA.setBackgroundColor(resources.getColor(R.color.darkPurple))
                    btB.setBackgroundColor(resources.getColor(R.color.darkPurple))
                    btC.setBackgroundColor(resources.getColor(R.color.darkPurple))
                    btD.setBackgroundColor(resources.getColor(R.color.darkPurple))

                    var timer = object : CountDownTimer(segundosRestantes * 1000, 1000)
                    {
                        override fun onTick(millisUntilFinished: Long)
                        {
                            segundosRestantes -= 1
                            val minutosRestantes = segundosRestantes / 60
                            val segundosEnMinutosRestantes = segundosRestantes - minutosRestantes * 60

                            val strSegundos = segundosEnMinutosRestantes.toString()

                            tvTiempo.setText("$minutosRestantes:${
                                if (strSegundos.length == 2) strSegundos
                                else "0" + strSegundos
                            }")

                        }
                        override fun onFinish()
                        {
                            mostrarRespuestaCorrecta(correcta)
                            Toast.makeText(context, "Lo siento, se acabó el tiempo", Toast.LENGTH_LONG).show()
                        }
                    }

                    timer.start()

                    btA.setOnClickListener {
                        timer.cancel()
                        mostrarRespuestaCorrecta(correcta)
                        Timer().schedule(2000) { cargarPregunta(index + 1, idEncuesta, numPreguntas) }

                    }

                    btB.setOnClickListener {
                        timer.cancel()
                        mostrarRespuestaCorrecta(correcta)
                        Timer().schedule(2000) { cargarPregunta(index + 1, idEncuesta, numPreguntas) }
                    }

                    btC.setOnClickListener {
                        timer.cancel()
                        mostrarRespuestaCorrecta(correcta)
                        Timer().schedule(2000) { cargarPregunta(index + 1, idEncuesta, numPreguntas) }
                    }

                    btD.setOnClickListener {
                        timer.cancel()
                        mostrarRespuestaCorrecta(correcta)
                        Timer().schedule(2000) { cargarPregunta(index + 1, idEncuesta, numPreguntas) }
                    }
                }
        }
        else
        {
            // ¡Encuesta Terminada!
        }
    }

    private fun mostrarRespuestaCorrecta(correcta: String)
    {
        when(correcta)
        {
            "A" ->{
                btA.setBackgroundColor(resources.getColor(R.color.greenAcierto))
                btB.setBackgroundColor(resources.getColor(R.color.redError))
                btC.setBackgroundColor(resources.getColor(R.color.redError))
                btD.setBackgroundColor(resources.getColor(R.color.redError))
            }

            "B" ->{
                btA.setBackgroundColor(resources.getColor(R.color.redError))
                btB.setBackgroundColor(resources.getColor(R.color.greenAcierto))
                btC.setBackgroundColor(resources.getColor(R.color.redError))
                btD.setBackgroundColor(resources.getColor(R.color.redError))
            }

            "C" ->{
                btA.setBackgroundColor(resources.getColor(R.color.redError))
                btB.setBackgroundColor(resources.getColor(R.color.redError))
                btC.setBackgroundColor(resources.getColor(R.color.greenAcierto))
                btD.setBackgroundColor(resources.getColor(R.color.redError))
            }

            "D" ->{
                btA.setBackgroundColor(resources.getColor(R.color.redError))
                btB.setBackgroundColor(resources.getColor(R.color.redError))
                btC.setBackgroundColor(resources.getColor(R.color.redError))
                btD.setBackgroundColor(resources.getColor(R.color.greenAcierto))
            }
        }
    }
}