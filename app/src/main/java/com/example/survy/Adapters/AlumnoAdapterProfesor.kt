package com.example.survy.Adapters

import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.survy.Clases.Alumno
import com.example.survy.R
import com.squareup.picasso.Picasso
import de.hdodenhof.circleimageview.CircleImageView

/**
 * Adaptador de alumnos para el menú Mis Alumnos del usuario Profesor.
 */
class AlumnoAdapterProfesor (var listaAlumnos: List<Alumno>) : RecyclerView.Adapter<AlumnoAdapterProfesor.AlumnoViewHolder>()
{
    private lateinit var mListener : onItemClickListener

    interface onItemClickListener
    {
        fun onItemClick(position: Int)
    }

    fun setOnClickListener (listener: onItemClickListener)
    {
        mListener = listener
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AlumnoViewHolder
    {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.alumnos_list_view_profesor, parent, false)
        return AlumnoViewHolder(itemView, mListener)
    }

    override fun onBindViewHolder(holder: AlumnoViewHolder, position: Int)
    {
        val alumno = listaAlumnos[position]
        Picasso.get().load(Uri.parse(alumno.fotoDePerfil.toString())).into(holder.civFotoDePerfil)
        holder.tvNombreApellidos.setText(alumno.nombre + " " + alumno.apellidos)
        holder.tvCurso.setText(alumno.curso)
    }

    override fun getItemCount(): Int
    {
        return listaAlumnos.size
    }

    class AlumnoViewHolder(itemView : View, listener: onItemClickListener): RecyclerView.ViewHolder(itemView)
    {
        val civFotoDePerfil = itemView.findViewById<CircleImageView>(R.id.civAlumnoListViewProfesor)
        val tvNombreApellidos = itemView.findViewById<TextView>(R.id.tv1AlumnoListViewProfesor)
        val tvCurso = itemView.findViewById<TextView>(R.id.tv2AlumnoListViewProfesor)

        init
        {
            itemView.setOnClickListener {
                listener.onItemClick(adapterPosition)
            }
        }

    }
}