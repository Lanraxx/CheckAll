package com.example.checkall.etiquetas;

import android.content.ContentValues;
import android.content.SharedPreferences;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import com.example.checkall.AdminSQLiteOpenHelper;
import com.example.checkall.R;

public class NuevaEtiquetaFragment extends Fragment {

    private TextView et_nombre;
    private RadioButton rb_azul, rb_marron, rb_verde, rb_naranja, rb_rosa, rb_morado;
    private ImageButton btn_atras;
    private Button btn_add_label;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_add_label, container, false);

        //Enlazamos
        et_nombre = (EditText) rootView.findViewById(R.id.et_nombre_label);
        rb_azul = (RadioButton) rootView.findViewById(R.id.rb_fondo_azul);
        rb_marron = (RadioButton) rootView.findViewById(R.id.rb_fondo_marron);
        rb_verde = (RadioButton) rootView.findViewById(R.id.rb_fondo_verde);
        rb_naranja = (RadioButton) rootView.findViewById(R.id.rb_fondo_naranja);
        rb_rosa = (RadioButton) rootView.findViewById(R.id.rb_fondo_rosa);
        rb_morado = (RadioButton) rootView.findViewById(R.id.rb_fondo_morado);
        btn_atras = (ImageButton) rootView.findViewById(R.id.imgBtn_flecha_atras3);
        btn_add_label = (Button) rootView.findViewById(R.id.btn_add_label);

        //Funcionalidad volver atrás
        btn_atras.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getParentFragmentManager().popBackStack();
            }
        });

        //Funcionalidad botón crear etiqueta
        btn_add_label.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Lógica del botón
                onAddLabel();
            }
        });

        return rootView;
    }

    public void onAddLabel () {
        AdminSQLiteOpenHelper admin = new AdminSQLiteOpenHelper(getContext(), "gestion", null, 1);
        SQLiteDatabase BdD = admin.getWritableDatabase();

        int pos = admin.getSizeTabla("etiquetas", getContext());
        int color = getColorId();
        String nombre = et_nombre.getText().toString();

        ContentValues registro = new ContentValues();
        registro.put("codigo", pos);
        registro.put("nombre", nombre);
        registro.put("color", color);

        // Comprobar que el usuario está logueado
        SharedPreferences preferences = getActivity().getSharedPreferences("my_preferences", getContext().MODE_PRIVATE);
        String userLogueado = "";
        if (preferences.getInt("logueado", 0) == 1)
            userLogueado = preferences.getString("user", "");
        registro.put("user", userLogueado);

        // Comprobar que no se repitan nombres en las etiquetas
        if (admin.comprobarNombre("etiquetas", "nombre", nombre, getContext())) {
            Toast.makeText(getContext(), "Ya existe una etiqueta con ese nombre", Toast.LENGTH_SHORT).show();
            return;
        }

        if (!nombre.isEmpty() && color!=0) {
            BdD.insert("etiquetas", null, registro);
            BdD.close();
            Toast.makeText(getContext(), "Etiqueta registrada", Toast.LENGTH_SHORT).show();
        } else
            Toast.makeText(getContext(), "Faltan campos por rellenar", Toast.LENGTH_SHORT).show();

        et_nombre.setText("");
        rb_azul.setChecked(false);
        rb_marron.setChecked(false);
        rb_verde.setChecked(false);
        rb_naranja.setChecked(false);
        rb_rosa.setChecked(false);
        rb_morado.setChecked(false);

        getParentFragmentManager().popBackStack();
    }

    public int getColorId () {
        if (rb_azul.isChecked())
            return R.drawable.fondo_etiqueta_blue;
        if (rb_marron.isChecked())
            return R.drawable.fondo_etiqueta_brown;
        if (rb_verde.isChecked())
            return R.drawable.fondo_etiqueta_lightgreen;
        if (rb_naranja.isChecked())
            return R.drawable.fondo_etiqueta_orange;
        if (rb_rosa.isChecked())
            return R.drawable.fondo_etiqueta_pink;
        if (rb_morado.isChecked())
            return R.drawable.fondo_etiqueta_purple;
        return 0;
    }
}