package com.example.survy.Main

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.survy.Authentication.LoginActivity
import com.example.survy.R
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import java.util.*
import kotlin.concurrent.schedule

class SplashActivity : AppCompatActivity()
{
    private val db = FirebaseFirestore.getInstance()

    override fun onCreate(savedInstanceState: Bundle?)
    {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)
    }

    override fun onStart()
    {
        super.onStart()

        val user = FirebaseAuth.getInstance().currentUser

        if (user != null)
        {
            val userEmail = user.email

            db.collection("alumnos")
                .whereEqualTo("email", userEmail)
                .get().addOnSuccessListener {
                    if (!it.isEmpty())
                    {
                        var id = it.documents.get(0).id

                        val intent = Intent(this, MainActivityAlumno::class.java)
                        intent.putExtra("idUsuario", id)
                        Timer().schedule(1000) { startActivity(intent) }
                    }
                    else
                    {
                        db.collection("profesores")
                            .whereEqualTo("email", userEmail).get()
                            .addOnSuccessListener {
                                if (!it.isEmpty)
                                {
                                    var id = it.documents.get(0).id

                                    val intent = Intent(this, MainActivityProfesor::class.java)
                                    intent.putExtra("idUsuario", id)
                                    Timer().schedule(1000) { startActivity(intent) }
                                }
                                else
                                {
                                    val intent = Intent(this, LoginActivity::class.java)
                                    Timer().schedule(1000) { startActivity(intent) }
                                }
                            }
                    }
                }
        }
        else
        {
            val intent = Intent(this, LoginActivity::class.java)
            Timer().schedule(1000) { startActivity(intent) }
        }
    }
}