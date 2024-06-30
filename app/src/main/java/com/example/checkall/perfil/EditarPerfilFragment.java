package com.example.checkall.perfil;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.checkall.R;

public class EditarPerfilFragment extends Fragment {

    private ImageView img_user1, img_user2, img_user3, img_user4, img_user5, img_user6;
    private ImageView img_user1_seleccionado, img_user2_seleccionado, img_user3_seleccionado, img_user4_seleccionado, img_user5_seleccionado, img_user6_seleccionado;
    private int imgPulsada = 0;
    private EditText et_nickname;
    private Button btn_cancelar, btn_guardar;
    private int avatarSeleccionado = 0;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_editar_perfil, container, false);

        //Enlazamos
        img_user1 = (ImageView) rootView.findViewById(R.id.img_user1);
        img_user2 = (ImageView) rootView.findViewById(R.id.img_user2);
        img_user3 = (ImageView) rootView.findViewById(R.id.img_user3);
        img_user4 = (ImageView) rootView.findViewById(R.id.img_user4);
        img_user5 = (ImageView) rootView.findViewById(R.id.img_user5);
        img_user6 = (ImageView) rootView.findViewById(R.id.img_user6);
        et_nickname = (EditText) rootView.findViewById(R.id.et_nickname);
        btn_cancelar = (Button) rootView.findViewById(R.id.btn_cancelar_edicion_perfil);
        btn_guardar = (Button) rootView.findViewById(R.id.btn_aceptar_edicion_perfil);
        img_user1_seleccionado = (ImageView) rootView.findViewById(R.id.img_user1_seleccionado);
        img_user2_seleccionado = (ImageView) rootView.findViewById(R.id.img_user2_seleccionado);
        img_user3_seleccionado = (ImageView) rootView.findViewById(R.id.img_user3_seleccionado);
        img_user4_seleccionado = (ImageView) rootView.findViewById(R.id.img_user4_seleccionado);
        img_user5_seleccionado = (ImageView) rootView.findViewById(R.id.img_user5_seleccionado);
        img_user6_seleccionado = (ImageView) rootView.findViewById(R.id.img_user6_seleccionado);

        // Se oculta el círculo de selección de todos los avatares al inicio
        img_user1_seleccionado.setVisibility(View.INVISIBLE);
        img_user2_seleccionado.setVisibility(View.INVISIBLE);
        img_user3_seleccionado.setVisibility(View.INVISIBLE);
        img_user4_seleccionado.setVisibility(View.INVISIBLE);
        img_user5_seleccionado.setVisibility(View.INVISIBLE);
        img_user6_seleccionado.setVisibility(View.INVISIBLE);


        //Dejamos seleccionaas las opciones que hay guardadas
        SharedPreferences sharedPreferences = rootView.getContext().getSharedPreferences("my_preferences", Context.MODE_PRIVATE);
        et_nickname.setText(sharedPreferences.getString("user", ""));

        //Funcionalidad botón de cancelar acción
        btn_cancelar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getParentFragmentManager().popBackStack();
            }
        });

        //Funcionalidad botón guardar acción
        btn_guardar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if ((!et_nickname.getText().toString().equals("")) && (imgPulsada != 0)) {
                    SharedPreferences sharedPreferences = rootView.getContext().getSharedPreferences("my_preferences", Context.MODE_PRIVATE);
                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    editor.putString("user", et_nickname.getText().toString());
                    editor.putInt("profile_image", imgPulsada);
                    editor.apply();
                    getParentFragmentManager().beginTransaction().replace(R.id.frame_layout, new UserFragment()).commit();
                }
                else
                    Toast.makeText(getContext(), "Faltan campos por rellenar", Toast.LENGTH_SHORT).show();
            }
        });

        // Eventos click para seleccionar avatar
        img_user1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                seleccionAvatar(1);
                imgPulsada = R.drawable.user1;
            }
        });
        img_user2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                seleccionAvatar(2);
                imgPulsada = R.drawable.user2;
            }
        });
        img_user3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                seleccionAvatar(3);
                imgPulsada = R.drawable.user3;
            }
        });
        img_user4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                seleccionAvatar(4);
                imgPulsada = R.drawable.user4;
            }
        });
        img_user5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                seleccionAvatar(5);
                imgPulsada = R.drawable.user5;
            }
        });
        img_user6.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                seleccionAvatar(6);
                imgPulsada = R.drawable.user6;
            }
        });


        return rootView;
    }

    public void seleccionAvatar (int clickAvatar) {
        switch (avatarSeleccionado) {
            case 1:
                switch (clickAvatar) {
                    case 1:
                        break;
                    case 2:
                        img_user1_seleccionado.setVisibility(View.INVISIBLE);
                        img_user2_seleccionado.setVisibility(View.VISIBLE);
                        avatarSeleccionado = 2;
                        break;
                    case 3:
                        img_user1_seleccionado.setVisibility(View.INVISIBLE);
                        img_user3_seleccionado.setVisibility(View.VISIBLE);
                        avatarSeleccionado = 3;
                        break;
                    case 4:
                        img_user1_seleccionado.setVisibility(View.INVISIBLE);
                        img_user4_seleccionado.setVisibility(View.VISIBLE);
                        avatarSeleccionado = 4;
                        break;
                    case 5:
                        img_user1_seleccionado.setVisibility(View.INVISIBLE);
                        img_user5_seleccionado.setVisibility(View.VISIBLE);
                        avatarSeleccionado = 5;
                        break;
                    case 6:
                        img_user1_seleccionado.setVisibility(View.INVISIBLE);
                        img_user6_seleccionado.setVisibility(View.VISIBLE);
                        avatarSeleccionado = 6;
                        break;
                }
            case 2:
                switch (clickAvatar) {
                    case 1:
                        img_user2_seleccionado.setVisibility(View.INVISIBLE);
                        img_user1_seleccionado.setVisibility(View.VISIBLE);
                        avatarSeleccionado = 1;
                        break;
                    case 2:
                        break;
                    case 3:
                        img_user2_seleccionado.setVisibility(View.INVISIBLE);
                        img_user3_seleccionado.setVisibility(View.VISIBLE);
                        avatarSeleccionado = 3;
                        break;
                    case 4:
                        img_user2_seleccionado.setVisibility(View.INVISIBLE);
                        img_user4_seleccionado.setVisibility(View.VISIBLE);
                        avatarSeleccionado = 4;
                        break;
                    case 5:
                        img_user2_seleccionado.setVisibility(View.INVISIBLE);
                        img_user5_seleccionado.setVisibility(View.VISIBLE);
                        avatarSeleccionado = 5;
                        break;
                    case 6:
                        img_user2_seleccionado.setVisibility(View.INVISIBLE);
                        img_user6_seleccionado.setVisibility(View.VISIBLE);
                        avatarSeleccionado = 6;
                        break;
                }
            case 3:
                switch (clickAvatar) {
                    case 1:
                        img_user3_seleccionado.setVisibility(View.INVISIBLE);
                        img_user1_seleccionado.setVisibility(View.VISIBLE);
                        avatarSeleccionado = 1;
                        break;
                    case 2:
                        img_user3_seleccionado.setVisibility(View.INVISIBLE);
                        img_user2_seleccionado.setVisibility(View.VISIBLE);
                        avatarSeleccionado = 2;
                        break;
                    case 3:
                        break;
                    case 4:
                        img_user3_seleccionado.setVisibility(View.INVISIBLE);
                        img_user4_seleccionado.setVisibility(View.VISIBLE);
                        avatarSeleccionado = 4;
                        break;
                    case 5:
                        img_user3_seleccionado.setVisibility(View.INVISIBLE);
                        img_user5_seleccionado.setVisibility(View.VISIBLE);
                        avatarSeleccionado = 5;
                        break;
                    case 6:
                        img_user3_seleccionado.setVisibility(View.INVISIBLE);
                        img_user6_seleccionado.setVisibility(View.VISIBLE);
                        avatarSeleccionado = 6;
                        break;
                }
            case 4:
                switch (clickAvatar) {
                    case 1:
                        img_user4_seleccionado.setVisibility(View.INVISIBLE);
                        img_user1_seleccionado.setVisibility(View.VISIBLE);
                        avatarSeleccionado = 1;
                        break;
                    case 2:
                        img_user4_seleccionado.setVisibility(View.INVISIBLE);
                        img_user2_seleccionado.setVisibility(View.VISIBLE);
                        avatarSeleccionado = 2;
                        break;
                    case 3:
                        img_user4_seleccionado.setVisibility(View.INVISIBLE);
                        img_user3_seleccionado.setVisibility(View.VISIBLE);
                        avatarSeleccionado = 3;
                        break;
                    case 4:
                        break;
                    case 5:
                        img_user4_seleccionado.setVisibility(View.INVISIBLE);
                        img_user5_seleccionado.setVisibility(View.VISIBLE);
                        avatarSeleccionado = 5;
                        break;
                    case 6:
                        img_user4_seleccionado.setVisibility(View.INVISIBLE);
                        img_user6_seleccionado.setVisibility(View.VISIBLE);
                        avatarSeleccionado = 6;
                        break;
                }
            case 5:
                switch (clickAvatar) {
                    case 1:
                        img_user5_seleccionado.setVisibility(View.INVISIBLE);
                        img_user1_seleccionado.setVisibility(View.VISIBLE);
                        avatarSeleccionado = 1;
                        break;
                    case 2:
                        img_user5_seleccionado.setVisibility(View.INVISIBLE);
                        img_user2_seleccionado.setVisibility(View.VISIBLE);
                        avatarSeleccionado = 2;
                        break;
                    case 3:
                        img_user5_seleccionado.setVisibility(View.INVISIBLE);
                        img_user3_seleccionado.setVisibility(View.VISIBLE);
                        avatarSeleccionado = 3;
                        break;
                    case 4:
                        img_user5_seleccionado.setVisibility(View.INVISIBLE);
                        img_user4_seleccionado.setVisibility(View.VISIBLE);
                        avatarSeleccionado = 4;
                        break;
                    case 5:
                        break;
                    case 6:
                        img_user5_seleccionado.setVisibility(View.INVISIBLE);
                        img_user6_seleccionado.setVisibility(View.VISIBLE);
                        avatarSeleccionado = 6;
                        break;
                }
            case 6:
                switch (clickAvatar) {
                    case 1:
                        img_user6_seleccionado.setVisibility(View.INVISIBLE);
                        img_user1_seleccionado.setVisibility(View.VISIBLE);
                        avatarSeleccionado = 1;
                        break;
                    case 2:
                        img_user6_seleccionado.setVisibility(View.INVISIBLE);
                        img_user2_seleccionado.setVisibility(View.VISIBLE);
                        avatarSeleccionado = 2;
                        break;
                    case 3:
                        img_user6_seleccionado.setVisibility(View.INVISIBLE);
                        img_user3_seleccionado.setVisibility(View.VISIBLE);
                        avatarSeleccionado = 3;
                        break;
                    case 4:
                        img_user6_seleccionado.setVisibility(View.INVISIBLE);
                        img_user4_seleccionado.setVisibility(View.VISIBLE);
                        avatarSeleccionado = 4;
                        break;
                    case 5:
                        img_user6_seleccionado.setVisibility(View.INVISIBLE);
                        img_user5_seleccionado.setVisibility(View.VISIBLE);
                        avatarSeleccionado = 5;
                        break;
                    case 6:
                        break;
                }
            case 0:
                switch (clickAvatar) {
                    case 1:
                        img_user1_seleccionado.setVisibility(View.VISIBLE);
                        avatarSeleccionado = 1;
                        break;
                    case 2:
                        img_user2_seleccionado.setVisibility(View.VISIBLE);
                        avatarSeleccionado = 2;
                        break;
                    case 3:
                        img_user3_seleccionado.setVisibility(View.VISIBLE);
                        avatarSeleccionado = 3;
                        break;
                    case 4:
                        img_user4_seleccionado.setVisibility(View.VISIBLE);
                        avatarSeleccionado = 4;
                        break;
                    case 5:
                        img_user5_seleccionado.setVisibility(View.VISIBLE);
                        avatarSeleccionado = 5;
                        break;
                    case 6:
                        img_user6_seleccionado.setVisibility(View.VISIBLE);
                        avatarSeleccionado = 6;
                        break;
                }
        }
    }
}