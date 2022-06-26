package com.example.survy.Fragments.MisEncuestas.Alumno

import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.os.VibrationEffect
import android.os.Vibrator
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AlertDialog
import com.example.survy.R
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.zxing.integration.android.IntentIntegrator

class NuevaEncuestaFragmentAlumno : Fragment()
{
    private val db = FirebaseFirestore.getInstance()
    private lateinit var auth: FirebaseAuth

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View?
    {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_nueva_encuesta_alumno, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?)
    {
        super.onViewCreated(view, savedInstanceState)

        auth = FirebaseAuth.getInstance()

        val integrator = IntentIntegrator.forSupportFragment(this)

        integrator.setDesiredBarcodeFormats(IntentIntegrator.QR_CODE)
        integrator.setPrompt(getString(R.string.tvEscanerNuevaEncuestaAlumno))
        integrator.setBeepEnabled(true)

        integrator.initiateScan()
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?)
    {
        val result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data)

        if (result != null)
        {
            if (result.contents != null)
            {
                val vibrator = context?.getSystemService(Context.VIBRATOR_SERVICE) as Vibrator
                vibrator.vibrate(
                    VibrationEffect.createOneShot(
                        200,
                        VibrationEffect.DEFAULT_AMPLITUDE
                    )
                )

                /*db.collection("asignaturas").document(result.contents)
                    .get().addOnSuccessListener {
                        val nombre = it.getString("nombre") as String

                        mostrarAlerta(nombre, result.contents)
                    }*/
            }
        } else
        {
            super.onActivityResult(requestCode, resultCode, data)
        }
    }

    fun mostrarAlerta(nombre: String, idAsignatura: String)
    {
        val dialogBuilder = AlertDialog.Builder(requireContext())
        dialogBuilder.setTitle("Añadir Asignatura")
        dialogBuilder.setMessage("¿Deseas matricularte en la asignatura " + nombre + "?")
        dialogBuilder.setPositiveButton("Sí", DialogInterface.OnClickListener {
                dialog, id ->
                    dialog.cancel()
            /*val idAlumno = auth.currentUser!!.uid

            db.collection("matriculado")
                .whereEqualTo("idAsignatura", idAsignatura)
                .whereEqualTo("idAlumno", idAlumno)
                .get()
                .addOnCompleteListener { task ->
                    if (task.result.isEmpty)
                    {
                        db.collection("asignaturas").document(idAsignatura).get()
                            .addOnSuccessListener {
                                val cursoAsignatura = it.getString("curso")
                                var numAlumnos = it.getLong("numAlumnos")!!.toInt()

                                db.collection("alumnos").document(idAlumno).get()
                                    .addOnSuccessListener {
                                        val cursoAlumno = it.getString("curso")

                                        if (cursoAlumno == cursoAsignatura)
                                        {
                                            db.collection("matriculado").document().set(
                                                hashMapOf("idAlumno" to idAlumno,
                                                    "idAsignatura" to idAsignatura))
                                                .addOnSuccessListener {
                                                    Toast.makeText(activity, "Te has matriculado correctamente", Toast.LENGTH_LONG).show()

                                                    db.collection("asignaturas").document(idAsignatura).update("numAlumnos", ++numAlumnos)

                                                    cambiarFragment(MisAsignaturasFragmentAlumno(), idAlumno, null)
                                                }
                                        }
                                        else
                                        {
                                            Toast.makeText(activity, "No puedes matricularte en esta asignatura porque no eres de este curso",
                                                Toast.LENGTH_LONG).show()
                                        }
                                    }
                            }
                    }
                    else
                    {
                        Toast.makeText(activity, "Ya estás matriculado en esta asignatura", Toast.LENGTH_LONG).show()
                    }
                }*/
        })
        dialogBuilder.setNegativeButton("Cancelar", DialogInterface.OnClickListener {
                dialog, id -> dialog.cancel()
        })

        val alerta = dialogBuilder.create()
        alerta.show()
    }
}