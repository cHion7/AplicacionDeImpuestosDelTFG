package com.example.aplicaciondeimpuestosdeltfg.vistas;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.RadioButton;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.aplicaciondeimpuestosdeltfg.R;

import java.util.List;

public class preguntasComunes extends AppCompatActivity {
    EditText etIngresoPregComunes, etEdadPregComunes, etPersonasACargoPregComunes;
    RadioButton viviendaPregComunes;
    Spinner spinner_situacion;
    ImageButton ibVolPregComunes;
    Button btEnviarCom;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_preguntas_comunes);

        // Declarar UI
        etIngresoPregComunes = findViewById(R.id.etIngresosCom);
        etEdadPregComunes = findViewById(R.id.etEdadCom);
        etPersonasACargoPregComunes = findViewById(R.id.etPersonasACargoCom);
        viviendaPregComunes = findViewById(R.id.radio_vivienda_si);
        spinner_situacion = findViewById(R.id.spinner_situacionPreguntasCom);
        ibVolPregComunes= findViewById(R.id.ibvolverPreguntasComunes);
        btEnviarCom = findViewById(R.id.btEnviarCom);

        List<String> valorSpinnerSituacion = List.of("Autónomo", "Asalariado", "Empresario", "Estudiante", "Jubilado");
        ArrayAdapter<String> arraySituacion = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, valorSpinnerSituacion);
        spinner_situacion.setAdapter(arraySituacion);
        spinner_situacion.setSelection(3);

        //Botón volver
        //Botón de volver atrás
        ibVolPregComunes.setOnClickListener(v ->{
            Intent intentAlPerfil = new Intent(preguntasComunes.this, MainActivity.class);
            intentAlPerfil.putExtra("selected_tab", "perfil");
            startActivity(intentAlPerfil);
            finish();
        });

        //Botón siguente página
        btEnviarCom.setOnClickListener(v -> {
            registrarPreguntasComunes();
        });
    }

    public void registrarPreguntasComunes() {
        //Obtener datos de las Preguntas Comunes
        String eleccion = spinner_situacion.getSelectedItem().toString();
        String ingresoBruto = etIngresoPregComunes.getText().toString();
        String edad = etEdadPregComunes.getText().toString();
        String personaACargo = etPersonasACargoPregComunes.getText().toString();
        boolean vivienda = viviendaPregComunes.isChecked();

        // Validación de los campos
        if (edad.isEmpty() || ingresoBruto.isEmpty() || eleccion.isEmpty() || personaACargo.isEmpty()) {
            Toast.makeText(preguntasComunes.this, "Por favor, complete todos los campos.", Toast.LENGTH_SHORT).show();
            return;
        }

        //Guardamos los datos en el sharePreference
        SharedPreferences sharedPreferences = getSharedPreferences("impuestos", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();

        editor.putString("eleccion", eleccion);
        editor.putString("ingresoBruto", ingresoBruto);
        editor.putString("edad", edad);
        editor.putString("personasACargo", personaACargo);
        editor.putBoolean("vivienda", vivienda);
        editor.apply();

        //Pasa a la siguiente actividad de Preguntas Específicas
        Intent intent;
        if (eleccion.equals("Autónomo")) {
            intent = new Intent(this, AutonomoPerfilar.class);
        } else if (eleccion.equals("Empresario")) {
            intent = new Intent(this, EmpresarioPerfilar.class);
        } else if (eleccion.equals("Estudiante")) {
            intent = new Intent(this, EstudiantePerfilar.class);
        } else if (eleccion.equals("Jubilado")) {
            intent = new Intent(this, JubiladoPerfilar.class);
        } else { // Esto cubrirá el caso de "Asalariado" y cualquier otro valor no previsto
            intent = new Intent(this, AsalariadoPerfilar.class);
        }
        Toast.makeText(this, "Ya queda poco", Toast.LENGTH_SHORT).show();
        startActivity(intent);
        finish();
    }
}
