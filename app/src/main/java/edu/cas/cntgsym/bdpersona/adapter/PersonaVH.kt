package com.example.myroomvmmv.bdpersona.adapter

import androidx.recyclerview.widget.RecyclerView
import edu.cas.cntgsym.bdpersona.entity.Persona
import edu.cas.cntgsym.databinding.FilaPersonaBinding


class PersonaVH(val filaPersona: FilaPersonaBinding): RecyclerView.ViewHolder(filaPersona.root) {

    fun rellenarFila (persona: Persona)
    {
        this.filaPersona.idpersona.text = persona.id.toString()
        this.filaPersona.nombre.text = persona.nombre
        this.filaPersona.edad.text = persona.edad.toString()
    }
}