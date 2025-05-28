package com.example.aplicaciondeimpuestosdeltfg.vistas;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.aplicaciondeimpuestosdeltfg.Login;
import com.example.aplicaciondeimpuestosdeltfg.R;
import com.example.aplicaciondeimpuestosdeltfg.RegistroRevision;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public class AutonomoPerfilar extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_autonomo_perfilar);
        EditText editTextDate = findViewById(R.id.editTextDate);
        EditText actividadEconomica = findViewById(R.id.actividadEconomica);
        EditText gastosDeducibles = findViewById(R.id.gastosDeducibles);
        EditText ivaSoportado = findViewById(R.id.ivaSoportado);
        EditText ivaRepercutido = findViewById(R.id.ivaRepercutido);
        RadioButton radio_coche_si = findViewById(R.id.radio_coche_si);
        RadioButton radio_coche_no = findViewById(R.id.radio_coche_no);
        Button btn_enviar = findViewById(R.id.btn_enviarAutonomo);

        btn_enviar.setOnClickListener(v -> {
            boolean coche = radio_coche_si.isChecked();
            String fecha = editTextDate.getText().toString();
            String actividad = actividadEconomica.getText().toString();
            String gastos = gastosDeducibles.getText().toString();
            String iva = ivaSoportado.getText().toString();
            String ivaRep = ivaRepercutido.getText().toString();

            boolean trabaja = getIntent().getBooleanExtra("trabaja", false);
            String hijos = getIntent().getStringExtra("hijos");
            String ingreso = getIntent().getStringExtra("ingreso");
            String situacion = getIntent().getStringExtra("situacion");
            boolean viviendaExtra = getIntent().getBooleanExtra("viviendaExtra", false);
            String discapacidad = getIntent().getStringExtra("discapacidad");


            FirebaseAuth mAuth = FirebaseAuth.getInstance();
            DatabaseReference referencia = FirebaseDatabase.getInstance().getReference("Usuarios").child(mAuth.getCurrentUser().getUid()).child("datosPersonales");

            if (fecha.isEmpty() || actividad.isEmpty() || gastos.isEmpty() || iva.isEmpty() || ivaRep.isEmpty()) {
                Toast.makeText(this, "Por favor, completa todos los campos", Toast.LENGTH_SHORT).show();
                return;
            }



            DatabaseReference ref = FirebaseDatabase.getInstance()
                    .getReference("Usuarios")
                    .child((mAuth.getCurrentUser()).getUid())
                    .child("datosPersonales");

                Map<String, Object> datosUsuario = new HashMap<>();
                datosUsuario.put("coche", coche);
                datosUsuario.put("fecha", fecha);
                datosUsuario.put("actividad", actividad);
                datosUsuario.put("gastos", gastos);
                datosUsuario.put("ivaSoportado", iva);
                datosUsuario.put("ivaRepercutido", ivaRep);
                datosUsuario.put("trabaja", trabaja);
                datosUsuario.put("hijos", hijos);
                datosUsuario.put("ingreso", ingreso);
                datosUsuario.put("situacion", situacion);
                datosUsuario.put("viviendaExtra", viviendaExtra);
                datosUsuario.put("discapacidad", discapacidad);

                ref.setValue(datosUsuario).addOnCompleteListener(dbTask -> {
                    if (dbTask.isSuccessful()) {
                        Toast.makeText(this, "Datos guardados correctamente.", Toast.LENGTH_LONG).show();
                        finish();
                    } else {
                        Toast.makeText(this, "Error al guardar los datos.", Toast.LENGTH_SHORT).show();
                    }
                });


        });
    }
}