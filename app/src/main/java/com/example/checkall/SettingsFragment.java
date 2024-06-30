package com.example.checkall;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatDelegate;
import androidx.appcompat.widget.SwitchCompat;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.TextView;

import com.example.checkall.perfil.CambiarPasswordFragment;
import com.example.checkall.perfil.CerrarSesionFragment;

public class SettingsFragment extends Fragment {

    private Button btn_cerrar_sesion, btn_cambiar_password;
    private TextView txt_sesion, txt_password;
    private SwitchCompat themeSwitch;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_settings, container, false);

        //Enlazamos
        btn_cerrar_sesion = (Button) view.findViewById(R.id.btn_cerrar_sesion);
        btn_cambiar_password = (Button) view.findViewById(R.id.btn_cambiar_password);
        txt_sesion = (TextView) view.findViewById(R.id.txt_sesion);
        txt_password = (TextView) view.findViewById(R.id.txt_cambiar_password);
        themeSwitch = (SwitchCompat) view.findViewById(R.id.themeSwitch);

        //Ocultar parte de la sesion en caso de que no haya un inicio activo
        SharedPreferences sharedPreferences = view.getContext().getSharedPreferences("my_preferences", Context.MODE_PRIVATE);
        if (sharedPreferences.getInt("logueado", 0) == 0) {
            txt_sesion.setVisibility(View.GONE);
            btn_cerrar_sesion.setVisibility(View.GONE);
            txt_password.setVisibility(View.GONE);
            btn_cambiar_password.setVisibility(View.GONE);
        }

        // Obtener el tema almacenado en las preferencias compartidas
        if (sharedPreferences.getBoolean("darkMode", false)) {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
        } else {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        }

        // Establecer el estado inicial del Switch basado en las preferencias compartidas
        themeSwitch.setChecked(sharedPreferences.getBoolean("darkMode", false));
        // Funcionalidad del Switch de cambiar de modo claro a oscuro y viceversa
        themeSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
                } else {
                    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
                }
                // Guardar el estado del tema en las preferencias compartidas
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putBoolean("darkMode", isChecked);
                editor.apply();
            }
        });

        //Funcionalidad botón de cerrar sesión
        btn_cerrar_sesion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getParentFragmentManager().beginTransaction().add(R.id.frame_layout, new CerrarSesionFragment()).addToBackStack(null).commit();
            }
        });

        //Funcionalidad botón de cambiar contraseña
        btn_cambiar_password.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Abrir fragmento de cambiar contraseña
                getParentFragmentManager().beginTransaction().add(R.id.frame_layout, new CambiarPasswordFragment()).addToBackStack(null).commit();
            }
        });
        return view;
    }
}