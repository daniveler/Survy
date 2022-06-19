package com.example.survy.Fragments.MiPerfil.Alumno

import android.app.Activity.RESULT_OK
import android.content.Intent
import android.graphics.Color
import android.net.Uri
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import com.example.survy.R
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage
import com.squareup.picasso.Picasso
import de.hdodenhof.circleimageview.CircleImageView

class ModificarPerfilFragmentAlumno : Fragment()
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
        return inflater.inflate(R.layout.fragment_modificar_perfil_alumno, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?)
    {
        super.onViewCreated(view, savedInstanceState)

        var etNombre = view.findViewById<EditText>(R.id.etNombreModificarPerfilAlumno)
        var etApellidos = view.findViewById<EditText>(R.id.etApellidosModificarPerfilAlumno)
        var spinnerCurso = view.findViewById<Spinner>(R.id.spinnerModificarPerfilAlumno)

        civFotoDePerfil = view.findViewById<CircleImageView>(R.id.civFotoDePerfilModificarPerfilAlumno)

        var btPassword = view.findViewById<Button>(R.id.btPasswordModificarPerfilAlumno)
        var btAplicarCambios = view.findViewById<Button>(R.id.btAplicarCambiosModificarPerfilAlumno)
        var btCancelar = view.findViewById<Button>(R.id.btCancelarModificarPerfilAlumno)

        val cursos = resources.getStringArray(R.array.cursos)
        val spinnerAdapter = object  : ArrayAdapter<String>(requireContext(), android.R.layout.simple_spinner_dropdown_item, cursos)
        {
            override fun isEnabled(position: Int): Boolean = position != 0

            override fun getDropDownView(position: Int, convertView: View?, parent: ViewGroup): View
            {
                val view: TextView = super.getDropDownView(position, convertView, parent) as TextView
                //set the color of first item in the drop down list to gray
                if(position == 0)
                {
                    view.setTextColor(Color.GRAY)
                }
                return view
            }
        }

        spinnerCurso.adapter = spinnerAdapter

        idUsuario = arguments?.getString("idUsuario", "") ?: ""
        var rol = arguments?.getString("rol", "") ?: ""

        db.collection("alumnos").document(idUsuario).get().addOnSuccessListener {
            Picasso.get().load(it.getString("fotoDePerfil")).into(civFotoDePerfil)
            etNombre.setText(it.get("nombre") as String?)
            etApellidos.setText(it.get("apellidos") as String?)
            spinnerCurso.setSelection(cursos.indexOf(it.get("curso") as String?))
        }

        civFotoDePerfil.setOnClickListener {
            val intent = Intent(Intent.ACTION_PICK)
            intent.type = "image/*"
            startActivityForResult(intent, 1000)
        }


        btAplicarCambios.setOnClickListener {
            var nombre = etNombre.text.toString()
            var apellidos = etApellidos.text.toString()
            var curso = spinnerCurso.selectedItem.toString()

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
            else if(curso == "Curso")
            {
                Toast.makeText(context, "Por favor, introduzca su nuevo curso",
                    Toast.LENGTH_LONG).show()
            }
            else
            {
                val storageReference = storage.getReference("user_profile_pics/$idUsuario")
                storageReference.downloadUrl.addOnSuccessListener {
                    db.collection("alumnos").document(idUsuario).update("nombre", nombre)
                    db.collection("alumnos").document(idUsuario).update("apellidos", apellidos)
                    db.collection("alumnos").document(idUsuario).update("curso", curso)
                    db.collection("alumnos").document(idUsuario).update("fotoDePerfil", it)

                    Toast.makeText(context, "Datos actualizados correctamente",
                        Toast.LENGTH_LONG).show()

                    cambiarFragment(MiPerfilFragmentAlumno(), idUsuario, rol)
                }
            }
        }

        btCancelar.setOnClickListener {
            cambiarFragment(MiPerfilFragmentAlumno(), idUsuario, rol)
        }

        btPassword.setOnClickListener {
            cambiarFragment(ModificarPasswordFragmentAlumno(), idUsuario, rol)
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

    fun cambiarFragment(framentCambiar: Fragment, idUsuario: String, rol: String)
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