package com.example.checkall;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class AdminSQLiteOpenHelper extends SQLiteOpenHelper {
    public AdminSQLiteOpenHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    @Override
    public void onCreate(SQLiteDatabase miBBDD) {
        miBBDD.execSQL("create table tareas (codigo int primary key, nombre text, descripcion text, fecha text, prioridad text, etiqueta text, colorEtiqueta int, checkbox int, user text)");
        miBBDD.execSQL("create table etiquetas (codigo int primary key, nombre text, color int, user text)");
        miBBDD.execSQL("create table recordatorios (codTask int primary key, nombre text, fecha text, hora text)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
    }

    public int getSizeTabla(String nombreTabla, Context context) {
        int size = 0;
        SQLiteDatabase bd = null;
        Cursor cursor = null;

        try {
            // Abre la base de datos en modo lectura
            AdminSQLiteOpenHelper dbHelper = new AdminSQLiteOpenHelper(context, "gestion", null, 1);
            bd = dbHelper.getReadableDatabase();

            // Ejecuta la consulta para obtener el número de filas en la tabla
            String consulta = "SELECT COUNT(*) FROM " + nombreTabla;
            cursor = bd.rawQuery(consulta, null);

            // Mueve el cursor al primer resultado
            if (cursor.moveToFirst()) {
                size = cursor.getInt(0); // El resultado estará en la primera columna
            }
        } finally {
            // Asegúrate de cerrar el cursor y la base de datos después de usarlos
            if (cursor != null) {
                cursor.close();
            }
            if (bd != null) {
                bd.close();
            }
        }

        return size;
    }

    public void borrarRecordatorio(int id, Context context) {
        SQLiteDatabase bd = null;

        try {
            // Abre la base de datos en modo escritura
            AdminSQLiteOpenHelper dbHelper = new AdminSQLiteOpenHelper(context, "gestion", null, 1);
            bd = dbHelper.getWritableDatabase();

            // Ejecuta la consulta para borrar el recordatorio
            bd.delete("recordatorios", "codTask = " + id, null);
        } finally {
            // Asegúrate de cerrar la base de datos después de usarla
            if (bd != null) {
                bd.close();
            }
        }
    }

    public boolean comprobarNombre(String tablaLabels, String nombre, String nombre1, Context context) {
        boolean existe = false;
        SQLiteDatabase bd = null;
        Cursor cursor = null;

        try {
            // Abre la base de datos en modo lectura
            AdminSQLiteOpenHelper dbHelper = new AdminSQLiteOpenHelper(context, "gestion", null, 1);
            bd = dbHelper.getReadableDatabase();

            // Ejecuta la consulta para comprobar si existe un nombre igual
            String consulta = "SELECT * FROM " + tablaLabels + " WHERE " + nombre + " = '" + nombre1 + "'";
            cursor = bd.rawQuery(consulta, null);

            // Mueve el cursor al primer resultado
            if (cursor.moveToFirst()) {
                existe = true;
            }
        } finally {
            // Asegúrate de cerrar el cursor y la base de datos después de usarlos
            if (cursor != null) {
                cursor.close();
            }
            if (bd != null) {
                bd.close();
            }
        }

        return existe;
    }

    public void eliminarEtiquetas(Context context) {
        SQLiteDatabase bd = null;

        try {
            // Abre la base de datos en modo escritura
            AdminSQLiteOpenHelper dbHelper = new AdminSQLiteOpenHelper(context, "gestion", null, 1);
            bd = dbHelper.getWritableDatabase();

            // Ejecuta la consulta para borrar la etiqueta
            bd.delete("etiquetas", null, null);
        } finally {
            // Asegúrate de cerrar la base de datos después de usarla
            if (bd != null) {
                bd.close();
            }
        }
    }
    public void eliminarTareas(Context context) {
        SQLiteDatabase bd = null;

        try {
            // Abre la base de datos en modo escritura
            AdminSQLiteOpenHelper dbHelper = new AdminSQLiteOpenHelper(context, "gestion", null, 1);
            bd = dbHelper.getWritableDatabase();

            // Ejecuta la consulta para borrar todas las tareas
            bd.delete("tareas", null, null);
        } finally {
            // Asegúrate de cerrar la base de datos después de usarla
            if (bd != null) {
                bd.close();
            }
        }
    }

    public void eliminarTareasNull(Context context) {
        SQLiteDatabase bd = null;

        try {
            // Abre la base de datos en modo escritura
            AdminSQLiteOpenHelper dbHelper = new AdminSQLiteOpenHelper(context, "gestion", null, 1);
            bd = dbHelper.getWritableDatabase();

            // Ejecuta la consulta para borrar todas las tareas con usuario null
            bd.delete("tareas", "user = ?", new String[]{""});
        } finally {
            // Asegúrate de cerrar la base de datos después de usarla
            if (bd != null) {
                bd.close();
            }
        }
    }

    public void eliminarEtiquetasNull(Context context) {
        SQLiteDatabase bd = null;

        try {
            // Abre la base de datos en modo escritura
            AdminSQLiteOpenHelper dbHelper = new AdminSQLiteOpenHelper(context, "gestion", null, 1);
            bd = dbHelper.getWritableDatabase();

            // Ejecuta la consulta para borrar todas las tareas con usuario null
            bd.delete("etiquetas", "user = ?", new String[]{""});
        } finally {
            // Asegúrate de cerrar la base de datos después de usarla
            if (bd != null) {
                bd.close();
            }
        }
    }
}
