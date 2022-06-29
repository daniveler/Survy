package com.example.survy.Fragments.Resultados

import android.graphics.Color
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import com.example.survy.R
import com.google.firebase.firestore.FirebaseFirestore

class ResultadoEncuestaFragmentAlumno : Fragment()
{
    private val db = FirebaseFirestore.getInstance()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View?
    {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_resultado_encuesta_alumno, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?)
    {
        super.onViewCreated(view, savedInstanceState)

        val tvNota = view.findViewById<TextView>(R.id.tvNotaResultadoEncuestaAlumno)
        val tvMensaje = view.findViewById<TextView>(R.id.tvMensajeResultadoEncuestaAlumno)

        val idUsuario = arguments?.getString("idUsuario") ?: ""
        val idEncuesta = arguments?.getString("idEncuesta") ?: ""
        var nota = arguments?.getString("nota") ?: ""


        nota = nota.replace(',', '.')

        if (nota.toDouble() >= 0.0 && nota.toDouble() < 2.0)
        {
            tvNota.setText(nota.replace(".00", ""))
            tvNota.setTextColor(resources.getColor(R.color.redError))
            tvMensaje.setTextColor(resources.getColor(R.color.redError))
            tvMensaje.setText(getString(R.string.tvMensaje01ResultadoEncuestaAlumno))

        }
        else if (nota.toDouble() >= 2.0 && nota.toDouble() < 4.0)
        {
            tvNota.setText(nota.replace(".00", ""))
            tvNota.setTextColor(resources.getColor(R.color.redError))
            tvMensaje.setTextColor(resources.getColor(R.color.redError))
            tvMensaje.setText(getString(R.string.tvMensaje23ResultadoEncuestaAlumno))
        }
        else if (nota.toDouble() >= 5.0 && nota.toDouble() < 6.0)
        {
            tvNota.setText(nota.replace(".00", ""))
            tvNota.setTextColor(resources.getColor(R.color.yellowSuficiente))
            tvMensaje.setTextColor(resources.getColor(R.color.yellowSuficiente))
            tvMensaje.setText(getString(R.string.tvMensaje5ResultadoEncuestaAlumno))
        }
        else if (nota.toDouble() >= 6.0 && nota.toDouble() < 7.0)
        {
            tvNota.setText(nota.replace(".00", ""))
            tvNota.setTextColor(resources.getColor(R.color.orangeBien))
            tvMensaje.setTextColor(resources.getColor(R.color.orangeBien))
            tvMensaje.setText(getString(R.string.tvMensaje6ResultadoEncuestaAlumno))

        }
        else if (nota.toDouble() >= 7.0 && nota.toDouble() < 8.0)
        {
            tvNota.setText(nota.replace(".00", ""))
            tvNota.setTextColor(resources.getColor(R.color.orangeBien))
            tvMensaje.setTextColor(resources.getColor(R.color.orangeBien))
            tvMensaje.setText(getString(R.string.tvMensaje7ResultadoEncuestaAlumno))
        }
        else if (nota.toDouble() >= 8.0 && nota.toDouble() < 10.0)
        {
            tvNota.setText(nota.replace(".00", ""))
            tvNota.setTextColor(resources.getColor(R.color.greenMuyBien))
            tvMensaje.setTextColor(resources.getColor(R.color.greenMuyBien))
            tvMensaje.setText(getString(R.string.tvMensaje89ResultadoEncuestaAlumno))
        }
        else if (nota.toDouble() == 10.0)
        {
            tvNota.setText(nota.replace(".00", ""))
            tvNota.setTextColor(resources.getColor(R.color.greenAcierto))
            tvMensaje.setTextColor(resources.getColor(R.color.greenAcierto))
            tvMensaje.setText(getString(R.string.tvMensaje10ResultadoEncuestaAlumno))
        }
        else { Toast.makeText(context, "Error al obtener la nota", Toast.LENGTH_LONG).show() }
    }
}