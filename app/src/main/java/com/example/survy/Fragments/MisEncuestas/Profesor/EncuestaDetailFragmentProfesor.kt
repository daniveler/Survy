package com.example.survy.Fragments.MisEncuestas.Profesor

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import com.example.survy.Fragments.Preguntas.PreguntasFragmentProfesor
import com.example.survy.Fragments.Resultados.Profesor.VerResultadosEncuestaFragmentProfesor
import com.example.survy.R
import com.google.firebase.firestore.FirebaseFirestore
import com.squareup.picasso.Picasso

class EncuestaDetailFragmentProfesor : Fragment()
{
    private val db = FirebaseFirestore.getInstance()

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
        val btCancelar          = view.findViewById<Button>(R.id.btCancelarEncuestaDetailProfesor)

        val idUsuario = arguments?.getString("idUsuario") ?: ""
        val idAsignatura = arguments?.getString("idAsignatura") ?: ""
        val idEncuesta = arguments?.getString("idEncuesta") ?: ""

        db.collection("encuestas").document(idEncuesta)
            .get().addOnSuccessListener {
                Picasso.get().load(it.getString("icono")).into(ivIcono)
                tvNombre.setText(it.getString("nombre"))
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
}