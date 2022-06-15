package com.example.survy.Fragments.MisAsignaturas.Profesor

import android.net.Uri
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import com.example.survy.Clases.Asignatura
import com.example.survy.R
import com.google.firebase.firestore.FirebaseFirestore

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

        val ivIcono = view.findViewById<ImageView>(R.id.ivIconoAsignaturaDetailProfesor)
        val tvNombre = view.findViewById<TextView>(R.id.tvNombreAsignaturaDetailProfesor)
        val tvCurso = view.findViewById<TextView>(R.id.tvCursoAsignaturaDetailProfesor)

        val btVerEncuestas = view.findViewById<Button>(R.id.btVerEncuestasAsignaturaDetailProfesor)
        val btCancelar = view.findViewById<Button>(R.id.btCancelarAsignaturaDetailProfesor)
        val btEditar = view.findViewById<Button>(R.id.btEditarAsignaturaDetailProfesor)

        val email = arguments?.getString("email") ?: ""
        val idAsignatura = arguments?.getString("asignatura") ?: ""

        db.collection("asignaturas").document(idAsignatura)
            .get().addOnSuccessListener {
                ivIcono.setImageURI(Uri.parse(it.getString("icono")))
                tvNombre.setText(it.getString("nombre"))
                tvCurso.setText(it.getString("curso"))
        }

        btVerEncuestas.setOnClickListener {

        }

        btEditar.setOnClickListener {
            cambiarFragment(EditarAsignaturaFragmentProfesor(), email, idAsignatura)
        }

        btCancelar.setOnClickListener {
            cambiarFragment(MisAsignaturasFragmentProfesor(), email, idAsignatura)
        }
    }

    fun cambiarFragment(framentCambiar: Fragment, email: String, idAsignatura: String?)
    {
        var args = Bundle()
        args.putString("email", email)
        args.putString("asignatura", idAsignatura)

        var fragment = framentCambiar
        fragment.arguments = args

        var fragmentManager = requireActivity().supportFragmentManager

        fragmentManager.beginTransaction()
            .replace(R.id.fragmentContainerProfesor, fragment)
            .commit()
    }
}