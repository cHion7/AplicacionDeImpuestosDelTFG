package com.example.aplicaciondeimpuestosdeltfg.gestor;

import java.util.Calendar;

public class Evento {
    public enum Tipo { GASTO, COBRO }

    private Calendar fecha;
    private Tipo tipo;

    public Evento(Calendar fecha, Tipo tipo) {
        this.fecha = fecha;
        this.tipo = tipo;
    }

    public Calendar getFecha() {
        return fecha;
    }

    public Tipo getTipo() {
        return tipo;
    }
}