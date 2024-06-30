package com.example.checkall.tareas;


import com.example.checkall.R;
import com.example.checkall.etiquetas.Etiqueta;

public class Tarea {
    private int id;
    private String name, descripcion, fecha, prioridad;
    private boolean checked;
    private Etiqueta etiqueta;

    public Tarea(int id, String name, String descripcion, String fecha, String prioridad, Etiqueta etiqueta, boolean checked) {
        this.id = id;
        this.name = name;
        this.descripcion = descripcion;
        this.fecha = fecha;
        this.prioridad = prioridad;
        this.etiqueta = etiqueta;
        this.checked = checked;
    }
    //Getter y Setter
    public int getId() {
        return id;
    }
    public String getName() {
        return name;
    }
    public String getFecha() {
        return fecha;
    }
    public Etiqueta getEtiqueta() {
        return etiqueta;
    }
    public String getDescripcion() {
        return descripcion;
    }

    public String getPrioridad() {
        return prioridad;
    }

    public void setId(int id) {
        this.id = id;
    }
    public void setName(String name) {
        this.name = name;
    }
    public void setChecked(boolean checked) {
        this.checked = checked;
    }
    public void setFecha(String date) {
        this.fecha = fecha;
    }
    public void setEtiqueta(Etiqueta etiqueta) {
        this.etiqueta = etiqueta;
    }

    public void setPrioridad(String prioridad) {
        this.prioridad = prioridad;
    }

    //Otros métodos
    public boolean isChecked() {
        return checked;
    }
    public int getFondoPrioridad(String prioridad) {
        int color = 0;
        switch (prioridad) {
            case "Alta":
                color = R.drawable.fondo_prioridad_alta;
                break;
            case "Media":
                color = R.drawable.fondo_prioridad_media;
                break;
            case "Baja":
                color = R.drawable.fondo_prioridad_baja;
                break;
        }
        return color;
    }
}
