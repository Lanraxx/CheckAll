package com.example.checkall.tareas;

import android.app.AlarmManager;
import android.app.DatePickerDialog;
import android.app.PendingIntent;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.provider.Settings;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.checkall.AdminSQLiteOpenHelper;
import com.example.checkall.R;
import com.example.checkall.etiquetas.Etiqueta;

import java.util.Calendar;
import java.util.HashSet;

public class TareaFragment extends Fragment {

    private int id;
    private TextView txt_nombre, txt_descripcion, txt_etiqueta, txt_fecha, txt_prioridad, txt_recordatorio;
    private ImageView imgBtn_back, img_editar, img_eliminar, img_recordatorio;
    private Button btn_recordatorio;
    private Calendar calendar = Calendar.getInstance();

    public TareaFragment(int id) {
        this.id = id;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_task, container, false);

        txt_nombre = (TextView) view.findViewById(R.id.txt_nombre);
        txt_descripcion = (TextView) view.findViewById(R.id.txt_descripcion2);
        txt_etiqueta = (TextView) view.findViewById(R.id.txt_etiqueta);
        txt_fecha = (TextView) view.findViewById(R.id.txt_fecha);
        txt_prioridad = (TextView) view.findViewById(R.id.txt_prioridad3);
        imgBtn_back = (ImageView) view.findViewById(R.id.imgBtn_back);
        img_editar = (ImageView) view.findViewById(R.id.imgBtn_editar);
        img_eliminar = (ImageView) view.findViewById(R.id.imgBtn_eliminar2);
        btn_recordatorio = (Button) view.findViewById(R.id.btn_recordatorio);
        img_recordatorio = (ImageView) view.findViewById(R.id.img_reminder2);
        txt_recordatorio = (TextView) view.findViewById(R.id.txt_reminder);

        // Comprobamos que el usuario esté logueado para mostrar la funcionalidad de establecer recordatorio
        SharedPreferences preferences = getContext().getSharedPreferences("my_preferences", Context.MODE_PRIVATE);
        if (preferences.getInt("logueado", 0) == 0) {
            btn_recordatorio.setVisibility(View.GONE);
        }

        //Mostramos los datos de la tarea
        MostrarTask();

        //Funcionalidad de volver atrás
        imgBtn_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getParentFragmentManager().beginTransaction().replace(R.id.frame_layout, new HomeFragment()).commit();
            }
        });

        //Funcionalidad de editar
        img_editar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getParentFragmentManager().beginTransaction().replace(R.id.frame_layout, new NuevaEditarTareaFragment(id, "TareaFragment")).commit();
            }
        });

        //Funcionalidad de eliminar
        img_eliminar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getParentFragmentManager().beginTransaction().add(R.id.frame_layout, new EliminarTareaFragment(id)).commit();
            }
        });

        btn_recordatorio.setOnClickListener(v -> {
            if (checkExactAlarmPermission()) {
                showDatePicker();
            } else {
                requestExactAlarmPermission();
            }
        });

        return view;
    }

    public void MostrarTask() {
        AdminSQLiteOpenHelper admin = new AdminSQLiteOpenHelper(getContext(), "gestion", null, 1);
        SQLiteDatabase BdD = admin.getWritableDatabase();

        Cursor fila = BdD.rawQuery
                ("select codigo, nombre, descripcion, fecha, prioridad, etiqueta, colorEtiqueta from tareas where codigo =" + id, null);

        if (fila.moveToFirst()) {
            Tarea t = new Tarea(fila.getInt(0),
                    fila.getString(1),
                    fila.getString(2),
                    fila.getString(3),
                    fila.getString(4),
                    new Etiqueta(fila.getString(5), fila.getInt(6)),
                    false);

            txt_nombre.setText(t.getName());
            txt_descripcion.setText(t.getDescripcion());
            txt_etiqueta.setText(t.getEtiqueta().getName());
            txt_etiqueta.setBackgroundResource(t.getEtiqueta().getColorId());
            txt_prioridad.setText(t.getPrioridad());
            txt_prioridad.setBackgroundResource(t.getFondoPrioridad(t.getPrioridad()));
            txt_fecha.setText(t.getFecha());
            txt_recordatorio.setText("");
            img_recordatorio.setVisibility(View.GONE);

            // Mostrar recordatorio si existe
            Cursor fila2 = BdD.rawQuery("select hora, fecha from recordatorios where codTask = " + id, null);
            if (fila2.moveToFirst()) {
                txt_recordatorio.setText(fila2.getString(0) + "\n" + fila2.getString(1));
                img_recordatorio.setVisibility(View.VISIBLE);
            }
            BdD.close();
        }
    }

    private boolean checkExactAlarmPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            AlarmManager alarmManager = (AlarmManager) getContext().getSystemService(Context.ALARM_SERVICE);
            return alarmManager.canScheduleExactAlarms();
        }
        return true; // Permiso no necesario para versiones anteriores
    }

    private void requestExactAlarmPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            Intent intent = new Intent(Settings.ACTION_REQUEST_SCHEDULE_EXACT_ALARM,
                    Uri.parse("package:" + getContext().getPackageName()));
            startActivity(intent);
        } else {
            Toast.makeText(getContext(), "Permiso para programar alarmas exactas no está disponible en esta versión de Android", Toast.LENGTH_SHORT).show();
        }
    }
    private void showDatePicker() {
        DatePickerDialog datePickerDialog = new DatePickerDialog(getContext(), (view, year, month, dayOfMonth) -> {
            calendar.set(Calendar.YEAR, year);
            calendar.set(Calendar.MONTH, month);
            calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
            showTimePicker();
        }, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH));

        datePickerDialog.show();
    }

    private void showTimePicker() {
        TimePickerDialog timePickerDialog = new TimePickerDialog(getContext(), (view, hourOfDay, minute) -> {
            calendar.set(Calendar.HOUR_OF_DAY, hourOfDay);
            calendar.set(Calendar.MINUTE, minute);
            calendar.set(Calendar.SECOND, 0);
            scheduleReminder();
        }, calendar.get(Calendar.HOUR_OF_DAY), calendar.get(Calendar.MINUTE), true);

        timePickerDialog.show();
    }

    private void scheduleReminder() {
        AlarmManager alarmManager = (AlarmManager) getContext().getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent(getContext(), ReminderReceiver.class);
        intent.putExtra("id", id); // Usar el id de la tarea para hacer único el PendingIntent
        intent.putExtra("nombre", txt_nombre.getText().toString());
        PendingIntent pendingIntent = PendingIntent.getBroadcast(getContext(), id, intent, PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_IMMUTABLE);

        alarmManager.setExact(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), pendingIntent);
        Toast.makeText(getContext(), "Recordatorio programado", Toast.LENGTH_SHORT).show();

        // Guardar el recordatorio en la base de datos
        // Nueva entrada si no tenía, actualización si ya tenía
        AdminSQLiteOpenHelper admin = new AdminSQLiteOpenHelper(getContext(), "gestion", null, 1);
        SQLiteDatabase bd = admin.getWritableDatabase();
        String fecha = calendar.get(Calendar.DAY_OF_MONTH) + "/" + (calendar.get(Calendar.MONTH) + 1) + "/" + calendar.get(Calendar.YEAR);
        String formattedTime = String.format("%02d:%02d", calendar.get(Calendar.HOUR_OF_DAY), calendar.get(Calendar.MINUTE));
        HashSet<Integer> ids = new HashSet<>();
        Cursor c = bd.rawQuery("select codTask from recordatorios", null);
        while (c.moveToNext()) {
            ids.add(c.getInt(0));
        }
        if (ids.contains(id)) {
            bd.execSQL("update recordatorios set hora = '" + formattedTime + "', fecha = '" + fecha + "' where codTask = " + id);
        } else {
            bd.execSQL("insert into recordatorios values (" + id + ", '" + txt_nombre.getText().toString() + "', '" + fecha + "', '" + formattedTime + "')");
        }
        bd.close();

        getParentFragmentManager().beginTransaction().replace(R.id.frame_layout, new TareaFragment(id)).commit();
    }

}