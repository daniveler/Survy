package com.example.survy.Fragments.MisAsignaturas.Profesor

import android.content.ContentValues.TAG
import android.content.Context
import android.graphics.Color
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import com.example.survy.R
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class NuevaAsignaturaFragmentProfesor : Fragment()
{
    private val db = FirebaseFirestore.getInstance()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View?
    {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_nueva_asignatura_profesor, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?)
    {
        super.onViewCreated(view, savedInstanceState)

        var email = FirebaseAuth.getInstance().currentUser?.email ?: ""

        val etNombre = view.findViewById<EditText>(R.id.etNombreNuevaAsignaturaProfesor)
        val spinnerCursos = view.findViewById<Spinner>(R.id.spinnerCursoNuevaAsignaturaProfesor)
        val ivFoto = view.findViewById<ImageView>(R.id.ivFotoNuevaAsignaturaProfesor)
        val btCrear = view.findViewById<Button>(R.id.btCrearNuevaAsignaturaProfesor)
        val btCancelar = view.findViewById<Button>(R.id.btCancelarNuevaAsignaturaProfesor)

        val cursos = resources.getStringArray(R.array.cursos)
        val spinnerAdapter = object  : ArrayAdapter<String>(activity as Context, android.R.layout.simple_spinner_dropdown_item, cursos)
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

        spinnerCursos.adapter = spinnerAdapter

        btCrear.setOnClickListener {
            val nombre = etNombre.text.toString()
            val curso = spinnerCursos.selectedItem.toString()
            //val foto =

            if (nombre.isBlank())
            {
                Toast.makeText(context, "Por favor, introduzca el nombre de la asignatura",
                    Toast.LENGTH_LONG).show()
            }
            else if (curso == "Curso" || curso.isBlank())
            {
                Toast.makeText(context, "Por favor, introduzca el curso de la asignatura",
                    Toast.LENGTH_LONG).show()
            }
            else
            {
                var dataAsignatura = hashMapOf("nombre" to nombre,
                    "curso" to curso)

                db.collection("asignaturas").add(dataAsignatura)
                    .addOnSuccessListener {
                        var dataImparte = hashMapOf("idProfesor" to email,
                            "idAsignatura" to it.id)

                        db.collection("imparte").add(dataImparte)
                    }

                Toast.makeText(context, "Asignatura creada correctamente",
                    Toast.LENGTH_LONG).show()

                cambiarFragment(MisAsignaturasFragmentProfesor(), email)
            }
        }

        btCancelar.setOnClickListener {
            var email = FirebaseAuth.getInstance().currentUser?.email ?: ""
            cambiarFragment(MisAsignaturasFragmentProfesor(), email)
        }
    }

    fun cambiarFragment(framentCambiar: Fragment, email: String)
    {
        var args = Bundle()
        args.putString("email", email)

        var fragment = framentCambiar
        fragment.arguments = args

        var fragmentManager = requireActivity().supportFragmentManager

        fragmentManager.beginTransaction()
            .replace(R.id.fragmentContainerProfesor, fragment)
            .commit()
    }
}