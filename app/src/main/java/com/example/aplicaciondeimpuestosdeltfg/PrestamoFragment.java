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
                interesPosteriorText.setText(String.format("Plazo: %.0f años", interesPosterior));

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

        if (interes <= 0 || plazo <= 0 || capital <= 0) {
            mensualidadText.setText("Error");
            mensualidadPosteriorText.setText("-");
            return;
        }

        float r = interes / 12f / 100f;
        int n = (int) plazo * 12;

        if (!impuestoPosterior.isChecked()) {
            double factor = Math.pow(1 + r, n);
            double cuota = capital * r * factor / (factor - 1);
            mensualidadText.setText(String.format("%.2f €", cuota));
            mensualidadPosteriorText.setText("-");

            //-------------------------------Da mal es una caca FFFFFFFFFFFFFFFFFFFFF
        } else {

            if (plazoConCambio <= 0 || plazoConCambio >= plazo || interesPosterior <= 0) {
                mensualidadText.setText("Error");
                mensualidadPosteriorText.setText("Error");
                return;
            }

            float r1 = interes / 12f / 100f;
            int n1 = (int) plazoConCambio * 12;
            double factor1 = Math.pow(1 + r1, n1);
            double cuota1 = capital * r1 * factor1 / (factor1 - 1);

            double capitalRestante = capital * factor1 - (cuota1 * (factor1 - 1) / r1);

            float r2 = interesPosterior / 12f / 100f;
            int n2 = (int) ((plazo - plazoConCambio) * 12);
            double factor2 = Math.pow(1 + r2, n2);
            double cuota2 = capitalRestante * r2 * factor2 / (factor2 - 1);

            mensualidadText.setText(String.format("%.2f €", cuota1));
            mensualidadPosteriorText.setText(String.format("%.2f €", cuota2));
        }
    }
}

