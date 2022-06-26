package com.example.survy.Fragments.MisAlumnos

import android.content.DialogInterface
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.SearchView
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.survy.Clases.Asignatura
import com.example.survy.Adapters.AsignaturaAdapterAlumno
import com.example.survy.R
import com.google.firebase.firestore.FirebaseFirestore
import java.util.*

class VerAsignaturasAlumnoFragmentProfesor : Fragment()
{
    private val db = FirebaseFirestore.getInstance()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View?
    {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_ver_asignaturas_alumno_profesor, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?)
    {
        super.onViewCreated(view, savedInstanceState)

        var idAlumno = arguments?.getString("idAlumno", "") ?: ""

        val searchView          = view.findViewById<SearchView>(R.id.searchViewVerAsignaturasAlumnoProfesor)
        val tvNoHayAsignaturas  = view.findViewById<TextView>(R.id.tvEmptyVerAsignaturasAlumnoProfesor)
        val rvListaAsignaturas  = view.findViewById<RecyclerView>(R.id.recyclerViewVerAsignaturasAlumnoProfesor)
        val btCancelar          = view.findViewById<Button>(R.id.btCancelarVerAsignaturasAlumnoProfesor)

        val listaAsignaturas = mutableListOf<Asignatura>()
        var listaAsignaturasBusqueda = mutableListOf<Asignatura>()

        var sinAsignaturas = true

        db.collection("matriculado")
            .whereEqualTo("idAlumno", idAlumno)
            .get()
            .addOnSuccessListener { task ->
                for (document in task)
                {
                    sinAsignaturas = false

                    val idAsignatura = document.data.get("idAsignatura").toString()

                    db.collection("asignaturas")
                        .document(idAsignatura)
                        .get().addOnSuccessListener {
                            if (!task.isEmpty) { tvNoHayAsignaturas.visibility = View.GONE }

                            val id = it.id
                            val nombre = it.data?.get("nombre").toString()
                            val idProfesor = it.data?.get("idProfesor").toString()
                            val curso = it.data?.get("curso").toString()
                            val icono = it.data?.get("icono").toString()
                            val numAlumnos = it.data?.get("numAlumnos").toString().toInt()

                            var asignatura = Asignatura(id, nombre, idProfesor, curso, icono, numAlumnos)

                            listaAsignaturas.add(asignatura)

                            var adapter = AsignaturaAdapterAlumno(listaAsignaturas)

                            rvListaAsignaturas.layoutManager = LinearLayoutManager(context)
                            rvListaAsignaturas.setHasFixedSize(true)
                            rvListaAsignaturas.adapter = adapter

                            adapter.setOnItemClickListener(object: AsignaturaAdapterAlumno.onItemClickListener {
                                override fun onItemClick(position: Int)
                                {
                                    var asignaturaActual = listaAsignaturas.get(position)

                                    mostrarAlerta(idAlumno, asignaturaActual)
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

                                        var adapter = AsignaturaAdapterAlumno(listaAsignaturasBusqueda)

                                        rvListaAsignaturas.layoutManager = LinearLayoutManager(context)
                                        rvListaAsignaturas.setHasFixedSize(true)
                                        rvListaAsignaturas.adapter = adapter

                                        adapter.setOnItemClickListener(object: AsignaturaAdapterAlumno.onItemClickListener{
                                            override fun onItemClick(position: Int)
                                            {
                                                var asignaturaActual = listaAsignaturasBusqueda.get(position)

                                                mostrarAlerta(idAlumno, asignaturaActual)
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
                }
            }

        btCancelar.setOnClickListener {
            cambiarFragment(MisAlumnosProfesor(), idAlumno)
        }

    }

    fun cambiarFragment(fragmentCambiar: Fragment, idAlumno: String)
    {
        var args = Bundle()
        args.putString("idUsuario", idAlumno)

        var fragment = fragmentCambiar

        fragment.arguments = args

        var fragmentManager = requireActivity().supportFragmentManager

        fragmentManager.beginTransaction()
            .replace(R.id.fragmentContainerProfesor, fragment)
            .commit()
    }

    fun mostrarAlerta(idAlumno: String, asignatura: Asignatura)
    {
        val dialogBuilder = AlertDialog.Builder(requireContext())
        dialogBuilder.setTitle(getString(R.string.alertTitleVerAsingnaturasAlumnoProfesor) + " " + asignatura.nombre + "?")
        dialogBuilder.setMessage(R.string.alertTextVerAsingnaturasAlumnoProfesor)

        dialogBuilder.setPositiveButton("SÍ", DialogInterface.OnClickListener { dialog, id ->
            db.collection("matriculado")
                .whereEqualTo("idAlumno", idAlumno)
                .whereEqualTo("idAsignatura", asignatura.id)
                .get().addOnSuccessListener { task ->
                    for(document in task)
                    {
                        db.collection("matriculado").document(document.id).delete()

                        var numAlumnos = 0
                        db.collection("asignaturas").document(asignatura.id)
                            .get().addOnSuccessListener {
                                numAlumnos = it.getLong("numAlumnos")!!.toInt()
                                numAlumnos--

                                db.collection("asignaturas").document(asignatura.id).update("numAlumnos", numAlumnos)
                            }
                    }
                }
        })

        // FALTA ELIMINAR ENCUESTAS

        dialogBuilder.setNegativeButton("Cancelar", DialogInterface.OnClickListener { dialog, id ->
            dialog.cancel()
        })

        val alerta = dialogBuilder.create()
        alerta.show()
    }
}