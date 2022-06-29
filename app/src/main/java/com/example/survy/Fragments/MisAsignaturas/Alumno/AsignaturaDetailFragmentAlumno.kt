package com.example.survy.Fragments.MisAsignaturas.Alumno

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import com.example.survy.Fragments.MisEncuestas.Alumno.MisEncuestasFragmentAlumno
import com.example.survy.Fragments.Resultados.VerResultadosAsignaturaFragmentAlumno
import com.example.survy.R
import com.google.firebase.firestore.FirebaseFirestore
import com.squareup.picasso.Picasso

class AsignaturaDetailFragmentAlumno : Fragment()
{
    private val db = FirebaseFirestore.getInstance()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View?
    {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_asignatura_detail_alumno, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?)
    {
        super.onViewCreated(view, savedInstanceState)

        val ivIcono         = view.findViewById<ImageView>(R.id.ivIconoAsignaturaDetailAlumno)
        val tvNombre        = view.findViewById<TextView>(R.id.tvNombreAsignaturaDetailAlumno)
        val tvCurso         = view.findViewById<TextView>(R.id.tvCursoAsignaturaDetailAlumno)

        val btVerEncuestas  = view.findViewById<Button>(R.id.btVerEncuestasAsignaturaDetailAlumno)
        val btVerResultados = view.findViewById<Button>(R.id.btVerResultadosAsignaturaDetailAlumno)
        val btCancelar      = view.findViewById<Button>(R.id.btCancelarAsignaturaDetailAlumno)

        val idUsuario = arguments?.getString("idUsuario") ?: ""
        val idAsignatura = arguments?.getString("idAsignatura") ?: ""

        db.collection("asignaturas").document(idAsignatura)
            .get().addOnSuccessListener {
                Picasso.get().load(it.getString("icono")).into(ivIcono)
                tvNombre.setText(it.getString("nombre"))
                tvCurso.setText(it.getString("curso"))
            }

        btVerEncuestas.setOnClickListener {
            cambiarFragment(MisEncuestasFragmentAlumno(), idUsuario, idAsignatura)
        }

        btVerResultados.setOnClickListener {
            cambiarFragment(VerResultadosAsignaturaFragmentAlumno(), idUsuario, idAsignatura)
        }

        btCancelar.setOnClickListener {
            cambiarFragment(MisAsignaturasFragmentAlumno(), idUsuario, idAsignatura)
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
            .replace(R.id.fragmentContainerAlumno, fragment)
            .commit()
    }
}