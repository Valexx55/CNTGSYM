package edu.cas.cntgsym.bdpersona.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import edu.cas.cntgsym.bdpersona.dao.PersonaDao
import edu.cas.cntgsym.bdpersona.entity.Persona

/**
 * ESTA ES LA BASE DE DATOS QUE REFERENCIA A TODAS LAS ENTITIES/CLASES-TABLA
 * ANTIGUA SQLITEOPENHELPER
 */
@Database(entities = [Persona::class], version = 1)
abstract class AppDatabase : RoomDatabase() {
    abstract fun personaDao(): PersonaDao

    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getDatabase(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "personas_db"
                ).build().also {
                    INSTANCE = it
                }
            }
        }
    }
}