package com.example.survy

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import android.widget.Toast
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.drawerlayout.widget.DrawerLayout
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

        navView.setNavigationItemSelectedListener {
            when(it.itemId)
            {
                R.id.itemHome -> Toast.makeText(applicationContext, "Home", Toast.LENGTH_LONG).show()
                R.id.itemMisAsignaturas -> Toast.makeText(applicationContext, "Mis Asignaturas", Toast.LENGTH_LONG).show()
                R.id.itemMisAlumnos -> Toast.makeText(applicationContext, "Mis Alumnos", Toast.LENGTH_LONG).show()
                R.id.itemResultados -> Toast.makeText(applicationContext, "MisResultados", Toast.LENGTH_LONG).show()
                R.id.itemMiPerfil -> Toast.makeText(applicationContext, "Mi Perfil", Toast.LENGTH_LONG).show()
            }
            true
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean
    {
        if (toggle.onOptionsItemSelected(item))
        {
            return true
        }
        return super.onOptionsItemSelected(item)
    }
}