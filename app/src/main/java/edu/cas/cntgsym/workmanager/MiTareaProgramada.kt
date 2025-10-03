package edu.cas.cntgsym.workmanager

import android.content.Context
import android.util.Log
import androidx.work.Worker
import androidx.work.WorkerParameters
import edu.cas.cntgsym.util.Constantes

class MiTareaProgramada(context: Context, workParmas: WorkerParameters) :
    Worker(context, workParmas) {


    override fun doWork(): Result {

        //dato de entrada al proceso
        val userId = inputData.getString("USER_ID")

        //aquí metemos nuestra lógica de 2º plano
        val timestamp = java.text.SimpleDateFormat("HH:mm:ss", java.util.Locale.getDefault())
            .format(java.util.Date())
        Log.d(Constantes.ETIQUETA_LOG, "[$timestamp] Ejecutando trabajo MiTareaProgramada: $userId")

        return Result.success()

    }


}