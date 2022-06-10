package com.example.survy.Fragments.MiPerfil.Alumno

import android.graphics.Color
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import com.example.survy.R
import com.google.firebase.firestore.FirebaseFirestore
import de.hdodenhof.circleimageview.CircleImageView

class ModificarPerfilFragmentAlumno : Fragment()
{
    private val db = FirebaseFirestore.getInstance()

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

        var email = arguments?.getString("email", "") ?: ""
        var rol = arguments?.getString("rol", "") ?: ""

        db.collection("alumnos").document(email).get().addOnSuccessListener {
            //civFotoDePerfil.setImageURI(it.get("fotoDePerfil") as Uri?)
            etNombre.setText(it.get("nombre") as String?)
            etApellidos.setText(it.get("apellidos") as String?)
            spinnerCurso.setSelection(cursos.indexOf(it.get("curso") as String?))
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
                db.collection("alumnos").document(email).update("nombre", nombre)
                db.collection("alumnos").document(email).update("apellidos", apellidos)
                db.collection("alumnos").document(email).update("curso", curso)

                var nombreHeader = requireActivity().findViewById<TextView>(R.id.tvNombreHeader)
                var civHeader =  requireActivity().findViewById<CircleImageView>(R.id.civHeader)

                nombreHeader.setText(nombre)
                //civHeader

                Toast.makeText(context, "Datos actualizados correctamente",
                    Toast.LENGTH_LONG).show()

                cambiarFragment(MiPerfilFragmentAlumno(), email, rol)

            }
        }

        btCancelar.setOnClickListener {
            cambiarFragment(MiPerfilFragmentAlumno(), email, rol)
        }

        btPassword.setOnClickListener {
            cambiarFragment(ModificarPasswordFragmentAlumno(), email, rol)
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
            .replace(R.id.fragmentContainerAlumno, fragment)
            .commit()
    }
}