package com.example.aplicaciondeimpuestosdeltfg;

public class Prestamo {
    private int mes;
    private double tipoInteres;
    private double cuota;
    private double amortizado;
    private double intereses;
    private double capitalPendiente;

    public Prestamo(int mes, double tipoInteres, double cuota,
                         double amortizado, double intereses, double capitalPendiente) {
        this.mes = mes;
        this.tipoInteres = tipoInteres;
        this.cuota = cuota;
        this.amortizado = amortizado;
        this.intereses = intereses;
        this.capitalPendiente = capitalPendiente;
    }


    // Getters (necesarios para mostrar los datos)
    public int getNumMeses() { return mes; }
    public double getTipoInteres() { return tipoInteres; }
    public double getCuota() { return cuota; }
    public double getAmortizacion() { return amortizado; }
    public double getIntereses() { return intereses; }
    public double getCapitalPendiente() { return capitalPendiente; }


}

