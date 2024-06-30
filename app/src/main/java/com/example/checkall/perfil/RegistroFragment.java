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

public class RegistroFragment extends Fragment {

    private Button btnRegistrarse;
    private EditText et_usuario, et_contrasena;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_registro, container, false);

        //Enlazamos
        btnRegistrarse = view.findViewById(R.id.btn_registro2);
        et_usuario = view.findViewById(R.id.et_nickname_register);
        et_contrasena = view.findViewById(R.id.et_password2);

        //Evento click
        btnRegistrarse.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Obtener datos
                String user = et_usuario.getText().toString();
                String password = et_contrasena.getText().toString();

                //Validar
                if(user.isEmpty() || password.isEmpty()){
                    //Mostrar mensaje
                    et_usuario.setError("Campo requerido");
                    et_contrasena.setError("Campo requerido");
                }else{
                    //Guardar datos
                    SharedPreferences preferences = getActivity().getSharedPreferences("my_preferences", MODE_PRIVATE);
                    SharedPreferences.Editor editor = preferences.edit();
                    editor.putString("user", user);
                    editor.putString("password", password);
                    editor.putInt("profile_image", 0);
                    editor.putInt("logueado", 1);
                    editor.putInt("tareas_registradas", 0);
                    editor.putInt("tareas_completadas", 0);
                    editor.apply();

                    //Eliminar datos anteriores de las dos tablas de la base de datos (tareas y etiquetas)
                    AdminSQLiteOpenHelper admin = new AdminSQLiteOpenHelper(getActivity(), "gestion", null, 1);
                    admin.eliminarTareas(getContext());
                    admin.eliminarEtiquetas(getContext());

                    //Abrir fragmento de user
                    getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.frame_layout, new UserFragment()).commit();
                }
            }
        });
        return view;
    }
}