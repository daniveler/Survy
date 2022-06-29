package com.example.survy.Fragments.Resultados.Alumno

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.survy.Adapters.ResultadoAdapterAlumno
import com.example.survy.Clases.Encuesta
import com.example.survy.Clases.Resultado
import com.example.survy.Fragments.MisAsignaturas.Alumno.AsignaturaDetailFragmentAlumno
import com.example.survy.R
import com.google.firebase.firestore.FirebaseFirestore


class VerResultadosAsignaturaFragmentAlumno : Fragment()
{
    private val db = FirebaseFirestore.getInstance()
    lateinit var idAsignatura: String
    lateinit var idUsuario: String
    lateinit var idEncuesta: String

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View?
    {
        // Inflate the layout for this fragment
        return inflater.inflate(
            R.layout.fragment_ver_resultados_asignatura_alumno,
            container,
            false
        )
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?)
    {
        super.onViewCreated(view, savedInstanceState)

        val tvEmpty             = view.findViewById<TextView>(R.id.tvEmptyVerResultadosAlumno)
        val rvListaResultados   = view.findViewById<RecyclerView>(R.id.recyclerViewVerResultadosAlumno)
        val btCancelar          = view.findViewById<Button>(R.id.btCancelarVerResultadosAlumno)

        idUsuario = arguments?.getString("idUsuario") ?: ""
        idAsignatura = arguments?.getString("idAsignatura") ?: ""

        val listaResultadosUsuario = mutableListOf<Resultado>()

        val mapaResultadosFinal = mutableMapOf<Resultado, Encuesta>()

        val listaEncuestas = mutableListOf<Encuesta>()

        db.collection("encuestas")
            .whereEqualTo("idAsignatura", idAsignatura)
            .get().addOnSuccessListener { task ->
                for (encuestaDoc in task)
                {
                    idEncuesta = encuestaDoc.id
                    val nombre = encuestaDoc.data.get("nombre").toString()
                    val descripcion = encuestaDoc.data.get("descripcion").toString()
                    val icono = encuestaDoc.data.get("icono").toString()

                    val encuesta = Encuesta(idEncuesta, idAsignatura, nombre, descripcion, icono)

                    listaEncuestas.add(encuesta)
                }

                db.collection("resultados")
                    .whereEqualTo("idUsuario", idUsuario)
                    .get().addOnSuccessListener { task ->
                        for (resultadosDoc in task)
                        {
                            val id = resultadosDoc.id
                            val idEncuesta = resultadosDoc.data.get("idEncuesta").toString()
                            val idAsignatura = resultadosDoc.data.get("idAsignatura").toString()
                            val fecha = resultadosDoc.data.get("fecha").toString()
                            val nota = resultadosDoc.data.get("nota").toString()

                            val resultado = Resultado(id, idUsuario, idAsignatura, idEncuesta, fecha, nota)

                            listaResultadosUsuario.add(resultado)
                        }

                        for (encuesta in listaEncuestas) // Todas las encuestas de la asignatura
                        {
                            for (resultado in listaResultadosUsuario) // Todos los resultados de Dani en todas las encuestas
                            {
                                if (resultado.idEncuesta == encuesta.id)
                                {
                                    mapaResultadosFinal.put(resultado, encuesta)
                                }
                            }
                        }

                        if (mapaResultadosFinal.isEmpty()) { tvEmpty.visibility = View.VISIBLE }

                        var adapter = ResultadoAdapterAlumno(mapaResultadosFinal)

                        rvListaResultados.layoutManager = LinearLayoutManager(context)
                        rvListaResultados.setHasFixedSize(true)
                        rvListaResultados.adapter = adapter

                        adapter.setOnItemClickListener(object: ResultadoAdapterAlumno.onItemClickListener {
                            override fun onItemClick(position: Int)
                            {

                            }
                        })
                    }
            }

        btCancelar.setOnClickListener {
            cambiarFragment(AsignaturaDetailFragmentAlumno(), idUsuario, idAsignatura)
        }
    }

    fun cambiarFragment(framentCambiar: Fragment, idUsuario: String, idAsignatura: String?)
    {
        var args = Bundle()
        args.putString("idUsuario", idUsuario)
        args.putString("idAsignatura", idAsignatura)

        var fragment = framentCambiar
        fragment.arguments = args

        var fragmentManager = requireActivity().supportFragmentManager

        fragmentManager.beginTransaction()
            .replace(R.id.fragmentContainerAlumno, fragment)
            .commit()
    }

    /*fun cargarEncuestas(firestoreCallback: FirestoreCallbackEncuestas)
    {
        val listaEncuestas = mutableListOf<Encuesta>()
        val listaResultados = mutableListOf<Resultado>()


        db.collection("encuestas")
            .whereEqualTo("idAsignatura", idAsignatura)
            .get().addOnSuccessListener { task ->
                for (encuestaDoc in task)
                {
                    idEncuesta = encuestaDoc.id
                    val nombre = encuestaDoc.data.get("nombre").toString()
                    val descripcion = encuestaDoc.data.get("descripcion").toString()
                    val icono = encuestaDoc.data.get("icono").toString()

                    val encuesta = Encuesta(idEncuesta, idAsignatura, nombre, descripcion, icono)

                    listaEncuestas.add(encuesta)
                }

                Log.i("DANI", "Encuestas: " + listaEncuestas)
            }
    }


    fun cargarResultados(firestoreCallback: FirestoreCallbackResultados)
    {
        val listaResultados = mutableListOf<Resultado>()

        db.collection("resultados")
            .whereEqualTo("idUsuario", idUsuario)
            .whereEqualTo("idEncuesta", idEncuesta)
            .get().addOnSuccessListener { resultadosDoc ->
                for (resultadoDoc in resultadosDoc)
                {
                    val idResultado = resultadoDoc.id
                    val idUsuario = idUsuario
                    val idEncuesta = idEncuesta
                    val fechaResultado = resultadoDoc.data.get("fecha").toString()
                    val notaResultado = resultadoDoc.data.get("nota").toString()

                    val resultado = Resultado(idResultado, idUsuario, idEncuesta, fechaResultado, notaResultado)

                    listaResultados.add(resultado)
                }

                firestoreCallback.onCallBack(listaResultados)
            }
    }*/


    /*interface FirestoreCallbackResultados
    {
        fun onCallBack(lista: List<Resultado>)
    }

    interface FirestoreCallbackEncuestas
    {
        fun onCallBack(lista: List<Resultado>)
    }*/
}