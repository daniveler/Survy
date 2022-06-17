package com.example.survy.Fragments.MisAsignaturas.Profesor

import android.content.Context
import android.graphics.Color
import android.net.Uri
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.survy.Clases.AsignaturaAdapter
import com.example.survy.Clases.GridIconosAdapter
import com.example.survy.R
import com.google.android.gms.tasks.Task
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ListResult
import com.google.firebase.storage.StorageReference
import com.google.firebase.storage.ktx.storage
import com.squareup.picasso.Picasso
import kotlin.collections.ArrayList

class EditarAsignaturaFragmentProfesor : Fragment()
{
    private val db = FirebaseFirestore.getInstance()
    private val storage = Firebase.storage

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View?
    {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_editar_asignatura_profesor, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?)
    {
        super.onViewCreated(view, savedInstanceState)

        val idAsignatura = arguments?.getString("asignatura") ?: ""

        val etNombre = view.findViewById<EditText>(R.id.etNombreEditarAsignaturaProfesor)
        val spinnerCurso = view.findViewById<Spinner>(R.id.spinnerCursoEditarAsignaturaProfesor)
        val ivIcono = view.findViewById<ImageView>(R.id.ivIconoEditarAsignaturaProfesor)

        val btGuardarCambios = view.findViewById<Button>(R.id.btGuardarCambiosEditarAsignaturaProfesor)
        val btCancelar = view.findViewById<Button>(R.id.btCancelarEditarAsignaturaProfesor)

        val btGuardarIcono = view.findViewById<Button>(R.id.btGuardarIconoEditarAsignaturaProfesor)
        val btCancelarIcono = view.findViewById<Button>(R.id.btCancelarGuardarIconoEditarAsignaturaProfesor)

        val rvIconos = view.findViewById<RecyclerView>(R.id.rvIconosEditarAsignaturaProfesor)

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

        var iconoActual = Uri.parse("")

        spinnerCurso.adapter = spinnerAdapter

        db.collection("asignaturas")
            .document(idAsignatura)
            .get()
            .addOnSuccessListener {
                etNombre.setText(it.data?.get("nombre").toString())
                spinnerCurso.setSelection(cursos.indexOf(it.get("curso").toString()))

                iconoActual = Uri.parse(it.data?.get("icono").toString())
                Picasso.get().load(it.getString("icono")).into(ivIcono)
            }

        btGuardarCambios.setOnClickListener {
            val nombre = etNombre.text.toString()
            val curso = spinnerCurso.selectedItem.toString()
            val icono = iconoActual

            db.collection("asignaturas").document(idAsignatura).update("nombre", nombre)
            db.collection("asignaturas").document(idAsignatura).update("curso", curso)
            db.collection("asignaturas").document(idAsignatura).update("icono", icono)

            cambiarFragment(AsignaturaDetailFragmentProfesor(), idAsignatura)
        }

        btCancelar.setOnClickListener {
            cambiarFragment(AsignaturaDetailFragmentProfesor(), idAsignatura)
        }

        ivIcono.setOnClickListener {
            var iconoAntiguo = ivIcono.drawable

            etNombre.visibility = View.GONE
            spinnerCurso.visibility = View.GONE
            btGuardarCambios.visibility = View.GONE
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

                        adapter.setOnItemClickListener(object: GridIconosAdapter.onItemClickListener{
                            override fun onItemClick(position: Int)
                            {
                                iconoActual = listaIconos.get(position)
                                Picasso.get().load(iconoActual).into(ivIcono)
                            }
                        })

                        btGuardarIcono.setOnClickListener {
                            etNombre.visibility = View.VISIBLE
                            spinnerCurso.visibility = View.VISIBLE
                            btGuardarCambios.visibility = View.VISIBLE
                            btCancelar.visibility = View.VISIBLE

                            rvIconos.visibility = View.GONE
                            btGuardarIcono.visibility = View.GONE
                            btCancelarIcono.visibility = View.GONE
                        }

                        btCancelarIcono.setOnClickListener {
                            etNombre.visibility = View.VISIBLE
                            spinnerCurso.visibility = View.VISIBLE
                            btGuardarCambios.visibility = View.VISIBLE
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
    }

    fun cambiarFragment(framentCambiar: Fragment, idAsignatura: String?)
    {
        var args = Bundle()
        args.putString("asignatura", idAsignatura)

        var fragment = framentCambiar
        fragment.arguments = args

        var fragmentManager = requireActivity().supportFragmentManager

        fragmentManager.beginTransaction()
            .replace(R.id.fragmentContainerProfesor, fragment)
            .commit()
    }
}

