package com.example.checkall.tareas;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import androidx.core.app.NotificationCompat;

import com.example.checkall.AdminSQLiteOpenHelper;
import com.example.checkall.MainActivity;
import com.example.checkall.R;

import java.util.ArrayList;
import java.util.List;

public class ReminderReceiver extends BroadcastReceiver {
    List<String> mensajesNotificacion = new ArrayList<>();

    @Override
    public void onReceive(Context context, Intent intent) {
        mensajesNotificacion.add("Cuanto antes lo hagas, antes descansas...");
        mensajesNotificacion.add("Deja tiktok y haz cosas");
        mensajesNotificacion.add("¡Es hora de hacer tu tarea!");
        mensajesNotificacion.add("No dejes que la procrastinación te domine...");
        mensajesNotificacion.add("Va, en serio, hazlo ya");
        mensajesNotificacion.add("¡No te olvides de tu tarea!");
        mensajesNotificacion.add("No lo dejes para mañana");

        // Seleccionar un mensaje aleatorio
        int random = (int) (Math.random() * mensajesNotificacion.size());


        // Obtener los datos del Intent
        int id = intent.getIntExtra("id", 0);
        String nombre = intent.getStringExtra("nombre");

        Intent notificationIntent = new Intent(context, MainActivity.class);
        notificationIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);

        PendingIntent pendingIntent = PendingIntent.getActivity(context, id, notificationIntent, PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_IMMUTABLE);


        // Mostrar una notificación o cualquier otra acción
        NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);

        // Crear una notificación
        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(context, "TASK_REMINDER_CHANNEL")
                .setSmallIcon(R.mipmap.ic_launcher)
                .setContentTitle(nombre)
                .setContentText(mensajesNotificacion.get(random))
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setAutoCancel(true)
                .setContentIntent(pendingIntent);  // Asociar el PendingIntent;

        // Mostrar la notificación
        notificationManager.notify(id, notificationBuilder.build());

        // Borrar recordatorio de la base de datos
        AdminSQLiteOpenHelper admin = new AdminSQLiteOpenHelper(context, "gestion", null, 1);
        admin.borrarRecordatorio(id, context);
    }
}

