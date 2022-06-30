package com.example.survy.Fragments.MisEncuestas.Profesor

import android.content.DialogInterface
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import com.example.survy.Fragments.MisEncuestas.Alumno.MisEncuestasFragmentAlumno
import com.example.survy.Fragments.Preguntas.PreguntasFragmentProfesor
import com.example.survy.Fragments.Resultados.Profesor.VerResultadosEncuestaFragmentProfesor
import com.example.survy.R
import com.google.firebase.firestore.FirebaseFirestore
import com.squareup.picasso.Picasso

class EncuestaDetailFragmentProfesor : Fragment()
{
    private val db = FirebaseFirestore.getInstance()

    private lateinit var nombre : String

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View?
    {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_encuesta_detail_profesor, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?)
    {
        super.onViewCreated(view, savedInstanceState)

        val ivIcono             = view.findViewById<ImageView>(R.id.ivIconoEncuestaDetailProfesor)
        val tvNombre            = view.findViewById<TextView>(R.id.tvNombreEncuestaDetailProfesor)
        val tvDescripcion       = view.findViewById<TextView>(R.id.tvDescripcionEncuestaDetailProfesor)

        val btVerPreguntas      = view.findViewById<Button>(R.id.btVerPreguntasEncuestaDetailProfesor)
        val btverResultados     = view.findViewById<Button>(R.id.btVerResultadosEncuestaDetailProfesor)
        val btEditarEncuesta    = view.findViewById<Button>(R.id.btEditarEncuestaDetailProfesor)
        val btEliminarEncuesta  = view.findViewById<Button>(R.id.btEliminarEncuestaDetailProfesor)
        val btCancelar          = view.findViewById<Button>(R.id.btCancelarEncuestaDetailProfesor)

        val idUsuario = arguments?.getString("idUsuario") ?: ""
        val idAsignatura = arguments?.getString("idAsignatura") ?: ""
        val idEncuesta = arguments?.getString("idEncuesta") ?: ""

        db.collection("encuestas").document(idEncuesta)
            .get().addOnSuccessListener {
                Picasso.get().load(it.getString("icono")).into(ivIcono)

                nombre = it.getString("nombre").toString()
                tvNombre.setText(nombre)
                tvDescripcion.setText(it.getString("descripcion"))
            }

        btVerPreguntas.setOnClickListener {
            cambiarFragment(PreguntasFragmentProfesor(), idUsuario, idEncuesta, idAsignatura)
        }

        btverResultados.setOnClickListener {
            cambiarFragment(VerResultadosEncuestaFragmentProfesor(), idUsuario, idEncuesta, idAsignatura)
        }

        btEditarEncuesta.setOnClickListener {
            cambiarFragment(EditarEncuestaFragmentProfesor(), idUsuario, idEncuesta, idAsignatura)
        }

        btEliminarEncuesta.setOnClickListener {
            mostrarAlerta(idEncuesta, nombre, idUsuario, idAsignatura)
        }

        btCancelar.setOnClickListener {
            cambiarFragment(MisEncuestasFragmentProfesor(), idUsuario, idEncuesta, idAsignatura)
        }
    }

    fun cambiarFragment(framentCambiar: Fragment, idUsuario: String, idEncuesta: String?, idAsignatura: String)
    {
        var args = Bundle()
        args.putString("idUsuario", idUsuario)
        args.putString("idEncuesta", idEncuesta)
        args.putString("idAsignatura", idAsignatura)

        var fragment = framentCambiar
        fragment.arguments = args

        var fragmentManager = requireActivity().supportFragmentManager

        fragmentManager.beginTransaction()
            .replace(R.id.fragmentContainerProfesor, fragment)
            .commit()
    }

    fun mostrarAlerta(idEncuesta: String, nombreEncuesta: String, idUsuario: String, idAsignatura: String)
    {
        val dialogBuilder = AlertDialog.Builder(requireContext())
        dialogBuilder.setTitle(getString(R.string.alertTitleEncuestaDetailProfesor) + " " + nombreEncuesta + "?")
        dialogBuilder.setMessage(R.string.alertTextEncuestaDetailProfesor)

        dialogBuilder.setPositiveButton("SÍ", DialogInterface.OnClickListener { dialog, id ->
            // Eliminar preguntas
            db.collection("preguntas")
                .whereEqualTo("idEncuesta", idEncuesta)
                .get().addOnSuccessListener { task ->
                    for (preguntaDoc in task)
                    {
                        db.collection("preguntas").document(preguntaDoc.id).delete()
                    }
                }

            // Eliminar resultados
            db.collection("resultados")
                .whereEqualTo("idEncuesta", idEncuesta)
                .get().addOnSuccessListener { task ->
                    for (resultadoDoc in task)
                    {
                        db.collection("resultados").document(resultadoDoc.id).delete()
                    }
                }

            // Eliminar encuesta
            db.collection("encuestas").document(idEncuesta).delete()

            Toast.makeText(context, "Encuesta eliminada correctamente", Toast.LENGTH_LONG).show()

            cambiarFragment(MisEncuestasFragmentProfesor(), idUsuario, idEncuesta, idAsignatura)
        })

        dialogBuilder.setNegativeButton("Cancelar", DialogInterface.OnClickListener { dialog, id ->
            dialog.cancel()
        })

        val alerta = dialogBuilder.create()
        alerta.show()
    }
}