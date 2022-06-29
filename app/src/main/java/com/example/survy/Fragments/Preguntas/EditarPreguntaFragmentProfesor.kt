package com.example.survy.Fragments.Preguntas

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import com.example.survy.R
import com.google.firebase.firestore.FirebaseFirestore

class EditarPreguntaFragmentProfesor : Fragment()
{
    private val db = FirebaseFirestore.getInstance()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View?
    {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_editar_pregunta_profesor, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?)
    {
        super.onViewCreated(view, savedInstanceState)

        val etTitulo = view.findViewById<EditText>(R.id.etTituloEditarPreguntaProfesor)

        val tvTiempo = view.findViewById<TextView>(R.id.tvTiempoEditarPreguntaProfesor)
        val tvUnidad = view.findViewById<TextView>(R.id.tvUnidadEditarPreguntaProfesor)
        val btPlus = view.findViewById<ImageButton>(R.id.btPlusEditarPreguntaProfesor)
        val btMinus = view.findViewById<ImageButton>(R.id.btMinusEditarPreguntaProfesor)

        val etA = view.findViewById<EditText>(R.id.etRespuestaAEditarPreguntaProfesor)
        val etB = view.findViewById<EditText>(R.id.etRespuestaBEditarPreguntaProfesor)
        val etC = view.findViewById<EditText>(R.id.etRespuestaCEditarPreguntaProfesor)
        val etD = view.findViewById<EditText>(R.id.etRespuestaDEditarPreguntaProfesor)

        val etCorrecta = view.findViewById<EditText>(R.id.etLetraCorrectaEditarPreguntaProfesor)

        val btGuardarCambios = view.findViewById<Button>(R.id.btGuardarCambiosEditarPreguntaProfesor)
        val btCancelar = view.findViewById<Button>(R.id.btCancelarEditarPreguntaProfesor)

        val idUsuario = arguments?.getString("idUsuario") ?: ""
        val idAsignatura = arguments?.getString("idAsignatura") ?: ""
        val idEncuesta = arguments?.getString("idEncuesta") ?: ""
        val idPregunta = arguments?.getString("idPregunta") ?: ""

        db.collection("preguntas").document(idPregunta)
            .get().addOnSuccessListener {
                val correcta = it.data?.get("correcta").toString()

                etTitulo.setText(it.data?.get("titulo").toString())

                tvTiempo.setText(it.data?.get("tiempo").toString())

                etA.setText(it.data?.get("respuestaA").toString())
                etB.setText(it.data?.get("respuestaB").toString())
                etC.setText(it.data?.get("respuestaC").toString())
                etD.setText(it.data?.get("respuestaD").toString())

                etCorrecta.setText(correcta)
            }

        btPlus.setOnClickListener {
            var tiempo = tvTiempo.text.toString().toInt()
            var unidad = tvUnidad.text.toString()

            if (unidad == "seg" && tiempo in 1..49)
            {
                tiempo += 10
            }
            else if (unidad == "seg" && tiempo == 50)
            {
                tiempo = 1
                unidad = "min"
            }
            else if (unidad == "min" && tiempo in 1..4)
            {
                tiempo += 1
            }

            tvTiempo.setText(tiempo.toString())
            tvUnidad.setText(unidad)
        }

        btMinus.setOnClickListener {
            var tiempo = tvTiempo.text.toString().toInt()
            var unidad = tvUnidad.text.toString()

            if (unidad == "seg" && tiempo in 21..50)
            {
                tiempo -= 10
            }
            else if (unidad == "min" && tiempo == 1)
            {
                tiempo = 50
                unidad = "seg"
            }
            else if (unidad == "min" && tiempo in 2..5)
            {
                tiempo -= 1
            }

            tvTiempo.setText(tiempo.toString())
            tvUnidad.setText(unidad)
        }

        btGuardarCambios.setOnClickListener {
            val titulo = etTitulo.text.toString()
            val tiempo = tvTiempo.text.toString().toInt()

            if (tiempo in 1..5) { tvUnidad.setText("min") }
            else { tvUnidad.setText("seg") }

            val respuestaA = etA.text.toString()
            val respuestaB = etB.text.toString()
            val respuestaC = etC.text.toString()
            val respuestaD = etD.text.toString()

            val correcta = etCorrecta.text.toString()

            if (titulo.isBlank())
            {
                Toast.makeText(context, "Por favor, introduzca el título de la pregunta",
                    Toast.LENGTH_LONG).show()
            }
            else if (respuestaA.isBlank())
            {
                Toast.makeText(context, "Por favor, introduzca la respuesta A",
                    Toast.LENGTH_LONG).show()
            }
            else if (respuestaB.isBlank())
            {
                Toast.makeText(context, "Por favor, introduzca la respuesta B",
                    Toast.LENGTH_LONG).show()
            }
            else if (respuestaC.isBlank())
            {
                Toast.makeText(context, "Por favor, introduzca la respuesta C",
                    Toast.LENGTH_LONG).show()
            }
            else if (respuestaD.isBlank())
            {
                Toast.makeText(context, "Por favor, introduzca la respuesta D",
                    Toast.LENGTH_LONG).show()
            }
            else if (correcta.isBlank() || correcta != "A" && correcta != "B"
                && correcta != "C" && correcta != "D")
            {
                Toast.makeText(context, "Por favor, introduzca una respuesta correcta válida (A, B, C, D)",
                    Toast.LENGTH_LONG).show()
            }
            else
            {
                db.collection("preguntas").document(idPregunta).update("titulo", titulo)
                db.collection("preguntas").document(idPregunta).update("tiempo", tiempo)
                db.collection("preguntas").document(idPregunta).update("respuestaA", respuestaA)
                db.collection("preguntas").document(idPregunta).update("respuestaB", respuestaB)
                db.collection("preguntas").document(idPregunta).update("respuestaC", respuestaC)
                db.collection("preguntas").document(idPregunta).update("respuestaD", respuestaD)
                db.collection("preguntas").document(idPregunta).update("correcta", correcta)

                Toast.makeText(context, "Pregunta modificada correctamente",
                    Toast.LENGTH_LONG).show()

                cambiarFragment(PreguntasFragmentProfesor(), idUsuario, idEncuesta, idAsignatura, idPregunta)
            }
        }

        btCancelar.setOnClickListener {
            cambiarFragment(PreguntasFragmentProfesor(), idUsuario, idEncuesta, idAsignatura, idPregunta)
        }
    }

    fun cambiarFragment(framentCambiar: Fragment, idUsuario: String, idEncuesta: String, idAsignatura: String, idPregunta: String?)
    {
        var args = Bundle()
        args.putString("idUsuario", idUsuario)
        args.putString("idEncuesta", idEncuesta)
        args.putString("idAsignatura", idAsignatura)
        args.putString("idPregunta", idPregunta)

        var fragment = framentCambiar
        fragment.arguments = args

        var fragmentManager = requireActivity().supportFragmentManager

        fragmentManager.beginTransaction()
            .replace(R.id.fragmentContainerProfesor, fragment)
            .commit()
    }
}