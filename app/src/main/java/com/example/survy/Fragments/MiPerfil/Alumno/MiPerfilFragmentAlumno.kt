package com.example.survy.Fragments.MiPerfil.Alumno

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import com.example.survy.R
import com.google.firebase.firestore.FirebaseFirestore
import de.hdodenhof.circleimageview.CircleImageView

class MiPerfilFragmentAlumno : Fragment()
{
    private val db = FirebaseFirestore.getInstance()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View?
    {
        return inflater.inflate(R.layout.fragment_mi_perfil_alumno, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?)
    {
        super.onViewCreated(view, savedInstanceState)

        var civFotoDePerfil = view.findViewById<CircleImageView>(R.id.civMiPerfilAlumno)
        var tvNombre = view.findViewById<TextView>(R.id.tvNombreMiPerfilAlumno)
        var tvApellidos = view.findViewById<TextView>(R.id.tvApellidosMiPerfilAlumno)
        var tvEmail = view.findViewById<TextView>(R.id.tvEmailMiPerfilAlumno)
        var tvCurso = view.findViewById<TextView>(R.id.tvCursoMiPerfilAlumno)

        //var tvNombreHeader = view.findViewById<TextView>(R.id.tvNombreHeader)
        //var civFotoDePerfilHeader = view.findViewById<CircleImageView>(R.id.civHeader)

        var btModificarMisDatos = view.findViewById<Button>(R.id.btModificarDatosMiPerfilAlumno)

        var idUsuario = arguments?.getString("idUsuario", "") ?: ""

            db.collection("alumnos").document(idUsuario).get().addOnSuccessListener {
                //civFotoDePerfil.setImageURI(it.get("fotoDePerfil") as Uri?)
                //civHeader.setImageURI(it.get("fotoDePerfil") as Uri?)
                tvNombre.setText(it.get("nombre") as String?)
                //tvNombreHeader.setText(it.get("nombre") as String?)
                tvApellidos.setText(it.get("apellidos") as String?)
                tvEmail.setText(it.get("email") as String?)
                tvCurso.setText(it.get("curso") as String?)
            }

        btModificarMisDatos.setOnClickListener {
            cambiarFragment(ModificarPerfilFragmentAlumno(), idUsuario)
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
            .replace(R.id.fragmentContainerAlumno, fragment)
            .commit()
    }
}