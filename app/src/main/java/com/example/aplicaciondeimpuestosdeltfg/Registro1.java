package com.example.aplicaciondeimpuestosdeltfg;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Button;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.material.textfield.TextInputEditText;

public class Registro1 extends AppCompatActivity {
    private TextInputEditText etNombreRegistro, etDNIRegistro, etfechaRegistro, etEstadoRegistro, etDireccionRegistro, etTelefonoRegistro;
    private Button btSiguiente1Registro;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_registro1);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.sesion1), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        //Inicializamos los elementos del UI
        etNombreRegistro = findViewById(R.id.etNombreReg);
        etDNIRegistro = findViewById(R.id.etDNIReg);
        etfechaRegistro = findViewById(R.id.etfechaReg);
        etEstadoRegistro = findViewById(R.id.etEstadoReg);
        etDireccionRegistro = findViewById(R.id.etDireccionReg);
        etTelefonoRegistro = findViewById(R.id.etTelefonoReg);
        btSiguiente1Registro = findViewById(R.id.btSiguienteReg1);

        //Registrar datos 1
        btSiguiente1Registro.setOnClickListener(v -> {
            registrarDatos1();
        });
    }

    public void registrarDatos1(){
        // Obtenemos los datos introducidos por el usuario
        String nombre = etNombreRegistro.getText().toString();
        String dni = etDNIRegistro.getText().toString();
        String fechaNacimiento = etfechaRegistro.getText().toString();
        String estadoCivil = etEstadoRegistro.getText().toString();
        String direccion = etDireccionRegistro.getText().toString();
        String telefono = etTelefonoRegistro.getText().toString();

        //Validación campos
        if (nombre.isEmpty()||dni.isEmpty()||fechaNacimiento.isEmpty()||estadoCivil.isEmpty()||direccion.isEmpty()||telefono.isEmpty()){
            Toast.makeText(Registro1.this, "Por favor, complete todos los campos.", Toast.LENGTH_SHORT).show();
            return;
        }

        // Guardamos los datos en SharedPreferences
        SharedPreferences sharedPreferences = getSharedPreferences("registro_usuario", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();

        editor.putString("nombre", nombre);
        editor.putString("dni", dni);
        editor.putString("fechaNacimiento", fechaNacimiento);
        editor.putString("estadoCivil", estadoCivil);
        editor.putString("direccion", direccion);
        editor.putString("telefono", telefono);
        editor.apply(); // Guardo los cambios

        //Pasa al siguiente registro 2
        Intent intentReg2 = new Intent(Registro1.this, Registro2.class);
        startActivity(intentReg2);
    }
}