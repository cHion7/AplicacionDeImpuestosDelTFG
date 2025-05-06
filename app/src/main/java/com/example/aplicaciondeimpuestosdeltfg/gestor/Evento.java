package com.example.aplicaciondeimpuestosdeltfg.gestor;

import java.util.Calendar;

public class Evento {
    private String dinero;
    public enum Tipo { GASTO, COBRO }
    private String categoria;
    private Calendar fecha;
    private Tipo tipo;
    private String titulo;
    private String descripcion;

    public Evento(String dinero, String categoria, Calendar fecha, Tipo tipo, String titulo, String descripcion) {
        this.dinero = dinero;
        this.categoria = categoria;
        this.fecha = fecha;
        this.tipo = tipo;
        this.titulo = titulo;
        this.descripcion = descripcion;
    }

    public Calendar getFecha() {
        return fecha;
    }

    public Tipo getTipo() {
        return tipo;
    }

    public String getDinero() {
        return dinero;
    }

    public void setDinero(String dinero) {
        this.dinero = dinero;
    }

    public String getCategoria() {
        return categoria;
    }

    public void setCategoria(String categoria) {
        this.categoria = categoria;
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }
}