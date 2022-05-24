package com.example.survy.Authentication

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import com.example.survy.MainActivity
import com.example.survy.R
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

class RegisterEmailActivity : AppCompatActivity()
{
    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?)
    {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register_email)

        auth = Firebase.auth

        var etNombre = findViewById<EditText>(R.id.etNombre)
        var etApellidos = findViewById<EditText>(R.id.etApellidos)
        //var etCurso = findViewById<EditText>(R.id.etApellidos)
        var etEmail = findViewById<EditText>(R.id.etEmail)
        var etPassword = findViewById<EditText>(R.id.etPassword)

        var btRegister = findViewById<Button>(R.id.btRegister)


        btRegister.setOnClickListener{
            var nombre = etNombre.text.toString()
            var apellidos = etApellidos.text.toString()
            var email = etEmail.text.toString()
            val password = etPassword.text.toString()

            if (nombre.isBlank())
            {
                Toast.makeText(this, "Por favor, introduzca su nombre",
                    Toast.LENGTH_LONG).show()
            }
            else if (apellidos.isBlank())
            {
                Toast.makeText(this, "Por favor, introduzca sus apellidos",
                    Toast.LENGTH_LONG).show()
            }
            else if (email.isBlank())
            {
                Toast.makeText(this, "Por favor, introduzca su correo electónico",
                    Toast.LENGTH_LONG).show()
            }
            else if (password.isBlank())
            {
                Toast.makeText(this, "Por favor, introduzca su contraseña",
                    Toast.LENGTH_LONG).show()
            }
            else if (password.length < 6)
            {
                Toast.makeText(this, "La contraseña debe tener al menos 6 caracteres",
                    Toast.LENGTH_LONG).show()
            }
            else
            {
                auth.createUserWithEmailAndPassword(email, password)
                    .addOnCompleteListener(this) { task ->
                        if (task.isSuccessful) {
                            // Sign in success, update UI with the signed-in user's information
                            Toast.makeText(this, "createUserWithEmail:success",
                                Toast.LENGTH_LONG).show()

                            var intent = Intent(applicationContext, MainActivity::class.java)
                            startActivity(intent)

                            val user = auth.currentUser

                            //updateUI(user)
                        } else {
                            // If sign in fails, display a message to the user.
                            Toast.makeText(this, "Authentication failed.",
                                Toast.LENGTH_SHORT).show()
                            //updateUI(null)
                        }
                    }
            }
        }
    }
}