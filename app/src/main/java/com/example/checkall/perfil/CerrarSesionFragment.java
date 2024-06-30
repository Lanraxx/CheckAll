package com.example.checkall.perfil;

import static android.content.Context.MODE_PRIVATE;

import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.example.checkall.R;
import com.example.checkall.SettingsFragment;

public class CerrarSesionFragment extends Fragment {

    private Button btn_si_cerrar, btn_no_cerrar;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_cerrar_sesion, container, false);

        //Enlazamos
        btn_si_cerrar = (Button) view.findViewById(R.id.btn_si_cerrar);
        btn_no_cerrar = (Button) view.findViewById(R.id.btn_no_cerrar);

        //Funcionalidad botón de cerrar sesión
        btn_si_cerrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Eliminamos datos del SharedPreferences
                SharedPreferences sharedPreferences = v.getContext().getSharedPreferences("my_preferences", MODE_PRIVATE);
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putInt("logueado", 0);
                editor.apply();
                getParentFragmentManager().beginTransaction().replace(R.id.frame_layout, new SettingsFragment()).addToBackStack(null).commit();
            }
        });

        //Funcionalidad botón de no cerrar sesión
        btn_no_cerrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getParentFragmentManager().beginTransaction().replace(R.id.frame_layout, new SettingsFragment()).addToBackStack(null).commit();
            }
        });
        return view;
    }
}