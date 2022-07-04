package com.example.survy.Adapters

import android.graphics.Color
import android.os.Build
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.annotation.RequiresApi
import androidx.recyclerview.widget.RecyclerView
import com.example.survy.Clases.Encuesta
import com.example.survy.Clases.Resultado
import com.example.survy.R
import com.squareup.picasso.Picasso
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

/**
 * Adaptador de resultados para el usuario Alumno.
 */
class ResultadoAdapterAlumno (val mapaResultados : Map<Resultado, Encuesta>) : RecyclerView.Adapter<ResultadoAdapterAlumno.ResultadoViewHolder>()
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

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ResultadoViewHolder
    {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.resultado_list_view_alumno, parent, false)
        return ResultadoViewHolder(itemView, mListener)
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onBindViewHolder(holder: ResultadoViewHolder, position: Int)
    {
        val resultados = mapaResultados.keys.toList()
        val encuestas = mapaResultados.values.toList()

        val resultado = resultados.get(position)
        val encuesta = encuestas.get(position)

        Picasso.get().load(encuesta.icono).into(holder.ivIcono)
        holder.tvNombre.setText(encuesta.nombre)

        val date = LocalDateTime.parse(resultado.fecha)

        val fechaStr = date.format(DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm"))

        holder.tvFecha.setText(fechaStr)

        val nota = resultado.nota.replace(',', '.')

        if (nota.toDouble() >= 0.0 && nota.toDouble() < 4.0)
        {
            holder.tvNota.setText("Nota: " + nota.replace(".00", ""))
            holder.tvNota.setTextColor(Color.parseColor("#9E2C2C"))

        }
        else if (nota.toDouble() >= 5.0 && nota.toDouble() < 6.0)
        {
            holder.tvNota.setText("Nota: " + nota.replace(".00", ""))
            holder.tvNota.setTextColor(Color.parseColor("#DDAE21"))
        }
        else if (nota.toDouble() >= 6.0 && nota.toDouble() < 8.0)
        {
            holder.tvNota.setText("Nota: " + nota.replace(".00", ""))
            holder.tvNota.setTextColor(Color.parseColor("#FF9800"))
        }
        else if (nota.toDouble() >= 8.0 && nota.toDouble() < 10.0)
        {
            holder.tvNota.setText("Nota: " + nota.replace(".00", ""))
            holder.tvNota.setTextColor(Color.parseColor("#72A53D"))
        }
        else if (nota.toDouble() == 10.0)
        {
            holder.tvNota.setText("Nota: " + nota.replace(".00", ""))
            holder.tvNota.setTextColor(Color.parseColor("#2C8C2B"))
        }
    }

    override fun getItemCount(): Int
    {
        return mapaResultados.size
    }

    class ResultadoViewHolder(itemView : View, listener: onItemClickListener): RecyclerView.ViewHolder(itemView)
    {
        val ivIcono = itemView.findViewById<ImageView>(R.id.ivIconoEncuestaResultadoList)
        val tvNombre = itemView.findViewById<TextView>(R.id.tvNombreEncuestaResultadoList)
        val tvFecha = itemView.findViewById<TextView>(R.id.tvFechaEncuestaResultadoList)
        val tvNota = itemView.findViewById<TextView>(R.id.tvNotaEncuestaResultadoList)

        init
        {
            itemView.setOnClickListener {
                listener.onItemClick(adapterPosition)
            }
        }
    }
}