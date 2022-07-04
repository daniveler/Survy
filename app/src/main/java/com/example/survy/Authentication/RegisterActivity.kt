package com.example.survy.Authentication

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import com.example.survy.R
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

/**
 * Activity de registro en la aplicación.
 */
class RegisterActivity : AppCompatActivity()
{
    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?)
    {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)

        auth = Firebase.auth

        val btEmail = findViewById<Button>(R.id.btEmailRegister)
        val btRegister = findViewById<Button>(R.id.btLoginMail)

        btEmail.setOnClickListener {
            val i = Intent(this, ElegirRolActivity::class.java)
            i.putExtra("previousIntent", "Register")
            startActivity(i)
        }

        btRegister.setOnClickListener {
            finish()
        }
    }
}