package com.example.aplicaciondeimpuestosdeltfg.vistas;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.Gravity;
import android.widget.Button;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.aplicaciondeimpuestosdeltfg.R;

import java.util.ArrayList;

public class tablaPrestamo extends AppCompatActivity {

    TableLayout tlTabla;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tabla_prestamo);

        tlTabla = findViewById(R.id.tlTabla);
        Button btnVolver = findViewById(R.id.btnVolver);

        btnVolver.setOnClickListener(v -> finish());

        Intent intent = getIntent();
        float capital = intent.getFloatExtra("capital", 0);
        float plazo = intent.getFloatExtra("plazo", 0);
        float interes = intent.getFloatExtra("interes", 0);
        float interesPosterior = intent.getFloatExtra("interesPosterior", 0);
        float plazoConCambio = intent.getFloatExtra("plazoConCambio", 0);
        boolean tieneCambio = intent.getBooleanExtra("tieneCambio", false);
        ArrayList<Prestamo> listaDePrestamo = creacionArrays(capital, plazo, interes, interesPosterior, plazoConCambio, tieneCambio);

        agregarFila("Mes", "Interés (%)", "Cuota", "Intereses", "Capital Pendiente", true);


        for (Prestamo p : listaDePrestamo) {
            agregarFila(
                    String.valueOf(p.getNumMeses()),
                    String.format("%.2f", p.getTipoInteres()),
                    String.format("%.2f", p.getCuota()),
                    String.format("%.2f", p.getIntereses()),
                    String.format("%.2f", p.getCapitalPendiente()),
                    false
            );
        }
    }

    private void agregarFila(String mes, String interes, String cuota, String intereses, String capitalPendiente, boolean esEncabezado) {
        TableRow fila = new TableRow(this);
        fila.setPadding(4, 4, 4, 4);

        TextView[] celdas = {
                crearCelda(mes, esEncabezado),
                crearCelda(interes, esEncabezado),
                crearCelda(cuota, esEncabezado),
                crearCelda(intereses, esEncabezado),
                crearCelda(capitalPendiente, esEncabezado)
        };

        for (TextView celda : celdas) {
            fila.addView(celda);
        }
        tlTabla.addView(fila);
    }

    private TextView crearCelda(String texto, boolean esEncabezado) {
        TextView tv = new TextView(this);
        tv.setText(texto);
        tv.setGravity(Gravity.CENTER);
        tv.setPadding(10, 10, 10, 10);
        tv.setTextColor(esEncabezado ? Color.WHITE : Color.BLACK);
        tv.setBackgroundColor(esEncabezado ? Color.BLACK : Color.TRANSPARENT);
        return tv;
    }

    public ArrayList<Prestamo> creacionArrays(float capital, float plazo, float interes,
                                              float interesPosterior, float plazoConCambio,
                                              boolean tieneCambio) {
        ArrayList<Prestamo> datos = new ArrayList<>();
        int mesesTotales = (int) (plazo * 12);
        int mesesCambio = tieneCambio ? (int) (plazoConCambio * 12) : mesesTotales;

        double tasa = interes / 100.0 / 12.0;
        double tasaPosterior = tieneCambio ? interesPosterior / 100.0 / 12.0 : tasa;
        double capitalActual = capital;
        double cuotaActual = calcularCuota(capital, tasa, mesesTotales);
        boolean cambioAplicado = false;

        for (int mes = 0; mes < mesesTotales; mes++) {
            if (tieneCambio && !cambioAplicado && mes >= mesesCambio) {
                capitalActual = calcularCapitalPendiente(capital, tasa, mesesCambio, cuotaActual);
                tasa = tasaPosterior;
                mesesTotales -= mesesCambio;
                cuotaActual = calcularCuota(capitalActual, tasa, mesesTotales);
                cambioAplicado = true;
            }

            double interesMensual = capitalActual * tasa;
            double amortizacion = cuotaActual - interesMensual;

            if (amortizacion > capitalActual) {
                amortizacion = capitalActual;
                interesMensual = cuotaActual - amortizacion;
            }
            datos.add(new Prestamo(
                    mes + 1,
                    tasa * 12 * 100,
                    cuotaActual,
                    amortizacion,
                    interesMensual,
                    capitalActual - amortizacion
            ));

            capitalActual -= amortizacion;
        }
        return datos;
    }

    private double calcularCuota(double capital, double tasa, int meses) {
        if (tasa == 0) return capital / meses;
        return (capital * tasa) / (1 - Math.pow(1 + tasa, -meses));
    }

    private double calcularCapitalPendiente(double capital, double tasa, int mesesPagados, double cuota) {
        return capital * Math.pow(1 + tasa, mesesPagados) -
                cuota * (Math.pow(1 + tasa, mesesPagados) - 1) / tasa;
    }
}
