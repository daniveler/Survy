package com.example.survy.Fragments.Resultados.Alumno

import android.content.DialogInterface
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.survy.Adapters.ResultadoAdapterAlumno
import com.example.survy.Clases.Encuesta
import com.example.survy.Clases.Resultado
import com.example.survy.Fragments.MisEncuestas.Alumno.EncuestaDetailFragmentAlumno
import com.example.survy.Fragments.Preguntas.PreguntasFragmentProfesor
import com.example.survy.R
import com.google.firebase.firestore.FirebaseFirestore

/**
 * Vista en forma de lista de los resultados en una encuesta de un usuario Alumno.
 */
class VerResultadosEncuestaFragmentAlumno : Fragment()
{
    private val db = FirebaseFirestore.getInstance()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View?
    {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_ver_resultados_encuesta_alumno, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?)
    {
        super.onViewCreated(view, savedInstanceState)

        val idUsuario = arguments?.getString("idUsuario") ?: ""
        val idAsignatura = arguments?.getString("idAsignatura") ?: ""
        val idEncuesta = arguments?.getString("idEncuesta") ?: ""

        val tvEmpty             = view.findViewById<TextView>(R.id.tvEmptyVerResultadosEncuestaAlumno)
        val rvListaResultados   = view.findViewById<RecyclerView>(R.id.recyclerViewVerResultadosEncuestaAlumno)
        val btCancelar          = view.findViewById<Button>(R.id.btCancelarVerResultadosEncuestaAlumno)

        val mapaResultadosFinal = mutableMapOf<Resultado, Encuesta>()

        val listaResultados = mutableListOf<Resultado>()
        val listaEncuesta = mutableListOf<Encuesta>()

        db.collection("resultados")
            .whereEqualTo("idUsuario", idUsuario)
            .whereEqualTo("idEncuesta", idEncuesta)
            .get().addOnSuccessListener { task ->
                for (resultadosDoc in task)
                {
                    val id = resultadosDoc.id
                    val idEncuesta = idEncuesta
                    val idAsignatura = resultadosDoc.data.get("idAsignatura").toString()
                    val fecha = resultadosDoc.data.get("fecha").toString()
                    val nota = resultadosDoc.data.get("nota").toString()

                    val resultado = Resultado(id, idUsuario, idAsignatura, idEncuesta, fecha, nota)

                    listaResultados.add(resultado)
                }

                for (resultado in listaResultados)
                {
                    db.collection("encuestas").document(idEncuesta)
                        .get().addOnSuccessListener {
                            val id = it.id
                            val idAsignatura = it.getString("idAsignatura").toString()
                            val nombre = it.getString("nombre").toString()
                            val descripcion = it.getString("descripcion").toString()
                            val icono = it.getString("icono").toString()

                            val encuesta = Encuesta(id, idAsignatura, nombre, descripcion, icono)

                            mapaResultadosFinal.put(resultado, encuesta)

                            if (mapaResultadosFinal.isEmpty()) { tvEmpty.visibility = View.VISIBLE }

                            var adapter = ResultadoAdapterAlumno(mapaResultadosFinal)

                            rvListaResultados.layoutManager = LinearLayoutManager(context)
                            rvListaResultados.setHasFixedSize(true)
                            rvListaResultados.adapter = adapter

                            adapter.setOnItemClickListener(object: ResultadoAdapterAlumno.onItemClickListener {
                                override fun onItemClick(position: Int)
                                {

                                }
                            })
                        }
                }
            }

        btCancelar.setOnClickListener {
            cambiarFragment(EncuestaDetailFragmentAlumno(), idUsuario, idAsignatura, idEncuesta)
        }
    }

    fun cambiarFragment(framentCambiar: Fragment, idUsuario: String, idAsignatura: String, idEncuesta: String)
    {
        var args = Bundle()
        args.putString("idUsuario", idUsuario)
        args.putString("idAsignatura", idAsignatura)
        args.putString("idEncuesta", idEncuesta)

        var fragment = framentCambiar
        fragment.arguments = args

        var fragmentManager = requireActivity().supportFragmentManager

        fragmentManager.beginTransaction()
            .replace(R.id.fragmentContainerAlumno, fragment)
            .commit()
    }
}