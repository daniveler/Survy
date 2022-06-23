package com.example.survy.Fragments.MisAsignaturas.Profesor

import android.content.Context
import android.graphics.Color
import android.net.Uri
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.survy.Clases.GridIconosAdapter
import com.example.survy.R
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ListResult
import com.google.firebase.storage.StorageReference
import com.google.firebase.storage.ktx.storage
import com.squareup.picasso.Picasso

class NuevaAsignaturaFragmentProfesor : Fragment()
{
    private val db = FirebaseFirestore.getInstance()
    private val storage = Firebase.storage

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

        var idUsuario = arguments?.getString("idUsuario", "") ?: ""

        val etNombre = view.findViewById<EditText>(R.id.etNombreNuevaAsignaturaProfesor)
        val spinnerCursos = view.findViewById<Spinner>(R.id.spinnerCursoNuevaAsignaturaProfesor)
        val ivIcono = view.findViewById<ImageView>(R.id.ivFotoNuevaAsignaturaProfesor)

        val btCrear = view.findViewById<Button>(R.id.btCrearNuevaAsignaturaProfesor)
        val btCancelar = view.findViewById<Button>(R.id.btCancelarNuevaAsignaturaProfesor)

        val rvIconos = view.findViewById<RecyclerView>(R.id.rvIconosNuevaAsignaturaProfesor)

        val btGuardarIcono = view.findViewById<Button>(R.id.btGuardarIconoNuevaAsignaturaProfesor)
        val btCancelarIcono = view.findViewById<Button>(R.id.btCancelarGuardarIconoNuevaAsignaturaProfesor)

        ivIcono.resources.getDrawable(R.drawable.survy_logo)

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

        var iconoAntiguo = Uri.parse("android.resource://" + requireActivity().packageName +
                "/" + resources.getResourceTypeName(R.drawable.survy_logo) +
                "/" + resources.getResourceEntryName(R.drawable.survy_logo))

        var iconoActual = iconoAntiguo

        spinnerCursos.adapter = spinnerAdapter

        ivIcono.setOnClickListener {
            var iconoAntiguo = ivIcono.drawable

            etNombre.visibility = View.GONE
            spinnerCursos.visibility = View.GONE
            btCrear.visibility = View.GONE
            btCancelar.visibility = View.GONE

            rvIconos.visibility = View.VISIBLE
            btGuardarIcono.visibility = View.VISIBLE
            btCancelarIcono.visibility = View.VISIBLE

            var storageRef = storage.reference
            var iconosRef = storageRef.child("iconfinder_pack")

            val listaIconos: ArrayList<Uri> = ArrayList()

            val listAllTask: Task<ListResult> = iconosRef.listAll()
            listAllTask.addOnCompleteListener { result ->
                val items: List<StorageReference> = result.result!!.items

                items.forEachIndexed { index, item ->
                    item.downloadUrl.addOnSuccessListener {
                        listaIconos.add(it)
                    }.addOnCompleteListener {
                        var adapter = GridIconosAdapter(listaIconos)

                        rvIconos.layoutManager = GridLayoutManager(context, 3)
                        rvIconos.setHasFixedSize(true)
                        rvIconos.adapter = adapter

                        adapter.setOnItemClickListener(object: GridIconosAdapter.onItemClickListener {
                            override fun onItemClick(position: Int)
                            {
                                iconoActual = listaIconos.get(position)
                                Picasso.get().load(iconoActual).into(ivIcono)
                            }
                        })

                        btGuardarIcono.setOnClickListener {
                            etNombre.visibility = View.VISIBLE
                            spinnerCursos.visibility = View.VISIBLE
                            btCrear.visibility = View.VISIBLE
                            btCancelar.visibility = View.VISIBLE

                            rvIconos.visibility = View.GONE
                            btGuardarIcono.visibility = View.GONE
                            btCancelarIcono.visibility = View.GONE
                        }

                        btCancelarIcono.setOnClickListener {
                            etNombre.visibility = View.VISIBLE
                            spinnerCursos.visibility = View.VISIBLE
                            btCrear.visibility = View.VISIBLE
                            btCancelar.visibility = View.VISIBLE

                            rvIconos.visibility = View.GONE
                            btGuardarIcono.visibility = View.GONE
                            btCancelarIcono.visibility = View.GONE

                            ivIcono.setImageDrawable(iconoAntiguo)
                        }
                    }
                }
            }
        }

        btCrear.setOnClickListener {
            val nombre = etNombre.text.toString()
            val curso = spinnerCursos.selectedItem.toString()
            val icono = iconoActual

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
            else if (icono == iconoAntiguo)
            {
                Toast.makeText(context, "Por favor, seleccione un icono",
                    Toast.LENGTH_LONG).show()
            }
            else
            {
                var dataAsignatura = hashMapOf("nombre" to nombre,
                    "idProfesor" to idUsuario,
                    "curso" to curso,
                    "icono" to icono,
                    "numAlumnos" to 0
                    )

                db.collection("asignaturas").add(dataAsignatura)

                Toast.makeText(context, "Asignatura creada correctamente",
                    Toast.LENGTH_LONG).show()

                cambiarFragment(MisAsignaturasFragmentProfesor(), idUsuario, "MisAsignaturas")
            }
        }

        btCancelar.setOnClickListener {
            var email = FirebaseAuth.getInstance().currentUser?.email ?: ""
            cambiarFragment(MisAsignaturasFragmentProfesor(), email, "MisAsignaturas")
        }
    }

    fun cambiarFragment(framentCambiar: Fragment, idUsuario: String, vieneDe: String)
    {
        var args = Bundle()
        args.putString("idUsuario", idUsuario)
        args.putString("vieneDe", vieneDe)

        var fragment = framentCambiar
        fragment.arguments = args

        var fragmentManager = requireActivity().supportFragmentManager

        fragmentManager.beginTransaction()
            .replace(R.id.fragmentContainerProfesor, fragment)
            .commit()
    }
}