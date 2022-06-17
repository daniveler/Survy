package com.example.survy.Fragments.MisAsignaturas.Profesor

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.LinearLayout
import android.widget.SearchView
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.survy.Clases.Asignatura
import com.example.survy.Clases.AsignaturaAdapter
import com.example.survy.R
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.toObject
import com.google.firebase.firestore.ktx.toObjects
import java.util.*
import kotlin.concurrent.schedule

class MisAsignaturasFragmentProfesor : Fragment()
{
    private val db = FirebaseFirestore.getInstance()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View?
    {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_mis_asignaturas_profesor, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?)
    {
        super.onViewCreated(view, savedInstanceState)

        var user = FirebaseAuth.getInstance().currentUser
        var email = user?.email ?: ""

        val searchView = view.findViewById<SearchView>(R.id.searchViewMisAsignaturasProfesor)
        val tvNoHayAsignaturas = view.findViewById<TextView>(R.id.tvMisAsignaturasProfesor)
        val rvListaAsignaturas = view.findViewById<RecyclerView>(R.id.recyclerViewMisAsignaturasProfesor)
        val btNuevaAsignatura = view.findViewById<Button>(R.id.btNuevaAsignaturaMisAsignaturasProfesor)

        val listaAsignaturas = mutableListOf<Asignatura>()
        var listaAsignaturasBusqueda = mutableListOf<Asignatura>()

        db.collection("asignaturas")
            .whereEqualTo("idProfesor", email)
            .get()
            .addOnSuccessListener { task ->
                for (document in task)
                {
                    // Falta número de alumnos
                    val id = document.id
                    val nombre = document.data.get("nombre").toString()
                    val idProfesor = document.data.get("idProfesor").toString()
                    val curso = document.data.get("curso").toString()
                    val icono = document.data.get("icono").toString()

                    var asignatura = Asignatura(id, nombre, idProfesor, curso, icono)

                    listaAsignaturas.add(asignatura)
                }
                tvNoHayAsignaturas.visibility = View.GONE

                var adapter = AsignaturaAdapter(listaAsignaturas)

                rvListaAsignaturas.layoutManager = LinearLayoutManager(context)
                rvListaAsignaturas.setHasFixedSize(true)
                rvListaAsignaturas.adapter = adapter

                adapter.setOnItemClickListener(object: AsignaturaAdapter.onItemClickListener{
                    override fun onItemClick(position: Int)
                    {
                        var asignaturaActual = listaAsignaturas.get(position)

                        cambiarFragment(AsignaturaDetailFragmentProfesor(), email, asignaturaActual.id)
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
                            listaAsignaturasBusqueda.clear()

                            listaAsignaturas.forEach {
                                if (it.nombre.lowercase(Locale.getDefault()).contains(searchText))
                                {
                                    listaAsignaturasBusqueda.add(it)
                                }
                            }

                            var adapter = AsignaturaAdapter(listaAsignaturasBusqueda)

                            rvListaAsignaturas.layoutManager = LinearLayoutManager(context)
                            rvListaAsignaturas.setHasFixedSize(true)
                            rvListaAsignaturas.adapter = adapter

                            adapter.setOnItemClickListener(object: AsignaturaAdapter.onItemClickListener{
                                override fun onItemClick(position: Int)
                                {
                                    var asignaturaActual = listaAsignaturasBusqueda.get(position)

                                    cambiarFragment(AsignaturaDetailFragmentProfesor(), email, asignaturaActual.id)
                                }
                            })

                            rvListaAsignaturas.adapter!!.notifyDataSetChanged()
                        }
                        else
                        {
                            listaAsignaturasBusqueda.clear()
                            listaAsignaturasBusqueda.addAll(listaAsignaturas)
                            rvListaAsignaturas.adapter!!.notifyDataSetChanged()
                        }

                        return true
                    }
                })
            }

        btNuevaAsignatura.setOnClickListener {
            val email = user!!.email ?: ""
            cambiarFragment(NuevaAsignaturaFragmentProfesor(), email, null)
        }
    }

    fun cambiarFragment(framentCambiar: Fragment, email: String, idAsignatura: String?)
    {
        var args = Bundle()
        args.putString("email", email)
        args.putString("asignatura", idAsignatura ?: "")

        var fragment = framentCambiar
        fragment.arguments = args

        var fragmentManager = requireActivity().supportFragmentManager

        fragmentManager.beginTransaction()
            .replace(R.id.fragmentContainerProfesor, fragment)
            .commit()
    }
}