package com.example.survy.Clases

import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.survy.R

class GridIconosAdapter(val listaIconos : List<Uri>) : RecyclerView.Adapter<GridIconosAdapter.GridIconosViewHolder>()
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

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): GridIconosViewHolder
    {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.asignatura_list_view_profesor, parent, false)
        return GridIconosViewHolder(itemView, mListener)
    }

    override fun onBindViewHolder(holder: GridIconosViewHolder, position: Int)
    {
        val icono = listaIconos[position]

        holder.ivIcono.setImageURI(Uri.parse(icono.toString()))
    }

    override fun getItemCount(): Int
    {
        return listaIconos.size
    }

    class GridIconosViewHolder(itemView : View, listener: onItemClickListener): RecyclerView.ViewHolder(itemView)
    {
        val ivIcono = itemView.findViewById<ImageView>(R.id.ivIconoAsignaturaList)

        init
        {
            itemView.setOnClickListener {
                listener.onItemClick(adapterPosition)
            }
        }
    }
}