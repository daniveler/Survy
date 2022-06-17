package com.example.survy.Authentication

import android.content.DialogInterface
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import com.example.survy.MainActivityAlumno
import com.example.survy.MainActivityProfesor
import com.example.survy.R
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.ktx.Firebase

class LoginEmailActivity : AppCompatActivity()
{
    private lateinit var auth: FirebaseAuth
    private val db = FirebaseFirestore.getInstance()

    override fun onCreate(savedInstanceState: Bundle?)
    {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login_email)

        auth = Firebase.auth

        var etEmail = findViewById<EditText>(R.id.etEmailLogin)
        var etPassword = findViewById<EditText>(R.id.etPasswordLogin)
        var btLogin = findViewById<Button>(R.id.btLogin)

        val bundle = intent.extras
        val rol = bundle?.getString("rol")

        if (rol == "Alumno")
        {
            etEmail.setText("daniel01velerdas@gmail.com")
        } else
        {
            etEmail.setText("daniveler@usal.es")
        }

        etPassword.setText("dani2000")

        btLogin.setOnClickListener {
            if (etEmail.text.isBlank())
            {
                Toast.makeText(
                    this, "Por favor, introduzca su correo electónico",
                    Toast.LENGTH_LONG
                ).show()
            } else if (etPassword.text.isBlank())
            {
                Toast.makeText(
                    this, "Por favor, introduzca su contraseña",
                    Toast.LENGTH_LONG
                ).show()
            } else
            {
                var email = etEmail.text.toString().lowercase()
                var password = etPassword.text.toString()

                if (rol == "Alumno")
                {
                    db.collection("alumnos")
                        .whereEqualTo("email", email)
                        .get().addOnSuccessListener {
                            if (!it.isEmpty)
                            {
                                var id = it.documents.get(0).id

                                auth.signInWithEmailAndPassword(email, password)
                                    .addOnCompleteListener(this) { task ->
                                        if (task.isSuccessful)
                                        {
                                            val user = auth.currentUser

                                            if (user!!.isEmailVerified)
                                            {
                                                var i = Intent(this, MainActivityAlumno::class.java)
                                                i.putExtra("idUsuario", id)
                                                startActivity(i)
                                            } else
                                            {
                                                Toast.makeText(
                                                    this,
                                                    "El usuario aún no ha sido verificado. Por favor, consulte su email",
                                                    Toast.LENGTH_LONG
                                                ).show()
                                            }
                                        }
                                        else
                                        {
                                            Toast.makeText(
                                                this, "El usuario o contraseña son incorrectos",
                                                Toast.LENGTH_SHORT
                                            ).show()
                                        }
                                    }
                            }
                            else
                            {
                                Toast.makeText(
                                    this, "Error al iniciar sesión",
                                    Toast.LENGTH_SHORT
                                ).show()
                            }


                        }
                } else if (rol == "Profesor")
                {
                    db.collection("profesores")
                        .whereEqualTo("email", email)
                        .get().addOnSuccessListener {
                            if (!it.isEmpty)
                            {
                                var id = it.documents.get(0).id

                                auth.signInWithEmailAndPassword(email, password)
                                    .addOnCompleteListener(this) { task ->
                                        if (task.isSuccessful)
                                        {
                                            val user = auth.currentUser

                                            if (user!!.isEmailVerified)
                                            {
                                                var i = Intent(this, MainActivityProfesor::class.java)
                                                i.putExtra("idUsuario", id)
                                                startActivity(i)
                                            } else
                                            {
                                                Toast.makeText(
                                                    this,
                                                    "El usuario aún no ha sido verificado. Por favor, consulte su email",
                                                    Toast.LENGTH_LONG
                                                ).show()
                                            }
                                        } else
                                        {
                                            Toast.makeText(
                                                this, "El usuario o contraseña son incorrectos",
                                                Toast.LENGTH_SHORT
                                            ).show()
                                        }
                                    }
                            }
                            else
                            {
                                Toast.makeText(
                                    this, "Error al iniciar sesión",
                                    Toast.LENGTH_SHORT
                                ).show()
                            }
                        }
                }
            }
        }
    }
}