package com.example.checkall.tareas;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;

import com.example.checkall.AdminSQLiteOpenHelper;
import com.example.checkall.R;

public class EliminarTareaFragment extends Fragment {
    private int id;
    public EliminarTareaFragment(int id) {
        this.id = id;
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_eliminar, container, false);

        // Obtener referencias a los botones
        Button btn_no_eliminar = view.findViewById(R.id.btn_no_eliminar);
        Button btn_eliminar = view.findViewById(R.id.btn_eliminar);

        // Obtenemos referencia al nombre
        TextView txt_tarea = view.findViewById(R.id.txt_eliminar_tarea);
        AdminSQLiteOpenHelper admin = new AdminSQLiteOpenHelper(getContext(), "gestion", null, 1);
        SQLiteDatabase BdD = admin.getWritableDatabase();
        Cursor fila = BdD.rawQuery
                ("select nombre from tareas where codigo =" + id, null);
        if (fila.moveToFirst())
            txt_tarea.setText(new StringBuilder().append("¿Eliminar \"").append(fila.getString(0)).append("\"?").toString());


        btn_no_eliminar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                BdD.close();
                getParentFragmentManager().beginTransaction().hide(EliminarTareaFragment.this).commit();
            }
        });

        btn_eliminar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int size = admin.getSizeTabla("tareas", getContext());

                int done = BdD.delete("tareas", "codigo=" + id, null);
                if (done == 1) {
                    for (int i = id; i <= size; i++) {
                        ContentValues modificacion = new ContentValues();
                        modificacion.put("codigo", i);
                        BdD.update("tareas", modificacion, "codigo=" + (i + 1), null);

                        // Eliminar el recordatorio asociado si existe
                        BdD.delete("recordatorios", "codTask=" + i, null);
                    }
                    Toast.makeText(getContext(), "El producto ha sido eliminado", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(getContext(), "El producto no existe", Toast.LENGTH_SHORT).show();
                }
                BdD.close();

                getParentFragmentManager().beginTransaction().replace(R.id.frame_layout, new HomeFragment()).commit();
            }
        });

        return  view;
    }
}
