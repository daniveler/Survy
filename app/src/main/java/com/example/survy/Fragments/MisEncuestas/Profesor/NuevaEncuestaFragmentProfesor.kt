package com.example.survy.Fragments.MisEncuestas.Profesor

import android.net.Uri
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.survy.Adapters.GridIconosAdapter
import com.example.survy.Fragments.MisAsignaturas.Profesor.MisAsignaturasFragmentProfesor
import com.example.survy.R
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ListResult
import com.google.firebase.storage.StorageReference
import com.google.firebase.storage.ktx.storage
import com.squareup.picasso.Picasso
import de.hdodenhof.circleimageview.CircleImageView

class NuevaEncuestaFragmentProfesor : Fragment()
{
    private val db = FirebaseFirestore.getInstance()
    private val storage = Firebase.storage

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View?
    {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_nueva_encuesta_profesor, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?)
    {
        super.onViewCreated(view, savedInstanceState)

        val etNombre        = view.findViewById<EditText>(R.id.etNombreNuevaEncuestaProfesor)
        val etDescripcion   = view.findViewById<EditText>(R.id.etDescripcionNuevaEncuestaProfesor)
        val civIcono        = view.findViewById<CircleImageView>(R.id.civFotoNuevaEncuestaProfesor)

        val rvIconos        = view.findViewById<RecyclerView>(R.id.rvIconosNuevaEncuestaProfesor)

        val btCrearEncuesta = view.findViewById<Button>(R.id.btCrearNuevaEncuestaProfesor)
        val btCancelar      = view.findViewById<Button>(R.id.btCancelarNuevaEncuestaProfesor)

        val btGuardarIcono  = view.findViewById<Button>(R.id.btGuardarIconoNuevaEncuestaProfesor)
        val btCancelarIcono = view.findViewById<Button>(R.id.btCancelarGuardarIconoNuevaEncuestaProfesor)

        val idUsuario = arguments?.getString("idUsuario") ?: ""
        val idAsignatura = arguments?.getString("idAsignatura") ?: ""

        var iconoAntiguo = Uri.parse("android.resource://" + requireActivity().packageName +
                "/" + resources.getResourceTypeName(R.drawable.survy_logo) +
                "/" + resources.getResourceEntryName(R.drawable.survy_logo))

        var iconoActual = iconoAntiguo

        civIcono.setOnClickListener {
            var iconoAntiguo = civIcono.drawable

            etNombre.visibility = View.GONE
            etDescripcion.visibility = View.GONE
            btCrearEncuesta.visibility = View.GONE
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

                        adapter.setOnItemClickListener(object: GridIconosAdapter.onItemClickListener {
                            override fun onItemClick(position: Int)
                            {
                                iconoActual = listaIconos.get(position)
                                Picasso.get().load(iconoActual).into(civIcono)
                            }
                        })

                        btGuardarIcono.setOnClickListener {
                            etNombre.visibility = View.VISIBLE
                            etDescripcion.visibility = View.VISIBLE
                            btCrearEncuesta.visibility = View.VISIBLE
                            btCancelar.visibility = View.VISIBLE

                            rvIconos.visibility = View.GONE
                            btGuardarIcono.visibility = View.GONE
                            btCancelarIcono.visibility = View.GONE
                        }

                        btCancelarIcono.setOnClickListener {
                            etNombre.visibility = View.VISIBLE
                            etDescripcion.visibility = View.VISIBLE
                            btCrearEncuesta.visibility = View.VISIBLE
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

        btCrearEncuesta.setOnClickListener {
            val nombre = etNombre.text.toString()
            val descripcion = etDescripcion.text.toString()
            val icono = iconoActual

            if (nombre.isBlank())
            {
                Toast.makeText(context, "Por favor, introduzca el nombre de la asignatura",
                    Toast.LENGTH_LONG).show()
            }
            else if (descripcion.isBlank())
            {
                Toast.makeText(context, "Por favor, introduzca la descripción de la asignatura",
                    Toast.LENGTH_LONG).show()
            }
            else if (icono == iconoAntiguo)
            {
                Toast.makeText(context, "Por favor, seleccione un icono",
                    Toast.LENGTH_LONG).show()
            }
            else
            {
                var dataEncuesta = hashMapOf("nombre" to nombre,
                    "idAsignatura" to idAsignatura,
                    "descripcion" to descripcion,
                    "icono" to icono
                )

                db.collection("encuestas").add(dataEncuesta)

                Toast.makeText(context, "Encuesta creada correctamente",
                    Toast.LENGTH_LONG).show()

                cambiarFragment(MisEncuestasFragmentProfesor(), idUsuario, idAsignatura)
            }
        }

        btCancelar.setOnClickListener {
            cambiarFragment(MisEncuestasFragmentProfesor(), idUsuario, idAsignatura)
        }
    }

    fun cambiarFragment(framentCambiar: Fragment, idUsuario: String, idAsignatura: String)
    {
        var args = Bundle()
        args.putString("idUsuario", idUsuario)
        args.putString("idAsignatura", idAsignatura)

        var fragment = framentCambiar
        fragment.arguments = args

        var fragmentManager = requireActivity().supportFragmentManager

        fragmentManager.beginTransaction()
            .replace(R.id.fragmentContainerProfesor, fragment)
            .commit()
    }
}