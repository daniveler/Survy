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

        etPassword.setText("dani2000")

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
                var email = etEmail.text.toString().lowercase()
                var password = etPassword.text.toString()

                auth.signInWithEmailAndPassword(email, password)
                    .addOnCompleteListener(this) { task ->
                        if (task.isSuccessful)
                        {
                            // Sign in success, update UI with the signed-in user's information

                            val user = auth.currentUser

                            if (user!!.isEmailVerified)
                            {
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
                                Toast.makeText(this, "El usuario aún no ha sido verificado. Por favor, consulte su email",
                                    Toast.LENGTH_LONG).show()
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