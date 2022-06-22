package com.example.survy.Fragments.MisAsignaturas.Alumno

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContract
import androidx.activity.result.contract.ActivityResultContracts
import com.example.survy.R
import com.google.zxing.BarcodeFormat
import com.google.zxing.client.android.BeepManager
import com.journeyapps.barcodescanner.BarcodeCallback
import com.journeyapps.barcodescanner.BarcodeView

class MatricularAlumnoFragmentAlumno : Fragment()
{
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View?
    {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_matricular_alumno_alumno, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?)
    {
        super.onViewCreated(view, savedInstanceState)

        /*val beepManager = BeepManager(activity)

        val barcodeView = view.findViewById<BarcodeView>(R.id.bvMatricularAlumnoAlumno)

        barcodeView.resume()

        val callback = BarcodeCallback { result ->
            if (result == null)
                return@BarcodeCallback
            else
            {
                val codigoLeido = result.text

                beepManager.playBeepSoundAndVibrate()

                Toast.makeText(context, "Código leído: " + codigoLeido, Toast.LENGTH_LONG).show()
            }
        }

        barcodeView.decodeContinuous(callback)*/
    }
}