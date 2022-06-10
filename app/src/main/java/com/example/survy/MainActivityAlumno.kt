package com.example.survy

import android.content.DialogInterface
import android.content.Intent
import android.graphics.Color
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
import com.example.survy.Fragments.MisAlumnos.MisAlumnosFragment
import com.example.survy.Fragments.MisAsignaturas.MisAsignaturasFragment
import com.example.survy.Fragments.Resultados.ResultadosFragmentProfesor
import com.google.android.material.navigation.NavigationView
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.ktx.Firebase
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
        val email = bundle?.getString("email")

        val rol = "Alumno"
        //val nombre = bundle?.getString("nombre")

        var navView : NavigationView = findViewById(R.id.nav_view_alumno)

        header = navView.getHeaderView(0)

        setupMainActivity(email ?: "")

        navView.setNavigationItemSelectedListener {
            when(it.itemId)
            {
                R.id.itemHomeAlumno ->
                {
                    cambiarFragment(HomeFragmentProfesor(), email, rol)
                    supportActionBar?.title = getString(R.string.titleHome)
                }
                R.id.itemMisAsignaturasAlumno ->
                {
                    cambiarFragment(MisAsignaturasFragment(), email, rol)
                }
                R.id.itemNuevaEncuestaAlumno -> cambiarFragment(MisAlumnosFragment(), email, rol)
                R.id.itemMisResultadosAlumno -> cambiarFragment(ResultadosFragmentProfesor(), email, rol)
                R.id.itemMiPerfilAlumno ->
                {
                    cambiarFragment(MiPerfilFragmentAlumno(), email, rol)
                    supportActionBar?.title = getString(R.string.titleMiPerfil)
                }
                R.id.itemCerrarSesionAlumno -> mostrarAlertaCerrarSesion()
            }
            true
        }
    }

    private fun setupMainActivity(email: String?)
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

        if (email.isNullOrBlank())
        {
            Toast.makeText(this, "Error al iniciar sesión", Toast.LENGTH_LONG).show()
            finish()
        }
        else
        {
            var civFotoPerfil = header.findViewById<CircleImageView>(R.id.civHeader)
            var tvEmailHeader = header.findViewById<TextView>(R.id.tvEmailHeader)
            var tvNombreHeader = header.findViewById<TextView>(R.id.tvNombreHeader)

            //civFotoPerfil.setImageURI(null)

            db.collection("alumnos").document(email).get().addOnSuccessListener {
                tvNombreHeader.setText(it.get("nombre") as String?)
                //civFotoPerfil.setImageURI(it.get("fotoDePerfil") as Uri?)
            }
            civFotoPerfil.setImageResource(R.drawable.default_profile_image)
            tvEmailHeader.setText(email)
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

    private fun cambiarFragment (fragmentCambiar : Fragment, email: String?, rol: String)
    {
        var args = Bundle()
        args.putString("email", email)
        args.putString("rol", rol)

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
        dialogBuilder.setPositiveButton("Cerrar Sesión", DialogInterface.OnClickListener{
                dialog, id ->
            Firebase.auth.signOut()

            var i = Intent(this, LoginActivity::class.java)
            startActivity(i)
            finish()
        })
        dialogBuilder.setNegativeButton("Cancelar", DialogInterface.OnClickListener{
                dialog, id -> dialog.cancel()
        })

        val alerta = dialogBuilder.create()
        alerta.show()
    }

    fun actualizarHeader(nombre: String)
    {
        var civFotoPerfil = header.findViewById<CircleImageView>(R.id.civHeader)
        var tvNombreHeader = header.findViewById<TextView>(R.id.tvNombreHeader)

        tvNombreHeader.setText(nombre)
        //civFotoPerfil.setImageURI(fotoDePerfil)
    }
}