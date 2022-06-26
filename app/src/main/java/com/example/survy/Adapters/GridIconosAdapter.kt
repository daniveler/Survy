package com.example.survy.Adapters

import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.example.survy.R
import com.squareup.picasso.Picasso

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
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.icon_item_grid, parent, false)
        return GridIconosViewHolder(itemView, mListener)
    }

    override fun onBindViewHolder(holder: GridIconosViewHolder, position: Int)
    {
        val icono = listaIconos[position]

        Picasso.get().load(icono).into(holder.ivIcono)
    }

    override fun getItemCount(): Int
    {
        return listaIconos.size
    }

    class GridIconosViewHolder(itemView : View, listener: onItemClickListener): RecyclerView.ViewHolder(itemView)
    {
        val ivIcono = itemView.findViewById<ImageView>(R.id.ivIconoItemGrid)

        init
        {
            itemView.setOnClickListener {
                listener.onItemClick(adapterPosition)
            }
        }
    }
}