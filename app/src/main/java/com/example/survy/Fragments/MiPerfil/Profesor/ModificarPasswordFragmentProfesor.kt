package com.example.survy.Fragments.MiPerfil.Profesor

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import com.example.survy.R
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

/**
 * Fragment de consulta de perfil de los usuarios Profesor.
 */
class ModificarPasswordFragmentProfesor : Fragment()
{
    private val db = FirebaseFirestore.getInstance()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View?
    {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_modificar_password_profesor, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?)
    {
        super.onViewCreated(view, savedInstanceState)

        val etPassword1 = view.findViewById<EditText>(R.id.etModificarPassword1Profesor)
        val etPassword2 = view.findViewById<EditText>(R.id.etModificarPassword2Profesor)
        val btModificarPassword = view.findViewById<Button>(R.id.btIntroducirCodigoMatricularAlumnoAlumno)
        val btCancelar = view.findViewById<Button>(R.id.btCancelarModificarPasswordProfesor)

        var idUsuario = arguments?.getString("idUsuario", "") ?: ""

        btModificarPassword.setOnClickListener {
            val user = FirebaseAuth.getInstance().currentUser

            val password1 = etPassword1.text.toString()
            val password2 = etPassword2.text.toString()

            if (password1.isBlank())
            {
                Toast.makeText(context, "Por favor, introduzca su nueva contraseña",
                    Toast.LENGTH_LONG).show()
            }
            else if (password2.isBlank())
            {
                Toast.makeText(context, "Por favor, repita su nueva contraseña",
                    Toast.LENGTH_LONG).show()
            }
            else if (password1 != password2)
            {
                Toast.makeText(context, "Ambas contraseñas deben coincidir",
                    Toast.LENGTH_LONG).show()
            }
            else
            {
                user!!.updatePassword(password1).addOnCompleteListener {
                    if (it.isSuccessful)
                    {
                        Toast.makeText(context, "Contraseña actualizada correctamente",
                            Toast.LENGTH_LONG).show()

                        cambiarFragment(MiPerfilFragmentProfesor(), idUsuario)
                    }

                    else
                        Toast.makeText(context, "updatePassword: Failed",
                            Toast.LENGTH_LONG).show()
                }
            }
        }

        btCancelar.setOnClickListener {
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