package com.example.survy.Fragments.MisAlumnos

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import com.example.survy.R
import com.google.firebase.firestore.FirebaseFirestore
import com.google.zxing.BarcodeFormat
import com.journeyapps.barcodescanner.BarcodeEncoder
import com.squareup.picasso.Picasso
import de.hdodenhof.circleimageview.CircleImageView
import java.io.ByteArrayOutputStream

class MatricularAlumnoFragmentProfesor : Fragment()
{
    private val db = FirebaseFirestore.getInstance()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View?
    {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_matricular_alumno_profesor, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?)
    {
        super.onViewCreated(view, savedInstanceState)

        val ivIcono = view.findViewById<ImageView>(R.id.ivIconoAsignaturaDetailMatricularAlumnoProfesor)
        val tvNombre = view.findViewById<TextView>(R.id.tvNombreAsignaturaDetailMatricularAlumnoProfesor)
        val tvCurso = view.findViewById<TextView>(R.id.tvCursoAsignaturaDetailMatricularAlumnoProfesor)

        val btGenerarCodigo = view.findViewById<Button>(R.id.btGenerarCodigoMatricularAlumnoProfesor)
        val btCompartir = view.findViewById<Button>(R.id.btCompartirCodigoMatricularAlumnoProfesor)
        val btCancelar = view.findViewById<Button>(R.id.btCancelarMatricularAlumnoProfesor)

        val idUsuario = arguments?.getString("idUsuario") ?: ""
        val idAsignatura = arguments?.getString("asignatura") ?: ""

        db.collection("asignaturas").document(idAsignatura)
            .get().addOnSuccessListener {
                Picasso.get().load(it.getString("icono")).into(ivIcono)
                tvNombre.setText(it.getString("nombre"))
                tvCurso.setText(it.getString("curso"))
            }

        btGenerarCodigo.setOnClickListener {
            val barcodeEncoder = BarcodeEncoder()
            val bitmap = barcodeEncoder.encodeBitmap(idAsignatura, BarcodeFormat.QR_CODE, 264, 264)

            ivIcono.setImageBitmap(bitmap)

            Toast.makeText(context, "Toque el QR para copiar el código en el portapapeles", Toast.LENGTH_SHORT).show()

            ivIcono.setOnClickListener {
                val clipBoard = requireActivity().getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
                clipBoard.setPrimaryClip(ClipData.newPlainText(idAsignatura, idAsignatura))

                Toast.makeText(context, "Código copiado en el portapapeles", Toast.LENGTH_LONG).show()
            }
        }

        btCompartir.setOnClickListener {
            val barcodeEncoder = BarcodeEncoder()
            val bitmap = barcodeEncoder.encodeBitmap(idAsignatura, BarcodeFormat.QR_CODE, 264, 264)

            val bytes = ByteArrayOutputStream()

            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, bytes)

            val qrUri = MediaStore.Images.Media.insertImage(requireContext().contentResolver, bitmap, "Qr" + tvNombre.text, null)

            val intent = Intent().apply {
                this.action = Intent.ACTION_SEND
                this.putExtra(Intent.EXTRA_STREAM, Uri.parse(qrUri))
                this.type = "image/jpeg"
                this.putExtra(Intent.EXTRA_TEXT, "¡Matricúlate en la asignatura " + tvNombre.text + "!\n\nCódigo: " + idAsignatura)
            }

            startActivity(Intent.createChooser(intent, "Enviar código de matriculación a: "))
        }

        btCancelar.setOnClickListener {
            cambiarFragment(AsignaturasMatricularAlumnoFragmentProfesor(), idUsuario, idAsignatura)
        }
    }

    fun cambiarFragment(framentCambiar: Fragment, idUsuario: String, idAsignatura: String)
    {
        var args = Bundle()
        args.putString("idUsuario", idUsuario)
        args.putString("idAsignatura", idAsignatura)
        var fragment = framentCambiar
        fragment.arguments = args

        var fragmentManager = requireActivity().supportFragmentManager

        fragmentManager.beginTransaction()
            .replace(R.id.fragmentContainerProfesor, fragment)
            .commit()
    }

}