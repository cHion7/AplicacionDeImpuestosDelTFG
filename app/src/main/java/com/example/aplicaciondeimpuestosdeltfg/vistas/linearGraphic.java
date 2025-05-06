package com.example.aplicaciondeimpuestosdeltfg.vistas;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.widget.Button;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.aplicaciondeimpuestosdeltfg.R;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.Description;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter;
import com.github.mikephil.charting.formatter.LargeValueFormatter;

import java.util.ArrayList;
import java.util.List;

public class linearGraphic extends AppCompatActivity {
    private LineChart lineChart;
    private List<String> xValues = new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_linear_graphic);
        lineChart = findViewById(R.id.chart);
        Button salidaLinear = findViewById(R.id.salidaLinear);


        Intent intent = getIntent();
        float capital = intent.getFloatExtra("capital", 0);
        float plazo = intent.getFloatExtra("plazo", 0);
        float interes = intent.getFloatExtra("interes", 0);
        float interesPosterior = intent.getFloatExtra("interesPosterior", 0);
        float plazoConCambio = intent.getFloatExtra("plazoConCambio", 0);
        boolean tieneCambio = intent.getBooleanExtra("tieneCambio", false);

        configureChart();
        salidaLinear.setOnClickListener(v -> {
            finish();
        });


        List<Entry> capitalEntries = new ArrayList<>();
        List<Entry> interesesEntries = new ArrayList<>();
        calcularDatosPrestamo(capital, plazo, interes, interesPosterior, plazoConCambio, tieneCambio, capitalEntries, interesesEntries);

        //Como se crean las lineas de la grafica
        LineDataSet dataSetCapital = new LineDataSet(capitalEntries, "Capital Pendiente");
        dataSetCapital.setDrawCircles(false);
        dataSetCapital.setDrawValues(false);
        dataSetCapital.setLineWidth(2f);
        dataSetCapital.setColor(Color.BLUE);
        dataSetCapital.setColor(Color.BLUE);
        dataSetCapital.setCircleColor(Color.BLUE);
        dataSetCapital.setMode(LineDataSet.Mode.CUBIC_BEZIER);

        LineDataSet dataSetIntereses = new LineDataSet(interesesEntries, "Intereses Acumulados");
        dataSetIntereses.setDrawCircles(false);
        dataSetIntereses.setDrawValues(false);
        dataSetIntereses.setLineWidth(2f);
        dataSetIntereses.setColor(Color.RED);
        dataSetIntereses.setCircleColor(Color.RED);
        dataSetIntereses.setMode(LineDataSet.Mode.CUBIC_BEZIER);


        //Implemntar las lineas en el grafico
        LineData lineData = new LineData(dataSetCapital, dataSetIntereses);
        lineChart.setData(lineData);
        lineChart.invalidate();
    }
    //condiguraciones que se les da a la grafica
    private void configureChart() {
        XAxis xAxis = lineChart.getXAxis();
        xAxis.setGranularity(50f);
        xAxis.setValueFormatter(new IndexAxisValueFormatter(xValues));

        YAxis yAxis = lineChart.getAxisLeft();
        yAxis.setAxisMinimum(0f);
        yAxis.setValueFormatter(new LargeValueFormatter());

        lineChart.setPinchZoom(true);
        lineChart.setScaleEnabled(true);

        xAxis.setLabelRotationAngle(-45);
        xAxis.setGranularityEnabled(true);
        xAxis.setAvoidFirstLastClipping(true);

    }
    //calculo de datos
    private void calcularDatosPrestamo(float capital, float plazo, float interes,
                                       float interesPosterior, float plazoConCambio,
                                       boolean tieneCambio,
                                       List<Entry> capitalEntries,
                                       List<Entry> interesesEntries) {

        int mesesTotales = (int) (plazo * 12);
        int mesesCambio = tieneCambio ? (int) (plazoConCambio * 12) : mesesTotales;

        double tasa = interes / 100 / 12;
        double cuota = calcularCuota(capital, tasa, mesesTotales);
        double capitalPendiente = capital;
        double interesesAcumulados = 0;


        boolean cambioAplicado = false;
        int mesActual = 0;
        int mesesRestantes = mesesTotales;

        while(mesesRestantes > 0) {
            if(tieneCambio && !cambioAplicado && mesActual >= mesesCambio) {
                // Aplicar cambio de tasa
                capitalPendiente = calcularCapitalPendiente(capital, tasa, mesesCambio, cuota);
                tasa = interesPosterior / 100 / 12;
                mesesRestantes = mesesTotales - mesesCambio;
                cuota = calcularCuota(capitalPendiente, tasa, mesesRestantes);
                cambioAplicado = true;
            }

            double interesMensual = capitalPendiente * tasa;
            double amortizacion = cuota - interesMensual;

            if(amortizacion > capitalPendiente) {
                amortizacion = capitalPendiente;
                interesMensual = cuota - amortizacion;
            }

            interesesAcumulados += interesMensual;
            capitalPendiente -= amortizacion;


            capitalEntries.add(new Entry(mesActual, (float) capitalPendiente));
            interesesEntries.add(new Entry(mesActual, (float) interesesAcumulados));
            xValues.add("Mes " + (mesActual + 1));

            mesActual++;
            mesesRestantes--;
        }
    }

    private double calcularCuota(double capital, double tasa, int meses) {
        if(tasa == 0) return capital / meses;
        return (capital * tasa) / (1 - Math.pow(1 + tasa, -meses));
    }

    private double calcularCapitalPendiente(double capital, double tasa, int mesesPagados, double cuota) {
        return capital * Math.pow(1 + tasa, mesesPagados) - cuota * (Math.pow(1 + tasa, mesesPagados) - 1) / tasa;
    }
}