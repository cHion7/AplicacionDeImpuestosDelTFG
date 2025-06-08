package com.example.aplicaciondeimpuestosdeltfg.vistas;

import android.os.Bundle;

import androidx.core.content.ContextCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.core.view.GravityCompat;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;

import com.example.aplicaciondeimpuestosdeltfg.R;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.data.PieEntry;

import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;

import java.util.ArrayList;

public class CalculatorFragment extends Fragment {
    private float precio = 0;
    private float ahorro = 0;
    private float plazo = 0;
    private float impuesto = 0;

    private DrawerLayout drawerLayout;

    private TextView cuotaMensual, importeHipoteca, porcentajeFinanciacion,
            precioInmuebleResultado, impuestosGastosCompra, costeTotalInmueble,
            ahorroAportadoResultado, importeHipotecaResultado, interesHipoteca,
            costeTotalConHipoteca;

    PieChart pieChart;

    ArrayList<String> valoresX = new ArrayList<>();
    ArrayList<PieEntry> valoresY = new ArrayList<>();
    ArrayList<Integer> colores = new ArrayList<>();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_calculator, container, false);


        Button botonImpuestoRestar = view.findViewById(R.id.botonImpuestoRestar);
        Button botonImpuestoSumar = view.findViewById(R.id.botonImpuestoSumar);
        Button botonHipoteca = view.findViewById(R.id.boton1);
        Button botonImpuestos = view.findViewById(R.id.boton2);
        Button botonOtros = view.findViewById(R.id.boton3);

        TextView impuestoValor = view.findViewById(R.id.ImpuestoValor);
        TextView precioInmueble = view.findViewById(R.id.textViewPrecio);
        TextView ahorroAportado = view.findViewById(R.id.textViewAhorro);
        TextView plazoPrestamo = view.findViewById(R.id.textViewPlazo);
        cuotaMensual = view.findViewById(R.id.cuotaMnesual);
        importeHipoteca = view.findViewById(R.id.importeHipoteca);
        porcentajeFinanciacion = view.findViewById(R.id.porcentajeFinanciacion);
        precioInmuebleResultado = view.findViewById(R.id.precioInmuebleResultado);
        impuestosGastosCompra = view.findViewById(R.id.impuestosGastosCompra);
        costeTotalInmueble = view.findViewById(R.id.costeTotalInmueble);
        ahorroAportadoResultado = view.findViewById(R.id.ahorroAportadoResultado);
        importeHipotecaResultado = view.findViewById(R.id.importeHipotecaResultado);
        interesHipoteca = view.findViewById(R.id.interesHipoteca);
        costeTotalConHipoteca = view.findViewById(R.id.costeTotalConHipoteca);


        SeekBar seekerPrecioInmueble = view.findViewById(R.id.seekBarPrecio);
        SeekBar seekBarAhorroAportado = view.findViewById(R.id.seekBarAhorro);
        SeekBar seekBarPlazoPrestamo = view.findViewById(R.id.seekBarPlazo);

        drawerLayout = view.findViewById(R.id.drawer_layout);
        ImageView botonAbrirMenu = view.findViewById(R.id.btn_open_menu);

        pieChart = view.findViewById(R.id.pcGrafica);


        botonImpuestoRestar.setOnClickListener(v -> {
            if (impuesto > 0) impuesto -= 1;
            impuestoValor.setText(String.format("Impuesto: %.0f %%", impuesto));
            actualizarValores();
        });

        botonImpuestoSumar.setOnClickListener(v -> {
            impuesto += 1;
            impuestoValor.setText(String.format("Impuesto: %.0f %%", impuesto));
            actualizarValores();
        });


        // Menú lateral
        botonAbrirMenu.setOnClickListener(v -> {
            if (drawerLayout != null && !drawerLayout.isDrawerOpen(GravityCompat.START)) {
                drawerLayout.openDrawer(GravityCompat.START);

                botonHipoteca.setOnClickListener(i -> cargarFragment(new CalculatorFragment()));
                botonImpuestos.setOnClickListener(i -> cargarFragment(new PrestamoFragment()));
                botonOtros.setOnClickListener(i -> cargarFragment(new CriptoRiesgoFragment()));

            }
        });

        // SeekBars
        seekerPrecioInmueble.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                precio = progress;

                precioInmueble.setText(String.format("Precio: %.0f €", precio));
            }

            @Override public void onStartTrackingTouch(SeekBar seekBar) {}

            @Override public void onStopTrackingTouch(SeekBar seekBar) {
                actualizarValores();
            }
        });

        seekBarAhorroAportado.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                float minimoAhorro = precio * 0.10f;
                if (progress < minimoAhorro) {
                    ahorro = minimoAhorro;
                    seekBar.setProgress((int) ahorro);
                    ahorroAportado.setText(String.format(
                            "El ahorro debe ser al menos el 10%% del precio: %.0f €", ahorro));
                } else if (progress > precio) {
                    ahorro = precio;
                    seekBar.setProgress((int) ahorro);
                    ahorroAportado.setText(String.format("Ahorro: %.0f €", ahorro));
                } else {
                    ahorro = progress;
                    ahorroAportado.setText(String.format("Ahorro: %.0f €", ahorro));
                }
            }

            @Override public void onStartTrackingTouch(SeekBar seekBar) {}

            @Override public void onStopTrackingTouch(SeekBar seekBar) {
                actualizarValores();
            }
        });

        seekBarPlazoPrestamo.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                plazo = progress;
                plazoPrestamo.setText(String.format("Plazo: %.0f años", plazo));
            }

            @Override public void onStartTrackingTouch(SeekBar seekBar) {}

            @Override public void onStopTrackingTouch(SeekBar seekBar) {
                actualizarValores();
            }
        });

        return view;
    }

    private void cargarFragment(Fragment fragmentoCmabio) {
        if (getActivity() != null) {
            getActivity().getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.mainPageFragmentContainer, fragmentoCmabio)
                    .addToBackStack(null)
                    .commit();
        }
    }

    private void actualizarValores() {
        if (precio <= 0 || plazo <= 0 || impuesto <= 0) {
            cuotaMensual.setText("Introduce todos los datos");
            importeHipoteca.setText("—");
            porcentajeFinanciacion.setText("—");
            impuestosGastosCompra.setText("—");
            costeTotalInmueble.setText("—");
            importeHipotecaResultado.setText("—");
            ahorroAportadoResultado.setText("—");
            precioInmuebleResultado.setText("—");
            interesHipoteca.setText("—");
            costeTotalConHipoteca.setText("—");
            return;
        }

        valoresX.clear();
        valoresY.clear();
        colores.clear();

        // Calculamos gastos
        float gastos = 0;
            gastos = precio * 0.10f;


        float hipoteca = (precio + gastos) - ahorro;
        float porcentajeFinanciacionValor = (hipoteca / precio) * 100;

        float interesAnual = impuesto / 100;
        float interesMensual = interesAnual / 12;
        int numeroCuotas = (int) (plazo * 12);

        double cuotaMensualCalculada = 0;
        if (hipoteca > 0 && interesMensual > 0 && numeroCuotas > 0) {
            double pow = Math.pow(1 + interesMensual, numeroCuotas);
            cuotaMensualCalculada = hipoteca * ((interesMensual * pow) / (pow - 1));
        }

        double totalPagado = cuotaMensualCalculada * numeroCuotas;
        double interesesTotales = totalPagado - hipoteca;
        double costeTotal = precio + gastos + interesesTotales;

        importeHipoteca.setText(String.format("%.2f €", hipoteca));
        impuestosGastosCompra.setText(String.format("%.2f €", gastos));
        costeTotalInmueble.setText(String.format("%.2f €", precio + gastos));
        cuotaMensual.setText(String.format("%.2f €", cuotaMensualCalculada));
        importeHipotecaResultado.setText(String.format("%.2f €", hipoteca));
        interesHipoteca.setText(String.format("%.2f %%", interesAnual * 100));
        costeTotalConHipoteca.setText(String.format("%.2f €", costeTotal));

        creacionDeGrafica(hipoteca, gastos, interesesTotales);
    }


    private void creacionDeGrafica(float hipoteca, float gastos, double intereses) {
        pieChart.setHoleRadius(40f);
        pieChart.setRotationEnabled(true);
        pieChart.animateXY(1500, 1500);

        valoresY.clear();
        colores.clear();

        valoresY.add(new PieEntry(precio, "Precio del inmueble"));
        valoresY.add(new PieEntry((float) intereses, "Intereses"));
        valoresY.add(new PieEntry(hipoteca, "Hipoteca"));
        valoresY.add(new PieEntry(ahorro, "Ahorro aportado"));


        colores.add(ContextCompat.getColor(requireContext(), R.color.grafica_rojo));
        colores.add(ContextCompat.getColor(requireContext(), R.color.grafica_naranja));
        colores.add(ContextCompat.getColor(requireContext(), R.color.grafica_azul_medio));
        colores.add(ContextCompat.getColor(requireContext(), R.color.grafica_azul_claro));


        PieDataSet set = new PieDataSet(valoresY, "");
        set.setSliceSpace(5f);
        set.setColors(colores);

        PieData data = new PieData(set);
        pieChart.setData(data);
        pieChart.highlightValues(null);
        pieChart.invalidate();
        pieChart.setCenterTextSize(20f);
        data.setValueTextSize(15f);
        pieChart.getDescription().setEnabled(false);


    }
}
