package com.example.survy.Fragments.MisAsignaturas.Profesor

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import com.example.survy.Fragments.MisEncuestas.Profesor.MisEncuestasFragmentProfesor
import com.example.survy.R
import com.google.firebase.firestore.FirebaseFirestore
import com.squareup.picasso.Picasso

class AsignaturaDetailFragmentProfesor : Fragment()
{
    private val db = FirebaseFirestore.getInstance()

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

        val idUsuario = arguments?.getString("idUsuario") ?: ""
        val idAsignatura = arguments?.getString("idAsignatura") ?: ""

        db.collection("asignaturas").document(idAsignatura)
            .get().addOnSuccessListener {
                Picasso.get().load(it.getString("icono")).into(ivIcono)
                tvNombre.setText(it.getString("nombre"))
                tvCurso.setText(it.getString("curso"))
        }

        btVerEncuestas.setOnClickListener {
            cambiarFragment(MisEncuestasFragmentProfesor(), idUsuario, idAsignatura)
        }

        btEditar.setOnClickListener {
            cambiarFragment(EditarAsignaturaFragmentProfesor(), idUsuario, idAsignatura)
        }

        btCancelar.setOnClickListener {
            cambiarFragment(MisAsignaturasFragmentProfesor(), idUsuario, idAsignatura)
        }
    }

    fun cambiarFragment(framentCambiar: Fragment, idUsuario: String, idAsignatura: String)
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
}