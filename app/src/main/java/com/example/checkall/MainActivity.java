package com.example.checkall;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.view.View;

import com.example.checkall.calendario.CalendarFragment;
import com.example.checkall.databinding.ActivityMainBinding;
import com.example.checkall.perfil.SinLoguearFragment;
import com.example.checkall.perfil.UserFragment;
import com.example.checkall.tareas.HomeFragment;
import com.example.checkall.tareas.NuevaEditarTareaFragment;
import com.jakewharton.threetenabp.AndroidThreeTen;


public class MainActivity extends AppCompatActivity {

    ActivityMainBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        AndroidThreeTen.init(this);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        createNotificationChannel();

        replaceFragment(new HomeFragment());
        binding.bottomNavigationView.setBackground(null);

        binding.bottomNavigationView.setOnItemSelectedListener(item -> {
            SharedPreferences preferences = getSharedPreferences("my_preferences", MODE_PRIVATE);
            boolean logueado = preferences.getInt("logueado", 0) == 1;
            System.out.println("logueado: " + logueado);

            if (item.getItemId() == R.id.home)
                replaceFragment(new HomeFragment());
            if (item.getItemId() == R.id.calendar)
                replaceFragment(new CalendarFragment());
            if (item.getItemId() == R.id.settings)
                replaceFragment(new SettingsFragment());
            if ((item.getItemId() == R.id.user) && (logueado))
                replaceFragment(new UserFragment());
            if ((item.getItemId() == R.id.user) && (!logueado))
                replaceFragment(new SinLoguearFragment());
            return true;
        });

        binding.fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AdminSQLiteOpenHelper admin = new AdminSQLiteOpenHelper(v.getContext(), "gestion", null, 1);
                int proxId = admin.getSizeTabla("tareas", v.getContext()) + 1;
                replaceFragment(new NuevaEditarTareaFragment(proxId, "HomeFragment"));
            }
        });

        SharedPreferences sharedPreferences = getSharedPreferences("my_preferences", Context.MODE_PRIVATE);
        // Obtener el tema almacenado en las preferencias compartidas
        if (sharedPreferences.getBoolean("darkMode", false)) {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
        } else {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        }
    }

    private void replaceFragment(Fragment fragment) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.frame_layout, fragment);
        fragmentTransaction.commit();
    }

    private void createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            CharSequence name = "Tarea Reminder";
            String description = "Channel for Tarea Reminder";
            int importance = NotificationManager.IMPORTANCE_HIGH;
            NotificationChannel channel = new NotificationChannel("TASK_REMINDER_CHANNEL", name, importance);
            channel.setDescription(description);

            NotificationManager notificationManager = getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
        }
    }

}