package com.example.survy.Fragments.MisEncuestas.Alumno

import android.os.Bundle
import android.os.CountDownTimer
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import com.example.survy.R
import com.google.firebase.firestore.FirebaseFirestore


class ResolverEncuestaFragmentAlumno : Fragment()
{
    private val db = FirebaseFirestore.getInstance()

    private lateinit var timer : CountDownTimer
    private var segundosRestantes = 0L

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

        val tvPregunta = view.findViewById<TextView>(R.id.tvTituloPreguntaResolverEncuestaAlumno)
        val tvTiempo = view.findViewById<TextView>(R.id.tvTiempoPreguntaResolverEncuestaAlumno)

        val btA = view.findViewById<Button>(R.id.btRespuestaAResolverEncuestaAlumno)
        val btB = view.findViewById<Button>(R.id.btRespuestaBResolverEncuestaAlumno)
        val btC = view.findViewById<Button>(R.id.btRespuestaCResolverEncuestaAlumno)
        val btD = view.findViewById<Button>(R.id.btRespuestaDResolverEncuestaAlumno)

        segundosRestantes = 60L

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
                Toast.makeText(context, "Lo siento, se acabó el tiempo", Toast.LENGTH_LONG).show()
            }
        }

        timer.start()

        val idUsuario = arguments?.getString("idUsuario") ?: ""
        val idAsignatura = arguments?.getString("idAsignatura") ?: ""
        val idEncuesta = arguments?.getString("idEncuesta") ?: ""
    }
}