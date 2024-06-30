package com.example.checkall.perfil;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.checkall.R;

public class UserFragment extends Fragment {

    private Button btn_editar;
    private TextView txt_nickname;
    private ImageView img_avatar;
    private TextView txt_tareas_registradas, txt_tareas_completadas;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_user, container, false);

        //Enlazamos
        btn_editar = (Button) rootView.findViewById(R.id.btn_editar_perfil);
        txt_nickname = (TextView) rootView.findViewById(R.id.txt_nombre_perfil);
        img_avatar = (ImageView) rootView.findViewById(R.id.img_perfil);
        txt_tareas_registradas = (TextView) rootView.findViewById(R.id.txt_tareas_registradas);
        txt_tareas_completadas = (TextView) rootView.findViewById(R.id.txt_tareas_completadas);

        SharedPreferences sharedPreferences = rootView.getContext().getSharedPreferences("my_preferences", Context.MODE_PRIVATE);
        txt_nickname.setText(sharedPreferences.getString("user", ""));
        //Si el avatar no está vacío, se muestra la imagen elegida, si no, se muestra la imagen por defecto
        if (sharedPreferences.getInt("profile_image", 0) != 0) {
            //Recogemos los datos del SharedPreferences
            img_avatar.setImageResource(sharedPreferences.getInt("profile_image", 0));
        }

        // Recogemos los datos de las tareas registradas y completadas
        int tareas_registradas = sharedPreferences.getInt("tareas_registradas", 0);
        int tareas_completadas = sharedPreferences.getInt("tareas_completadas", 0);
        txt_tareas_registradas.setText("Tareas registradas: " + tareas_registradas);
        txt_tareas_completadas.setText("Tareas completadas: " + tareas_completadas);

        //Funcionalidad botón de editar perfil
        btn_editar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getParentFragmentManager().beginTransaction().add(R.id.frame_layout, new EditarPerfilFragment()).addToBackStack(null).commit();
            }
        });

        return rootView;
    }
}