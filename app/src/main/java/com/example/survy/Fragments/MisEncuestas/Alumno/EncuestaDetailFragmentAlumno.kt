package com.example.survy.Fragments.MisEncuestas.Alumno

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
import com.example.survy.Fragments.MisAsignaturas.Profesor.MisAsignaturasFragmentProfesor
import com.example.survy.Fragments.MisEncuestas.Profesor.MisEncuestasFragmentProfesor
import com.example.survy.Fragments.Resultados.Alumno.VerResultadosEncuestaFragmentAlumno
import com.example.survy.R
import com.google.firebase.firestore.FirebaseFirestore
import com.squareup.picasso.Picasso

/**
 * Vista detallada de una encuesta de una asignatura en la que está matriculado un usuario Alumno.
 */
class EncuestaDetailFragmentAlumno : Fragment()
{
    private val db = FirebaseFirestore.getInstance()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View?
    {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_encuesta_detail_alumno, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?)
    {
        super.onViewCreated(view, savedInstanceState)

        val ivIcono             = view.findViewById<ImageView>(R.id.ivIconoEncuestaDetailAlumno)
        val tvNombre            = view.findViewById<TextView>(R.id.tvNombreEncuestaDetailAlumno)
        val tvDescripcion       = view.findViewById<TextView>(R.id.tvDescripcionEncuestaDetailAlumno)

        val btHacerEncuesta     = view.findViewById<Button>(R.id.btResolverEncuestaDetailAlumno)
        val btVerResultados     = view.findViewById<Button>(R.id.btVerResultadosEncuestaDetailAlumno)
        val btCancelar          = view.findViewById<Button>(R.id.btCancelarEncuestaDetailAlumno)

        val idUsuario = arguments?.getString("idUsuario") ?: ""
        val idAsignatura = arguments?.getString("idAsignatura") ?: ""
        val idEncuesta = arguments?.getString("idEncuesta") ?: ""

        db.collection("encuestas").document(idEncuesta)
            .get().addOnSuccessListener {
                Picasso.get().load(it.getString("icono")).into(ivIcono)
                tvNombre.setText(it.getString("nombre"))
                tvDescripcion.setText(it.getString("descripcion"))

                db.collection("preguntas").whereEqualTo("idEncuesta", idEncuesta)
                    .get().addOnSuccessListener {
                        val numPreguntas = it.documents.size

                        btHacerEncuesta.setOnClickListener {
                            if (numPreguntas == 0)
                            {
                                Toast.makeText(context, "No se puede resolver la encuesta porque aún no tiene preguntas. Inténtelo más tarde",
                                    Toast.LENGTH_LONG).show()
                            }
                            else
                                cambiarFragment(ResolverEncuestaFragmentAlumno(), idUsuario, idEncuesta, idAsignatura, numPreguntas)
                        }

                        btVerResultados.setOnClickListener {
                            cambiarFragment(VerResultadosEncuestaFragmentAlumno(), idUsuario, idEncuesta, idAsignatura, 0)
                        }
                    }
            }
    }

    fun cambiarFragment(framentCambiar: Fragment, idUsuario: String, idEncuesta: String?, idAsignatura: String, numPreguntas: Int)
    {
        var args = Bundle()
        args.putString("idUsuario", idUsuario)
        args.putString("idEncuesta", idEncuesta)
        args.putString("idAsignatura", idAsignatura)
        args.putInt("numPreguntas", numPreguntas)

        var fragment = framentCambiar
        fragment.arguments = args

        var fragmentManager = requireActivity().supportFragmentManager

        fragmentManager.beginTransaction()
            .replace(R.id.fragmentContainerAlumno, fragment)
            .commit()
    }

}