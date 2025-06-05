package com.example.aplicaciondeimpuestosdeltfg.vistas;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
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
import com.example.aplicaciondeimpuestosdeltfg.RegistroRevision;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AsalariadoPerfilar extends AppCompatActivity {
    Spinner spinner_contrato;
    RadioButton familiaNum;
    EditText edit_hijosCantida,gastosEscolares;
    Button btn_enviar;

    private FirebaseAuth firebaseAuth;
    private FirebaseDatabase database;
    private DatabaseReference nodoUsuario;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_asalariado_perfilar);
        spinner_contrato = findViewById(R.id.spinner_contrato);
        familiaNum = findViewById(R.id.radio_familia_si);
        edit_hijosCantida = findViewById(R.id.edadHijos);
        gastosEscolares = findViewById(R.id.gastosEscolares);
        btn_enviar = findViewById(R.id.btn_enviar);

        List<String> spinnerValues = List.of("Parcial", "Indefinido", "Estacional");
        ArrayAdapter<String> arraySituacion = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, spinnerValues);
        spinner_contrato.setAdapter(arraySituacion);
        spinner_contrato.setSelection(0);

        //Iniciamos Firebase
        firebaseAuth = FirebaseAuth.getInstance();
        database = FirebaseDatabase.getInstance("https://base-de-datos-del-tfg-1-default-rtdb.europe-west1.firebasedatabase.app/");
        nodoUsuario = database.getReference("Usuarios");

        //Recuperar datos del SharePreference
        SharedPreferences sharedPreferences = getSharedPreferences("impuestos", Context.MODE_PRIVATE);
        Boolean trabajo = sharedPreferences.getBoolean("trabaja", false);
        String numHijos = sharedPreferences.getString("hijos", "");
        String ingreso = sharedPreferences.getString("ingreso", "");
        String situacion = sharedPreferences.getString("situacion", "");
        String discapacidad = sharedPreferences.getString("discapacidad", "");
        Boolean viviendaExtra = sharedPreferences.getBoolean("viviendaExtra", false);

        // Botón enviar datos
        btn_enviar.setOnClickListener(v -> {
            FirebaseUser usuarioActual = firebaseAuth.getCurrentUser();
            if (usuarioActual != null) {
                mandarDatosAsalariado(usuarioActual, trabajo, numHijos, ingreso, situacion, discapacidad, viviendaExtra);
            }else{
                Toast.makeText(this, "No hay usuario autentificado.", Toast.LENGTH_SHORT).show();
            }
        });
    }
    public void mandarDatosAsalariado(FirebaseUser usuarioActual, Boolean trabajo, String numHijos, String ingreso, String situacion, String discapacidad, Boolean viviendaExtra){
        boolean familia = familiaNum.isChecked();
        String hijosEdades = edit_hijosCantida.getText().toString();
        String gastos = gastosEscolares.getText().toString();
        String contrato = spinner_contrato.getSelectedItem().toString();

        if (usuarioActual != null) { //registro fue exitoso y el usuario está disponible.
            // Crear un HashMap para almacenar los datos del usuario
            HashMap<String, Object> datosUsuario = new HashMap<>();
            datosUsuario.put("trabajo", trabajo);
            datosUsuario.put("numHijos", numHijos);
            datosUsuario.put("ingreso", ingreso);
            datosUsuario.put("situacion", situacion);
            datosUsuario.put("discapacidad", discapacidad);
            datosUsuario.put("viviendaExtra", viviendaExtra);

            datosUsuario.put("familia", familia);
            datosUsuario.put("hijosEdades", hijosEdades);
            datosUsuario.put("gastos", gastos);
            datosUsuario.put("contrato", contrato);

            //Obtener el email del usuario logueado
            String emailUsuario = usuarioActual.getEmail();
            if (emailUsuario == null || emailUsuario.isEmpty()) {
                Toast.makeText(this, "Error: El correo del usuario no está disponible.", Toast.LENGTH_SHORT).show();
                return;
            }

            // Convertir el email en clave válida para Firebase (reemplaza caracteres especiales)
            String emailKey = emailUsuario.replace(".", "_").replace("@", "_");

            // Guardar los datos en la base de datos Firebase
            nodoUsuario.child(emailKey).child("datosPersonales").setValue(datosUsuario).addOnCompleteListener(dbTask ->{

                if (dbTask.isSuccessful()) { //Escritura
                    Toast.makeText(this, "Datos guardados corectamente.", Toast.LENGTH_LONG).show();
                    //Redirigir al main
                    Intent intentAlLogin = new Intent(AsalariadoPerfilar.this, MainActivity.class);
                    startActivity(intentAlLogin);
                    finish();
                } else {
                    Toast.makeText(this, "Error al guardar datos en la base de datos.", Toast.LENGTH_SHORT).show();
                }
            });
        }else{
            Toast.makeText(this, "Error: Usuario no autenticado", Toast.LENGTH_SHORT).show();
        }
        Toast.makeText(this, "Usuario registrado exitosamente.", Toast.LENGTH_SHORT).show();

        /*boolean trabaja = getIntent().getBooleanExtra("trabaja", false);
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
        });*/
    };
}
