package com.example.survy.Adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.survy.Clases.Pregunta
import com.example.survy.R

class PreguntaAdapterProfesor(var listaPreguntas: List<Pregunta>) : RecyclerView.Adapter<PreguntaAdapterProfesor.PreguntaViewHolder>()
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

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PreguntaViewHolder
    {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.pregunta_list_view_profesor, parent, false)
        return PreguntaViewHolder(itemView, mListener)
    }

    override fun onBindViewHolder(holder: PreguntaViewHolder, position: Int)
    {
        val pregunta = listaPreguntas[position]
        val numero = position + 1

        holder.tvNumero.setText(numero.toString())
        holder.tvTitulo.setText(pregunta.titulo)
    }

    override fun getItemCount(): Int
    {
        return listaPreguntas.size
    }

    class PreguntaViewHolder(itemView : View, listener: onItemClickListener): RecyclerView.ViewHolder(itemView)
    {
        val tvNumero    = itemView.findViewById<TextView>(R.id.tvNumeroPregunta)
        val tvTitulo    = itemView.findViewById<TextView>(R.id.tv1PreguntaList)

        init
        {
            itemView.setOnClickListener {
                listener.onItemClick(adapterPosition)
            }
        }

    }
}