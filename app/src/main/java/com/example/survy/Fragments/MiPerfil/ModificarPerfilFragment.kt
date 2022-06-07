package com.example.survy.Fragments.MiPerfil

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.Spinner
import android.widget.Toast
import com.example.survy.R
import com.google.firebase.firestore.FirebaseFirestore

class ModificarPerfilFragment : Fragment()
{
    private val db = FirebaseFirestore.getInstance()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View?
    {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_modificar_perfil, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?)
    {
        super.onViewCreated(view, savedInstanceState)

        var etNombre = view.findViewById<EditText>(R.id.etNombreModificarPerfil)
        var etApellidos = view.findViewById<EditText>(R.id.etApellidosModificarPerfil)
        var spinnerCurso = view.findViewById<Spinner>(R.id.spinnerModificarPerfil)

        var btPassword = view.findViewById<Button>(R.id.btPasswordModificarPerfil)
        var btAplicarCambios = view.findViewById<Button>(R.id.btAplicarCambiosModificarPerfil)
        var btCancelar = view.findViewById<Button>(R.id.btCancelarModificarPerfil)

        var email = arguments?.getString("email", "") ?: ""

        btAplicarCambios.setOnClickListener {
            var nombre = etNombre.text.toString()
            var apellidos = etApellidos.text.toString()
            var curso = spinnerCurso.selectedItem.toString()

            if(nombre.isBlank())
            {
                Toast.makeText(context, "Por favor, introduzca su nombre",
                    Toast.LENGTH_LONG).show()
            }
            else if(apellidos.isBlank())
            {
                Toast.makeText(context, "Por favor, introduzca sus apellidos",
                    Toast.LENGTH_LONG).show()
            }
            else if(curso == "Curso")
            {
                Toast.makeText(context, "Por favor, introduzca su curso",
                    Toast.LENGTH_LONG).show()
            }
            else
            {
                db.collection("users").document(email).update("nombre", nombre)
                db.collection("users").document(email).update("apellidos", apellidos)
                db.collection("users").document(email).update("curso", curso)
            }
        }

        btCancelar.setOnClickListener {
            requireActivity().supportFragmentManager.popBackStack()
        }

        btPassword.setOnClickListener {
            var fragment = ModificarPasswordFragment()

            var fragmentManager = requireActivity().supportFragmentManager

            fragmentManager.beginTransaction()
                .replace(R.id.fragmentContainer, fragment)
                .commit()
        }
    }
}