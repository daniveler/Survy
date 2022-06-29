package com.example.survy.Fragments.Preguntas

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import com.example.survy.R
import com.google.firebase.firestore.FirebaseFirestore

class NuevaPreguntaFragmentProfesor : Fragment()
{
    private val db = FirebaseFirestore.getInstance()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View?
    {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_nueva_pregunta_profesor, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?)
    {
        super.onViewCreated(view, savedInstanceState)

        val etTitulo = view.findViewById<EditText>(R.id.etTituloNuevaPreguntaProfesor)

        val tvTiempo = view.findViewById<TextView>(R.id.tvTiempoNuevaPreguntaProfesor)
        val tvUnidad = view.findViewById<TextView>(R.id.tvUnidadNuevaPreguntaProfesor)
        val btPlus = view.findViewById<ImageButton>(R.id.btPlusNuevaPreguntaProfesor)
        val btMinus = view.findViewById<ImageButton>(R.id.btMinusNuevaPreguntaProfesor)

        val etA = view.findViewById<EditText>(R.id.etRespuestaANuevaPreguntaProfesor)
        val etB = view.findViewById<EditText>(R.id.etRespuestaBNuevaPreguntaProfesor)
        val etC = view.findViewById<EditText>(R.id.etRespuestaCNuevaPreguntaProfesor)
        val etD = view.findViewById<EditText>(R.id.etRespuestaDNuevaPreguntaProfesor)

        val etCorrecta = view.findViewById<EditText>(R.id.etLetraCorrectaNuevaPreguntaProfesor)

        val btCrearPregunta = view.findViewById<Button>(R.id.btCrearPreguntaNuevaPreguntaProfesor)
        val btCancelar = view.findViewById<Button>(R.id.btCancelarNuevaPreguntaProfesor)

        val idUsuario = arguments?.getString("idUsuario") ?: ""
        val idEncuesta = arguments?.getString("idEncuesta") ?: ""
        val idAsignatura = arguments?.getString("idAsignatura") ?: ""

        tvTiempo.text = "20"
        tvUnidad.text = "seg"

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

        btCrearPregunta.setOnClickListener {
            val titulo = etTitulo.text.toString()
            val tiempo = tvTiempo.text.toString()

            val respuestaA = etA.text.toString()
            val respuestaB = etB.text.toString()
            val respuestaC = etC.text.toString()
            val respuestaD = etD.text.toString()

            val correcta = etCorrecta.text.toString().uppercase()

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
                var dataPregunta = hashMapOf("titulo" to titulo,
                    "idEncuesta" to idEncuesta,
                    "tiempo" to tiempo.toInt(),
                    "respuestaA" to respuestaA,
                    "respuestaB" to respuestaB,
                    "respuestaC" to respuestaC,
                    "respuestaD" to respuestaD,
                    "correcta" to correcta
                )

                db.collection("preguntas").add(dataPregunta)

                Toast.makeText(context, "Pregunta creada correctamente",
                    Toast.LENGTH_LONG).show()

                cambiarFragment(PreguntasFragmentProfesor(), idUsuario, idEncuesta, idAsignatura)
            }
        }

    }

    fun cambiarFragment(framentCambiar: Fragment, idUsuario: String, idEncuesta: String, idAsignatura: String)
    {
        var args = Bundle()
        args.putString("idUsuario", idUsuario)
        args.putString("idEncuesta", idEncuesta)
        args.putString("idAsignatura", idAsignatura)

        var fragment = framentCambiar
        fragment.arguments = args

        var fragmentManager = requireActivity().supportFragmentManager

        fragmentManager.beginTransaction()
            .replace(R.id.fragmentContainerProfesor, fragment)
            .commit()
    }
}