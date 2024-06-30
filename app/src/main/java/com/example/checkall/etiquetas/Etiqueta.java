package com.example.checkall.etiquetas;

public class Etiqueta {
    private String name;
    private int colorId;

    public Etiqueta(String name, int colorId) {
        this.name = name;
        this.colorId = colorId;
    }

    public String getName() {
        return name;
    }
    public int getColorId() {
        return colorId;
    }
    public void setName(String name) {
        this.name = name;
    }
    public void setColorId(int colorId) {
        this.colorId = colorId;
    }
    public boolean isEmpty() {
        return name == null || colorId == 0;
    }
}
