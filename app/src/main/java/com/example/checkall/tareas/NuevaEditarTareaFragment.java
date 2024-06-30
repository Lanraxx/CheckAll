package com.example.checkall.tareas;

import static android.content.Context.MODE_PRIVATE;

import android.app.DatePickerDialog;
import android.content.ContentValues;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.checkall.AdminSQLiteOpenHelper;
import com.example.checkall.R;
import com.example.checkall.ayudas.AyudaTareaFragment;
import com.example.checkall.etiquetas.Etiqueta;
import com.example.checkall.etiquetas.EtiquetasAjustesFragment;
import com.example.checkall.etiquetas.SpinnerAdapter;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Objects;

public class NuevaEditarTareaFragment extends Fragment {
    private int id;
    private EditText et_nombre, et_descripcion;
    private RadioButton rb_alta, rb_media, rb_baja;
    private Button btn_crear_tarea, btn_fecha;
    private ImageButton btn_labels_settings, btn_help;
    private ImageView img_flecha_atras;
    private Spinner spinner;
    private ArrayList<Etiqueta> opcionesSpinner;
    private String fragmentAnterior;
    private TextView txt_fecha, txt_title_new_task;

    public NuevaEditarTareaFragment(int id, String fragmentAnterior) {
        this.id = id;
        this.fragmentAnterior = fragmentAnterior;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_add_task, container, false);

        //Enlazamos
        et_nombre = (EditText) rootView.findViewById(R.id.et_nombre_task);
        et_descripcion = (EditText) rootView.findViewById(R.id.et_descripcion);
        txt_fecha = (TextView) rootView.findViewById(R.id.txt_fecha2);
        rb_alta = (RadioButton) rootView.findViewById(R.id.rb_alta);
        rb_media = (RadioButton) rootView.findViewById(R.id.rb_media);
        rb_baja = (RadioButton) rootView.findViewById(R.id.rb_baja);
        spinner = (Spinner) rootView.findViewById(R.id.spinner);
        btn_crear_tarea = (Button) rootView.findViewById(R.id.btn_crear_tarea);
        img_flecha_atras = (ImageView) rootView.findViewById(R.id.imgBtn_flecha_atras2);
        btn_labels_settings = (ImageButton) rootView.findViewById(R.id.btn_labels_settings);
        btn_fecha = (Button) rootView.findViewById(R.id.btn_fecha);
        btn_help = (ImageButton) rootView.findViewById(R.id.btn_help_task);
        txt_title_new_task = (TextView) rootView.findViewById(R.id.txt_title_new_task);

        //Rellenamos el spinner recorriendo la base de datos de etiquetas
        opcionesSpinner = new ArrayList<>();
        AdminSQLiteOpenHelper admin = new AdminSQLiteOpenHelper(getContext(), "gestion", null, 1);
        SQLiteDatabase BdD = admin.getWritableDatabase();
        //Comprobar que el usuario esté logueado
        SharedPreferences preferences = getActivity().getSharedPreferences("my_preferences", getContext().MODE_PRIVATE);
        String userLogueado = "";
        if (preferences.getInt("logueado", 0) == 1) {
            userLogueado = preferences.getString("user", "");
        }
        Cursor fila = BdD.rawQuery("SELECT nombre, color FROM etiquetas WHERE user = ?", new String[]{userLogueado});
        while (fila.moveToNext()) {
            opcionesSpinner.add(new Etiqueta(
                    fila.getString(0),
                    fila.getInt(1)
            ));
        }
        fila.close();
        BdD.close();

        //Adaptar el spinner personalizado
        SpinnerAdapter adapter = new SpinnerAdapter(rootView.getContext(), R.layout.spinner_items, opcionesSpinner);
        spinner.setAdapter(adapter);

        //Variables locales para guardar la fecha de la tarea en caso de estar editándola
        int task_year = 0;
        int task_month = 0;
        int task_day = 0;

        //En caso de estar editando, se muestran los datos de la tarea en cuestion
        if (fragmentAnterior.equals("TareaFragment")) {
            txt_title_new_task.setText("Editar tarea");
            admin = new AdminSQLiteOpenHelper(getContext(), "gestion", null, 1);
            BdD = admin.getWritableDatabase();

            String prioridad;
            Etiqueta etiqueta = null;
            fila = BdD.rawQuery
                    ("select nombre, prioridad, etiqueta, colorEtiqueta, fecha, descripcion from tareas where codigo =" + id, null);
            if (fila.moveToFirst()) {
                et_nombre.setText(fila.getString(0));
                prioridad = fila.getString(1);
                if (Objects.equals(prioridad, "Alta"))
                    rb_alta.setChecked(true);
                else if (Objects.equals(prioridad, "Media"))
                    rb_media.setChecked(true);
                else if (Objects.equals(prioridad, "Baja"))
                    rb_baja.setChecked(true);
                etiqueta = new Etiqueta(fila.getString(2), fila.getInt(3));
                txt_fecha.setText(fila.getString(4));
                et_descripcion.setText(fila.getString(5));
                BdD.close();
            }

            // Encuentra la posición en el Spinner de los datos de la etiqueta de la tarea
            int posicionSeleccionada = -1;
            for (int i = 0; i < opcionesSpinner.size(); i++) {
                if (opcionesSpinner.get(i).getName().equals(etiqueta.getName()) &&
                        opcionesSpinner.get(i).getColorId() == etiqueta.getColorId()) {
                    posicionSeleccionada = i;
                    break;
                }
            }
            spinner.setSelection(posicionSeleccionada);

            //paso la fecha a enteros para poder trabajar con ellos
            String[] partes = txt_fecha.getText().toString().split("/");
            task_day = Integer.parseInt(partes[0]);
            task_month = Integer.parseInt(partes[1]);
            task_year = Integer.parseInt(partes[2]);
        }

        //Funcionalidad botón elegir fecha
        int finalTask_year = task_year;
        int finalTask_month = task_month;
        int finalTask_day = task_day;
        btn_fecha.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (fragmentAnterior.contains("HomeFragment")) {
                    DatePickerDialog datePickerDialog = new DatePickerDialog(rootView.getContext(), new DatePickerDialog.OnDateSetListener() {
                        @Override
                        public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                            txt_fecha.setText(dayOfMonth+"/"+(month+1)+"/"+year);
                        }
                    }, Calendar.getInstance().get(Calendar.YEAR), Calendar.getInstance().get(Calendar.MONTH), Calendar.getInstance().get(Calendar.DAY_OF_MONTH));
                    datePickerDialog.show();
                } else {
                    DatePickerDialog datePickerDialog = new DatePickerDialog(rootView.getContext(), new DatePickerDialog.OnDateSetListener() {
                        @Override
                        public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                            txt_fecha.setText(dayOfMonth+"/"+(month+1)+"/"+year);
                        }
                    }, finalTask_year, finalTask_month-1, finalTask_day);
                    datePickerDialog.show();
                }

            }
        });

        //Funcionalidad botón crear etiqueta nueva
        btn_labels_settings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Lógica del botón
                getParentFragmentManager().beginTransaction().replace(R.id.frame_layout, new EtiquetasAjustesFragment()).addToBackStack(null).commit();
            }
        });

        //Funcionalidad botón crear tarea
        btn_crear_tarea.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Lógica del botón
                onCrearActualizarTarea();
            }
        });
        //Funcionalidad volver atrás
        img_flecha_atras.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (fragmentAnterior.contains("HomeFragment"))      //Si quiero volver atrás desde la llamada de una tarea nueva
                    getParentFragmentManager().beginTransaction().replace(R.id.frame_layout, new HomeFragment()).commit();
                else                                                //Si quiero volver atrás desde la llamada de una tarea existente
                    getParentFragmentManager().beginTransaction().replace(R.id.frame_layout, new TareaFragment(id)).commit();
            }
        });

        //Funcionalidad botón ayuda
        btn_help.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Lógica del botón
                getParentFragmentManager().beginTransaction().add(R.id.frame_layout, new AyudaTareaFragment()).addToBackStack(null).commit();
            }
        });

        return rootView;
    }

    public void onCrearActualizarTarea() {
        AdminSQLiteOpenHelper admin = new AdminSQLiteOpenHelper(getContext(), "gestion", null, 1);
        SQLiteDatabase BdD = admin.getWritableDatabase();

        //Recoger datos
        String nombre = et_nombre.getText().toString();
        String descripcion = et_descripcion.getText().toString();
        String fecha = txt_fecha.getText().toString();
        String prioridad = getPrioridad();
        Etiqueta etiqueta = getEtiqueta();

        // Comprobar si faltan campos por rellenar
        if (nombre.isEmpty() || fecha.isEmpty() || prioridad == null || etiqueta == null) {
            Toast.makeText(getContext(), "Faltan campos por rellenar", Toast.LENGTH_SHORT).show();
            return;
        }

        ContentValues registro = new ContentValues();
        registro.put("codigo", id);
        registro.put("nombre", nombre);
        registro.put("descripcion", descripcion);
        registro.put("fecha", fecha);
        registro.put("prioridad", prioridad);
        registro.put("etiqueta", etiqueta.getName());
        registro.put("colorEtiqueta", etiqueta.getColorId());
        registro.put("checkbox", 0);

        //comprobar si el usuario está logueado
        SharedPreferences preferences = getActivity().getSharedPreferences("my_preferences", MODE_PRIVATE);
        if (preferences.getInt("logueado", 0) == 1) {
            registro.put("user", preferences.getString("user", null));
        } else {
            registro.put("user", "");
        }

        //Crear nueva tarea con un id dado
        if (id == admin.getSizeTabla("tareas", getContext()) + 1) {
            BdD.insert("tareas", null, registro);
            BdD.close();

            // Sumar +1 al conteo de tareas registradas si se está logueado
            if (preferences.getInt("logueado", 0) == 1) {
                SharedPreferences.Editor editor = preferences.edit();
                int contador_tareas = preferences.getInt("tareas_registradas", 0);
                editor.putInt("tareas_registradas", contador_tareas + 1);
                editor.apply();
            }

            Toast.makeText(getContext(), "Tarea registrada", Toast.LENGTH_SHORT).show();

        //Actualización de tarea con el id dado
        } else {
            BdD.update("tareas", registro, "codigo=" + id, null);
            BdD.close();
            Toast.makeText(getContext(), "Tarea actualizada", Toast.LENGTH_SHORT).show();
        }
        // Limpiar campos del formulario
        et_nombre.setText("");
        et_descripcion.setText("");
        rb_baja.setChecked(false);
        rb_media.setChecked(false);
        rb_alta.setChecked(false);
        spinner.setActivated(false);

        if (fragmentAnterior.contains("HomeFragment"))
            getParentFragmentManager().beginTransaction().replace(R.id.frame_layout, new HomeFragment()).commit();
        else
            getParentFragmentManager().beginTransaction().replace(R.id.frame_layout, new TareaFragment(id)).commit();
    }

    public String getPrioridad () {
        if (rb_alta.isChecked())
            return "Alta";
        if (rb_media.isChecked())
            return "Media";
        if (rb_baja.isChecked())
            return "Baja";
        return null;
    }

    public Etiqueta getEtiqueta () {
        return (Etiqueta) spinner.getSelectedItem();
    }

}