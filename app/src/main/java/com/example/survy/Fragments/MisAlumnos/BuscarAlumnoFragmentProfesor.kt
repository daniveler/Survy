package com.example.survy.Fragments.MisAlumnos

import android.net.Uri
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.SearchView
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.survy.Clases.Alumno
import com.example.survy.Clases.AlumnoAdapterProfesor
import com.example.survy.Clases.AsignaturaAdapterProfesor
import com.example.survy.R
import com.google.firebase.firestore.FirebaseFirestore
import java.util.*

class BuscarAlumnoFragmentProfesor : Fragment()
{
    private val db = FirebaseFirestore.getInstance()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View?
    {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_buscar_alumno_profesor, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?)
    {
        super.onViewCreated(view, savedInstanceState)

        val searchView = view.findViewById<SearchView>(R.id.searchViewBuscarAlumnosProfesor)
        val tvEmpty = view.findViewById<TextView>(R.id.tvEmptyBuscarAlumnoProfesor)
        val recyclerView = view.findViewById<RecyclerView>(R.id.recyclerViewBuscarAlumnoProfesor)

        val idUsuario = arguments?.getString("idUsuario") ?: ""

        val listaAlumnos = mutableListOf<Alumno>()
        val listaAlumnosBusqueda = mutableListOf<Alumno>()

        db.collection("alumnos").get()
            .addOnSuccessListener { task ->
                for (document in task)
                {
                    val id = document.id
                    val nombre = document.data.get("nombre").toString()
                    val apellidos = document.data.get("apellidos").toString()
                    val email = document.data.get("email").toString()
                    val curso = document.data.get("curso").toString()
                    val fotoDePerfil = Uri.parse(document.data.get("fotoDePerfil").toString())

                    var alumno = Alumno(id, nombre, apellidos, email, curso, fotoDePerfil)

                    listaAlumnos.add(alumno)

                    if (!task.isEmpty) { tvEmpty.visibility = View.GONE }

                    listaAlumnos.sortBy { it.curso }
                    var adapter = AlumnoAdapterProfesor(listaAlumnos)

                    recyclerView.layoutManager = LinearLayoutManager(context)
                    recyclerView.setHasFixedSize(true)
                    recyclerView.adapter = adapter

                    adapter.setOnClickListener(object: AlumnoAdapterProfesor.onItemClickListener{
                        override fun onItemClick(position: Int)
                        {
                            var alumnoActual = listaAlumnos.get(position)

                            Toast.makeText(context, "Alumno: " + alumnoActual.nombre + " " + alumnoActual.apellidos,
                                Toast.LENGTH_LONG).show()
                        }
                    })

                    searchView.setOnQueryTextListener(object: SearchView.OnQueryTextListener {
                        override fun onQueryTextSubmit(query: String?): Boolean
                        {
                            return true
                        }

                        override fun onQueryTextChange(newText: String?): Boolean
                        {
                            val searchText = newText!!.lowercase(Locale.getDefault())

                            if (searchText.isNotEmpty())
                            {
                                listaAlumnosBusqueda.clear()

                                listaAlumnos.forEach {
                                    if (it.nombre.lowercase(Locale.getDefault()).contains(searchText))
                                    {
                                        listaAlumnosBusqueda.add(it)
                                    }
                                }

                                listaAlumnosBusqueda.sortBy { it.curso }
                                var adapter = AlumnoAdapterProfesor(listaAlumnosBusqueda)

                                recyclerView.layoutManager = LinearLayoutManager(context)
                                recyclerView.setHasFixedSize(true)
                                recyclerView.adapter = adapter

                                adapter.setOnClickListener(object: AlumnoAdapterProfesor.onItemClickListener{
                                    override fun onItemClick(position: Int)
                                    {
                                        var alumnoActual = listaAlumnosBusqueda.get(position)

                                        Toast.makeText(context, "Alumno: " + alumnoActual.nombre + " " + alumnoActual.apellidos,
                                            Toast.LENGTH_LONG).show()
                                        //cambiarFragment(MatricularAlumnoFragmentProfesor(), idUsuario, asignaturaActual.id)
                                    }
                                })

                                recyclerView.adapter!!.notifyDataSetChanged()
                            }
                            else
                            {
                                listaAlumnosBusqueda.clear()
                                listaAlumnosBusqueda.addAll(listaAlumnos)
                                recyclerView.adapter!!.notifyDataSetChanged()
                            }

                            return true
                        }
                    })

                }
            }


    }
}