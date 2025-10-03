package edu.cas.cntgsym.bdpersona.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import edu.cas.cntgsym.bdpersona.db.AppDatabase
import edu.cas.cntgsym.bdpersona.entity.Persona
import edu.cas.cntgsym.bdpersona.repository.PersonaRepository
import kotlinx.coroutines.launch

/**
 * esta clase es el "NEGOCIO" las operaciones que se pueden hacer en mi app
 * a nivel de funcionalidad, están aquí
 */
class PersonaViewModel(application: Application) : AndroidViewModel(application) {

    private val repository: PersonaRepository
    val personas: LiveData<List<Persona>>

    init {
        val dao = AppDatabase.getDatabase(application).personaDao()
        repository = PersonaRepository(dao)
        personas = repository.todasLasPersonas
    }

    /*fun insertar(persona: Persona) = viewModelScope.launch {
        repository.insertar(persona)
    }*/

    fun insertar(persona: Persona) {
        viewModelScope.launch {
            repository.insertar(persona)
        }
    }

    fun borrar(persona: Persona) {
        viewModelScope.launch {
            repository.borrar(persona)
        }
    }
}