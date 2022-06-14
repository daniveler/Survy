package com.example.survy.Clases

import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.survy.R

class AsignaturaAdapter(val listaAsignaturas : List<Asignatura>) : RecyclerView.Adapter<AsignaturaAdapter.AsignaturaViewHolder>()
{
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AsignaturaViewHolder
    {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.asignatura_list_view_profesor, parent, false)
        return AsignaturaViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: AsignaturaViewHolder, position: Int)
    {
        val asignatura = listaAsignaturas[position]
        holder.ivFoto.setImageURI(Uri.parse(asignatura.imagen.toString()))
        holder.tvTitulo.setText(asignatura.nombre)
        holder.tvCurso.setText(asignatura.curso)
        holder.tvAlumnos.setText(asignatura.numAlumnos)
    }

    override fun getItemCount(): Int
    {
        return listaAsignaturas.size
    }

    class AsignaturaViewHolder(itemView : View) : RecyclerView.ViewHolder(itemView)
    {
        val ivFoto = itemView.findViewById<ImageView>(R.id.ivIconoAsignaturaList)
        val tvTitulo = itemView.findViewById<TextView>(R.id.tv1AsignaturaList)
        val tvCurso = itemView.findViewById<TextView>(R.id.tv2AsignaturaList)
        val tvAlumnos = itemView.findViewById<TextView>(R.id.tv3AsignaturaList)
    }
}