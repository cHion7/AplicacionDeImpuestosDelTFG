package com.example.aplicaciondeimpuestosdeltfg.gestor;

import com.google.firebase.database.Exclude;

import java.util.Calendar;

public class Evento {
    private String dinero;
    private String cobroOGasto;
    private String categoria;
    private long fechaMillis;
    private String titulo;
    private String descripcion;
    private String key;

    public Evento() {
    }

    public Evento(String dinero, String cobroOGasto, String categoria, String titulo, String descripcion, Calendar fecha) {
        this.dinero = dinero;
        this.cobroOGasto = cobroOGasto;
        this.categoria = categoria;
        this.titulo = titulo;
        this.descripcion = descripcion;
        this.fechaMillis = fecha.getTimeInMillis();  // Agregado
    }
    // Getters y Setters
    public String getTitulo() { return titulo; }
    public void setTitulo(String titulo) { this.titulo = titulo; }

    public String getDescripcion() { return descripcion; }
    public void setDescripcion(String descripcion) { this.descripcion = descripcion; }

    public String getCategoria() { return categoria; }
    public void setCategoria(String categoria) { this.categoria = categoria; }

    public String getCobroOGasto() { return cobroOGasto; }
    public void setCobroOGasto(String cobroOGasto) { this.cobroOGasto = cobroOGasto; }

    public String getDinero() { return dinero; }
    public void setDinero(String dinero) { this.dinero = dinero; }

    public long getFechaMillis() { return fechaMillis; }
    public void setFechaMillis(long fechaMillis) { this.fechaMillis = fechaMillis; }
    public String getKey() {
        return key;
    }
    public void setKey(String key) {
        this.key = key;
    }
}