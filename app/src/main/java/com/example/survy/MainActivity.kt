package com.example.survy

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import android.widget.Toast
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.drawerlayout.widget.DrawerLayout
import androidx.fragment.app.Fragment
import com.example.survy.Authentication.LoginActivity
import com.example.survy.Fragments.HomeFragment
import com.google.android.material.navigation.NavigationView

class MainActivity : AppCompatActivity()
{
    lateinit var toggle : ActionBarDrawerToggle

    override fun onCreate(savedInstanceState: Bundle?)
    {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        var drawerLayout : DrawerLayout = findViewById(R.id.drawerLayout)
        var navView : NavigationView = findViewById(R.id.nav_view)

        toggle = ActionBarDrawerToggle(this, drawerLayout, R.string.abierto, R.string.cerrado)

        drawerLayout.addDrawerListener(toggle)
        toggle.syncState()

        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        val fragment = supportFragmentManager.beginTransaction()
        fragment.replace(R.id.fragmentContainer, HomeFragment()).commit()

        supportActionBar?.title = "Home"

        navView.setNavigationItemSelectedListener {
            when(it.itemId)
            {
                R.id.itemHome ->
                {
                    cambiarFragment(HomeFragment())
                }
                R.id.itemMisAsignaturas -> Toast.makeText(applicationContext, "Mis Asignaturas", Toast.LENGTH_LONG).show()
                R.id.itemMisAlumnos -> Toast.makeText(applicationContext, "Mis Alumnos", Toast.LENGTH_LONG).show()
                R.id.itemResultados -> Toast.makeText(applicationContext, "MisResultados", Toast.LENGTH_LONG).show()
                R.id.itemMiPerfil -> Toast.makeText(applicationContext, "Mi Perfil", Toast.LENGTH_LONG).show()
                R.id.itemCerrarSesion ->
                {
                    var i = Intent(this, LoginActivity::class.java)
                    startActivity(i)
                    finish()
                }
            }
            true
        }
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
    }
}