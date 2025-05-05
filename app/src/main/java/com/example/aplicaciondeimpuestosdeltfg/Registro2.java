package com.example.aplicaciondeimpuestosdeltfg;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.material.textfield.TextInputEditText;

public class Registro2 extends AppCompatActivity {
    private TextInputEditText etEmpresaRegistro, etIngresosBrRegistro, etNominaRegistro, etDeclaracionRegistro;
    private Button btSiguiente2Registro;
    private Spinner spTipoLaboralRegistro;
    private TextView txtPerfilSeleccionado;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_registro2);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.sesion2), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        //Iniciar UI
        etEmpresaRegistro = findViewById(R.id.etEmpresaReg);
        etIngresosBrRegistro = findViewById(R.id.etIngresosBrReg);
        etNominaRegistro = findViewById(R.id.etNominaReg);
        etDeclaracionRegistro = findViewById(R.id.etDeclaracionReg);
        btSiguiente2Registro = findViewById(R.id.btSiguienteReg2);
        spTipoLaboralRegistro = findViewById(R.id.spTipoLaboralReg);
        txtPerfilSeleccionado = findViewById(R.id.txtPerfilSeleccionado);

        // Spinner de perfil de usuarios
        String [] opciones = {"Estudiante", "Desempleado", "Trabajador Autónomo (Freelancer)", "Empleado (trabajador ajeno)", "Jubilado"};

        // Creamos un ArrayAdapter para llenar el Spinner con las opciones
              ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, opciones);
              adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
              spTipoLaboralRegistro.setAdapter(adapter);

        // Listener para manejar la selección de un ítem del Spinner
        spTipoLaboralRegistro.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                // Obtenemos la opción seleccionada
                String perfilSeleccionado = parent.getItemAtPosition(position).toString();
                // Mostramos el perfil seleccionado en el TextView
                txtPerfilSeleccionado.setText("Perfil seleccionado: " + perfilSeleccionado);
            }
            //Cuando no se selecciona nada
            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // Mostrar un Toast
                Toast.makeText(Registro2.this, "Campo obligatorio, elige una de las opciones", Toast.LENGTH_SHORT).show();
            }
        });
        //Registrar Datos 2
        btSiguiente2Registro.setOnClickListener(v -> {
            registrarDatos2();
        });
    }
    public void registrarDatos2(){
        // Obtener datos del Fragmento 2
        String empresa = etEmpresaRegistro.getText().toString();
        String ingresosBrutos = etIngresosBrRegistro.getText().toString();
        String nomina = etNominaRegistro.getText().toString();
        String declaraciones = etDeclaracionRegistro.getText().toString();

        //Validación de campo
        if (empresa.isEmpty() || ingresosBrutos.isEmpty() || nomina.isEmpty() || declaraciones.isEmpty()) {
            Toast.makeText(Registro2.this, "Por favor, complete todos los campos.", Toast.LENGTH_SHORT).show();
            return;
        }

        //Guardamos los datos en el sharedPreference
        SharedPreferences sharedPreferences = getSharedPreferences("registro_usuario", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();

        editor.putString("empresa", empresa);
        editor.putString("ingresosBrutos", ingresosBrutos);
        editor.putString("nomina", nomina);
        editor.putString("declaraciones", declaraciones);
        editor.apply(); // Guardo los cambios

        //Pasa a la siguiente actividad de Registro Revisión
        Intent intentReg3 = new Intent(Registro2.this, RegistroRevision.class);
        startActivity(intentReg3);
    }
}