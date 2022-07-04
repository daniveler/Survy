package com.example.survy.Fragments.MisAsignaturas.Profesor

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
import com.example.survy.Clases.Asignatura
import com.example.survy.Clases.Resultado
import com.example.survy.Fragments.MisAlumnos.VerAsignaturasAlumnoFragmentProfesor
import com.example.survy.Fragments.MisEncuestas.Profesor.MisEncuestasFragmentProfesor
import com.example.survy.R
import com.google.firebase.firestore.FirebaseFirestore
import com.squareup.picasso.Picasso
import kotlin.concurrent.timerTask

/**
 * Vista detallada de una asignatura de un usuario Profesor.
 */
class AsignaturaDetailFragmentProfesor : Fragment()
{
    private val db = FirebaseFirestore.getInstance()

    lateinit var nombre : String

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View?
    {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_asignatura_detail_profesor, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?)
    {
        super.onViewCreated(view, savedInstanceState)

        val ivIcono         = view.findViewById<ImageView>(R.id.ivIconoAsignaturaDetailProfesor)
        val tvNombre        = view.findViewById<TextView>(R.id.tvNombreAsignaturaDetailProfesor)
        val tvCurso         = view.findViewById<TextView>(R.id.tvCursoAsignaturaDetailProfesor)

        val btVerEncuestas  = view.findViewById<Button>(R.id.btVerEncuestasAsignaturaDetailProfesor)
        val btCancelar      = view.findViewById<Button>(R.id.btCancelarAsignaturaDetailProfesor)
        val btEditar        = view.findViewById<Button>(R.id.btEditarAsignaturaDetailProfesor)
        val btEliminar      = view.findViewById<Button>(R.id.btEliminarAsignaturaDetailProfesor)

        val idUsuario = arguments?.getString("idUsuario") ?: ""
        val idAsignatura = arguments?.getString("idAsignatura") ?: ""

        db.collection("asignaturas").document(idAsignatura)
            .get().addOnSuccessListener {
                Picasso.get().load(it.getString("icono")).into(ivIcono)

                nombre = it.getString("nombre").toString()
                tvNombre.setText(nombre)
                tvCurso.setText(it.getString("curso"))
        }

        btVerEncuestas.setOnClickListener {
            cambiarFragment(MisEncuestasFragmentProfesor(), idUsuario, idAsignatura)
        }

        btEditar.setOnClickListener {
            cambiarFragment(EditarAsignaturaFragmentProfesor(), idUsuario, idAsignatura)
        }

        btEliminar.setOnClickListener {
            mostrarAlerta(idAsignatura, nombre, idUsuario)
        }

        btCancelar.setOnClickListener {
            cambiarFragment(MisAsignaturasFragmentProfesor(), idUsuario, idAsignatura)
        }
    }

    fun cambiarFragment(framentCambiar: Fragment, idUsuario: String, idAsignatura: String?)
    {
        var args = Bundle()
        args.putString("idUsuario", idUsuario)
        args.putString("idAsignatura", idAsignatura)

        var fragment = framentCambiar
        fragment.arguments = args

        var fragmentManager = requireActivity().supportFragmentManager

        fragmentManager.beginTransaction()
            .replace(R.id.fragmentContainerProfesor, fragment)
            .commit()
    }

    fun mostrarAlerta(idAsignatura: String, nombreAsignatura: String, idUsuario: String)
    {
        val dialogBuilder = AlertDialog.Builder(requireContext())
        dialogBuilder.setTitle(getString(R.string.alertTitleAsignaturaDetailProfesor) + " " + nombreAsignatura + "?")
        dialogBuilder.setMessage(R.string.alertTextAsignaturaDetailProfesor)

        dialogBuilder.setPositiveButton("SÍ", DialogInterface.OnClickListener { dialog, id ->
            // Eliminar matrículas
            db.collection("matriculado")
                .whereEqualTo("idAsignatura", idAsignatura)
                .get().addOnSuccessListener { task ->
                    for (matriculaDoc in task)
                    {
                        db.collection("matriculado").document(matriculaDoc.id).delete()
                    }
                }

            // Eliminar resultados
            db.collection("resultados")
                .whereEqualTo("idAsignatura", idAsignatura)
                .get().addOnSuccessListener { task ->
                    for (resultadoDoc in task)
                    {
                        db.collection("resultados").document(resultadoDoc.id).delete()
                    }
                }

            // Eliminar encuestas y preguntas
            db.collection("encuestas")
                .whereEqualTo("idAsignatura", idAsignatura)
                .get().addOnSuccessListener { task ->
                    for (encuestaDoc in task)
                    {
                        db.collection("preguntas")
                            .whereEqualTo("idEncuesta", encuestaDoc.id)
                            .get().addOnSuccessListener { task ->
                                for (preguntaDoc in task)
                                {
                                    db.collection("preguntas").document(preguntaDoc.id).delete()
                                }
                                db.collection("encuestas").document(encuestaDoc.id).delete()
                            }
                    }
                }

            // Eliminar asignatura
            db.collection("asignaturas").document(idAsignatura).delete()


            Toast.makeText(context, "Asignatura eliminada correctamente", Toast.LENGTH_LONG).show()

            cambiarFragment(MisAsignaturasFragmentProfesor(), idUsuario, null)
        })

        dialogBuilder.setNegativeButton("Cancelar", DialogInterface.OnClickListener { dialog, id ->
            dialog.cancel()
        })

        val alerta = dialogBuilder.create()
        alerta.show()
    }
}