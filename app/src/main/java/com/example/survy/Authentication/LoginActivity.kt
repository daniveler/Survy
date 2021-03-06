package com.example.survy.Authentication

import android.content.DialogInterface
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AlertDialog
import com.example.survy.R
import com.google.firebase.auth.FirebaseAuth

/**
 * Activity de inicio de sesión en la aplicación.
 */
class LoginActivity : AppCompatActivity()
{
    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?)
    {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        val btEmail = findViewById<Button>(R.id.btEmailLogin)
        val btRegister = findViewById<Button>(R.id.btRegisterMail)

        val bundle = intent.extras
        val vieneDeRegistro = bundle?.getBoolean("vieneDeRegistro") ?: false
        if (vieneDeRegistro) { mostrarAlerta() }

        btEmail.setOnClickListener {
            val i = Intent(this, ElegirRolActivity::class.java)
            i.putExtra("previousIntent", "Login")
            startActivity(i)
        }

        btRegister.setOnClickListener {
            val i = Intent(this, RegisterActivity::class.java)
            startActivity(i)
        }
    }

    fun mostrarAlerta()
    {
        val dialogBuilder = AlertDialog.Builder(this)
        dialogBuilder.setTitle("Usuario registrado correctamente")
        dialogBuilder.setMessage("Se le ha enviado un correo electrónico de verificación. Será necesario verificar su email para iniciar sesión")
        dialogBuilder.setNegativeButton("OK", DialogInterface.OnClickListener{
                dialog, id -> dialog.cancel()
        })

        val alerta = dialogBuilder.create()
        alerta.show()
    }
}