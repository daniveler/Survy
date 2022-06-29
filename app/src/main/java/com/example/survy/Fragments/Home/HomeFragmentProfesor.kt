package com.example.survy.Fragments.Home

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.survy.Adapters.AsignaturaAdapterHome
import com.example.survy.Adapters.AsignaturaAdapterProfesor
import com.example.survy.Clases.Asignatura
import com.example.survy.Fragments.MisAsignaturas.Profesor.AsignaturaDetailFragmentProfesor
import com.example.survy.Fragments.MisAsignaturas.Profesor.MisAsignaturasFragmentProfesor
import com.example.survy.R
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class HomeFragmentProfesor : Fragment()
{
    private val db = FirebaseFirestore.getInstance()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View?
    {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_home_profesor, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?)
    {
        super.onViewCreated(view, savedInstanceState)

        var user = FirebaseAuth.getInstance().currentUser
        val idUsuario = user?.uid ?: ""

        val tvNombre            = view.findViewById<TextView>(R.id.tvHolaHomeProfesor)
        val btMisAsignaturas    = view.findViewById<Button>(R.id.btMisAsignaturasHomeProfesor)
        val rvMisAsignaturas    = view.findViewById<RecyclerView>(R.id.rvMisAsignaturasHomeProfesor)

        val listaAsignaturas = mutableListOf<Asignatura>()

        db.collection("profesores").document(idUsuario)
            .get().addOnSuccessListener {
                tvNombre.setText("Hola, " + it.get("nombre").toString())
            }

        db.collection("asignaturas")
            .whereEqualTo("idProfesor", idUsuario)
            .get()
            .addOnSuccessListener { task ->
                for (document in task)
                {
                    val id = document.id
                    val nombre = document.data.get("nombre").toString()
                    val idProfesor = document.data.get("idProfesor").toString()
                    val curso = document.data.get("curso").toString()
                    val icono = document.data.get("icono").toString()
                    val numAlumnos = document.data.get("numAlumnos").toString().toInt()

                    var asignatura = Asignatura(id, nombre, idProfesor, curso, icono, numAlumnos)

                    listaAsignaturas.add(asignatura)
                }

                var adapter = AsignaturaAdapterHome(listaAsignaturas)

                rvMisAsignaturas.layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
                rvMisAsignaturas.setHasFixedSize(true)
                rvMisAsignaturas.adapter = adapter

                adapter.setOnItemClickListener(object : AsignaturaAdapterHome.onItemClickListener
                {
                    override fun onItemClick(position: Int)
                    {
                        var asignaturaActual = listaAsignaturas.get(position)

                        (activity as AppCompatActivity).supportActionBar?.title = getString(R.string.titleMisAsignaturasProfesor)
                        cambiarFragment(AsignaturaDetailFragmentProfesor(), idUsuario, asignaturaActual.id)
                    }
                })
            }

        btMisAsignaturas.setOnClickListener {
            (activity as AppCompatActivity).supportActionBar?.title = getString(R.string.titleMisAsignaturasProfesor)
            cambiarFragment(MisAsignaturasFragmentProfesor(), idUsuario, null)
        }
    }

    fun cambiarFragment(fragmentCambiar: Fragment, idUsuario: String, idAsignatura: String?)
    {
        var args = Bundle()
        args.putString("idUsuario", idUsuario)
        args.putString("idAsignatura", idAsignatura ?: "")

        var fragment = fragmentCambiar

        fragment.arguments = args

        var fragmentManager = requireActivity().supportFragmentManager

        fragmentManager.beginTransaction()
            .replace(R.id.fragmentContainerProfesor, fragment)
            .commit()
    }
}