package com.example.survy.Fragments.MiPerfil.Profesor

import android.app.Activity.RESULT_OK
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.fragment.app.Fragment
import com.example.survy.R
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage
import com.squareup.picasso.Picasso
import de.hdodenhof.circleimageview.CircleImageView


class ModificarPerfilFragmentProfesor : Fragment()
{
    private val db = FirebaseFirestore.getInstance()
    private val storage = Firebase.storage

    lateinit var fotoUri: Uri
    lateinit var idUsuario : String
    lateinit var civFotoDePerfil : CircleImageView

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

        civFotoDePerfil = view.findViewById<CircleImageView>(R.id.civFotoDePerfilModificarPerfilProfesor)

        val btPassword = view.findViewById<Button>(R.id.btPasswordModificarPerfilProfesor)
        val btAplicarCambios = view.findViewById<Button>(R.id.btAplicarCambiosModificarPerfilProfesor)
        val btCancelar = view.findViewById<Button>(R.id.btCancelarModificarPerfilProfesor)

        idUsuario = arguments?.getString("idUsuario", "") ?: ""

        db.collection("profesores").document(idUsuario).get().addOnSuccessListener {
            Picasso.get().load(it.getString("fotoDePerfil")).into(civFotoDePerfil)
            etNombre.setText(it.get("nombre") as String?)
            etApellidos.setText(it.get("apellidos") as String?)
        }

        civFotoDePerfil.setOnClickListener {
            val intent = Intent(Intent.ACTION_PICK)
            intent.type = "image/*"
            startActivityForResult(intent, 1000)
        }

        btAplicarCambios.setOnClickListener {
            var nombre = etNombre.text.toString()
            var apellidos = etApellidos.text.toString()

            if (nombre.isBlank())
            {
                Toast.makeText(
                    context, "Por favor, introduzca su nuevo nombre",
                    Toast.LENGTH_LONG
                ).show()
            }
            else if (apellidos.isBlank())
            {
                Toast.makeText(
                    context, "Por favor, introduzca sus nuevos apellidos",
                    Toast.LENGTH_LONG
                ).show()
            }
            else
            {
                val storageReference = storage.getReference("user_profile_pics/$idUsuario")
                storageReference.downloadUrl.addOnSuccessListener {
                    db.collection("profesores").document(idUsuario).update("nombre", nombre)
                    db.collection("profesores").document(idUsuario).update("apellidos", apellidos)
                    db.collection("profesores").document(idUsuario).update("fotoDePerfil", it)

                    Toast.makeText(
                        context, "Datos actualizados correctamente",
                        Toast.LENGTH_LONG
                    ).show()

                    cambiarFragment(MiPerfilFragmentProfesor(), idUsuario)
                }
            }
        }

        btCancelar.setOnClickListener {
            cambiarFragment(MiPerfilFragmentProfesor(), idUsuario)
        }

        btPassword.setOnClickListener {
            cambiarFragment(ModificarPasswordFragmentProfesor(), idUsuario)
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?)
    {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == 1000 && resultCode == RESULT_OK && data != null)
        {
            fotoUri = data?.data!!

            val nombreUri = "" + idUsuario

            val storageReference = storage.getReference("user_profile_pics/$nombreUri")

            storageReference.putFile(fotoUri).addOnSuccessListener {
                storageReference.downloadUrl.addOnSuccessListener {
                    Picasso.get().load(it).into(civFotoDePerfil)
                }
            }.addOnFailureListener{
                Toast.makeText(context, "Error al cargar la imagen de perfil", Toast.LENGTH_LONG).show()
            }
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