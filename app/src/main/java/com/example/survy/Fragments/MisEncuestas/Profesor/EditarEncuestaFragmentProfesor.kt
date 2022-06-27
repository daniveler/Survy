package com.example.survy.Fragments.MisEncuestas.Profesor

import android.net.Uri
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.survy.Adapters.GridIconosAdapter
import com.example.survy.Fragments.MisAsignaturas.Profesor.AsignaturaDetailFragmentProfesor
import com.example.survy.R
import com.google.android.gms.tasks.Task
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ListResult
import com.google.firebase.storage.StorageReference
import com.google.firebase.storage.ktx.storage
import com.squareup.picasso.Picasso
import de.hdodenhof.circleimageview.CircleImageView

class EditarEncuestaFragmentProfesor : Fragment()
{
    private val db = FirebaseFirestore.getInstance()
    private val storage = Firebase.storage

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View?
    {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_editar_encuesta_profesor, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?)
    {
        super.onViewCreated(view, savedInstanceState)

        val etNombre            = view.findViewById<EditText>(R.id.etNombreEditarEncuestaProfesor)
        val etDescripcion       = view.findViewById<EditText>(R.id.etDescripcionEditarEncuestaProfesor)
        val civIcono            = view.findViewById<CircleImageView>(R.id.civIconoEditarEncuestaProfesor)

        val btGuardarCambios    = view.findViewById<Button>(R.id.btGuardarCambiosEditarEncuestaProfesor)
        val btCancelar          = view.findViewById<Button>(R.id.btCancelarEditarEncuestaProfesor)

        val rvIconos            = view.findViewById<RecyclerView>(R.id.rvIconosEditarEncuestaProfesor)

        val btGuardarIcono      = view.findViewById<Button>(R.id.btGuardarIconoEditarEncuestaProfesor)
        val btCancelarIcono      = view.findViewById<Button>(R.id.btCancelarGuardarIconoEditarEncuestaProfesor)

        val idUsuario = arguments?.getString("idUsuario") ?: ""
        val idEncuesta = arguments?.getString("idEncuesta") ?: ""

        var iconoActual = Uri.parse("")

        db.collection("encuestas")
            .document(idEncuesta)
            .get()
            .addOnSuccessListener {
                etNombre.setText(it.data?.get("nombre").toString())
                etDescripcion.setText(it.get("descripcion").toString())
                iconoActual = Uri.parse(it.data?.get("icono").toString())
                Picasso.get().load(it.getString("icono")).into(civIcono)
            }

        btGuardarCambios.setOnClickListener {
            val nombre = etNombre.text.toString()
            val curso = etDescripcion.text.toString()
            val icono = iconoActual

            db.collection("encuestas").document(idEncuesta).update("nombre", nombre)
            db.collection("encuestas").document(idEncuesta).update("descripcion", curso)
            db.collection("encuestas").document(idEncuesta).update("icono", icono)

            cambiarFragment(EncuestaDetailFragmentProfesor(), idEncuesta)
        }

        btCancelar.setOnClickListener {
            cambiarFragment(EncuestaDetailFragmentProfesor(), idEncuesta)
        }

        civIcono.setOnClickListener {
            var iconoAntiguo = civIcono.drawable

            etNombre.visibility = View.GONE
            etDescripcion.visibility = View.GONE
            btGuardarCambios.visibility = View.GONE
            btCancelar.visibility = View.GONE

            rvIconos.visibility = View.VISIBLE
            btGuardarIcono.visibility = View.VISIBLE
            btCancelarIcono.visibility = View.VISIBLE

            var storageRef = storage.reference
            var iconosRef = storageRef.child("iconfinder_pack")

            val listaIconos: ArrayList<Uri> = ArrayList()

            val listAllTask: Task<ListResult> = iconosRef.listAll()
            listAllTask.addOnCompleteListener { result ->
                val items: List<StorageReference> = result.result!!.items

                items.forEachIndexed { index, item ->
                    item.downloadUrl.addOnSuccessListener {
                        listaIconos.add(it)
                    }.addOnCompleteListener {
                        var adapter = GridIconosAdapter(listaIconos)

                        rvIconos.layoutManager = GridLayoutManager(context, 3)
                        rvIconos.setHasFixedSize(true)
                        rvIconos.adapter = adapter

                        adapter.setOnItemClickListener(object: GridIconosAdapter.onItemClickListener{
                            override fun onItemClick(position: Int)
                            {
                                iconoActual = listaIconos.get(position)
                                Picasso.get().load(iconoActual).into(civIcono)
                            }
                        })

                        btGuardarIcono.setOnClickListener {
                            etNombre.visibility = View.VISIBLE
                            etDescripcion.visibility = View.VISIBLE
                            btGuardarCambios.visibility = View.VISIBLE
                            btCancelar.visibility = View.VISIBLE

                            rvIconos.visibility = View.GONE
                            btGuardarIcono.visibility = View.GONE
                            btCancelarIcono.visibility = View.GONE
                        }

                        btCancelarIcono.setOnClickListener {
                            etNombre.visibility = View.VISIBLE
                            etDescripcion.visibility = View.VISIBLE
                            btGuardarCambios.visibility = View.VISIBLE
                            btCancelar.visibility = View.VISIBLE

                            rvIconos.visibility = View.GONE
                            btGuardarIcono.visibility = View.GONE
                            btCancelarIcono.visibility = View.GONE

                            civIcono.setImageDrawable(iconoAntiguo)
                        }
                    }
                }
            }
        }
    }

    fun cambiarFragment(framentCambiar: Fragment, idEncuesta: String?)
    {
        var args = Bundle()
        args.putString("idEncuesta", idEncuesta)

        var fragment = framentCambiar
        fragment.arguments = args

        var fragmentManager = requireActivity().supportFragmentManager

        fragmentManager.beginTransaction()
            .replace(R.id.fragmentContainerProfesor, fragment)
            .commit()
    }
}