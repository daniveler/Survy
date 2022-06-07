package com.example.survy.Fragments.MiPerfil

import android.net.Uri
import android.os.Bundle
import android.os.Handler
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import com.example.survy.R
import com.google.firebase.firestore.FirebaseFirestore
import de.hdodenhof.circleimageview.CircleImageView

class MiPerfilFragment : Fragment()
{
    private val db = FirebaseFirestore.getInstance()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View?
    {
        return inflater.inflate(R.layout.fragment_mi_perfil, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?)
    {
        super.onViewCreated(view, savedInstanceState)

        var civFotoDePerfil = view.findViewById<CircleImageView>(R.id.civMiPerfil)
        var tvNombre = view.findViewById<TextView>(R.id.tvNombreMiPerfil)
        var tvApellidos = view.findViewById<TextView>(R.id.tvApellidosMiPerfil)
        var tvEmail = view.findViewById<TextView>(R.id.tvEmailMiPerfil)
        var tvCurso = view.findViewById<TextView>(R.id.tvCursoMiPerfil)

        var btModificarMisDatos = view.findViewById<Button>(R.id.btModificarDatosMiPerfil)

        var email = arguments?.getString("email", "") ?: ""

        db.collection("users").document(email).get().addOnSuccessListener {
            //civFotoDePerfil.setImageURI(it.get("fotoDePerfil") as Uri?)
            tvNombre.setText(it.get("nombre") as String?)
            tvApellidos.setText(it.get("apellidos") as String?)
            tvEmail.setText(email)
            tvCurso.setText(it.get("curso") as String?)
        }

        btModificarMisDatos.setOnClickListener {
            var args = Bundle()
            args.putString("email", email)

            var fragment = ModificarPerfilFragment()
            fragment.arguments = args

            var fragmentManager = requireActivity().supportFragmentManager

            fragmentManager.beginTransaction()
                .replace(R.id.fragmentContainer, fragment)
                .commit()
        }
    }
}