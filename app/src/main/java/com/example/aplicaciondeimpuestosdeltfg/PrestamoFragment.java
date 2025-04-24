package com.example.aplicaciondeimpuestosdeltfg;

import android.os.Bundle;

import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;

import com.example.aplicaciondeimpuestosdeltfg.vistas.CalculatorFragment;

import java.util.Locale;

public class PrestamoFragment extends Fragment {

    private float capital = 0;
    private float plazo = 0;
    private float interes = 0;
    private float interesPosterior = 0;
    private float plazoConCambio = 0;
    private CheckBox impuestoPosterior;
    TextView mensualidadText;
    TextView mensualidadPosteriorText;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflar el layout del fragment
        View view = inflater.inflate(R.layout.fragment_prestamo, container, false);

        Button botonHipoteca = view.findViewById(R.id.boton1);
        Button botonImpuestos = view.findViewById(R.id.boton2);
        Button botonOtros = view.findViewById(R.id.boton3);


        // Inicializar vistas desde el layout del fragment
        ImageView btn_open_menu = view.findViewById(R.id.boton_abrir_menu);

        impuestoPosterior = view.findViewById(R.id.checkbox_interes_posterior);

        TextView capitalText = view.findViewById(R.id.text_capital);
        TextView plazoText = view.findViewById(R.id.text_plazo);
        TextView interesText = view.findViewById(R.id.text_interes);
        TextView interesPosteriorText = view.findViewById(R.id.text_interes_posterior);
        TextView cambioAnioText = view.findViewById(R.id.text_anio_cambio);

        mensualidadText = view.findViewById(R.id.text_mensualidad);
        mensualidadPosteriorText = view.findViewById(R.id.text_mensualidad_posterior);

        SeekBar capitalInicial = view.findViewById(R.id.seekbar_capital);
        SeekBar plazoAmortizado = view.findViewById(R.id.seekbar_plazo);
        SeekBar tipoInteres = view.findViewById(R.id.seekbar_interes);
        SeekBar plazoAmortizadoPosterior = view.findViewById(R.id.seekbar_anio_cambio);
        SeekBar tipoInteresPosterior = view.findViewById(R.id.seekbar_interes_posterior);


        plazoAmortizadoPosterior.setEnabled(false);
        tipoInteresPosterior.setEnabled(false);

        DrawerLayout drawerLayout =  view.findViewById(R.id.drawer_layout);

        btn_open_menu.setOnClickListener(v -> {
            if (drawerLayout != null && !drawerLayout.isDrawerOpen(GravityCompat.START)) {
                drawerLayout.openDrawer(GravityCompat.START);

                botonHipoteca.setOnClickListener(i -> cargarFragment(new CalculatorFragment()));
                botonImpuestos.setOnClickListener(i -> cargarFragment(new PrestamoFragment()));
                botonOtros.setOnClickListener(i -> cargarFragment(new CriptoRiesgoFragment()));

            }
        });




        //--------------------------------------------------------------


        impuestoPosterior.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (isChecked) {
                tipoInteresPosterior.setEnabled(true);
                plazoAmortizadoPosterior.setEnabled(true);
            } else {

                tipoInteresPosterior.setEnabled(false);
                plazoAmortizadoPosterior.setEnabled(false);

            }
            actualizarValoresPrestamo();
        });



        tipoInteres.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                interes = progress;
                interesText.setText(String.format("Interes: %.0f %%", interes));

            }

            @Override public void onStartTrackingTouch(SeekBar seekBar) {}

            @Override public void onStopTrackingTouch(SeekBar seekBar) {
                actualizarValoresPrestamo();
            }
        });

        plazoAmortizadoPosterior.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                plazoConCambio = progress;
                cambioAnioText.setText(String.format("Plazo: %.0f años", plazoConCambio));

            }

            @Override public void onStartTrackingTouch(SeekBar seekBar) {}

            @Override public void onStopTrackingTouch(SeekBar seekBar) {
                actualizarValoresPrestamo();
            }
        });

        tipoInteresPosterior.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                interesPosterior = progress;
                interesPosteriorText.setText(String.format("Interés posterior: %.2f %%", interesPosterior));

            }

            @Override public void onStartTrackingTouch(SeekBar seekBar) {}

            @Override public void onStopTrackingTouch(SeekBar seekBar) {
                actualizarValoresPrestamo();
            }
        });



        capitalInicial.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                capital = progress;
                capitalText.setText(String.format("Precio: %.0f €", capital));
            }

            @Override public void onStartTrackingTouch(SeekBar seekBar) {}

            @Override public void onStopTrackingTouch(SeekBar seekBar) {
                actualizarValoresPrestamo();
            }
        });

        plazoAmortizado.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                plazo = progress;
                plazoText.setText(String.format("Plazo: %.0f años", plazo));
            }

            @Override public void onStartTrackingTouch(SeekBar seekBar) {}

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                actualizarValoresPrestamo();
            }
        });

        plazoAmortizadoPosterior.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                if (progress < plazo) {
                    plazoConCambio = progress;
                    cambioAnioText.setText(String.format("Cambio en el año: %.0f", plazoConCambio));
                } else {
                    cambioAnioText.setText("Debe ser menor al plazo total");
                }
            }

            @Override public void onStartTrackingTouch(SeekBar seekBar) {}

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                actualizarValoresPrestamo();
            }
        });
        return view;
    }

    private void cargarFragment(Fragment fragmentIr) {
        if (getActivity() != null) {
            getActivity().getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.mainPageFragmentContainer, fragmentIr)
                    .addToBackStack(null)
                    .commit();
        }
    }

    private void actualizarValoresPrestamo() {
        // Validación básica
        if (capital <= 0 || interes <= 0 || plazo <= 0) {
            mostrarError();
            return;
        }

        // Conversión a tasas y meses
        double tasaMensualInicial = interes / 100.0 / 12.0;
        int mesesTotales = (int)(plazo * 12);

        // 1) Cuota inicial (años completos, tasa inicial)
        double cuotaInicial = calcularCuota(capital, tasaMensualInicial, mesesTotales);
        mensualidadText.setText(String.format(Locale.getDefault(), "%.2f €", cuotaInicial));

        if (!impuestoPosterior.isChecked()) {
            mensualidadPosteriorText.setText("-");
            return;
        }

        // Validar parámetros de cambio
        if (plazoConCambio <= 0 || plazoConCambio >= plazo || interesPosterior <= 0) {
            mostrarError();
            return;
        }

        // Conversión para el tramo posterior
        double tasaMensualPosterior = interesPosterior / 100.0 / 12.0;
        int mesesHastaCambio = (int)(plazoConCambio * 12);
        int mesesRestantes = mesesTotales - mesesHastaCambio;

        // 2) Capital pendiente al cambio usando la cuota original
        double capitalPendiente = calcularCapitalPendiente(capital, tasaMensualInicial, mesesHastaCambio, cuotaInicial);

        // 3) Cuota posterior con el nuevo interés y meses restantes
        double cuotaPosterior = calcularCuota(capitalPendiente, tasaMensualPosterior, mesesRestantes);
        mensualidadPosteriorText.setText(String.format(Locale.getDefault(), "%.2f €", cuotaPosterior));
    }

    private double calcularCapitalPendiente(double capital, double tasaMensual, int cuotasPagadas, double cuota) {
        double factor = Math.pow(1 + tasaMensual, cuotasPagadas);
        if (tasaMensual <= 0) {
            return capital - cuota * cuotasPagadas;
        }
        return capital * factor - cuota * ((factor - 1) / tasaMensual);
    }

    private double calcularCuota(double capital, double tasaMensual, int numMeses) {
        // C = K * [ i*(1+i)^N / ((1+i)^N - 1) ]
        double factor = Math.pow(1 + tasaMensual, numMeses);
        if (tasaMensual <= 0 || factor == 1.0) {
            // Evita división por cero
            return capital / numMeses;
        }
        return capital * (tasaMensual * factor) / (factor - 1);
    }

    private double calcularCapitalPendiente(double capital, double tasaMensual, int cuotasPagadas) {
        // K_pend = K*(1+i)^n - C * [ ((1+i)^n - 1) / i ]
        double factor = Math.pow(1 + tasaMensual, cuotasPagadas);
        double cuota = calcularCuota(capital, tasaMensual, cuotasPagadas);
        if (tasaMensual <= 0) {
            // Si i=0, pendiente es lineal
            return capital - cuota * cuotasPagadas;
        }
        return capital * factor
                - cuota * ((factor - 1) / tasaMensual);
    }

    private void mostrarError() {
        mensualidadText.setText("Error");
        mensualidadPosteriorText.setText("Error");
    }


}


