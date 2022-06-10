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
import com.example.survy.Fragments.MiPerfil.Profesor.MiPerfilFragmentProfesor
import com.example.survy.Fragments.MisAlumnos.MisAlumnosFragment
import com.example.survy.Fragments.MisAsignaturas.MisAsignaturasFragment
import com.example.survy.Fragments.Resultados.ResultadosFragmentProfesor
import com.google.android.material.navigation.NavigationView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.ktx.Firebase
import de.hdodenhof.circleimageview.CircleImageView

class MainActivityProfesor : AppCompatActivity()
{
    lateinit var toggle : ActionBarDrawerToggle
    lateinit var drawerLayout : DrawerLayout
    lateinit var header : View

    private val db = FirebaseFirestore.getInstance()

    override fun onCreate(savedInstanceState: Bundle?)
    {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main_profesor)

        val bundle = intent.extras
        val email = bundle?.getString("email")

        var navView : NavigationView = findViewById(R.id.nav_view_profesor)

        header = navView.getHeaderView(0)

        setupMainActivity(email ?: "")

        navView.setNavigationItemSelectedListener {
            when(it.itemId)
            {
                R.id.itemHomeProfesor ->
                {
                    cambiarFragment(HomeFragmentProfesor(), email)
                    supportActionBar?.title = getString(R.string.titleHome)
                }
                R.id.itemMisAsignaturasProfesor ->
                {
                    cambiarFragment(MisAsignaturasFragment(), email)
                }
                R.id.itemMisAlumnosProfesor -> cambiarFragment(MisAlumnosFragment(), email)
                R.id.itemResultadosProfesor -> cambiarFragment(ResultadosFragmentProfesor(), email)
                R.id.itemMiPerfilProfesor ->
                {
                    cambiarFragment(MiPerfilFragmentProfesor(), email)
                    supportActionBar?.title = getString(R.string.titleMiPerfil)
                }
                R.id.itemCerrarSesionProfesor -> mostrarAlertaCerrarSesion()
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

    fun setupMainActivity(email: String?)
    {
        drawerLayout  = findViewById(R.id.drawerLayoutProfesor)

        toggle = ActionBarDrawerToggle(this, drawerLayout, R.string.abierto, R.string.cerrado)

        drawerLayout.addDrawerListener(toggle)
        toggle.syncState()

        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        val fragment = supportFragmentManager.beginTransaction()
        fragment.replace(R.id.fragmentContainerProfesor, HomeFragmentProfesor()).commit()

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

            db.collection("profesores").document(email).get().addOnSuccessListener {
                tvNombreHeader.setText(it.get("nombre") as String?)
                //civFotoPerfil.setImageURI(it.get("fotoDePerfil") as Uri?)
            }

            civFotoPerfil.setImageResource(R.drawable.default_profile_image)
            tvEmailHeader.setText(email)
        }
    }

    fun cambiarFragment (fragmentCambiar : Fragment, email: String?)
    {
        var args = Bundle()
        args.putString("rol", "Profesor")
        args.putString("email", email)

        fragmentCambiar.arguments = args

        var fragmentManager = supportFragmentManager

        fragmentManager.beginTransaction()
            .replace(R.id.fragmentContainerProfesor, fragmentCambiar)
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