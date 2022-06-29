package com.example.survy

import android.content.DialogInterface
import android.content.Intent
import android.graphics.drawable.ColorDrawable
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import android.view.View
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AlertDialog
import androidx.drawerlayout.widget.DrawerLayout
import androidx.fragment.app.Fragment
import com.example.survy.Authentication.LoginActivity
import com.example.survy.Fragments.Home.HomeFragmentProfesor
import com.example.survy.Fragments.MiPerfil.Alumno.MiPerfilFragmentAlumno
import com.example.survy.Fragments.MisAsignaturas.Alumno.MisAsignaturasFragmentAlumno
import com.example.survy.Fragments.MisEncuestas.Alumno.NuevaEncuestaFragmentAlumno
import com.example.survy.Fragments.Resultados.ResultadosFragmentProfesor
import com.google.android.material.navigation.NavigationView
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.ktx.Firebase
import com.squareup.picasso.Picasso
import de.hdodenhof.circleimageview.CircleImageView

class MainActivityAlumno : AppCompatActivity()
{
    lateinit var toggle : ActionBarDrawerToggle
    lateinit var drawerLayout : DrawerLayout
    lateinit var header : View

    private val db = FirebaseFirestore.getInstance()

    override fun onCreate(savedInstanceState: Bundle?)
    {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main_alumno)

        val bundle = intent.extras
        val idUsuario = bundle?.getString("idUsuario") ?: ""

        var navView : NavigationView = findViewById(R.id.nav_view_alumno)

        header = navView.getHeaderView(0)

        setupMainActivity(idUsuario ?: "")

        navView.setNavigationItemSelectedListener {
            when(it.itemId)
            {
                R.id.itemHomeAlumno ->
                {
                    cambiarFragment(HomeFragmentProfesor(), idUsuario)
                    supportActionBar?.title = getString(R.string.titleHome)
                }
                R.id.itemMisAsignaturasAlumno ->
                {
                    cambiarFragment(MisAsignaturasFragmentAlumno(), idUsuario)
                    supportActionBar?.title = getString(R.string.titleMisAsignaturasProfesor)
                }
                R.id.itemMiPerfilAlumno ->
                {
                    cambiarFragment(MiPerfilFragmentAlumno(), idUsuario)
                    supportActionBar?.title = getString(R.string.titleMiPerfil)
                }
                R.id.itemCerrarSesionAlumno -> mostrarAlertaCerrarSesion()
            }
            true
        }
    }

    private fun setupMainActivity(idUsuario: String?)
    {
        drawerLayout  = findViewById(R.id.drawerLayoutAlumno)

        toggle = ActionBarDrawerToggle(this, drawerLayout, R.string.abierto, R.string.cerrado)

        drawerLayout.addDrawerListener(toggle)
        toggle.syncState()

        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        val fragment = supportFragmentManager.beginTransaction()
        fragment.replace(R.id.fragmentContainerAlumno, HomeFragmentProfesor()).commit()

        supportActionBar?.title = getString(R.string.titleHome)
        supportActionBar!!.setBackgroundDrawable(ColorDrawable(resources.getColor(R.color.veryDarkPurple)))

        if (idUsuario.isNullOrBlank())
        {
            Toast.makeText(this, "Error al iniciar sesión", Toast.LENGTH_LONG).show()
            finish()
        }
        else
        {
            var civFotoPerfilHeader = header.findViewById<CircleImageView>(R.id.civHeader)
            var tvEmailHeader = header.findViewById<TextView>(R.id.tvEmailHeader)
            var tvNombreHeader = header.findViewById<TextView>(R.id.tvNombreHeader)

            db.collection("alumnos").document(idUsuario).get().addOnSuccessListener {
                tvNombreHeader.setText(it.get("nombre") as String?)
                tvEmailHeader.setText(it.get("email") as String?)
                Picasso.get().load(it.getString("fotoDePerfil")).into(civFotoPerfilHeader)
            }
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

    private fun cambiarFragment (fragmentCambiar : Fragment, idUsuario: String?)
    {
        var args = Bundle()
        args.putString("idUsuario", idUsuario)

        fragmentCambiar.arguments = args

        var fragmentManager = supportFragmentManager

        fragmentManager.beginTransaction()
            .replace(R.id.fragmentContainerAlumno, fragmentCambiar)
            .commit()


        drawerLayout.postDelayed({
            drawerLayout.closeDrawers()
        }, 200)
    }

    fun mostrarAlertaCerrarSesion()
    {
        val dialogBuilder = AlertDialog.Builder(this)
        dialogBuilder.setTitle("Cerrar Sesión")
        dialogBuilder.setMessage("¿Estás seguro de que deseas cerrar sesión?")
        dialogBuilder.setPositiveButton("Cerrar Sesión", DialogInterface.OnClickListener {
                dialog, id ->
            Firebase.auth.signOut()

            var i = Intent(this, LoginActivity::class.java)
            startActivity(i)
            finish()
        })
        dialogBuilder.setNegativeButton("Cancelar", DialogInterface.OnClickListener {
                dialog, id -> dialog.cancel()
        })

        val alerta = dialogBuilder.create()
        alerta.show()
    }
}