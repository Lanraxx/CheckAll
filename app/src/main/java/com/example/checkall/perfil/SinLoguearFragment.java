package com.example.checkall.perfil;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.example.checkall.R;

public class SinLoguearFragment extends Fragment {

    private Button btnIniciarSesion;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_sin_loguear, container, false);

        //Enlazamos
        btnIniciarSesion = view.findViewById(R.id.btn_login);

        //Evento click
        btnIniciarSesion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Abrir fragmento de login
                getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.frame_layout, new LoginFragment()).commit();
            }
        });

        return view;
    }
}