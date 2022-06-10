package com.example.survy.Fragments.MiPerfil.Profesor

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import com.example.survy.R
import com.google.firebase.firestore.FirebaseFirestore
import de.hdodenhof.circleimageview.CircleImageView

class ModificarPerfilFragmentProfesor : Fragment()
{
    private val db = FirebaseFirestore.getInstance()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View?
    {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_modificar_perfil_profesor, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?)
    {
        super.onViewCreated(view, savedInstanceState)

        var etNombre = view.findViewById<EditText>(R.id.etNombreModificarPerfilProfesor)
        var etApellidos = view.findViewById<EditText>(R.id.etApellidosModificarPerfilProfesor)

        var btPassword = view.findViewById<Button>(R.id.btPasswordModificarPerfilProfesor)
        var btAplicarCambios = view.findViewById<Button>(R.id.btAplicarCambiosModificarPerfilProfesor)
        var btCancelar = view.findViewById<Button>(R.id.btCancelarModificarPerfilProfesor)

        var email = arguments?.getString("email", "") ?: ""
        var rol = arguments?.getString("rol", "") ?: ""


        db.collection("profesores").document(email).get().addOnSuccessListener {
            //civFotoDePerfil.setImageURI(it.get("fotoDePerfil") as Uri?)
            etNombre.setText(it.get("nombre") as String?)
            etApellidos.setText(it.get("apellidos") as String?)
        }


        btAplicarCambios.setOnClickListener {
            var nombre = etNombre.text.toString()
            var apellidos = etApellidos.text.toString()

            if(nombre.isBlank())
            {
                Toast.makeText(context, "Por favor, introduzca su nuevo nombre",
                    Toast.LENGTH_LONG).show()
            }
            else if(apellidos.isBlank())
            {
                Toast.makeText(context, "Por favor, introduzca sus nuevos apellidos",
                    Toast.LENGTH_LONG).show()
            }
            else
            {
                db.collection("profesores").document(email).update("nombre", nombre)
                db.collection("profesores").document(email).update("apellidos", apellidos)

                var nombreHeader = requireActivity().findViewById<TextView>(R.id.tvNombreHeader)
                var civHeader =  requireActivity().findViewById<CircleImageView>(R.id.civHeader)

                nombreHeader.setText(nombre)
                //civHeader

                Toast.makeText(context, "Datos actualizados correctamente",
                    Toast.LENGTH_LONG).show()

                cambiarFragment(MiPerfilFragmentProfesor(), email, rol)

            }
        }

        btCancelar.setOnClickListener {
            cambiarFragment(MiPerfilFragmentProfesor(), email, rol)
        }

        btPassword.setOnClickListener {
            cambiarFragment(ModificarPasswordFragmentProfesor(), email, rol)
        }
    }

    fun cambiarFragment(framentCambiar: Fragment, email: String, rol: String)
    {
        var args = Bundle()
        args.putString("rol", rol)
        args.putString("email", email)

        var fragment = framentCambiar
        fragment.arguments = args

        var fragmentManager = requireActivity().supportFragmentManager

        fragmentManager.beginTransaction()
            .replace(R.id.fragmentContainerProfesor, fragment)
            .commit()
    }
}