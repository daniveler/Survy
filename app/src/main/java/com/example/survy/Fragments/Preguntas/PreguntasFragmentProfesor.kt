package com.example.survy.Fragments.Preguntas

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.survy.Adapters.PreguntaAdapterProfesor
import com.example.survy.Clases.Pregunta
import com.example.survy.Fragments.MisEncuestas.Profesor.EncuestaDetailFragmentProfesor
import com.example.survy.R
import com.google.firebase.firestore.FirebaseFirestore

/**
 * Vista en forma de lista de las preguntas de una encuesta de un usuario Profesor.
 */
class PreguntasFragmentProfesor : Fragment()
{
    private val db = FirebaseFirestore.getInstance()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View?
    {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_preguntas_profesor, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?)
    {
        super.onViewCreated(view, savedInstanceState)

        val tvEmpty = view.findViewById<TextView>(R.id.tvEmptyPreguntasProfesor)

        val rvPreguntas = view.findViewById<RecyclerView>(R.id.recyclerViewPreguntasProfesor)

        val btNuevaPregunta = view.findViewById<Button>(R.id.btNuevaPreguntaPreguntasProfesor)
        val btCancelar = view.findViewById<Button>(R.id.btCancelarPreguntasProfesor)

        val idUsuario = arguments?.getString("idUsuario") ?: ""
        val idAsignatura = arguments?.getString("idAsignatura") ?: ""
        val idEncuesta = arguments?.getString("idEncuesta") ?: ""

        val listaPreguntas = mutableListOf<Pregunta>()
        var listaPreguntasBusqueda = mutableListOf<Pregunta>()

        db.collection("preguntas")
            .whereEqualTo("idEncuesta", idEncuesta)
            .get()
            .addOnSuccessListener { task ->
                for (document in task)
                {
                    val id = document.id
                    val titulo = document.data.get("titulo").toString()
                    val tiempo = document.data.get("tiempo").toString().toInt()
                    val respuestaA = document.data.get("respuestaA").toString()
                    val respuestaB = document.data.get("respuestaB").toString()
                    val respuestaC = document.data.get("respuestaC").toString()
                    val respuestaD = document.data.get("respuestaD").toString()
                    val respuestaCorrecta = document.data.get("respuestaCorrecta").toString()

                    var pregunta = Pregunta(id, titulo, tiempo, respuestaA, respuestaB,
                        respuestaC, respuestaD, respuestaCorrecta)

                    listaPreguntas.add(pregunta)
                }
                if (task.isEmpty) { tvEmpty.visibility = View.VISIBLE }

                var adapter = PreguntaAdapterProfesor(listaPreguntas)

                rvPreguntas.layoutManager = LinearLayoutManager(context)
                rvPreguntas.setHasFixedSize(true)
                rvPreguntas.adapter = adapter

                adapter.setOnClickListener(object: PreguntaAdapterProfesor.onItemClickListener {
                    override fun onItemClick(position: Int)
                    {
                        var preguntaActual = listaPreguntas.get(position)

                        cambiarFragment(EditarPreguntaFragmentProfesor(), idUsuario, idEncuesta, idAsignatura, preguntaActual.id)
                    }
                })
            }

        btNuevaPregunta.setOnClickListener {
            cambiarFragment(NuevaPreguntaFragmentProfesor(), idUsuario, idEncuesta, idAsignatura, null)
        }

        btCancelar.setOnClickListener {
            cambiarFragment(EncuestaDetailFragmentProfesor(), idUsuario, idEncuesta, idAsignatura, null)
        }
    }

    fun cambiarFragment(framentCambiar: Fragment, idUsuario: String, idEncuesta: String, idAsignatura: String, idPregunta: String?)
    {
        var args = Bundle()
        args.putString("idUsuario", idUsuario)
        args.putString("idEncuesta", idEncuesta)
        args.putString("idAsignatura", idAsignatura)
        args.putString("idPregunta", idPregunta)

        var fragment = framentCambiar
        fragment.arguments = args

        var fragmentManager = requireActivity().supportFragmentManager

        fragmentManager.beginTransaction()
            .replace(R.id.fragmentContainerProfesor, fragment)
            .commit()
    }
}