package com.example.aplicaciondeimpuestosdeltfg;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Patterns;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.aplicaciondeimpuestosdeltfg.vistas.MainActivity;
import com.google.android.material.textfield.TextInputEditText;

public class Registro extends AppCompatActivity {
    private TextInputEditText etUsuarioReg, etContrasenaReg;
    private Button btSiguienteReg;
    private TextView btVolverLoginReg;

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
        btVolverLoginReg = findViewById(R.id.btVolverLoginRegistro);

        //Botón siguiente
        btSiguienteReg.setOnClickListener(v -> {
            siguiente();
        });

        //Botón volver al Login
        btVolverLoginReg.setOnClickListener(v -> {
            Intent intentAlLogin = new Intent(Registro.this, Login.class);
            startActivity(intentAlLogin);
        });
    }

    public void siguiente(){
        // Obtenemos los datos introducidos por el usuario
        String correo = etUsuarioReg.getText().toString().trim();
        String contrasena = etContrasenaReg.getText().toString().trim();

        //Validaciones de campo
        if(correo.isEmpty() || contrasena.isEmpty()){
            Toast.makeText(Registro.this, "Por favor, completa los campos.", Toast.LENGTH_SHORT).show();
            return;
        }
        //Validación del email
        if (!android.util.Patterns.EMAIL_ADDRESS.matcher(correo).matches()) {
            Toast.makeText(Registro.this, "Por facor, ingresa un email válido.", Toast.LENGTH_SHORT).show();
            return;
        }
        //Validación de la contraseña
        if(contrasena.length() < 6){
            Toast.makeText(Registro.this, "La contraseña debe tener al menos 6 carácteres.", Toast.LENGTH_SHORT).show();
            return;
        }

        // Guardamos los datos en SharedPreferences
        SharedPreferences sharedPreferences = getSharedPreferences("registro_usuario", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("correo", correo);
        editor.putString("password", contrasena);
        editor.apply(); // Guardo los cambios

        // Pasar a la página principal
        Intent intentMain = new Intent(Registro.this, MainActivity.class);
        startActivity(intentMain);
    }
}