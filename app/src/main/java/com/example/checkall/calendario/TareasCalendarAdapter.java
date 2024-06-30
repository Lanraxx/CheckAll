package com.example.checkall.calendario;

import static android.content.Context.MODE_PRIVATE;

import android.content.Context;
import android.content.SharedPreferences;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.checkall.R;
import com.example.checkall.tareas.Tarea;
import com.example.checkall.tareas.TareaFragment;

import java.util.List;

public class TareasCalendarAdapter extends RecyclerView.Adapter<TareasCalendarAdapter.ViewHolder> {

    private List<Tarea> itemList;
    private FragmentManager fragmentManager;
    private OnCheckboxClickListener checkboxClickListener;

    public interface OnCheckboxClickListener {
        void onCheckboxClick(int id, Tarea tarea, boolean isChecked);
    }
    public void setOnCheckboxClickListener(OnCheckboxClickListener listener) {
        this.checkboxClickListener = listener;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        private TextView name;
        private TextView prioridad;
        private TextView etiqueta;
        private CheckBox checkBox;
        public Context context;

        public ViewHolder(View v) {
            super(v);
            context = v.getContext();
            name = (TextView) v.findViewById(R.id.name);
            prioridad = (TextView) v.findViewById(R.id.txt_prioridad2);
            etiqueta = (TextView) v.findViewById(R.id.etiqueta);
            checkBox = (CheckBox) v.findViewById(R.id.checkBox);
        }
    }

    public TareasCalendarAdapter(List<Tarea> items, FragmentManager fragmentManager, OnCheckboxClickListener checkboxClickListener) {
        itemList = items;
        this.fragmentManager = fragmentManager;
        this.checkboxClickListener = checkboxClickListener;
    }

    @NonNull
    @Override
    public TareasCalendarAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.task_calendar_card, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Tarea item = itemList.get(position);
        holder.name.setText(item.getName());
        holder.prioridad.setText(itemList.get(position).getPrioridad());
        holder.prioridad.setBackgroundResource(itemList.get(position).getFondoPrioridad(itemList.get(position).getPrioridad()));
        holder.etiqueta.setText(itemList.get(position).getEtiqueta().getName());
        holder.etiqueta.setBackgroundResource(itemList.get(position).getEtiqueta().getColorId());

        // Abrir tarea
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Crear una instancia del fragmento
                Fragment fragment = new TareaFragment(itemList.get(holder.getAdapterPosition()).getId());
                // Abrir el fragmento en la actividad
                fragmentManager.beginTransaction()
                        .replace(R.id.frame_layout, fragment)
                        .addToBackStack(null)
                        .commit();
            }
        });

        // Establecer un OnClickListener para el checkbox
        holder.checkBox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (checkboxClickListener != null) {
                    if (holder.checkBox.isChecked()) {
                        SharedPreferences preferences = holder.context.getSharedPreferences("my_preferences", MODE_PRIVATE);
                        if (preferences.getInt("logueado", 0) == 1) {
                            SharedPreferences.Editor editor = preferences.edit();
                            int contador_tareas_completadas = preferences.getInt("tareas_completadas", 0);
                            editor.putInt("tareas_completadas", contador_tareas_completadas + 1);
                            editor.apply();
                        }
                    } else {
                        SharedPreferences preferences = holder.context.getSharedPreferences("my_preferences", MODE_PRIVATE);
                        if (preferences.getInt("logueado", 0) == 1) {
                            SharedPreferences.Editor editor = preferences.edit();
                            int contador_tareas_completadas = preferences.getInt("tareas_completadas", 0);
                            if (contador_tareas_completadas > 0)
                                editor.putInt("tareas_completadas", contador_tareas_completadas - 1);
                            editor.apply();
                        }
                    }

                    checkboxClickListener.onCheckboxClick(itemList.get(holder.getAdapterPosition()).getId(), itemList.get(holder.getAdapterPosition()), holder.checkBox.isChecked());
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return itemList.size();
    }
}

