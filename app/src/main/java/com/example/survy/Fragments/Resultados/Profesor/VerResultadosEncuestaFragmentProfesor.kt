package com.example.survy.Fragments.Resultados.Profesor

import android.net.Uri
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.survy.Adapters.ResultadoAdapterAlumno
import com.example.survy.Adapters.ResultadoAdapterProfesor
import com.example.survy.Clases.Alumno
import com.example.survy.Clases.Encuesta
import com.example.survy.Clases.Resultado
import com.example.survy.Fragments.MisEncuestas.Alumno.EncuestaDetailFragmentAlumno
import com.example.survy.Fragments.MisEncuestas.Profesor.EncuestaDetailFragmentProfesor
import com.example.survy.R
import com.google.firebase.firestore.FirebaseFirestore

class VerResultadosEncuestaFragmentProfesor : Fragment()
{
    private val db = FirebaseFirestore.getInstance()

    private lateinit var idAlumno : String

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View?
    {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_ver_resultados_encuesta_profesor, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?)
    {
        super.onViewCreated(view, savedInstanceState)

        val idUsuario = arguments?.getString("idUsuario") ?: ""
        val idAsignatura = arguments?.getString("idAsignatura") ?: ""
        val idEncuesta = arguments?.getString("idEncuesta") ?: ""

        val tvEmpty             = view.findViewById<TextView>(R.id.tvEmptyVerResultadosEncuestaProfesor)
        val rvListaResultados   = view.findViewById<RecyclerView>(R.id.recyclerViewVerResultadosEncuestaProfesor)
        val btCancelar          = view.findViewById<Button>(R.id.btCancelarVerResultadosEncuestaProfesor)

        val mapaResultadosFinal = mutableMapOf<Resultado, Alumno>()

        val listaResultados = mutableListOf<Resultado>()
        val listaAlumnos = mutableListOf<Alumno>()

        db.collection("resultados")
            .whereEqualTo("idEncuesta", idEncuesta)
            .get().addOnSuccessListener { task ->
                for (resultadosDoc in task)
                {
                    val id = resultadosDoc.id
                    val idEncuesta = idEncuesta
                    idAlumno = resultadosDoc.data.get("idUsuario").toString()
                    val idAsignatura = resultadosDoc.data.get("idAsignatura").toString()
                    val fecha = resultadosDoc.data.get("fecha").toString()
                    val nota = resultadosDoc.data.get("nota").toString()

                    val resultado = Resultado(id, idAlumno, idAsignatura, idEncuesta, fecha, nota)

                    listaResultados.add(resultado)
                }

                for (resultado in listaResultados)
                {
                    db.collection("alumnos").document(idAlumno)
                        .get().addOnSuccessListener {
                            val id = it.id
                            val nombre = it.getString("nombre").toString()
                            val apellidos = it.getString("apellidos").toString()
                            val email = it.getString("email").toString()
                            val curso = it.getString("curso").toString()
                            val fotoDePerfil = Uri.parse(it.getString("fotoDePerfil").toString())

                            val alumno = Alumno(id, nombre, apellidos, email, curso, fotoDePerfil)

                            mapaResultadosFinal.put(resultado, alumno)

                            if (mapaResultadosFinal.isEmpty()) { tvEmpty.visibility = View.VISIBLE }

                            var adapter = ResultadoAdapterProfesor(mapaResultadosFinal)

                            rvListaResultados.layoutManager = LinearLayoutManager(context)
                            rvListaResultados.setHasFixedSize(true)
                            rvListaResultados.adapter = adapter

                            adapter.setOnItemClickListener(object: ResultadoAdapterProfesor.onItemClickListener {
                                override fun onItemClick(position: Int)
                                {

                                }
                            })
                        }
                }
            }

        btCancelar.setOnClickListener {
            cambiarFragment(EncuestaDetailFragmentProfesor(), idUsuario, idAsignatura, idEncuesta)
        }
    }

    fun cambiarFragment(framentCambiar: Fragment, idUsuario: String, idAsignatura: String, idEncuesta: String)
    {
        var args = Bundle()
        args.putString("idUsuario", idUsuario)
        args.putString("idAsignatura", idAsignatura)
        args.putString("idEncuesta", idEncuesta)

        var fragment = framentCambiar
        fragment.arguments = args

        var fragmentManager = requireActivity().supportFragmentManager

        fragmentManager.beginTransaction()
            .replace(R.id.fragmentContainerProfesor, fragment)
            .commit()
    }
}