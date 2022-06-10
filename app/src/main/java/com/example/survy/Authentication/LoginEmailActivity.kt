package com.example.survy.Authentication

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import com.example.survy.MainActivityAlumno
import com.example.survy.MainActivityProfesor
import com.example.survy.R
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

class LoginEmailActivity : AppCompatActivity()
{
    private lateinit var auth: FirebaseAuth

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


        if (rol == "Alumno") { etEmail.setText("daniel01velerdas@gmail.com") }
        else { etEmail.setText("daniveler@usal.es") }

        etPassword.setText("20001000")

        btLogin.setOnClickListener {
            if(etEmail.text.isBlank())
            {
                Toast.makeText(this, "Por favor, introduzca su correo electónico",
                    Toast.LENGTH_LONG).show()
            }
            else if (etPassword.text.isBlank())
            {
                Toast.makeText(this, "Por favor, introduzca su contraseña",
                    Toast.LENGTH_LONG).show()
            }
            else
            {
                var email = etEmail.text.toString()
                var password = etPassword.text.toString()

                auth.signInWithEmailAndPassword(email, password)
                    .addOnCompleteListener(this) { task ->
                        if (task.isSuccessful)
                        {
                            // Sign in success, update UI with the signed-in user's information
                            Toast.makeText(this, "signInUserWithEmail:success",
                                Toast.LENGTH_LONG).show()

                            var user = auth.currentUser

                            if (rol == "Alumno")
                            {
                                var i = Intent(this, MainActivityAlumno::class.java)
                                i.putExtra("email", email)
                                startActivity(i)
                            }
                            else if (rol == "Profesor")
                            {
                                var i = Intent(this, MainActivityProfesor::class.java)
                                i.putExtra("email", email)
                                startActivity(i)
                            }
                        }
                        else
                        {
                            // If sign in fails, display a message to the user.
                            Toast.makeText(this, "El usuario o contraseña son incorrectos",
                                Toast.LENGTH_SHORT).show()
                        }
                }
            }
        }
    }
}