package edu.cas.cntgsym.bdpersona

import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Typeface
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.myroomvmmv.bdpersona.adapter.AdapterPersonas
import com.google.android.material.snackbar.Snackbar
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

        //programo el itemtouchheleper
        val itemTouchHelper = ItemTouchHelper(itemTouchHelperCallback)
        itemTouchHelper.attachToRecyclerView(binding.recview)


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

    //este objeto entra en funcionamiento cuando arrastro o deslizo un elmento de la lista/item
    val itemTouchHelperCallback = object : ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT) {
        override fun onMove(
            recyclerView: RecyclerView, viewHolder: RecyclerView.ViewHolder, target: RecyclerView.ViewHolder
        ): Boolean {
            return false // No necesitamos mover los elementos, solo manejar el deslizamiento
        }

        override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
            val position = viewHolder.adapterPosition
            val persona = this@ActivityPersonaDB.adapterPersonas.listaPersonas[position] // Método que debes crear en tu adaptador
            // Aquí es donde eliminamos el ítem
            personaViewModel.borrar(persona)

            // Mostrar Snackbar para deshacer la eliminación
            Snackbar.make(this@ActivityPersonaDB.binding.recview, "Persona eliminada", Snackbar.LENGTH_LONG)
                .setAction("Deshacer") {
                    // Si el usuario quiere deshacer, simplemente reinsertamos el ítem
                    personaViewModel.insertar(persona)
                }
                .show()
        }

        override fun onChildDraw(
            c: Canvas,
            recyclerView: RecyclerView,
            viewHolder: RecyclerView.ViewHolder,
            dX: Float, // desplazamiento horizontal
            dY: Float,
            actionState: Int,
            isCurrentlyActive: Boolean
        ) {
            super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive)

            // Solo aplicar si se está deslizando a la izquierda dX negativo
            if (actionState == ItemTouchHelper.ACTION_STATE_SWIPE && dX < 0) {
                val itemView = viewHolder.itemView
                val paint = Paint()
                paint.color = Color.RED

                // Dibuja el fondo rojo
                c.drawRect(
                    itemView.right.toFloat() + dX, // izquierda del fondo
                    itemView.top.toFloat(),
                    itemView.right.toFloat(),      // derecha del fondo
                    itemView.bottom.toFloat(),
                    paint
                )

                // Carga el icono
                val deleteIcon =
                    ContextCompat.getDrawable(recyclerView.context, R.drawable.outline_auto_delete_24)
                val iconMargin = 32
                val iconSize = 64

                deleteIcon?.let {
                    val iconTop = itemView.top + (itemView.height - iconSize) / 2
                    val iconLeft = itemView.right - iconMargin - iconSize
                    val iconRight = itemView.right - iconMargin
                    val iconBottom = iconTop + iconSize

                    it.setBounds(iconLeft, iconTop, iconRight, iconBottom)
                    it.draw(c)

                    // 3. Texto "Eliminar"
                    val text = "Eliminar"
                    val textPaint = Paint()
                    textPaint.color = Color.WHITE
                    textPaint.textSize = 40f
                    textPaint.isAntiAlias = true
                    textPaint.typeface = Typeface.create(Typeface.DEFAULT, Typeface.BOLD)

                    // Calcular ancho del texto
                    val textWidth = textPaint.measureText(text)

                    // Dibujar texto a la izquierda del ícono
                    val textX = iconLeft - textWidth - 20f
                    val textY = itemView.top + itemView.height / 2f + 15f // Ajuste vertical

                    c.drawText(text, textX, textY, textPaint)
                }
            }
        }
    }



}