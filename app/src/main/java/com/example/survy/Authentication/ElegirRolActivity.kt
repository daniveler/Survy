package com.example.survy.Authentication

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import com.example.survy.R

class ElegirRolActivity : AppCompatActivity()
{
    override fun onCreate(savedInstanceState: Bundle?)
    {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_elegir_rol)

        val btAlumno = findViewById<Button>(R.id.btAlumnoElegirRol)
        val btProfesor = findViewById<Button>(R.id.btProfesorElegirRol)

        val previousIntent = intent.getStringExtra("previousIntent")

        btAlumno.setOnClickListener {
            if (previousIntent == "Login")
            {
                var intent = Intent(this, LoginEmailActivity::class.java)
                intent.putExtra("rol", "Alumno")
                startActivity(intent)
            }
            else if (previousIntent == "Register")
            {
                var intent = Intent(this, RegisterEmailActivity::class.java)
                intent.putExtra("rol", "Alumno")
                startActivity(intent)
            }
        }

        btProfesor.setOnClickListener {
            if (previousIntent == "Login")
            {
                var intent = Intent(this, LoginEmailActivity::class.java)
                intent.putExtra("rol", "Profesor")
                startActivity(intent)
            }
            else if (previousIntent == "Register")
            {
                var intent = Intent(this, RegisterEmailActivity::class.java)
                intent.putExtra("rol", "Profesor")
                startActivity(intent)
            }
        }
    }
}