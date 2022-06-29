package com.example.survy.Fragments.MisEncuestas.Alumno

import android.os.Build
import android.os.Bundle
import android.os.CountDownTimer
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.annotation.RequiresApi
import com.example.survy.Fragments.Resultados.Alumno.ResultadoEncuestaFragmentAlumno
import com.example.survy.R
import com.google.firebase.firestore.FirebaseFirestore
import java.time.LocalDateTime
import java.util.*
import kotlin.concurrent.schedule


class ResolverEncuestaFragmentAlumno : Fragment()
{
    private val db = FirebaseFirestore.getInstance()

    private lateinit var idAsignatura : String

    private lateinit var timer : CountDownTimer
    private var segundosRestantes = 0L

    private var puntuacion = 0

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

    @RequiresApi(Build.VERSION_CODES.O)
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
        idAsignatura = arguments?.getString("idAsignatura") ?: ""
        val idEncuesta = arguments?.getString("idEncuesta") ?: ""

        val numPreguntas = arguments?.getInt("numPreguntas") ?: -1

        val indice = 0

        cargarPregunta(indice, idEncuesta, numPreguntas, idUsuario)
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun cargarPregunta(index: Int, idEncuesta: String, numPreguntas: Int, idUsuario: String)
    {
        db.collection("preguntas").whereEqualTo("idEncuesta", idEncuesta)
            .get().addOnSuccessListener {
                if (index < numPreguntas)
                {

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
                    } else if (tiempo in 20..50)
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

                    btA.isClickable = true
                    btB.isClickable = true
                    btC.isClickable = true
                    btD.isClickable = true

                    var timer = object : CountDownTimer(segundosRestantes * 1000, 1000)
                    {
                        override fun onTick(millisUntilFinished: Long)
                        {
                            segundosRestantes -= 1
                            val minutosRestantes = segundosRestantes / 60
                            val segundosEnMinutosRestantes =
                                segundosRestantes - minutosRestantes * 60

                            val strSegundos = segundosEnMinutosRestantes.toString()

                            tvTiempo.setText(
                                "$minutosRestantes:${
                                    if (strSegundos.length == 2) strSegundos
                                    else "0" + strSegundos
                                }"
                            )

                        }

                        override fun onFinish()
                        {
                            mostrarRespuestaCorrecta(correcta)
                            Toast.makeText(
                                context,
                                "Lo siento, se acabó el tiempo",
                                Toast.LENGTH_LONG
                            ).show()
                        }
                    }

                    timer.start()

                    btA.setOnClickListener {
                        btA.isClickable = false
                        btB.isClickable = false
                        btC.isClickable = false
                        btD.isClickable = false

                        if (correcta == "A")
                        {
                            puntuacion++
                        }

                        timer.cancel()
                        mostrarRespuestaCorrecta(correcta)
                        Timer().schedule(2000) {
                            cargarPregunta(index + 1, idEncuesta, numPreguntas, idUsuario)
                        }
                    }

                    btB.setOnClickListener {
                        btA.isClickable = false
                        btB.isClickable = false
                        btC.isClickable = false
                        btD.isClickable = false

                        if (correcta == "B")
                        {
                            puntuacion++
                        }

                        timer.cancel()
                        mostrarRespuestaCorrecta(correcta)
                        Timer().schedule(2000) {
                            cargarPregunta(index + 1, idEncuesta, numPreguntas, idUsuario)
                        }
                    }

                    btC.setOnClickListener {
                        btA.isClickable = false
                        btB.isClickable = false
                        btC.isClickable = false
                        btD.isClickable = false

                        if (correcta == "C")
                        {
                            puntuacion++
                        }

                        timer.cancel()
                        mostrarRespuestaCorrecta(correcta)
                        Timer().schedule(2000) {
                            cargarPregunta(index + 1, idEncuesta, numPreguntas, idUsuario)
                        }
                    }

                    btD.setOnClickListener {
                        btA.isClickable = false
                        btB.isClickable = false
                        btC.isClickable = false
                        btD.isClickable = false

                        if (correcta == "D")
                        {
                            puntuacion++
                        }

                        timer.cancel()
                        mostrarRespuestaCorrecta(correcta)
                        Timer().schedule(2000) {
                            cargarPregunta(index + 1, idEncuesta, numPreguntas, idUsuario)
                        }
                    }
                }
                else
                {
                    val puntuacionTotal = "%.2f".format(puntuacion.toDouble() / numPreguntas * 10)
                    val fechaActual = LocalDateTime.now().toString()

                    val dataResultado = hashMapOf(
                        "idUsuario" to idUsuario,
                        "idEncuesta" to idEncuesta,
                        "idAsignatura" to idAsignatura,
                        "nota" to puntuacionTotal,
                        "fecha" to fechaActual
                    )

                    db.collection("resultados").add(dataResultado)

                    Toast.makeText(context, "Calculando tu nota...", Toast.LENGTH_LONG).show()

                    Timer().schedule(1000) {
                        cambiarFragment(ResultadoEncuestaFragmentAlumno(), idUsuario, idEncuesta, puntuacionTotal)
                    }
                }
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

    fun cambiarFragment(framentCambiar: Fragment, idUsuario: String, idEncuesta: String, nota: String)
    {
        var args = Bundle()
        args.putString("idUsuario", idUsuario)
        args.putString("idEncuesta", idEncuesta)
        args.putString("nota", nota)

        var fragment = framentCambiar
        fragment.arguments = args

        var fragmentManager = requireActivity().supportFragmentManager

        fragmentManager.beginTransaction()
            .replace(R.id.fragmentContainerAlumno, fragment)
            .commit()
    }
}