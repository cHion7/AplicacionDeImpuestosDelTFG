package com.example.aplicaciondeimpuestosdeltfg.vistas;

import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.Spinner;
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
import java.util.List;
import java.util.Map;

public class AsalariadoPerfilar extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_asalariado_perfilar);
        Spinner spinner_contrato = findViewById(R.id.spinner_contrato);
        RadioButton familiaNum = findViewById(R.id.radio_familia_si);
        EditText edit_hijosCantida = findViewById(R.id.edadHijos);
        EditText gastosEscolares = findViewById(R.id.gastosEscolares);
        Button btn_enviar = findViewById(R.id.btn_enviar);

        List<String> spinnerValues = List.of("Parcial", "Indefinido", "Estacional");
        ArrayAdapter<String> arraySituacion = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, spinnerValues);
        spinner_contrato.setAdapter(arraySituacion);
        spinner_contrato.setSelection(0);


        btn_enviar.setOnClickListener(v -> {
            boolean familia = familiaNum.isChecked();
            String hijosEdades = edit_hijosCantida.getText().toString();
            String gastos = gastosEscolares.getText().toString();
            String contrato = spinner_contrato.getSelectedItem().toString();


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
            datosUsuario.put("familia", familia);
            datosUsuario.put("hijos", hijosEdades);
            datosUsuario.put("gastos", gastos);
            datosUsuario.put("contrato", contrato);

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
