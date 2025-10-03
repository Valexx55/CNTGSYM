package edu.cas.cntgsym.bdpersona.repository

import androidx.lifecycle.LiveData
import edu.cas.cntgsym.bdpersona.dao.PersonaDao
import edu.cas.cntgsym.bdpersona.entity.Persona


/**
 * CAPA DE ACCESO A DATOS
 * separa qué operativa tengo de dónde están
 */
class PersonaRepository (private val personaDao: PersonaDao) {

    val todasLasPersonas: LiveData<List<Persona>> = personaDao.obtenerTodas()

    suspend fun insertar(persona: Persona) {
        personaDao.insertar(persona)
    }

    suspend fun borrar(persona: Persona) {
        personaDao.borrar(persona)
    }
}