package com.example.survy.Fragments.MisAlumnos

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import com.example.survy.Fragments.MiPerfil.Alumno.ModificarPerfilFragmentAlumno
import com.example.survy.R
import com.google.firebase.firestore.FirebaseFirestore
import com.squareup.picasso.Picasso
import de.hdodenhof.circleimageview.CircleImageView

class AlumnoDetailFragmentProfesor : Fragment()
{
    private val db = FirebaseFirestore.getInstance()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View?
    {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_alumno_detail_profesor, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?)
    {
        super.onViewCreated(view, savedInstanceState)

        var civFotoDePerfil         = view.findViewById<CircleImageView>(R.id.civAlumnoDetailProfesor)
        var tvNombre                = view.findViewById<TextView>(R.id.tvNombreAlumnoDetailProfesor)
        var tvApellidos             = view.findViewById<TextView>(R.id.tvApellidosAlumnoDetailProfesor)
        var tvEmail                 = view.findViewById<TextView>(R.id.tvEmailAlumnoDetailProfesor)
        var tvCurso                 = view.findViewById<TextView>(R.id.tvCursoAlumnoDetailProfesor)

        var btVerAsignaturas     = view.findViewById<Button>(R.id.btVerAsignaturasDetailProfesor)
        var btCancelar           = view.findViewById<Button>(R.id.btCancelarAlumnoDetailProfesor)

        val idAlumno = arguments?.getString("idUsuario") ?: ""

        db.collection("alumnos").document(idAlumno).get().addOnSuccessListener {
            Picasso.get().load(it.getString("fotoDePerfil")).into(civFotoDePerfil)

            tvNombre.setText(it.get("nombre") as String?)
            tvApellidos.setText(it.get("apellidos") as String?)
            tvEmail.setText(it.get("email") as String?)
            tvCurso.setText(it.get("curso") as String?)
        }

        btVerAsignaturas.setOnClickListener {
            cambiarFragment(VerAsignaturasAlumnoFragmentProfesor(), idAlumno)
        }

        btCancelar.setOnClickListener {
            cambiarFragment(BuscarAlumnoFragmentProfesor(), null)
        }
    }

    fun cambiarFragment(fragmentCambiar: Fragment, idAlumno: String?)
    {
        var args = Bundle()
        args.putString("idAlumno", idAlumno)

        var fragment = fragmentCambiar

        fragment.arguments = args

        var fragmentManager = requireActivity().supportFragmentManager

        fragmentManager.beginTransaction()
            .replace(R.id.fragmentContainerProfesor, fragment)
            .commit()
    }

}