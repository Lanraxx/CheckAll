package com.example.checkall.tareas;

import static android.content.Context.MODE_PRIVATE;

import android.content.ContentValues;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.TextView;

import com.example.checkall.AdminSQLiteOpenHelper;
import com.example.checkall.R;
import com.example.checkall.ayudas.AyudaHomeFragment;
import com.example.checkall.etiquetas.Etiqueta;
import com.example.checkall.etiquetas.SpinnerAdapter;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class HomeFragment extends Fragment {

    private RecyclerView recyclerTareas;
    private RecyclerView.Adapter adapterTareas;
    private RecyclerView.LayoutManager lManagerTareas;
    private EliminarTareaFragment eliminarTareaFragment;
    private Spinner spinner_filtros;
    private ArrayList<Etiqueta> opcionesSpinner;
    private Etiqueta filtro;
    private List<Tarea> itemsP, itemsC, itemsPOriginal, itemsCOriginal, allItems;
    private ImageButton btn_help_pendientes;
    private TextView txt_bienvenida;

    public static class PrioridadComparator implements Comparator<Tarea> {
        @Override
        public int compare(Tarea t1, Tarea t2) {
            List<String> order = Arrays.asList("Alta", "Media", "Baja");
            String priority1 = t1.getPrioridad();
            String priority2 = t2.getPrioridad();
            return Integer.compare(order.indexOf(priority1), order.indexOf(priority2));
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_home, container, false);

        // Enlazar texto de bienvenida y personalizar
        SharedPreferences preferences = getActivity().getSharedPreferences("my_preferences", MODE_PRIVATE);
        int logueado = preferences.getInt("logueado", 0);
        String userLogueado = "";
        if (logueado == 1) {
            txt_bienvenida = (TextView) rootView.findViewById(R.id.txt_mensaje_home);
            userLogueado = preferences.getString("user", "");
            txt_bienvenida.setText("¡Hola, " + userLogueado + "!");
        }

        // Obtener los botones de ayuda
        btn_help_pendientes = (ImageButton) rootView.findViewById(R.id.btn_help_pendientes);

        // Listener para el botón de ayuda de tareas pendientes
        btn_help_pendientes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Mostrar el fragmento AyudaFragment
                AyudaHomeFragment ayudaFragment = new AyudaHomeFragment();
                getParentFragmentManager().beginTransaction().add(R.id.frame_layout, ayudaFragment).addToBackStack(null).commit();
            }
        });

        // Obtener el spinner de filtros
        spinner_filtros = (Spinner) rootView.findViewById(R.id.spinner_filtros);
        // Rellenamos el spinner recorriendo la base de datos de etiquetas
        opcionesSpinner = new ArrayList<>();
        opcionesSpinner.add(new Etiqueta("Sin filtro", 0));
        AdminSQLiteOpenHelper admin = new AdminSQLiteOpenHelper(getContext(), "gestion", null, 1);
        SQLiteDatabase BdD = admin.getWritableDatabase();
        Cursor fila = BdD.rawQuery("SELECT nombre, color FROM etiquetas WHERE user = ?", new String[]{userLogueado});
        while (fila.moveToNext()) {
            opcionesSpinner.add(new Etiqueta(
                    fila.getString(0),
                    fila.getInt(1)
            ));
        }
        fila.close();

        //Adaptar el spinner personalizado
        SpinnerAdapter adapter = new SpinnerAdapter(rootView.getContext(), R.layout.spinner_items, opcionesSpinner);
        spinner_filtros.setAdapter(adapter);


        // Inicializar listas con todas las tareas
        itemsP = new ArrayList<>();
        itemsC = new ArrayList<>();
        itemsPOriginal = new ArrayList<>();
        itemsCOriginal = new ArrayList<>();
        allItems = new ArrayList<>();
        boolean check;

        // Comprobar si se está logueado
        preferences = getActivity().getSharedPreferences("my_preferences", MODE_PRIVATE);
        logueado = preferences.getInt("logueado", 0);
        if (logueado == 1)
            userLogueado = preferences.getString("user", "");


        fila = BdD.rawQuery("SELECT codigo, nombre, descripcion, fecha, prioridad, etiqueta, colorEtiqueta, checkbox, user FROM tareas WHERE user = ?", new String[]{userLogueado});

        //Recorrer la bbdd para ir creando los objetos Tarea y metiéndolos en la lista
        while (fila.moveToNext()) {
            check = fila.getInt(7) == 1; //si es = 1, check = true
            Tarea tarea = new Tarea(
                    fila.getInt(0),
                    fila.getString(1),
                    fila.getString(2),
                    fila.getString(3),
                    fila.getString(4),
                    new Etiqueta(fila.getString(5),
                            fila.getInt(6)),
                    check
                );
            if (check) {
                itemsC.add(tarea);
                itemsCOriginal.add(tarea);
            } else {
                itemsP.add(tarea);
                itemsPOriginal.add(tarea);
            }
        }
        fila.close();
        BdD.close();

        // Obtener el Recycler
        recyclerTareas = (RecyclerView) rootView.findViewById(R.id.rw_tareas);

        // Usar un administrador para LinearLayout
        lManagerTareas = new LinearLayoutManager(getContext());
        recyclerTareas.setLayoutManager(lManagerTareas);

        // Ordenamos las pendientes que son las importantes
        // Crea una instancia del comparador de prioridad
        PrioridadComparator comparator = new PrioridadComparator();
        // Ordena la lista utilizando el comparador
        Collections.sort(itemsP, comparator);

        // Unimos ambas listas, dejando últimas las checked
        allItems.addAll(itemsP);
        allItems.addAll(itemsC);


        // Crear un nuevo adaptador para las pendientes
        adapterTareas = new TareasAdapter(allItems, getParentFragmentManager(), new TareasAdapter.OnDeleteIconClickListener() {
            @Override
            public void onDeleteIconClick(int position) {
                // Mostrar el fragmento EliminarTareaFragment
                eliminarTareaFragment = new EliminarTareaFragment(position);
                getParentFragmentManager().beginTransaction().add(R.id.frame_layout, eliminarTareaFragment).commit();
            }
        }, new TareasAdapter.OnCheckboxClickListener() {
            @Override
            public void onCheckboxClick(int id, Tarea tarea, boolean isChecked) {
                // Actualizar el campo checkbox de la tarea en la base de datos
                AdminSQLiteOpenHelper admin = new AdminSQLiteOpenHelper(getContext(), "gestion", null, 1);
                SQLiteDatabase BdD = admin.getWritableDatabase();
                ContentValues registro = new ContentValues();
                registro.put("checkbox", isChecked ? 1 : 0);
                BdD.update("tareas", registro, "codigo=" + id, null);
                BdD.close();

                // Actualizar el adaptador
                adapterTareas.notifyDataSetChanged();
                getParentFragmentManager().beginTransaction().replace(R.id.frame_layout, new HomeFragment()).commit();
            }
        });

        // Luego, pasas esta lista ordenada a tu adaptador de RecyclerView
        recyclerTareas.setAdapter(adapterTareas);


        // Listener para el spinner de filtros
        spinner_filtros.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                filtro = (Etiqueta) spinner_filtros.getSelectedItem();

                // Restaurar las listas originales
                itemsP.clear();
                itemsP.addAll(itemsPOriginal);
                itemsC.clear();
                itemsC.addAll(itemsCOriginal);

                // Limpiar la lista combinada
                List<Tarea> filteredItems = new ArrayList<>();

                if (!filtro.getName().equals("Sin filtro")) {
                    for (Tarea tarea : itemsP) {
                        if (tarea.getEtiqueta().getName().equals(filtro.getName()) && tarea.getEtiqueta().getColorId() == filtro.getColorId()) {
                            filteredItems.add(tarea);
                        }
                    }
                    // ordenar por prioridad las pendientes
                    Collections.sort(filteredItems, new PrioridadComparator());
                    for (Tarea tarea : itemsC) {
                        if (tarea.getEtiqueta().getName().equals(filtro.getName()) && tarea.getEtiqueta().getColorId() == filtro.getColorId()) {
                            filteredItems.add(tarea);
                        }
                    }
                } else {
                    // Si no hay filtro, agregar todas las tareas después de ordenar las pendientes por prioridad
                    Collections.sort(itemsP, new PrioridadComparator());
                    filteredItems.addAll(itemsP);
                    filteredItems.addAll(itemsC);
                }

                // Actualizar la lista de tareas
                allItems.clear();
                allItems.addAll(filteredItems);

                // Notificar al adaptador que los datos han cambiado
                adapterTareas.notifyDataSetChanged();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // No hacer nada
            }
        });

        return rootView;
    }
}