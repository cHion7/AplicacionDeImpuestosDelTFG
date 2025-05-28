package com.example.aplicaciondeimpuestosdeltfg.vistas;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.aplicaciondeimpuestosdeltfg.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;
import java.util.Map;

public class JubiladoPerfilar extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_jubilado_perfilar);
        RadioButton radio_jubilado_si = findViewById(R.id.radio_vivienda2_si);
        EditText cobroPension = findViewById(R.id.cobroPension);
        EditText gastosMedicos = findViewById(R.id.gastosMedicos);
        Button btn_enviar = findViewById(R.id.btn_enviar);




        btn_enviar.setOnClickListener(v -> {
            boolean segundaVivienda = radio_jubilado_si.isChecked();
            String cobroPensionario = cobroPension.getText().toString();
            String gastoMedico = gastosMedicos.getText().toString();

            boolean trabaja = getIntent().getBooleanExtra("trabaja", false);
            String hijos = getIntent().getStringExtra("hijos");
            String ingreso = getIntent().getStringExtra("ingreso");
            String situacion = getIntent().getStringExtra("situacion");
            boolean viviendaExtra = getIntent().getBooleanExtra("viviendaExtra", false);
            String discapacidad = getIntent().getStringExtra("discapacidad");


            FirebaseAuth mAuth = FirebaseAuth.getInstance();
            DatabaseReference referencia = FirebaseDatabase.getInstance().getReference("Usuarios").child(mAuth.getCurrentUser().getUid()).child("datosPersonales");


            DatabaseReference ref = FirebaseDatabase.getInstance()
                    .getReference("Usuarios")
                    .child((mAuth.getCurrentUser()).getUid())
                    .child("datosPersonales");

            Map<String, Object> datosUsuario = new HashMap<>();
            datosUsuario.put("2vivienda", segundaVivienda);
            datosUsuario.put("pension", cobroPensionario);
            datosUsuario.put("medicos", gastoMedico);

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