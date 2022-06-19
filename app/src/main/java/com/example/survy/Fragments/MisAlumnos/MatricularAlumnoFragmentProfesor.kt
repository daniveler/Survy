package com.example.survy.Fragments.MisAlumnos

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import com.example.survy.R
import com.google.firebase.firestore.FirebaseFirestore
import com.squareup.picasso.Picasso
import de.hdodenhof.circleimageview.CircleImageView

class MatricularAlumnoFragmentProfesor : Fragment()
{
    private val db = FirebaseFirestore.getInstance()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View?
    {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_matricular_alumno_profesor, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?)
    {
        super.onViewCreated(view, savedInstanceState)

        val ivIcono = view.findViewById<CircleImageView>(R.id.ivIconoAsignaturaDetailMatricularAlumnoProfesor)
        val tvNombre = view.findViewById<TextView>(R.id.tvNombreAsignaturaDetailMatricularAlumnoProfesor)
        val tvCurso = view.findViewById<TextView>(R.id.tvCursoAsignaturaDetailMatricularAlumnoProfesor)

        val btGenerarCodigo = view.findViewById<Button>(R.id.btGenerarCodigoMatricularAlumnoProfesor)
        val btCancelar = view.findViewById<Button>(R.id.btCancelarMatricularAlumnoProfesor)

        val idUsuario = arguments?.getString("idUsuario") ?: ""
        val idAsignatura = arguments?.getString("asignatura") ?: ""

        db.collection("asignaturas").document(idAsignatura)
            .get().addOnSuccessListener {
                Picasso.get().load(it.getString("icono")).into(ivIcono)
                tvNombre.setText(it.getString("nombre"))
                tvCurso.setText(it.getString("curso"))
            }

        btGenerarCodigo.setOnClickListener {

        }

        btCancelar.setOnClickListener {
            cambiarFragment(MisAlumnosFragment(), idUsuario)
        }
    }

    fun cambiarFragment(framentCambiar: Fragment, idUsuario: String)
    {
        var args = Bundle()
        args.putString("idUsuario", idUsuario)

        var fragment = framentCambiar
        fragment.arguments = args

        var fragmentManager = requireActivity().supportFragmentManager

        fragmentManager.beginTransaction()
            .replace(R.id.fragmentContainerProfesor, fragment)
            .commit()
    }

}