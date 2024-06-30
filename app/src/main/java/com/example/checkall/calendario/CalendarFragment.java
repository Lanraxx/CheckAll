package com.example.checkall.calendario;

import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.checkall.AdminSQLiteOpenHelper;
import com.example.checkall.tareas.HomeFragment;
import com.example.checkall.R;
import com.example.checkall.etiquetas.Etiqueta;
import com.example.checkall.tareas.Tarea;
import com.prolificinteractive.materialcalendarview.CalendarDay;
import com.prolificinteractive.materialcalendarview.OnDateSelectedListener;
import com.prolificinteractive.materialcalendarview.MaterialCalendarView;

import org.threeten.bp.LocalDate;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;


public class CalendarFragment extends Fragment {
    private TareasCalendarAdapter adapterCalendar;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_calendar, container, false);

        // Enlazamos
        MaterialCalendarView calendarView = view.findViewById(R.id.calendarView);
        RecyclerView recyclerCalendar = view.findViewById(R.id.rw_tareas_calendario);

        recyclerCalendar.setHasFixedSize(true);
        RecyclerView.LayoutManager lManagerCalendar = new LinearLayoutManager(getContext());
        recyclerCalendar.setLayoutManager(lManagerCalendar);

        // Configurar el calendario (ejemplo: establecer la fecha de hoy como seleccionada)
        calendarView.setSelectedDate(CalendarDay.today());

        Set<LocalDate> dates = new HashSet<>();
        // Comprobar si se está logueado
        SharedPreferences preferences = getActivity().getSharedPreferences("my_preferences", getContext().MODE_PRIVATE);
        String userLogueado = "";
        if (preferences.getInt("logueado", 0) == 1) {
            userLogueado = preferences.getString("user", "");
        }
        // Recorrer base de datos para pintar un punto rojo en las fechas en las que haya tareas pendientes
        AdminSQLiteOpenHelper admin = new AdminSQLiteOpenHelper(getContext(), "gestion", null, 1);
        try (SQLiteDatabase bd = admin.getWritableDatabase(); Cursor fila = bd.rawQuery("select fecha from tareas where checkbox == 0 and user = ?", new String[]{userLogueado})) {
            if (fila.moveToFirst()) {
                do {
                    String fecha = fila.getString(0);
                    String[] parts = fecha.split("/");
                    int year = Integer.parseInt(parts[2]);
                    int month = Integer.parseInt(parts[1]);
                    int day = Integer.parseInt(parts[0]);

                    dates.add(LocalDate.of(year, month, day));
                } while (fila.moveToNext());
            }
            // Decorar el calendario con un punto debajo de las fechas de las tareas pendientes
            calendarView.addDecorator(new EventDecorator(Color.RED, dates));
        } catch (Exception e) {
            System.out.println("Error al acceder a la base de datos: " + e.getMessage());
        }


        // Establecer un listener para manejar las selecciones de fecha
        calendarView.setOnDateChangedListener(new OnDateSelectedListener() {
            @Override
            public void onDateSelected(MaterialCalendarView widget, CalendarDay date, boolean selected) {
                List<Tarea> tareas = new ArrayList<>();

                // Recorrer base de datos para recoger las fechas de las tareas pendientes
                AdminSQLiteOpenHelper admin = new AdminSQLiteOpenHelper(getContext(), "gestion", null, 1);
                String dia_seleccionado = date.getDay() + "/" + date.getMonth() + "/" + date.getYear();
                //Tenemos en cuenta el usuario logueado
                String userLogueado = "";
                SharedPreferences preferences = getActivity().getSharedPreferences("my_preferences", getContext().MODE_PRIVATE);
                if (preferences.getInt("logueado", 0) == 1) {
                    userLogueado = preferences.getString("user", "");
                }
                String[] selectionArgs = { dia_seleccionado, userLogueado };
                try (SQLiteDatabase bd = admin.getWritableDatabase(); Cursor fila = bd.rawQuery("select codigo, nombre, descripcion, fecha, prioridad, etiqueta, colorEtiqueta, checkbox, user from tareas where checkbox == 0 and fecha = ? and user = ?", selectionArgs)) {
                    if (fila.moveToFirst()) {
                        do {
                            tareas.add(new Tarea(fila.getInt(0),
                                    fila.getString(1),
                                    fila.getString(2),
                                    fila.getString(3),
                                    fila.getString(4),
                                    new Etiqueta(fila.getString(5), fila.getInt(6)),
                                    fila.getInt(7) == 1));
                        } while (fila.moveToNext());
                    }
                } catch (Exception e) {
                    System.out.println("Error al acceder a la base de datos: " + e.getMessage());
                }

                // En caso de que haya tareas pendientes en esa fecha
                if (dates.contains(date.getDate())) {
                    // Crea una instancia del comparador de prioridad
                    HomeFragment.PrioridadComparator comparator = new HomeFragment.PrioridadComparator();
                    Collections.sort(tareas, comparator);
                    adapterCalendar = new TareasCalendarAdapter(tareas, getParentFragmentManager(), new TareasCalendarAdapter.OnCheckboxClickListener() {
                        @Override
                        public void onCheckboxClick(int id, Tarea tarea, boolean isChecked) {
                            // Actualizar la base de datos
                            AdminSQLiteOpenHelper admin = new AdminSQLiteOpenHelper(getContext(), "gestion", null, 1);
                            try (SQLiteDatabase bd = admin.getWritableDatabase()) {
                                bd.execSQL("update tareas set checkbox = " + (isChecked ? 1 : 0) + " where codigo = " + id);
                            } catch (Exception e) {
                                System.out.println("Error al acceder a la base de datos: " + e.getMessage());
                            }
                        }
                    });
                    recyclerCalendar.setAdapter(adapterCalendar);

                } else {
                    // Si no hay tareas pendientes, mostrar un mensaje
                    recyclerCalendar.setAdapter(new TareasCalendarAdapter(new ArrayList<>(), null, null));

                }
            }
        });

        return view;
    }
}