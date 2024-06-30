package com.example.checkall.tareas;

import static android.content.Context.MODE_PRIVATE;

import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Paint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.RecyclerView;

import com.example.checkall.AdminSQLiteOpenHelper;
import com.example.checkall.R;

import java.util.HashSet;
import java.util.List;

public class TareasAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private static final int VIEW_TYPE_PENDIENTE = 0;
    private static final int VIEW_TYPE_COMPLETADA = 1;

    private List<Tarea> items;
    private OnDeleteIconClickListener deleteIconClickListener;
    private OnCheckboxClickListener checkboxClickListener;
    private FragmentManager fragmentManager;

    // Interfaz para el listener de clic en la papelera
    public interface OnDeleteIconClickListener {
        void onDeleteIconClick(int id);
    }

    // Interfaz para el listener de clic en el checkbox
    public interface OnCheckboxClickListener {
        void onCheckboxClick(int id, Tarea tarea, boolean isChecked);
    }

    public TareasAdapter(List<Tarea> items, FragmentManager fragmentManager, OnDeleteIconClickListener deleteIconClickListener, OnCheckboxClickListener checkboxClickListener) {
        this.items = items;
        this.deleteIconClickListener = deleteIconClickListener;
        this.checkboxClickListener = checkboxClickListener;
        this.fragmentManager = fragmentManager;
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    @Override
    public int getItemViewType(int position) {
        // Determina el tipo de vista basado en el estado de la tarea
        int vista = items.get(position).isChecked() ? VIEW_TYPE_COMPLETADA : VIEW_TYPE_PENDIENTE;
        return vista;
        //return items.get(position).isChecked() ? VIEW_TYPE_COMPLETADA : VIEW_TYPE_PENDIENTE;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == VIEW_TYPE_PENDIENTE) {
            View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.task_card, parent, false);
            return new PendienteViewHolder(v);
        } else {
            View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.tarea_completada_card, parent, false);
            return new CompletadaViewHolder(v);
        }
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        Tarea tarea = items.get(position);
        if (holder instanceof PendienteViewHolder) {
            ((PendienteViewHolder) holder).bind(tarea);
        } else if (holder instanceof CompletadaViewHolder) {
            ((CompletadaViewHolder) holder).bind(tarea);
        }
    }

    // ViewHolder para tareas pendientes
    public class PendienteViewHolder extends RecyclerView.ViewHolder {
        public TextView name;
        public TextView fecha;
        public TextView etiqueta;
        public CheckBox checkBox;
        public ImageButton imgBtn_eliminar;
        public TextView txt_prioridad;
        public ImageView img_reminder;
        public Context context;

        public PendienteViewHolder(View v) {
            super(v);
            context = v.getContext();
            name = v.findViewById(R.id.name);
            fecha = v.findViewById(R.id.fecha);
            etiqueta = v.findViewById(R.id.etiqueta);
            imgBtn_eliminar = v.findViewById(R.id.imgBtn_delete_card);
            checkBox = v.findViewById(R.id.checkBox);
            txt_prioridad = v.findViewById(R.id.txt_prioridad);
            img_reminder = v.findViewById(R.id.img_reminder);
        }

        public void bind(Tarea tarea) {
            name.setText(tarea.getName());
            fecha.setText(tarea.getFecha());
            etiqueta.setText(tarea.getEtiqueta().getName());
            etiqueta.setBackgroundResource(tarea.getEtiqueta().getColorId());
            txt_prioridad.setText(tarea.getPrioridad());
            txt_prioridad.setBackgroundResource(tarea.getFondoPrioridad(tarea.getPrioridad()));

            // Comprobar si la tarea tiene un recordatorio
            AdminSQLiteOpenHelper admin = new AdminSQLiteOpenHelper(itemView.getContext(), "gestion", null, 1);
            SQLiteDatabase bd = admin.getReadableDatabase();
            HashSet<Integer> set = new HashSet<>();
            String query = "SELECT codTask FROM recordatorios";
            Cursor cursor = bd.rawQuery(query, null);
            while (cursor.moveToNext()) {
                set.add(cursor.getInt(0));
            }
            img_reminder.setVisibility(set.contains(tarea.getId()) ? View.VISIBLE : View.GONE);
            bd.close();

            // Eliminar tarea
            imgBtn_eliminar.setOnClickListener(v -> {
                if (deleteIconClickListener != null) {
                    deleteIconClickListener.onDeleteIconClick(tarea.getId());
                }
            });

            // Abrir tarea
            itemView.setOnClickListener(v -> {
                Fragment fragment = new TareaFragment(tarea.getId());
                fragmentManager.beginTransaction()
                        .replace(R.id.frame_layout, fragment)
                        .addToBackStack(null)
                        .commit();
            });

            // Checkbox
            checkBox.setChecked(false);
            checkBox.setOnClickListener(v -> {
                if (checkboxClickListener != null) {
                    SharedPreferences preferences = context.getSharedPreferences("my_preferences", MODE_PRIVATE);
                    if (preferences.getInt("logueado", 0) == 1) {
                        SharedPreferences.Editor editor = preferences.edit();
                        int contador_tareas_completadas = preferences.getInt("tareas_completadas", 0);
                        editor.putInt("tareas_completadas", contador_tareas_completadas + 1);
                        editor.apply();
                    }
                    checkboxClickListener.onCheckboxClick(tarea.getId(), tarea, checkBox.isChecked());
                }
            });
        }
    }

    // ViewHolder para tareas completadas
    public class CompletadaViewHolder extends RecyclerView.ViewHolder {
        public TextView name;
        public CheckBox checkBox;
        public ImageButton imgBtn_eliminar;
        public Context context;

        public CompletadaViewHolder(View v) {
            super(v);
            context = v.getContext();
            name = v.findViewById(R.id.name);
            imgBtn_eliminar = v.findViewById(R.id.imgBtn_delete_card);
            checkBox = v.findViewById(R.id.checkBox);
        }

        public void bind(Tarea tarea) {
            name.setText(tarea.getName());
            name.setPaintFlags(name.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
            checkBox.setChecked(true);

            // Eliminar tarea
            imgBtn_eliminar.setOnClickListener(v -> {
                if (deleteIconClickListener != null) {
                    deleteIconClickListener.onDeleteIconClick(tarea.getId());
                }
            });

            // Abrir tarea
            itemView.setOnClickListener(v -> {
                Fragment fragment = new TareaFragment(tarea.getId());
                fragmentManager.beginTransaction()
                        .replace(R.id.frame_layout, fragment)
                        .addToBackStack(null)
                        .commit();
            });

            // Checkbox
            checkBox.setOnClickListener(v -> {
                if (checkboxClickListener != null) {
                    SharedPreferences preferences = context.getSharedPreferences("my_preferences", MODE_PRIVATE);
                    if (preferences.getInt("logueado", 0) == 1) {
                        SharedPreferences.Editor editor = preferences.edit();
                        int contador_tareas_completadas = preferences.getInt("tareas_completadas", 0);
                        editor.putInt("tareas_completadas", contador_tareas_completadas - 1);
                        editor.apply();
                    }
                    checkboxClickListener.onCheckboxClick(tarea.getId(), tarea, checkBox.isChecked());

                    // Recargar el fragmento actual
                    FragmentTransaction fragmentTransaction = ((AppCompatActivity) context).getSupportFragmentManager().beginTransaction();
                    fragmentTransaction.replace(R.id.frame_layout, new HomeFragment());
                    fragmentTransaction.commit();
                }
            });
        }
    }
}
