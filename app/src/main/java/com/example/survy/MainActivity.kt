package com.example.survy

import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.preference.PreferenceActivity
import android.view.MenuItem
import android.view.View
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.core.view.get
import androidx.drawerlayout.widget.DrawerLayout
import androidx.fragment.app.Fragment
import com.example.survy.Authentication.LoginActivity
import com.example.survy.Fragments.*
import com.google.android.material.navigation.NavigationView
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import de.hdodenhof.circleimageview.CircleImageView

class MainActivity : AppCompatActivity()
{
    lateinit var toggle : ActionBarDrawerToggle
    lateinit var drawerLayout : DrawerLayout
    lateinit var header : View

    override fun onCreate(savedInstanceState: Bundle?)
    {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val bundle = intent.extras
        val email = bundle?.getString("email")
        //val nombre = bundle?.getString("nombre")

        var navView : NavigationView = findViewById(R.id.nav_view)

        header = navView.getHeaderView(0)

        setupMainActivity(email ?: "")

        navView.setNavigationItemSelectedListener {
            when(it.itemId)
            {
                R.id.itemHome -> cambiarFragment(HomeFragment())
                R.id.itemMisAsignaturas -> cambiarFragment(MisAsignaturasFragment())
                R.id.itemMisAlumnos -> cambiarFragment(MisAlumnosFragment())
                R.id.itemResultados -> cambiarFragment(MisResultadosFragment())
                R.id.itemMiPerfil -> cambiarFragment(MiPerfilFragment())
                R.id.itemCerrarSesion ->
                {
                    Firebase.auth.signOut()

                    var i = Intent(this, LoginActivity::class.java)
                    startActivity(i)
                    finish()
                }
            }
            true
        }
    }

    private fun setupMainActivity(email : String)
    {
        drawerLayout  = findViewById(R.id.drawerLayout)

        toggle = ActionBarDrawerToggle(this, drawerLayout, R.string.abierto, R.string.cerrado)

        drawerLayout.addDrawerListener(toggle)
        toggle.syncState()

        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        val fragment = supportFragmentManager.beginTransaction()
        fragment.replace(R.id.fragmentContainer, HomeFragment()).commit()

        supportActionBar?.title = "Home"

        var civFotoPerfil = header.findViewById<CircleImageView>(R.id.circleImageView)
        var tvEmailHeader = header.findViewById<TextView>(R.id.tvEmailHeader)

        tvEmailHeader.text = email

        //tvNombreHeader.text = nombre


        //val user = Firebase.auth.currentUser

        /*tvNombreHeader.text = user?.displayName
        tvEmailHeader.text = user?.email
        civFotoPerfil.setImageURI(user?.photoUrl)*/

        //civFotoPerfil.setImageResource(R.drawable.default_profile_image)

    }

    override fun onBackPressed()
    {

    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean
    {
        if (toggle.onOptionsItemSelected(item))
        {
            return true
        }
        return super.onOptionsItemSelected(item)
    }

    private fun cambiarFragment (fragmentCambiar : Fragment)
    {
        var fragmentManager = supportFragmentManager
        var fragmentTransaction = fragmentManager.beginTransaction()
        fragmentTransaction.replace(R.id.fragmentContainer, fragmentCambiar)
        fragmentTransaction.commit()
        drawerLayout.closeDrawers()
    }
}