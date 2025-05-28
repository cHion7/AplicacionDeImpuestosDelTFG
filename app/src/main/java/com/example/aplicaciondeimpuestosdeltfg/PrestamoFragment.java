package com.example.aplicaciondeimpuestosdeltfg;

import android.content.Intent;
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
import com.example.aplicaciondeimpuestosdeltfg.vistas.linearGraphic;

import java.util.Locale;

public class PrestamoFragment extends Fragment {

    private float capital = 0;
    private float plazo = 0;
    private float interes = 0;

    private CheckBox impuestoPosterior;
    TextView mensualidadText;
    TextView mensualidadPosteriorText;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_prestamo, container, false);

        Button botonHipoteca = view.findViewById(R.id.boton1);
        Button botonImpuestos = view.findViewById(R.id.boton2);
        Button botonOtros = view.findViewById(R.id.boton3);
        Button botonVerGrafica = view.findViewById(R.id.Vergrafica);
        Button botonVerTabla = view.findViewById(R.id.irTabla);
        botonVerGrafica.setOnClickListener(v -> {
            Intent intent = new Intent(getActivity(), linearGraphic.class);
            intent.putExtra("capital", capital);
            intent.putExtra("plazo", plazo);
            intent.putExtra("interes", interes);


            startActivity(intent);
        });
        botonVerTabla.setOnClickListener(v -> {
            Intent intent = new Intent(getActivity(), tablaPrestamo.class);
            intent.putExtra("capital", capital);
            intent.putExtra("plazo", plazo);
            intent.putExtra("interes", interes);

            startActivity(intent);
        });
        // Inicializar vistas desde el layout del fragment
        ImageView btn_open_menu = view.findViewById(R.id.boton_abrir_menu);



        TextView capitalText = view.findViewById(R.id.text_capital);
        TextView plazoText = view.findViewById(R.id.text_plazo);
        TextView interesText = view.findViewById(R.id.text_interes);

        mensualidadText = view.findViewById(R.id.text_mensualidad);
        mensualidadPosteriorText = view.findViewById(R.id.text_mensualidad_posterior);

        SeekBar capitalInicial = view.findViewById(R.id.seekbar_capital);
        SeekBar plazoAmortizado = view.findViewById(R.id.seekbar_plazo);
        SeekBar tipoInteres = view.findViewById(R.id.seekbar_interes);




        DrawerLayout drawerLayout =  view.findViewById(R.id.drawer_layout);

        btn_open_menu.setOnClickListener(v -> {
            if (drawerLayout != null && !drawerLayout.isDrawerOpen(GravityCompat.START)) {
                drawerLayout.openDrawer(GravityCompat.START);

                botonHipoteca.setOnClickListener(i -> cargarFragment(new CalculatorFragment()));
                botonImpuestos.setOnClickListener(i -> cargarFragment(new PrestamoFragment()));
                botonOtros.setOnClickListener(i -> cargarFragment(new CriptoRiesgoFragment()));

            }
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
        if (capital <= 0 || plazo <= 0 || interes <= 0) {
            mensualidadText.setText("0.00 €");
            mensualidadPosteriorText.setText("-");
            return;
        }

        // Conversión de tasa anual a tasa mensual
        double tasaMensual = interes / 100.0 / 12.0;
        int meses = (int) (plazo);

        // Cálculo de la cuota fija mensual
        double cuotaMensual = calcularCuota(capital, tasaMensual, meses);

        // Total pagado y total de intereses
        double pagoTotal = cuotaMensual * meses;
        double interesesTotales = pagoTotal - capital;

        // Mostrar resultados
        mensualidadText.setText(String.format(Locale.getDefault(),
                "Anualidad: %.2f €\nTotal intereses: %.2f €\nPago total: %.2f €",
                cuotaMensual, interesesTotales, pagoTotal));

    }

    private double calcularCuota(double capital, double tasaMensual, int numMeses) {

        double factor = Math.pow(1 + tasaMensual, numMeses);
        return capital * (tasaMensual * factor) / (factor - 1);
    }




}


