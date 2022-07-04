package com.example.survy.Adapters

import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.survy.Clases.Encuesta
import com.example.survy.R
import com.squareup.picasso.Picasso

/**
 * Adaptador de encuestas, tanto para el usuario Alumno como Profesor.
 */
class EncuestaAdapter (var listaEncuestas: List<Encuesta>) : RecyclerView.Adapter<EncuestaAdapter.EncuestaViewHolderProfesor>()
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

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): EncuestaViewHolderProfesor
    {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.encuesta_list_view, parent, false)
        return EncuestaViewHolderProfesor(itemView, mListener)
    }

    override fun onBindViewHolder(holder: EncuestaViewHolderProfesor, position: Int)
    {
        val encuesta = listaEncuestas[position]

        Picasso.get().load(Uri.parse(encuesta.icono)).into(holder.ivIcono)
        holder.tvNombre.setText(encuesta.nombre)
        holder.tvDescripcion.setText(encuesta.descripcion)
    }

    override fun getItemCount(): Int
    {
        return listaEncuestas.size
    }

    class EncuestaViewHolderProfesor(itemView : View, listener: onItemClickListener): RecyclerView.ViewHolder(itemView)
    {
        val ivIcono         = itemView.findViewById<ImageView>(R.id.ivIconoEncuestaList)
        val tvNombre        = itemView.findViewById<TextView>(R.id.tv1EncuestaList)
        val tvDescripcion   = itemView.findViewById<TextView>(R.id.tv3EncuestaList)

        init
        {
            itemView.setOnClickListener {
                listener.onItemClick(adapterPosition)
            }
        }
    }
}