package com.example.aplicaciondeimpuestosdeltfg;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Button;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.material.textfield.TextInputEditText;

public class Registro extends AppCompatActivity {
    private TextInputEditText etUsuarioReg, etContrasenaReg;
    private Button btSiguienteReg;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_registro);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.sesion), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // Referenciamos los elementos del layout
        etUsuarioReg = findViewById(R.id.etUsuarioReg);
        etContrasenaReg = findViewById(R.id.etContrasenaReg);
        btSiguienteReg = findViewById(R.id.btSiguienteReg);

        //Botón siguiente
        btSiguienteReg.setOnClickListener(v -> {
            siguiente();
        });
    }
    public void siguiente(){
        // Obtenemos los datos introducidos por el usuario
        String correo = etUsuarioReg.getText().toString().trim();
        String contrasena = etContrasenaReg.getText().toString().trim();

        // Guardamos los datos en SharedPreferences
        SharedPreferences sharedPreferences = getSharedPreferences("registro_usuario", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();

        editor.putString("correo", correo);
        editor.putString("password", contrasena);
        editor.apply(); // Guardo los cambios

        // Pasar al siguiente actividad
        Intent intentReg1 = new Intent(Registro.this, Registro1.class);
        startActivity(intentReg1);
    }
}