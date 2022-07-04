package com.example.survy.Adapters

import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.survy.Clases.Asignatura
import com.example.survy.R
import com.squareup.picasso.Picasso

/**
 * Adaptador de asignaturas para el menú Home del usuario Profesor.
 */
class AsignaturaAdapterHome (val listaAsignaturas : List<Asignatura>) : RecyclerView.Adapter<AsignaturaAdapterHome.AsignaturaViewHolder>()
{
    private lateinit var mListener : onItemClickListener

    interface onItemClickListener
    {
        fun onItemClick(position: Int)
    }

    fun setOnItemClickListener (listener: onItemClickListener)
    {
        mListener = listener
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AsignaturaViewHolder
    {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.asignatura_home_view, parent, false)
        return AsignaturaViewHolder(itemView, mListener)
    }

    override fun onBindViewHolder(holder: AsignaturaViewHolder, position: Int)
    {
        val asignatura = listaAsignaturas[position]
        Picasso.get().load(Uri.parse(asignatura.icono.toString())).into(holder.ivIcono)
        holder.tvNombre.setText(asignatura.nombre)
        holder.tvCurso.setText(asignatura.curso)
    }

    override fun getItemCount(): Int
    {
        return listaAsignaturas.size
    }

    class AsignaturaViewHolder(itemView : View, listener: onItemClickListener): RecyclerView.ViewHolder(itemView)
    {
        val ivIcono = itemView.findViewById<ImageView>(R.id.ivIconoAsignaturaHomeView)
        val tvNombre = itemView.findViewById<TextView>(R.id.tvNombreAsignaturaHomeView)
        val tvCurso = itemView.findViewById<TextView>(R.id.tvCursoAsignaturaHomeView)

        init
        {
            itemView.setOnClickListener {
                listener.onItemClick(adapterPosition)
            }
        }
    }
}