package com.example.aplicaciondeimpuestosdeltfg.vistas;

import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
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

public class EmpresarioPerfilar extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_empresario_perfilar);

        Spinner spinnerTipoempresa = findViewById(R.id.spinnerTipoEmpresa);
        EditText edit_ingreso = findViewById(R.id.facturaAnual);
        EditText sueldoAdmin = findViewById(R.id.sueldoAdmin);
        EditText empleadosNum = findViewById(R.id.numeroEmpleados);
        Button btn_enviar = findViewById(R.id.btn_enviar);


        List<String> spinnerValues = List.of("S.A", "S.L", "Otro");
        ArrayAdapter<String> arraySituacion = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, spinnerValues);
        spinnerTipoempresa.setAdapter(arraySituacion);
        spinnerTipoempresa.setSelection(0);

        btn_enviar.setOnClickListener(v -> {

            String tipoEmpresa = spinnerTipoempresa.getSelectedItem().toString();
            String facturacionEmpresa = edit_ingreso.getText().toString();
            String sueldoJefe = sueldoAdmin.getText().toString();
            String numeroEmpleados = empleadosNum.getText().toString();

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
            datosUsuario.put("tipoEmpresa", tipoEmpresa);
            datosUsuario.put("sueldoJefe", sueldoJefe);
            datosUsuario.put("facturacion", facturacionEmpresa);
            datosUsuario.put("numeroEmpleados", numeroEmpleados);

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

