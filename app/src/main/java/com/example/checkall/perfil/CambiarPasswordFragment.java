package com.example.checkall.perfil;

import static android.content.Context.MODE_PRIVATE;

import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.checkall.R;
import com.example.checkall.SettingsFragment;

public class CambiarPasswordFragment extends Fragment {

    private Button btn_cambiar_password;
    private EditText et_new_password, et_old_password;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_cambiar_password, container, false);

        //Enlazamos
        btn_cambiar_password = view.findViewById(R.id.btn_change);
        et_new_password = view.findViewById(R.id.et_new_password);
        et_old_password = view.findViewById(R.id.et_old_password);

        //Evento click
        btn_cambiar_password.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Modificar la contraseña del SharedPreferences
                SharedPreferences sharedPreferences = v.getContext().getSharedPreferences("my_preferences", MODE_PRIVATE);
                SharedPreferences.Editor editor = sharedPreferences.edit();
                if (et_old_password.getText().toString().equals(sharedPreferences.getString("password", ""))) {
                    editor.putString("password", et_new_password.getText().toString());
                    editor.apply();
                } else {
                    Toast.makeText(v.getContext(), "Contraseña incorrecta", Toast.LENGTH_SHORT).show();
                    return;
                }
                getParentFragmentManager().beginTransaction().replace(R.id.frame_layout, new SettingsFragment()).addToBackStack(null).commit();
            }
        });
        return view;
    }
}