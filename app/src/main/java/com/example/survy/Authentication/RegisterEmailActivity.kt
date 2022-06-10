package com.example.survy.Authentication

import android.content.Intent
import android.graphics.Color
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import android.widget.*
import com.example.survy.MainActivityAlumno
import com.example.survy.MainActivityProfesor
import com.example.survy.R
import com.example.survy.Utils.Funciones
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.ktx.Firebase

class RegisterEmailActivity : AppCompatActivity()
{
    private lateinit var auth: FirebaseAuth
    private val db = FirebaseFirestore.getInstance()

    override fun onCreate(savedInstanceState: Bundle?)
    {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register_email)

        auth = Firebase.auth

        var etNombre = findViewById<EditText>(R.id.etNombreRegister)
        var etApellidos = findViewById<EditText>(R.id.etApellidosRegister)
        var spinnerCursos = findViewById<Spinner>(R.id.spinnerRegister)
        var etEmail = findViewById<EditText>(R.id.etEmailRegister)
        var etPassword = findViewById<EditText>(R.id.etPasswordRegister)

        var btRegister = findViewById<Button>(R.id.btRegister)

        val bundle = intent.extras
        val rol = bundle?.getString("rol")

        val cursos = resources.getStringArray(R.array.cursos)
        val spinnerAdapter = object  : ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, cursos)
        {
            override fun isEnabled(position: Int): Boolean = position != 0

            override fun getDropDownView(position: Int, convertView: View?, parent: ViewGroup): View
            {
                val view: TextView = super.getDropDownView(position, convertView, parent) as TextView
                //set the color of first item in the drop down list to gray
                if(position == 0)
                {
                    view.setTextColor(Color.GRAY)
                }
                return view
            }
        }

        spinnerCursos.adapter = spinnerAdapter

        if (rol == "Profesor") { spinnerCursos.visibility = View.GONE }

        btRegister.setOnClickListener{
            var nombre = etNombre.text.toString()
            var apellidos = etApellidos.text.toString()
            var email = etEmail.text.toString()
            val password = etPassword.text.toString()
            val uriFoto = Uri.parse("android.resource://" + packageName + "/" + R.drawable.default_profile_image)

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
            else if (spinnerCursos.selectedItem == "Curso" && rol == "Alumno")
            {
                Toast.makeText(this, "Por favor, introduzca su curso",
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

                            if (rol == "Alumno")
                            {
                                db.collection("alumnos").document(email).set(
                                    hashMapOf("nombre" to nombre,
                                        "apellidos" to apellidos,
                                        "fotoDePerfil" to uriFoto,
                                        "curso" to spinnerCursos.selectedItem.toString()),
                                )

                                var intent = Intent(applicationContext, MainActivityAlumno::class.java)
                                intent.putExtra("email", email)
                                intent.putExtra("nombre", nombre)
                                startActivity(intent)
                            }
                            else if (rol == "Profesor")
                            {
                                db.collection("profesores").document(email).set(
                                    hashMapOf("nombre" to nombre,
                                        "apellidos" to apellidos,
                                        "fotoDePerfil" to uriFoto)
                                )

                                var intent = Intent(applicationContext, MainActivityProfesor::class.java)
                                intent.putExtra("email", email)
                                intent.putExtra("nombre", nombre)
                                startActivity(intent)
                            }
                        }
                        else {
                            // If sign in fails, display a message to the user.
                            Toast.makeText(this, "Authentication failed.",
                                Toast.LENGTH_SHORT).show()
                        }
                    }
            }
        }
    }
}