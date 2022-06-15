package com.example.survy.Clases

import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.survy.R

class AsignaturaAdapter(val listaAsignaturas : List<Asignatura>) : RecyclerView.Adapter<AsignaturaAdapter.AsignaturaViewHolder>()
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
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.asignatura_list_view_profesor, parent, false)
        return AsignaturaViewHolder(itemView, mListener)
    }

    override fun onBindViewHolder(holder: AsignaturaViewHolder, position: Int)
    {
        val asignatura = listaAsignaturas[position]
        holder.ivFoto.setImageURI(Uri.parse(asignatura.icono.toString()))
        holder.tvTitulo.setText(asignatura.nombre)
        holder.tvCurso.setText(asignatura.curso)
    }

    override fun getItemCount(): Int
    {
        return listaAsignaturas.size
    }

    class AsignaturaViewHolder(itemView : View, listener: onItemClickListener): RecyclerView.ViewHolder(itemView)
    {
        val ivFoto = itemView.findViewById<ImageView>(R.id.ivIconoAsignaturaList)
        val tvTitulo = itemView.findViewById<TextView>(R.id.tv1AsignaturaList)
        val tvCurso = itemView.findViewById<TextView>(R.id.tv2AsignaturaList)
        val tvAlumnos = itemView.findViewById<TextView>(R.id.tv3AsignaturaList)

        init
        {
            itemView.setOnClickListener {
                listener.onItemClick(adapterPosition)
            }
        }
    }
}