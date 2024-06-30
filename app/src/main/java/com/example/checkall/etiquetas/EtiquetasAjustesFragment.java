package com.example.checkall.etiquetas;

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
import android.widget.Button;
import android.widget.ImageButton;

import com.example.checkall.AdminSQLiteOpenHelper;
import com.example.checkall.R;

import java.util.ArrayList;
import java.util.List;

public class EtiquetasAjustesFragment extends Fragment implements EtiquetaAdapter.LabelAdapterListener {

    private RecyclerView recycler;
    private RecyclerView.Adapter adapter;
    private RecyclerView.LayoutManager lManager;
    private ImageButton imgBtn_atras;
    private Button btn_new_label;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_labels_settings, container, false);

        imgBtn_atras = (ImageButton) rootView.findViewById(R.id.imgBtn_flecha_atras4);
        btn_new_label = (Button) rootView.findViewById(R.id.btn_new_label);

        // Inicializar
        List<Etiqueta> items = new ArrayList<>();
        AdminSQLiteOpenHelper admin = new AdminSQLiteOpenHelper(getContext(), "gestion", null, 1);
        SQLiteDatabase BdD = admin.getWritableDatabase();
        // Comprobar que el usuario esté logueado
        SharedPreferences preferences = getActivity().getSharedPreferences("my_preferences", getContext().MODE_PRIVATE);
        String userLogueado = "";
        if (preferences.getInt("logueado", 0) == 1) {
            userLogueado = preferences.getString("user", "");
        }
        //Recorrer la bbdd para ir creando los objetos Etiqueta y metiéndolos en la lista
        Cursor fila = BdD.rawQuery("SELECT nombre, color FROM etiquetas WHERE user = ?", new String[]{userLogueado});
        while (fila.moveToNext()) {
            items.add(new Etiqueta(
                    fila.getString(0),
                    fila.getInt(1)
            ));
        }
        fila.close();
        BdD.close();

        // Obtener el Recycler
        recycler = (RecyclerView) rootView.findViewById(R.id.etiquetas);
        recycler.setHasFixedSize(true);

        // Usar un administrador para LinearLayout
        lManager = new LinearLayoutManager(getContext());
        recycler.setLayoutManager(lManager);

        // Crear un nuevo adaptador
        adapter = new EtiquetaAdapter(items, getParentFragmentManager(), this);
        recycler.setAdapter(adapter);

        //Funcionalidad volver atrás
        imgBtn_atras.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getParentFragmentManager().popBackStack();
            }
        });

        //Funcionalidad nueva etiqueta
        btn_new_label.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getParentFragmentManager().beginTransaction().replace(R.id.frame_layout, new NuevaEtiquetaFragment()).addToBackStack(null).commit();
            }
        });

        return rootView;
    }

    @Override
    public void onLabelDeleted() {
        getParentFragmentManager().popBackStack();
    }
}