package com.example.survy.Fragments.MisAsignaturas.Alumno

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.example.survy.R
import com.google.zxing.integration.android.IntentIntegrator

class LeerQrActivity : AppCompatActivity()
{
    override fun onCreate(savedInstanceState: Bundle?)
    {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_leer_qr)

        IntentIntegrator(this).initiateScan()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?)
    {
        val result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data)

        if (result != null)
        {
            if (result.contents == null)
            {
                Toast.makeText(this, "Cancelado", Toast.LENGTH_LONG).show()
            }
            else
            {
                Toast.makeText(this, "El código escaneado es: " + result.contents, Toast.LENGTH_LONG).show()
                finish()
            }
        }
        else
        {
            super.onActivityResult(requestCode, resultCode, data)
        }
    }
}