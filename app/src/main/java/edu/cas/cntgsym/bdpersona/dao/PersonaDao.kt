package edu.cas.cntgsym.bdpersona.dao

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import edu.cas.cntgsym.bdpersona.entity.Persona


/**
 *Clase para describir las operaciones que puedo hacer con la Entidad
 * a nivel de base da datos (consultar, leer, borrar, etc...)
 */
@Dao
interface PersonaDao {

    @Insert
    suspend fun insertar(persona: Persona)

    @Query("SELECT * FROM personas ORDER BY nombre ASC")
    fun obtenerTodas(): LiveData<List<Persona>>

    @Delete
    suspend fun borrar(persona: Persona)
}