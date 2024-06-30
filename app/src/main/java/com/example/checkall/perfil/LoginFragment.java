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

import com.example.checkall.AdminSQLiteOpenHelper;
import com.example.checkall.R;

public class LoginFragment extends Fragment {

    private Button btnIniciarSesion, btnRegistrarse;
    private EditText etUsuario, etContrasena;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_login, container, false);

        //Enlazamos
        btnIniciarSesion = view.findViewById(R.id.btn_login2);
        btnRegistrarse = view.findViewById(R.id.btn_registro);
        etUsuario = view.findViewById(R.id.et_nickname_login);
        etContrasena = view.findViewById(R.id.et_password);

        //Evento click
        btnIniciarSesion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Obtener datos
                String user = etUsuario.getText().toString();
                String password = etContrasena.getText().toString();

                //Validar
                if(user.isEmpty() || password.isEmpty()){
                    //Mostrar mensaje
                    etUsuario.setError("Campo requerido");
                    etContrasena.setError("Campo requerido");
                }else{
                    //Verificar datos
                    SharedPreferences preferences = getActivity().getSharedPreferences("my_preferences", MODE_PRIVATE);
                    if (user.equals(preferences.getString("user", "")) && password.equals(preferences.getString("password", ""))) {
                        //Guardar login
                        SharedPreferences.Editor editor = preferences.edit();
                        editor.putInt("logueado", 1);
                        editor.apply();

                        //Eliminar tareas con el usuario null
                        AdminSQLiteOpenHelper admin = new AdminSQLiteOpenHelper(getContext(), "gestion", null, 1);
                        admin.eliminarTareasNull(getContext());
                        //Eliminar etiquetas con el usuario null
                        admin.eliminarEtiquetasNull(getContext());

                        //Abrir fragmento de user
                        getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.frame_layout, new UserFragment()).commit();
                    } else {
                        //Mostrar mensaje
                        etUsuario.setError("Usuario o contraseña incorrectos");
                        etContrasena.setError("Usuario o contraseña incorrectos");
                    }
                }
            }
        });

        //Evento click
        btnRegistrarse.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Abrir fragmento de registro
                getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.frame_layout, new RegistroFragment()).commit();
            }
        });
        return view;
    }
}