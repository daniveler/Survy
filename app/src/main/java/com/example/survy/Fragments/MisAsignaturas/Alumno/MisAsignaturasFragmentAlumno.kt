package com.example.survy.Fragments.MisAsignaturas.Alumno

import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.os.VibrationEffect
import android.os.Vibrator
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.SearchView
import android.widget.TextView
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.survy.Clases.Asignatura
import com.example.survy.Adapters.AsignaturaAdapterAlumno
import com.example.survy.R
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.zxing.integration.android.IntentIntegrator
import java.util.*

class MisAsignaturasFragmentAlumno : Fragment()
{
    private val db = FirebaseFirestore.getInstance()
    private lateinit var auth: FirebaseAuth

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

        auth = FirebaseAuth.getInstance()

        var idUsuario = arguments?.getString("idUsuario", "") ?: ""

        val searchView = view.findViewById<SearchView>(R.id.searchViewVerAsignaturasAlumnoProfesor)
        val tvNoHayAsignaturas = view.findViewById<TextView>(R.id.tvEmptyVerAsignaturasAlumnoProfesor)
        val rvListaAsignaturas = view.findViewById<RecyclerView>(R.id.recyclerViewVerAsignaturasAlumnoProfesor)
        val btNuevaAsignatura = view.findViewById<Button>(R.id.btCancelarVerAsignaturasAlumnoProfesor)

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

                                    cambiarFragment(AsignaturaDetailFragmentAlumno(), idUsuario, asignaturaActual.id)
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

                                                cambiarFragment(AsignaturaDetailFragmentAlumno(), idUsuario, asignaturaActual.id)
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

        btNuevaAsignatura.setOnClickListener {
            val integrator = IntentIntegrator.forSupportFragment(this)

            integrator.setDesiredBarcodeFormats(IntentIntegrator.QR_CODE)
            integrator.setPrompt(getString(R.string.tvEscanerQrMatricularAlumnoAlumno))
            integrator.setBeepEnabled(true)

            integrator.initiateScan()
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?)
    {
        val result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data)

        if (result != null)
        {
            if (result.contents != null)
            {
                val vibrator = context?.getSystemService(Context.VIBRATOR_SERVICE) as Vibrator
                    vibrator.vibrate(VibrationEffect.createOneShot(200, VibrationEffect.DEFAULT_AMPLITUDE))

                db.collection("asignaturas").document(result.contents)
                    .get().addOnSuccessListener {
                        val nombre = it.getString("nombre") as String

                        mostrarAlerta(nombre, result.contents)
                    }
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

    fun recargarFragment()
    {
        val fragmento = requireFragmentManager().beginTransaction()

        fragmento.setReorderingAllowed(false)

        fragmento.detach(this).attach(this).commit()
    }

    fun mostrarAlerta(nombre: String, idAsignatura: String)
    {
        val dialogBuilder = AlertDialog.Builder(requireContext())
        dialogBuilder.setTitle("Añadir Asignatura")
        dialogBuilder.setMessage("¿Deseas matricularte en la asignatura " + nombre + "?")
        dialogBuilder.setPositiveButton("Sí", DialogInterface.OnClickListener {
            dialog, id ->
                val idAlumno = auth.currentUser!!.uid

            db.collection("matriculado")
                .whereEqualTo("idAsignatura", idAsignatura)
                .whereEqualTo("idAlumno", idAlumno)
                .get()
                .addOnCompleteListener { task ->
                    if (task.result.isEmpty)
                    {
                        db.collection("asignaturas").document(idAsignatura).get()
                            .addOnSuccessListener {
                                val cursoAsignatura = it.getString("curso")
                                var numAlumnos = it.getLong("numAlumnos")!!.toInt()

                                db.collection("alumnos").document(idAlumno).get()
                                    .addOnSuccessListener {
                                        val cursoAlumno = it.getString("curso")

                                        if (cursoAlumno == cursoAsignatura)
                                        {
                                            db.collection("matriculado").document().set(
                                                hashMapOf("idAlumno" to idAlumno,
                                                    "idAsignatura" to idAsignatura))
                                                .addOnSuccessListener {
                                                    Toast.makeText(activity, "Te has matriculado correctamente", Toast.LENGTH_LONG).show()

                                                    db.collection("asignaturas").document(idAsignatura).update("numAlumnos", ++numAlumnos)

                                                    cambiarFragment(MisAsignaturasFragmentAlumno(), idAlumno, null)
                                                }
                                        }
                                        else
                                        {
                                            Toast.makeText(activity, "No puedes matricularte en esta asignatura porque no eres de este curso",
                                                Toast.LENGTH_LONG).show()
                                        }
                                    }
                            }
                    }
                    else
                    {
                        Toast.makeText(activity, "Ya estás matriculado en esta asignatura", Toast.LENGTH_LONG).show()
                    }
                }
        })
        dialogBuilder.setNegativeButton("Cancelar", DialogInterface.OnClickListener {
                dialog, id -> dialog.cancel()
        })

        val alerta = dialogBuilder.create()
        alerta.show()
    }
}