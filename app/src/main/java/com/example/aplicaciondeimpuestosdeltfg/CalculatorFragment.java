package com.example.aplicaciondeimpuestosdeltfg;

import android.content.Intent;
import android.os.Bundle;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.core.view.GravityCompat;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;

public class CalculatorFragment extends Fragment {

    private float precio = 0;
    private float ahorro = 0;
    private float plazo = 0;
    private float impuesto = 0;

    private CheckBox impuestoFijo, impuestoVariable;
    private DrawerLayout drawerLayout;

    private TextView cuotaMensual, importeHipoteca, porcentajeFinanciacion,
            precioInmuebleResultado, impuestosGastosCompra, costeTotalInmueble,
            ahorroAportadoResultado, importeHipotecaResultado, interesHipoteca,
            costeTotalConHipoteca;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_calculator, container, false);

        Button botonAnosRestar = view.findViewById(R.id.botonAnosRestar);
        Button botonAnosSumar = view.findViewById(R.id.botonAnosSumar);
        Button botonImpuestoRestar = view.findViewById(R.id.botonImpuestoRestar);
        Button botonImpuestoSumar = view.findViewById(R.id.botonImpuestoSumar);
        Button botonAhorroRestar = view.findViewById(R.id.botonAhorroRestar);
        Button botonAhorroSumar = view.findViewById(R.id.botonAhorroSumar);
        Button botonInmuebleRestar = view.findViewById(R.id.botonInmuebleRestar);
        Button botonInmuebleSumar = view.findViewById(R.id.botonInmuebleSumar);

        Button botonHipoteca = view.findViewById(R.id.boton1);
        Button botonImpuestos = view.findViewById(R.id.boton2);
        Button botonOtros = view.findViewById(R.id.boton3);

        TextView impuestoValor = view.findViewById(R.id.ImpuestoValor);
        TextView precioInmueble = view.findViewById(R.id.text_precio_inmueble);
        TextView ahorroAportado = view.findViewById(R.id.text_ahorro_aportado);
        TextView plazoPrestamo = view.findViewById(R.id.text_plazo_prestamo);
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

        impuestoFijo = view.findViewById(R.id.ImpuestoFijo);
        impuestoVariable = view.findViewById(R.id.ImpuestoVariable);

        SeekBar seekerPrecioInmueble = view.findViewById(R.id.precioInmueble);
        SeekBar seekBarAhorroAportado = view.findViewById(R.id.ahorroAportado);
        SeekBar seekBarPlazoPrestamo = view.findViewById(R.id.plazoPrestamo);

        drawerLayout = view.findViewById(R.id.drawer_layout);
        ImageView botonAbrirMenu = view.findViewById(R.id.btn_open_menu);



        // Botones suma/resta
        botonAnosRestar.setOnClickListener(v -> {
            if (plazo > 0) plazo -= 1;
            plazoPrestamo.setText(String.format("Plazo: %.0f años", plazo));
            actualizarValores();
        });

        botonAnosSumar.setOnClickListener(v -> {
            plazo += 1;
            plazoPrestamo.setText(String.format("Plazo: %.0f años", plazo));
            actualizarValores();
        });

        botonInmuebleRestar.setOnClickListener(v -> {
            if (precio >= 1000) precio -= 1000;
            precioInmueble.setText(String.format("Precio: %.0f €", precio));
            actualizarValores();
        });

        botonInmuebleSumar.setOnClickListener(v -> {
            precio += 1000;
            precioInmueble.setText(String.format("Precio: %.0f €", precio));
            actualizarValores();
        });

        botonAhorroRestar.setOnClickListener(v -> {
            float minimoAhorro = precio * 0.10f;
            if(minimoAhorro < ahorro){
            if (ahorro >= 1000) ahorro -= 1000;
            ahorroAportado.setText(String.format("Ahorro: %.0f €", ahorro));
            }else {
                ahorroAportado.setText(String.format("El ahorro debe ser como minimo el 10 porcientode precio: %.0f €", ahorro));
            }
            actualizarValores();
        });

        botonAhorroSumar.setOnClickListener(v -> {
            ahorro += 1000;
            if (ahorro > precio) ahorro = precio;
            ahorroAportado.setText(String.format("Ahorro: %.0f €", ahorro));
            actualizarValores();
        });

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
        impuestoFijo.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (isChecked) {
                impuestoVariable.setChecked(false);
            }
            actualizarValores();
        });

        impuestoVariable.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (isChecked) {
                impuestoFijo.setChecked(false);
            }
            actualizarValores();
        });


        // Menú lateral
        botonAbrirMenu.setOnClickListener(v -> {
            if (drawerLayout != null && !drawerLayout.isDrawerOpen(GravityCompat.START)) {
                drawerLayout.openDrawer(GravityCompat.START);

                botonHipoteca.setOnClickListener(i -> {
                    Intent intent = new Intent(view.getContext(), CalculatorFragment.class);
                    // intent.putExtra("producto", producto);
                });

                botonImpuestos.setOnClickListener(i -> {
                    Intent intent = new Intent(view.getContext(), CalculatorFragment.class);
                    // intent.putExtra("producto", producto);
                });

                botonOtros.setOnClickListener(i -> {
                    Intent intent = new Intent(view.getContext(), CalculatorFragment.class);
                    // intent.putExtra("producto", producto);
                });
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

    private void actualizarValores() {
        if (precio <= 0 || plazo <= 0|| impuesto <= 0) {
            cuotaMensual.setText("—");
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

        // Calcular gastos según tipo de impuesto
        float gastos = 0;
        if (impuestoFijo != null && impuestoFijo.isChecked()) {
            gastos = precio * 0.10f;
        } else if (impuestoVariable != null && impuestoVariable.isChecked()) {
            gastos = precio * (impuesto / 100);
        }

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
        porcentajeFinanciacion.setText(String.format("%.2f %%", porcentajeFinanciacionValor));
        impuestosGastosCompra.setText(String.format("%.2f €", gastos));
        costeTotalInmueble.setText(String.format("%.2f €", precio + gastos));
        cuotaMensual.setText(String.format("%.2f €", cuotaMensualCalculada));
        importeHipotecaResultado.setText(String.format("%.2f €", hipoteca));
        ahorroAportadoResultado.setText(String.format("%.2f €", ahorro));
        precioInmuebleResultado.setText(String.format("%.2f €", precio));
        interesHipoteca.setText(String.format("%.2f %%", interesAnual * 100));
        costeTotalConHipoteca.setText(String.format("%.2f €", costeTotal));
    }
}
