package com.example.checkall.etiquetas;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.checkall.AdminSQLiteOpenHelper;
import com.example.checkall.R;

import java.util.List;

public class EtiquetaAdapter extends RecyclerView.Adapter<EtiquetaAdapter.LabelViewHolder> {
    private List<Etiqueta> items;
    private FragmentManager fragmentManager;
    private LabelAdapterListener listener;

    public interface LabelAdapterListener {
        void onLabelDeleted();
    }


    public static class LabelViewHolder extends RecyclerView.ViewHolder {
        // Campos respectivos de un item
        public TextView name;
        public ImageButton imgBtn_eliminar;

        public LabelViewHolder(View v) {
            super(v);
            name = (TextView) v.findViewById(R.id.label_name);
            imgBtn_eliminar = (ImageButton) v.findViewById(R.id.imgBtn_delete_label);
        }
    }

    public EtiquetaAdapter(List<Etiqueta> items, FragmentManager fragmentManager, LabelAdapterListener listener) {
        this.items = items;
        this.fragmentManager = fragmentManager;
        this.listener = listener;
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    @Override
    public EtiquetaAdapter.LabelViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.label_card, viewGroup, false);
        return new EtiquetaAdapter.LabelViewHolder(v);
    }

    @Override
    public void onBindViewHolder(EtiquetaAdapter.LabelViewHolder viewHolder, int i) {
        viewHolder.name.setText(items.get(i).getName());
        viewHolder.name.setBackgroundResource(items.get(i).getColorId());

        // Establecer un OnClickListener para la función de la papelera
        viewHolder.imgBtn_eliminar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AdminSQLiteOpenHelper admin = new AdminSQLiteOpenHelper(viewHolder.itemView.getContext(), "gestion", null, 1);
                SQLiteDatabase BdD = admin.getWritableDatabase();

                // Verificar que la etiqueta no se esté usando en ninguna tarea
                if (etiquetaUsada(viewHolder, items)) {
                    Toast.makeText(viewHolder.itemView.getContext(), "No se puede eliminar porque está asignada a una tarea.", Toast.LENGTH_LONG).show();
                    return;
                }

                int size = admin.getSizeTabla("etiquetas", viewHolder.itemView.getContext());

                int done = BdD.delete("etiquetas", "codigo=" + (viewHolder.getAdapterPosition()), null);
                if (done == 1) {
                    for (int i = viewHolder.getAdapterPosition(); i <= size; i++) {
                        ContentValues modificacion = new ContentValues();
                        modificacion.put("codigo", i);
                        BdD.update("etiquetas", modificacion, "codigo=" + (i + 1), null);
                    }
                    Toast.makeText(viewHolder.itemView.getContext(), "Etiqueta eliminada con éxito", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(viewHolder.itemView.getContext(), "Etiqueta no encontrada", Toast.LENGTH_SHORT).show();
                }
                BdD.close();

                // Notificar al adaptador que los datos han cambiado
                notifyDataSetChanged();

                // Notificar al fragmento que se ha eliminado un elemento
                listener.onLabelDeleted();

            }
        });
    }

    public boolean etiquetaUsada (EtiquetaAdapter.LabelViewHolder viewHolder, List<Etiqueta> items) {
        AdminSQLiteOpenHelper admin = new AdminSQLiteOpenHelper(viewHolder.itemView.getContext(), "gestion", null, 1);
        SQLiteDatabase BdD = admin.getWritableDatabase();

        // Consultar a la base de datos que la etiqueta que se quiere eliminar no está asignada en ninguna tarea
        String nombre_label = items.get(viewHolder.getAdapterPosition()).getName();
        int color_label = items.get(viewHolder.getAdapterPosition()).getColorId();
        Cursor fila = BdD.rawQuery("SELECT etiqueta, colorEtiqueta FROM tareas", null);
        while (fila.moveToNext()) {
            if (fila.getString(0).equals(nombre_label) && fila.getInt(1) == color_label) {
                fila.close();
                BdD.close();
                return true;
            }
        }
        fila.close();
        BdD.close();

        return false;
    }

}

