package com.example.survy.Fragments.MisEncuestas.Profesor

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.SearchView
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.survy.Adapters.EncuestaAdapter
import com.example.survy.Clases.Encuesta
import com.example.survy.Fragments.MisAsignaturas.Profesor.AsignaturaDetailFragmentProfesor
import com.example.survy.R
import com.google.firebase.firestore.FirebaseFirestore
import java.util.*

class MisEncuestasFragmentProfesor : Fragment()
{
    private val db = FirebaseFirestore.getInstance()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View?
    {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_mis_encuestas_profesor, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?)
    {
        super.onViewCreated(view, savedInstanceState)

        val searchView          = view.findViewById<SearchView>(R.id.searchViewMisEncuestasProfesor)
        val tvEmpty             = view.findViewById<TextView>(R.id.tvEmptyMisEncuestasProfesor)
        val rvListaEncuestas    = view.findViewById<RecyclerView>(R.id.recyclerViewMisEncuestasProfesor)

        val btNuevaEncuesta     = view.findViewById<Button>(R.id.btNuevaEncuestaMisEncuestasProfesor)
        val btCancelar          = view.findViewById<Button>(R.id.btCancelarMisEncuestasProfesor)

        val idUsuario = arguments?.getString("idUsuario") ?: ""
        val idAsignatura = arguments?.getString("idAsignatura") ?: ""

        val listaEncuestas = mutableListOf<Encuesta>()
        var listaEncuestasBusqueda = mutableListOf<Encuesta>()

        db.collection("encuestas")
            .whereEqualTo("idAsignatura", idAsignatura)
            .get()
            .addOnSuccessListener { task ->
                for (document in task)
                {
                    val id = document.id
                    val idAsignatura = document.data.get("idAsignatura").toString()
                    val nombre = document.data.get("nombre").toString()
                    val descripcion = document.data.get("descripcion").toString()
                    val icono = document.data.get("icono").toString()

                    var encuesta = Encuesta(id, idAsignatura, nombre, descripcion, icono)

                    listaEncuestas.add(encuesta)
                }
                if (task.isEmpty) { tvEmpty.visibility = View.VISIBLE }

                var adapter = EncuestaAdapter(listaEncuestas)

                rvListaEncuestas.layoutManager = LinearLayoutManager(context)
                rvListaEncuestas.setHasFixedSize(true)
                rvListaEncuestas.adapter = adapter

                adapter.setOnItemClickListener(object: EncuestaAdapter.onItemClickListener {
                    override fun onItemClick(position: Int)
                    {
                        var encuestaActual = listaEncuestas.get(position)

                        cambiarFragment(EncuestaDetailFragmentProfesor(), idUsuario, encuestaActual.id, idAsignatura)
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
                            listaEncuestasBusqueda.clear()

                            listaEncuestas.forEach {
                                if (it.nombre.lowercase(Locale.getDefault()).contains(searchText))
                                {
                                    listaEncuestasBusqueda.add(it)
                                }
                            }

                            var adapter = EncuestaAdapter(listaEncuestasBusqueda)

                            rvListaEncuestas.layoutManager = LinearLayoutManager(context)
                            rvListaEncuestas.setHasFixedSize(true)
                            rvListaEncuestas.adapter = adapter

                            adapter.setOnItemClickListener(object: EncuestaAdapter.onItemClickListener{
                                override fun onItemClick(position: Int)
                                {
                                    var encuestaActual = listaEncuestasBusqueda.get(position)

                                    cambiarFragment(EncuestaDetailFragmentProfesor(), idUsuario, encuestaActual.id, idAsignatura)
                                }
                            })

                            rvListaEncuestas.adapter!!.notifyDataSetChanged()
                        }
                        else
                        {
                            listaEncuestasBusqueda.clear()
                            listaEncuestasBusqueda.addAll(listaEncuestas)
                            rvListaEncuestas.adapter!!.notifyDataSetChanged()
                        }

                        return true
                    }
                })
            }

        btNuevaEncuesta.setOnClickListener {
            cambiarFragment(NuevaEncuestaFragmentProfesor(), idUsuario, null, idAsignatura)
        }

        btCancelar.setOnClickListener {
            cambiarFragment(AsignaturaDetailFragmentProfesor(), idUsuario, null, idAsignatura)
        }
    }

    fun cambiarFragment(framentCambiar: Fragment, idUsuario: String, idEncuesta: String?, idAsignatura: String)
    {
        var args = Bundle()
        args.putString("idUsuario", idUsuario)
        args.putString("idEncuesta", idEncuesta)
        args.putString("idAsignatura", idAsignatura)

        var fragment = framentCambiar
        fragment.arguments = args

        var fragmentManager = requireActivity().supportFragmentManager

        fragmentManager.beginTransaction()
            .replace(R.id.fragmentContainerProfesor, fragment)
            .commit()
    }
}