package com.example.aplicaciondeimpuestosdeltfg.vistas;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.aplicaciondeimpuestosdeltfg.R;

import java.util.List;

public class afinarUsuario extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_afinar_usuario);

        RadioButton radio_trabaja_si = findViewById(R.id.radio_trabaja_si);
        EditText edit_num_hijos = findViewById(R.id.edit_num_hijos);
        EditText edit_ingreso = findViewById(R.id.edit_ingreso);
        Spinner spinner_situacion = findViewById(R.id.spinner_situacion);
        RadioButton radio_vivienda_si = findViewById(R.id.radio_vivienda_si);
        Spinner spinner_discapacidad = findViewById(R.id.spinner_discapacidad);
        Button btn_enviar = findViewById(R.id.btn_enviar);

        List<String> spinnerValues = List.of("Autónomo", "Asalariado", "Empresario", "Estudiante", "Jubilado");
        ArrayAdapter<String> arraySituacion = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, spinnerValues);
        spinner_situacion.setAdapter(arraySituacion);
        spinner_situacion.setSelection(3);

        List<String> spinnerValuesDiscapacidad = List.of("Ninguna", "Leve", "Moderada", "Grave");
        ArrayAdapter<String> adapterDiscapacidad = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, spinnerValuesDiscapacidad);
        spinner_discapacidad.setAdapter(adapterDiscapacidad);
        spinner_discapacidad.setSelection(0);

        btn_enviar.setOnClickListener(v -> {
            boolean trabaja = radio_trabaja_si.isChecked();
            String numHijos = edit_num_hijos.getText().toString();
            String ingreso = edit_ingreso.getText().toString();
            String situacion = spinner_situacion.getSelectedItem().toString();
            boolean tieneViviendaExtra = radio_vivienda_si.isChecked();
            String discapacidad = spinner_discapacidad.getSelectedItem().toString();


            Intent intent = null;
            if (situacion.equals("Autónomo")) {
                 intent = new Intent(this, AutonomoPerfilar.class);
            } else if (situacion.equals("Empresario")) {
                intent = new Intent(this, EmpresarioPerfilar.class);
            }
            else if (situacion.equals("Estudiante")) {
                intent = new Intent(this, EstudiantePerfilar.class);
            }
            else if (situacion.equals("Jubilado")) {
                intent = new Intent(this, JubiladoPerfilar.class);
            }
            else{
                intent = new Intent(this, AsalariadoPerfilar.class);
            }
            // Puedes enviar los datos con un Intent:

            intent.putExtra("trabaja", trabaja);
            intent.putExtra("hijos", numHijos);
            intent.putExtra("ingreso", ingreso);
            intent.putExtra("situacion", situacion);
            intent.putExtra("viviendaExtra", tieneViviendaExtra);
            intent.putExtra("discapacidad", discapacidad);
            Toast.makeText(this, "Ya queda poco", Toast.LENGTH_SHORT).show();
            startActivity(intent);
            finish();
        });
    }
}
