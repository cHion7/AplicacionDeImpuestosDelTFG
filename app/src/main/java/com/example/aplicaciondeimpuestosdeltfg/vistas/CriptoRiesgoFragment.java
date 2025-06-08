package com.example.aplicaciondeimpuestosdeltfg.vistas;

import android.os.Bundle;

import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;

import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.TextView;

import com.example.aplicaciondeimpuestosdeltfg.R;


public class CriptoRiesgoFragment extends Fragment {
    private  RadioButton ventaCripto;
    private RadioButton cambioCripto;
    private RadioButton regaloCripto;
    private RadioButton mineriaCripto;
    private RadioButton prestarCripto;
    private EditText dineroGanado;
    TextView ganaciaCripto;
    TextView impuestosCripto;
    TextView totalGanado;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        //Para moverse entre fragments
        View view = inflater.inflate(R.layout.fragment_cripto_riesgo, container, false);
        ImageView btn_open_menu = view.findViewById(R.id.boton_abrir_menu);
        DrawerLayout drawerLayout =  view.findViewById(R.id.drawer_layout);
        Button botonHipoteca = view.findViewById(R.id.boton1);
        Button botonImpuestos = view.findViewById(R.id.boton2);
        Button botonOtros = view.findViewById(R.id.boton3);

        ventaCripto = view.findViewById(R.id.ventaCripto);
        cambioCripto = view.findViewById(R.id.cambioCripto);
        regaloCripto = view.findViewById(R.id.regaloCripto);
        mineriaCripto = view.findViewById(R.id.mineriaCripto);
        prestarCripto = view.findViewById(R.id.preatarCripto);

        dineroGanado = view.findViewById(R.id.dineroGanado);

        ganaciaCripto = view.findViewById(R.id.ganaciaCripto);
        impuestosCripto = view.findViewById(R.id.impuestosCripto);
        totalGanado = view.findViewById(R.id.totalGanado);


        btn_open_menu.setOnClickListener(v -> {
            if (drawerLayout != null && !drawerLayout.isDrawerOpen(GravityCompat.START)) {
                drawerLayout.openDrawer(GravityCompat.START);

                botonHipoteca.setOnClickListener(i -> cargarFragment(new CalculatorFragment()));
                botonImpuestos.setOnClickListener(i -> cargarFragment(new PrestamoFragment()));
                botonOtros.setOnClickListener(i -> cargarFragment(new CriptoRiesgoFragment()));

            }
        });

        dineroGanado.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                actualizarValoresCriptomonedas();

            }

            @Override
            public void afterTextChanged(Editable s) {
                actualizarValoresCriptomonedas();
            }
        });
        if(ventaCripto.isChecked() ||cambioCripto.isChecked()||prestarCripto.isChecked()||regaloCripto.isChecked()||mineriaCripto.isChecked() ){
            actualizarValoresCriptomonedas();
        }





        return  view;
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
    private void actualizarValoresCriptomonedas() {
        String gananciasString = dineroGanado.getText().toString();
        double gananciasSinCambiar = Double.parseDouble(gananciasString);
        double ganancias = Double.parseDouble(gananciasString);
        double impuestos = 0.0;

        if(ventaCripto.isChecked() ||cambioCripto.isChecked()||prestarCripto.isChecked() ){
            if (ganancias <= 6000) {
                impuestos= ganancias * 0.19;
            } else if (ganancias <= 50000) {
                impuestos = (6000 * 0.19) +
                        ((ganancias - 6000) * 0.21);
            } else if (ganancias <= 200000) {
                impuestos = (6000 * 0.19) +
                        (44000 * 0.21) +
                        ((ganancias - 50000) * 0.23);
            } else if (ganancias <= 300000) {
                impuestos = (6000 * 0.19) +
                        (44000 * 0.21) +
                        (150000 * 0.23) +
                        ((ganancias - 200000) * 0.27);
            } else {
                impuestos = (6000 * 0.19) +
                        (44000 * 0.21) +
                        (150000 * 0.23) +
                        (100000 * 0.27) +
                        ((ganancias - 300000) * 0.28);
            }
            ganaciaCripto.setText(String.format( "%.2f €", gananciasSinCambiar));
            impuestosCripto.setText(String.format( "%.2f €", impuestos));
            totalGanado.setText(String.format( "%.2f €", ganancias-impuestos));

        }
        else if(regaloCripto.isChecked()||mineriaCripto.isChecked()){
            if (ganancias <= 12450) {
                impuestos = ganancias * 0.19;
            } else if (ganancias <= 20200) {
                impuestos = (12450 * 0.19) +
                        ((ganancias - 12450) * 0.24);
            } else if (ganancias <= 35200) {
                impuestos = (12450 * 0.19) +
                        ((20200 - 12450) * 0.24) +
                        ((ganancias - 20200) * 0.30);
            } else if (ganancias <= 60000) {
                impuestos = (12450 * 0.19) +
                        ((20200 - 12450) * 0.24) +
                        ((35200 - 20200) * 0.30) +
                        ((ganancias - 35200) * 0.37);
            } else if (ganancias <= 300000) {
                impuestos = (12450 * 0.19) +
                        ((20200 - 12450) * 0.24) +
                        ((35200 - 20200) * 0.30) +
                        ((60000 - 35200) * 0.37) +
                        ((ganancias - 60000) * 0.45);
            } else {
                impuestos = (12450 * 0.19) +
                        ((20200 - 12450) * 0.24) +
                        ((35200 - 20200) * 0.30) +
                        ((60000 - 35200) * 0.37) +
                        ((300000 - 60000) * 0.45) +
                        ((ganancias - 300000) * 0.47);
            }
            ganaciaCripto.setText(String.format( "%.2f €", gananciasSinCambiar));
            impuestosCripto.setText(String.format( "%.2f €", impuestos));
            totalGanado.setText(String.format( "%.2f €", ganancias-impuestos));


        }
        else{
             ganaciaCripto.setText("0");
             impuestosCripto.setText("0");
             totalGanado.setText("0");
        }
    }
}