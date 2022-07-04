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
import com.example.survy.Adapters.AsignaturaAdapterAlumno
import com.example.survy.Adapters.AsignaturaAdapterHome
import com.example.survy.Clases.Asignatura
import com.example.survy.Fragments.MisAsignaturas.Alumno.AsignaturaDetailFragmentAlumno
import com.example.survy.Fragments.MisAsignaturas.Alumno.MisAsignaturasFragmentAlumno
import com.example.survy.R
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

/**
 * Página principal de los usuarios Alumno.
 */
class HomeFragmentAlumno : Fragment()
{
    private val db = FirebaseFirestore.getInstance()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View?
    {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_home_alumno, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?)
    {
        super.onViewCreated(view, savedInstanceState)

        var user = FirebaseAuth.getInstance().currentUser
        val idUsuario = user?.uid ?: ""

        val tvNombre = view.findViewById<TextView>(R.id.tvHolaHomeAlumno)
        val btMisAsignaturas = view.findViewById<Button>(R.id.btMisAsignaturasHomeAlumno)
        val rvMisAsignaturas = view.findViewById<RecyclerView>(R.id.rvMisAsignaturasHomeAlumno)

        val listaAsignaturas = mutableListOf<Asignatura>()

        db.collection("alumnos").document(idUsuario)
            .get().addOnSuccessListener {
                tvNombre.setText("Hola, " + it.get("nombre").toString())
            }

        db.collection("matriculado")
            .whereEqualTo("idAlumno", idUsuario)
            .get()
            .addOnSuccessListener { task ->
                for (document in task)
                {
                    val idAsignatura = document.data.get("idAsignatura").toString()

                    db.collection("asignaturas")
                        .document(idAsignatura)
                        .get().addOnSuccessListener {
                            val id = it.id
                            val nombre = it.data?.get("nombre").toString()
                            val idProfesor = it.data?.get("idProfesor").toString()
                            val curso = it.data?.get("curso").toString()
                            val icono = it.data?.get("icono").toString()
                            val numAlumnos = it.data?.get("numAlumnos").toString().toInt()

                            var asignatura = Asignatura(id, nombre, idProfesor, curso, icono, numAlumnos)

                            listaAsignaturas.add(asignatura)

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
                                    cambiarFragment(AsignaturaDetailFragmentAlumno(), idUsuario, asignaturaActual.id)
                                }
                            })

                            btMisAsignaturas.setOnClickListener {
                                (activity as AppCompatActivity).supportActionBar?.title = getString(R.string.titleMisAsignaturasProfesor)
                                cambiarFragment(MisAsignaturasFragmentAlumno(), idUsuario, null)
                            }
                        }
                }
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
            .replace(R.id.fragmentContainerAlumno, fragment)
            .commit()
    }
}