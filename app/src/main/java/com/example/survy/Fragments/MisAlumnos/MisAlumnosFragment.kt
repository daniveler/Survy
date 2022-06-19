package com.example.survy.Fragments.MisAlumnos

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import com.example.survy.Fragments.MisAsignaturas.Profesor.MisAsignaturasFragmentProfesor
import com.example.survy.R

class MisAlumnosFragment : Fragment()
{
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View?
    {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_mis_alumnos, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?)
    {
        super.onViewCreated(view, savedInstanceState)

        val btMatricularAlumnos = view.findViewById<Button>(R.id.btMatricularAlumnosMisAlumnosProfesor)
        val btBuscarAlumno = view.findViewById<Button>(R.id.btBuscarAlumnoMisAlumnosProfesor)

        val idUsuario = arguments?.getString("idUsuario", "") ?: ""

        btMatricularAlumnos.setOnClickListener {
            cambiarFragment(MisAsignaturasFragmentProfesor(), idUsuario, "MisAlumnos")
        }

        btBuscarAlumno.setOnClickListener {
            cambiarFragment(BuscarAlumnoFragmentProfesor(), idUsuario, "MisAlumnos")
        }
    }

    fun cambiarFragment(framentCambiar: Fragment, idUsuario: String, vieneDe: String)
    {
        var args = Bundle()
        args.putString("idUsuario", idUsuario)
        args.putString("vieneDe", vieneDe)

        var fragment = framentCambiar
        fragment.arguments = args

        var fragmentManager = requireActivity().supportFragmentManager

        fragmentManager.beginTransaction()
            .replace(R.id.fragmentContainerProfesor, fragment)
            .commit()
    }
}