package com.example.survy.Fragments.MisAsignaturas.Alumno

import android.content.DialogInterface
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.SearchView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.survy.Authentication.LoginActivity
import com.example.survy.Clases.Asignatura
import com.example.survy.Clases.AsignaturaAdapterAlumno
import com.example.survy.Clases.AsignaturaAdapterProfesor
import com.example.survy.Fragments.MisAlumnos.MatricularAlumnoFragmentProfesor
import com.example.survy.Fragments.MisAsignaturas.Profesor.AsignaturaDetailFragmentProfesor
import com.example.survy.Fragments.MisAsignaturas.Profesor.NuevaAsignaturaFragmentProfesor
import com.example.survy.R
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.ktx.Firebase
import com.google.zxing.client.android.BeepManager
import com.google.zxing.integration.android.IntentIntegrator
import java.util.*

class MisAsignaturasFragmentAlumno : Fragment()
{
    private val db = FirebaseFirestore.getInstance()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View?
    {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_mis_asignaturas_alumno, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?)
    {
        super.onViewCreated(view, savedInstanceState)

        var user = FirebaseAuth.getInstance().currentUser
        var idUsuario = arguments?.getString("idUsuario", "") ?: ""

        val searchView = view.findViewById<SearchView>(R.id.searchViewMisAsignaturasAlumno)
        val tvNoHayAsignaturas = view.findViewById<TextView>(R.id.tvMisAsignaturasAlumno)
        val rvListaAsignaturas = view.findViewById<RecyclerView>(R.id.recyclerViewMisAsignaturasAlumno)
        val btNuevaAsignatura = view.findViewById<Button>(R.id.btNuevaAsignaturaMisAsignaturasAlumno)

        val listaAsignaturas = mutableListOf<Asignatura>()
        var listaAsignaturasBusqueda = mutableListOf<Asignatura>()

        var sinAsignaturas = true

        db.collection("matriculado")
            .whereEqualTo("idAlumno", idUsuario)
            .get()
            .addOnSuccessListener { task ->
                for (document in task)
                {
                    sinAsignaturas = false

                    val idAsignatura = document.data.get("idAsignatura").toString()

                    db.collection("asignaturas")
                        .document(idAsignatura)
                        .get().addOnSuccessListener {
                            val id = it.id
                            val nombre = it.data?.get("nombre").toString()
                            val idProfesor = it.data?.get("idProfesor").toString()
                            val curso = it.data?.get("curso").toString()
                            val icono = it.data?.get("icono").toString()

                            var asignatura = Asignatura(id, nombre, idProfesor, curso, icono)

                            listaAsignaturas.add(asignatura)
                        }
                }

                if (sinAsignaturas) { tvNoHayAsignaturas.visibility = View.GONE }

                var adapter = AsignaturaAdapterAlumno(listaAsignaturas)

                rvListaAsignaturas.layoutManager = LinearLayoutManager(context)
                rvListaAsignaturas.setHasFixedSize(true)
                rvListaAsignaturas.adapter = adapter

                /*adapter.setOnItemClickListener(object: AsignaturaAdapterAlumno.onItemClickListener {
                    override fun onItemClick(position: Int)
                    {
                        var asignaturaActual = listaAsignaturas.get(position)

                        cambiarFragment(AsignaturaDetailFragmentProfesor(), idUsuario, asignaturaActual.id)
                    }
                })*/

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

                            /*adapter.setOnItemClickListener(object: AsignaturaAdapterAlumno.onItemClickListener{
                                override fun onItemClick(position: Int)
                                {
                                    var asignaturaActual = listaAsignaturasBusqueda.get(position)

                                    cambiarFragment(MatricularAlumnoFragmentProfesor(), idUsuario, asignaturaActual.id)
                                }
                            })*/

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
            val integrator = IntentIntegrator.forSupportFragment(this)

            integrator.setDesiredBarcodeFormats(IntentIntegrator.QR_CODE)
            integrator.setPrompt(getString(R.string.tvEscanerQrMatricularAlumnoAlumno))
            integrator.setBeepEnabled(true)

            integrator.initiateScan()
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?)
    {
        val result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data)

        if (result != null)
        {
            if (result.contents == null)
            {
                Toast.makeText(activity, "Lectura de QR cancelada", Toast.LENGTH_LONG).show()
            }
            else
            {
                Toast.makeText(activity, "El valor escaneado es: " + result.contents, Toast.LENGTH_LONG).show()

                mostrarAlertaCerrarSesion(result.contents)
            }
        }
        else
        {
            super.onActivityResult(requestCode, resultCode, data)
        }
    }

    fun cambiarFragment(fragmentCambiar: Fragment, idUsuario: String, idAsignatura: String?)
    {
        var args = Bundle()
        args.putString("idUsuario", idUsuario)
        args.putString("asignatura", idAsignatura ?: "")

        var fragment = fragmentCambiar

        fragment.arguments = args

        var fragmentManager = requireActivity().supportFragmentManager

        fragmentManager.beginTransaction()
            .replace(R.id.fragmentContainerAlumno, fragment)
            .commit()
    }

    fun mostrarAlertaCerrarSesion(nombre: String)
    {
        val dialogBuilder = AlertDialog.Builder(requireContext())
        dialogBuilder.setTitle("Añadir Asignatura")
        dialogBuilder.setMessage("¿Deseas matricularte en la asignatura " + nombre)
        dialogBuilder.setPositiveButton("Sí", DialogInterface.OnClickListener {
                dialog, id -> Toast.makeText(activity, "Alumno matriculado", Toast.LENGTH_LONG).show()

        })
        dialogBuilder.setNegativeButton("Cancelar", DialogInterface.OnClickListener {
                dialog, id -> dialog.cancel()
        })

        val alerta = dialogBuilder.create()
        alerta.show()
    }
}