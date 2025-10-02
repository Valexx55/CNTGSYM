package edu.cas.cntgsym.servicio

import android.app.Notification
import android.app.PendingIntent
import android.app.Service
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.media.MediaPlayer
import android.os.Build
import android.os.IBinder
import android.util.Log
import android.widget.RemoteViews
import android.widget.Toast
import androidx.core.app.NotificationCompat
import edu.cas.cntgsym.R
import edu.cas.cntgsym.util.Constantes
import edu.cas.cntgsym.util.Notificaciones

class PlayService : Service() {


    // FIXME RESOLVER EL CLOSE, QUE DETIENE EL SERVICIO PERO NO LA MÚSICA
    companion object{

        // TODO ya funciona :) método
        // metodo
        var mediaPlayer: MediaPlayer? = null//ExoPlayer
        var sonando: Boolean = false //para controlar el estado de reprodución

        fun play(context: Context)
        {
            if (!sonando)
            {
                mediaPlayer = MediaPlayer.create(context, R.raw.audio)
                mediaPlayer!!.start()
                sonando=true
                mediaPlayer!!.setOnCompletionListener {
                    //detener la canción
                    sonando = false
                    val intent = Intent(context, PlayService::class.java)
                    context.stopService(intent)
                }
            }
        }

        fun stop(context: Context)
        {
            mediaPlayer?.stop()
            sonando=false
            mediaPlayer=null
            //paro el servicio
            // OJO añadido en esta implementación
            val intent = Intent(context, PlayService::class.java)
            context.stopService(intent)
        }
    }



    override fun onCreate() {
        super.onCreate()
        Log.d(Constantes.ETIQUETA_LOG, "En onCreate() de PlayServicio")
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        //return
        //super.onStartCommand(intent, flags, startId)
        Log.d(Constantes.ETIQUETA_LOG, "En onStartCommand() de PlayServicio")

        //la apariencia personalizada de la notificación
        val notificacionPersonalizada = RemoteViews(this.packageName, R.layout.controles_reproductor)

        val piPlay = crearPedingIntent(this, NotificationPlayButtonHandler::class.java, 105 )
        val piSkip = crearPedingIntent(this, NotificationSkipButtonHandler::class.java, 110 )
        val piClose = crearPedingIntent(this, NotificationCloseButtonHandler::class.java, 115 )
        val piPrev = crearPedingIntent(this, NotificationPrevButtonHandler::class.java, 120 )

        val piMainActivity = obtenerPendingIntentActivity()

        notificacionPersonalizada.setOnClickPendingIntent(R.id.notification_button_play, piPlay)
        notificacionPersonalizada.setOnClickPendingIntent(R.id.notification_button_close, piClose)
        notificacionPersonalizada.setOnClickPendingIntent(R.id.notification_button_prev, piPrev)
        notificacionPersonalizada.setOnClickPendingIntent(R.id.notification_button_skip, piSkip)

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            Notificaciones.crearCanalNotificacionForegroundService (this, Notificaciones.NOTIFICATION_CHANNEL_ID2, Notificaciones.NOTIFICATION_CHANNEL_NAME2)

        }

        //Genero la Notificación
        val notification: Notification =
            NotificationCompat.Builder(this, Notificaciones.NOTIFICATION_CHANNEL_ID2)
                .setContentTitle("Player segundo plano")
                .setTicker("Player segundo plano")
                .setContentText("Música maestro")
                .setSmallIcon(R.mipmap.ic_launcher_round) //icono peque : not plegada
                .setCustomContentView(notificacionPersonalizada)
                //.setContent(notificationView) //la vista personalizada, con sus PendingIntentAsociados
                .setContentIntent(piMainActivity) //la actividad a la que llamaremos si tocan la notificación
                .build() // y se hace

        //lanzo el servicio haciendo visible la notificación
        //y la actividad de reproducción
        startForeground(205, notification)
        play(this)


        return START_STICKY//en el caso de que se pare el servicio, no se reinicia
    }

    override fun onDestroy() {
        super.onDestroy()
        Log.d(Constantes.ETIQUETA_LOG, "En onDestroy() de PlayServicio")
        Toast.makeText(this, "Parando servicio onDestroy", Toast.LENGTH_SHORT).show()
        // después de finalizar el proceso, paro la música
        stop(this)
    }


    override fun onBind(intent: Intent): IBinder? {
        //esto sólo sería necesario programarlo si mi servicio actualiza una pantalla o similar
        //TODO("Return the communication channel to the service.")
        return null
    }

    fun obtenerPendingIntentActivity(): PendingIntent {
        var intentActivity: PendingIntent? = null

        val notificationIntent: Intent = Intent(this, PlayActivity::class.java)
        notificationIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP)
        //notificationIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK)
        intentActivity = PendingIntent.getActivity(
            this, 100, notificationIntent,
            PendingIntent.FLAG_IMMUTABLE
        )


        return intentActivity
    }

    fun crearPedingIntent (context: Context, receptor: Class<*>, requestCode: Int): PendingIntent
    {
        val intent = Intent(context, receptor)
        val pedingIntent = PendingIntent.getBroadcast(context, requestCode, intent, PendingIntent.FLAG_IMMUTABLE)
        return pedingIntent
    }


    //esta clase recibirá la señal de cerrar la notificación
    class NotificationCloseButtonHandler : BroadcastReceiver(){
        override fun onReceive(context: Context?, intent: Intent?) {
            Toast.makeText(context, "Close Seleccionado", Toast.LENGTH_SHORT).show()
            val intent = Intent(context, PlayService::class.java)
            context?.stopService(intent)

            //también podría detener un servicio así
            /*
            //elimino el servicio del "foreground"
            stopForeground(STOP_FOREGROUND_REMOVE)
            //lo detengo
            stopSelf()
            //paro la música

             */

        }
    }



    //esta clase recibirá la señal de reproducir la notificación
    class NotificationPlayButtonHandler : BroadcastReceiver(){
        override fun onReceive(context: Context?, intent: Intent?) {
            Toast.makeText(context, "Botón Play seleccionado", Toast.LENGTH_SHORT).show()
            play(context!!)
        }
    }


    //esta clase recibirá la señal de hacia delante la notificación
    class NotificationSkipButtonHandler : BroadcastReceiver(){
        override fun onReceive(context: Context?, intent: Intent?) {
            Toast.makeText(context, "Botón Skip seleccionado", Toast.LENGTH_SHORT).show()
        }
    }

    //esta clase recibirá la señal de hacia atrás la notificación
    class NotificationPrevButtonHandler : BroadcastReceiver(){
        override fun onReceive(context: Context?, intent: Intent?) {
            Toast.makeText(context, "Botón Prev seleccionado", Toast.LENGTH_SHORT).show()
        }
    }
}