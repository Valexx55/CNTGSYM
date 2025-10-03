package edu.cas.cntgsym.bdpersona

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.myroomvmmv.bdpersona.adapter.AdapterPersonas
import edu.cas.cntgsym.R
import edu.cas.cntgsym.bdpersona.entity.Persona
import edu.cas.cntgsym.bdpersona.viewmodel.PersonaViewModel
import edu.cas.cntgsym.databinding.ActivityPersonaDbBinding
import kotlin.getValue


class ActivityPersonaDB : AppCompatActivity() {


    val personas: MutableList<Persona> = mutableListOf()
    lateinit var binding: ActivityPersonaDbBinding
    lateinit var adapterPersonas: AdapterPersonas

    private val personaViewModel: PersonaViewModel by viewModels()//liveData sobrevive a rotaciones

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPersonaDbBinding.inflate(layoutInflater)
        setContentView(binding.root)

        adapterPersonas = AdapterPersonas(personas)
        binding.recview.adapter = adapterPersonas
        binding.recview.layoutManager = LinearLayoutManager(this)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        // Observador para la lista de personas
        personaViewModel.personas.observe(this, Observer { personas ->
            // Aquí puedes actualizar la UI con la lista de personas
            // Por ejemplo, mostrarla en un RecyclerView o algo similar
            personas?.let {
                // Tu lógica para manejar la lista
                Log.d("MIAPP", "Personas: $it")
                adapterPersonas.listaPersonas =it
                adapterPersonas.notifyDataSetChanged()
                //binding.recview.adapter = adapterPersonas

            }


        })
    }

    fun insertarPersona(view: View) {
        personaViewModel.insertar(Persona(nombre ="vale", edad =40))
    }

}