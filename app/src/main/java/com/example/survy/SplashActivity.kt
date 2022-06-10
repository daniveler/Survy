package com.example.survy

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.PersistableBundle
import android.widget.Toast
import com.example.survy.Authentication.LoginActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class SplashActivity : AppCompatActivity()
{
    private val db = FirebaseFirestore.getInstance()

    override fun onCreate(savedInstanceState: Bundle?)
    {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)

        val user = FirebaseAuth.getInstance().currentUser
        if (user != null)
        {
            val userEmail = user.email

            db.collection("alumnos").document(userEmail!!).get().addOnCompleteListener{
                if (it.isSuccessful)
                {
                    val document = it.result
                    if (document.exists())
                    {
                        val intent = Intent(this, MainActivityAlumno::class.java)
                        intent.putExtra("email", userEmail)
                        Handler().postDelayed({
                            startActivity(intent)
                        }, 1000)

                    }
                    else
                    {
                        db.collection("profesores").document(userEmail!!).get().addOnCompleteListener{
                            if (it.isSuccessful)
                            {
                                val document = it.result
                                if (document.exists())
                                {
                                    val intent = Intent(this, MainActivityProfesor::class.java)
                                    intent.putExtra("email", userEmail)
                                    Handler().postDelayed({
                                        startActivity(intent)
                                    }, 1000)
                                }
                                else
                                {
                                    val intent = Intent(this, LoginActivity::class.java)
                                    intent.putExtra("email", userEmail)
                                    Handler().postDelayed({
                                        startActivity(intent)
                                    }, 1000)
                                }


                            }
                            else{ Toast.makeText(this, "Error al recuperar la sesión", Toast.LENGTH_LONG).show() }
                        }
                    }
                }
                else
                {
                    Toast.makeText(this, "Error al recuperar la sesión", Toast.LENGTH_LONG).show()
                }
            }
        }
    }
}