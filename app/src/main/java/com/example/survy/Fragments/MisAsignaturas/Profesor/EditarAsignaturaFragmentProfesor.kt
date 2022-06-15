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
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.survy.Clases.GridIconosAdapter
import com.example.survy.R
import com.google.android.gms.tasks.OnSuccessListener
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage

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

        spinnerCurso.adapter = spinnerAdapter

        db.collection("asignaturas")
            .document(idAsignatura)
            .get()
            .addOnSuccessListener {
                etNombre.setText(it.data?.get("nombre").toString())
                spinnerCurso.setSelection(cursos.indexOf(it.get("curso").toString()))
                ivIcono.setImageURI(Uri.parse(it.data?.get("icono").toString()))
            }

        ivIcono.setOnClickListener {
            etNombre.visibility = View.GONE
            spinnerCurso.visibility = View.GONE
            btGuardarCambios.visibility = View.GONE
            btCancelar.visibility = View.GONE

            /*rvIconos.visibility = View.VISIBLE

            val listaIconos = obtenerIconos()

            var adapter = GridIconosAdapter(listaIconos)

            rvIconos.layoutManager = LinearLayoutManager(context)
            rvIconos.setHasFixedSize(true)
            rvIconos.adapter = adapter

            adapter.setOnItemClickListener(object: GridIconosAdapter.onItemClickListener{
                override fun onItemClick(position: Int)
                {
                    var iconoActual = listaIconos.get(position)

                    rvIconos.visibility = View.GONE

                    etNombre.visibility = View.VISIBLE
                    spinnerCurso.visibility = View.VISIBLE
                    btGuardarCambios.visibility = View.VISIBLE
                    btCancelar.visibility = View.VISIBLE

                    ivIcono.setImageURI(iconoActual)
                }
            })*/

            obtenerIconos()

        }


    }

    fun obtenerIconos(listener: OnSuccessListener) : List<Uri>
    {
        var storageRef = storage.reference
        //var iconosRef = storageRef.child("iconos_asignaturas_100px/icons8-7-cute-100.png")
        var iconosRef = storageRef.child("iconos_asignaturas_100px")

        var listaIconos = mutableListOf<Uri>()

        var uri : String
        /*iconosRef.downloadUrl.addOnSuccessListener {
            Log.i("DANI", "$it")
            uri = it.toString()
        }*/

        val successListener = iconosRef.listAll().result

        val successListenerChikito = successListener.items.forEach { item ->
                item.downloadUrl.result
        }

        

        return listaIconos
    }
}
