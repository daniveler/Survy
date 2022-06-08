package com.example.survy.Authentication

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AppCompatDelegate
import com.example.survy.MainActivityAlumno
import com.example.survy.R
import com.facebook.CallbackManager
import com.facebook.FacebookCallback
import com.facebook.FacebookException
import com.facebook.login.LoginManager
import com.facebook.login.LoginResult
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.firebase.auth.FacebookAuthProvider
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import java.lang.Exception

class LoginActivity : AppCompatActivity()
{
    private val GOOGLE_SIGN_IN = 1000
    private lateinit var auth: FirebaseAuth
    private val callbackManager = CallbackManager.Factory.create()

    override fun onCreate(savedInstanceState: Bundle?)
    {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)

        val btGoogle = findViewById<Button>(R.id.btGoogleLogin)
        val btFacebook = findViewById<Button>(R.id.btFacebookLogin)
        val btEmail = findViewById<Button>(R.id.btEmailLogin)
        val btRegister = findViewById<Button>(R.id.btRegisterMail)

        btGoogle.setOnClickListener{
            val googleConf = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id_fixed))
                .requestEmail()
                .build()

            val googleClient = GoogleSignIn.getClient(this, googleConf)

            var signInIntent = googleClient.signInIntent
            startActivityForResult(signInIntent, 1000)
        }

        btEmail.setOnClickListener {
            val i = Intent(this, ElegirRolActivity::class.java)
            i.putExtra("previousIntent", "Login")
            startActivity(i)
        }

        btFacebook.setOnClickListener {
            LoginManager.getInstance().logInWithReadPermissions(this, listOf("email"))

            LoginManager.getInstance().registerCallback(callbackManager,
                object : FacebookCallback<LoginResult>
                {
                    override fun onSuccess(result: LoginResult)
                    {
                        result?.let {
                            val token = it.accessToken

                            val credential = FacebookAuthProvider.getCredential(token.token)

                            auth.signInWithCredential(credential)
                                .addOnCompleteListener { task ->
                                    if(task.isSuccessful)
                                    {
                                        Toast.makeText(applicationContext, "Facebook LogIn Success",
                                            Toast.LENGTH_LONG).show()
                                    }
                                    else
                                    {
                                        Toast.makeText(applicationContext, "Facebook LogIn Failed",
                                            Toast.LENGTH_LONG).show()
                                    }
                                }
                        }
                    }

                    override fun onCancel() {}

                    override fun onError(error: FacebookException)
                    {
                        Toast.makeText(applicationContext, "Error al autenticar con Facebook",
                            Toast.LENGTH_LONG).show()
                    }
                })
        }

        btRegister.setOnClickListener {
            val i = Intent(this, RegisterActivity::class.java)
            startActivity(i)
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?)
    {
        callbackManager.onActivityResult(requestCode, resultCode, data)

        super.onActivityResult(requestCode, resultCode, data)

        if(requestCode == GOOGLE_SIGN_IN)
        {
            var task = GoogleSignIn.getSignedInAccountFromIntent(data)

            try
            {
                val account = task.getResult(ApiException::class.java)

                val credential = GoogleAuthProvider.getCredential(account.idToken, null)

                auth.signInWithCredential(credential)
                    .addOnCompleteListener(this) { task ->
                        if(task.isSuccessful)
                        {
                            Toast.makeText(this, "LogIn Success",
                                Toast.LENGTH_LONG).show()

                            val i = Intent(applicationContext, MainActivityAlumno::class.java)
                            startActivity(i)
                        }
                        else
                        {
                            Toast.makeText(this, "LogIn Failed",
                                Toast.LENGTH_LONG).show()
                        }

                    }
            }
            catch (e: Exception)
            {
                e.printStackTrace()
            }
        }
    }
}