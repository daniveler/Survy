package com.example.survy.Fragments.MisAsignaturas.Profesor

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.survy.MainActivityAlumno
import com.example.survy.R
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import java.util.*
import kotlin.concurrent.schedule

class MisAsignaturasFragmentProfesor : Fragment()
{
    private val db = FirebaseFirestore.getInstance()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View?
    {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_mis_asignaturas_profesor, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?)
    {
        super.onViewCreated(view, savedInstanceState)

        val user = FirebaseAuth.getInstance().currentUser

        if (user != null)
        {
            val userEmail = user.email

            db.collection("asignaturas").document(userEmail!!).get().addOnCompleteListener {
                if (it.isSuccessful)
                {
                    val document = it.result
                    if (!document.exists())
                    {

                    } else
                    {

                    }
                }
            }
        }
    }
}