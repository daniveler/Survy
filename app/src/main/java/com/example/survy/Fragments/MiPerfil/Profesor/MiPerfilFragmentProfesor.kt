package com.example.survy.Fragments.MiPerfil.Profesor

import android.net.Uri
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

/**
 * Fragment de consulta de perfil de los usuarios Alumno.
 */
class MiPerfilFragmentProfesor : Fragment()
{
    private val db = FirebaseFirestore.getInstance()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View?
    {
        return inflater.inflate(R.layout.fragment_mi_perfil_profesor, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?)
    {
        super.onViewCreated(view, savedInstanceState)

        var civFotoDePerfil         = view.findViewById<CircleImageView>(R.id.civMiPerfilProfesor)
        var tvNombre                = view.findViewById<TextView>(R.id.tvNombreMiPerfilProfesor)
        var tvApellidos             = view.findViewById<TextView>(R.id.tvApellidosMiPerfilProfesor)
        var tvEmail                 = view.findViewById<TextView>(R.id.tvEmailMiPerfilProfesor)

        var tvNombreHeader          = requireActivity().findViewById<TextView>(R.id.tvNombreHeader)
        var civFotoDePerfilHeader   = requireActivity().findViewById<CircleImageView>(R.id.civHeader)

        var btModificarMisDatos     = view.findViewById<Button>(R.id.btModificarDatosMiPerfilProfesor)

        var idUsuario = arguments?.getString("idUsuario", "") ?: ""

        db.collection("profesores").document(idUsuario).get().addOnSuccessListener {
            Picasso.get().load(it.getString("fotoDePerfil")).into(civFotoDePerfil)
            Picasso.get().load(it.getString("fotoDePerfil")).into(civFotoDePerfilHeader)

            tvNombre.setText(it.get("nombre") as String?)
            tvNombreHeader.setText(it.get("nombre") as String?)

            tvApellidos.setText(it.get("apellidos") as String?)
            tvEmail.setText(it.get("email") as String?)
        }

        btModificarMisDatos.setOnClickListener {
            cambiarFragment(ModificarPerfilFragmentProfesor(), idUsuario)
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