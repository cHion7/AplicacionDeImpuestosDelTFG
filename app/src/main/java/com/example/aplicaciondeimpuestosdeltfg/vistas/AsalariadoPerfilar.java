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

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

import com.example.aplicaciondeimpuestosdeltfg.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;
import java.util.List;

public class AsalariadoPerfilar extends AppCompatActivity {
    Spinner spinner_contrato;
    RadioButton familiaNumAsala;
    EditText etEdadHijosAsala, gastosEscolaresAsala;
    Button btEnviarAsala;
    ImageButton ibvolAsalariado;

    private FirebaseAuth firebaseAuth;
    private FirebaseDatabase database;
    private DatabaseReference nodoUsuario;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_asalariado_perfilar);
        spinner_contrato = findViewById(R.id.spinner_contrato);
        familiaNumAsala = findViewById(R.id.radio_familia_si);
        etEdadHijosAsala = findViewById(R.id.edadHijos);
        gastosEscolaresAsala = findViewById(R.id.gastosEscolares);
        ibvolAsalariado = findViewById(R.id.ibvolverAsalariado);
        btEnviarAsala = findViewById(R.id.btEnviarAsalariados);

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
        String eleccion = sharedPreferences.getString("situacion", "");
        String ingresoBruto = sharedPreferences.getString("ingresoBruto", "");
        String edad = sharedPreferences.getString("edad", "");
        String personasACargo = sharedPreferences.getString("personasACargo", "");
        Boolean vivienda = sharedPreferences.getBoolean("vivienda", false);

        //Botón volver
        ibvolAsalariado.setOnClickListener(v ->{
            Intent volverAPregComunes = new Intent(AsalariadoPerfilar.this, preguntasComunes.class);
            startActivity(volverAPregComunes);
        });

        // Botón enviar datos
        btEnviarAsala.setOnClickListener(v -> {
            FirebaseUser usuarioActual = firebaseAuth.getCurrentUser();
            if (usuarioActual != null) {
                mandarDatosAsalariado(usuarioActual, eleccion, ingresoBruto, edad, personasACargo, vivienda);
            }else{
                Toast.makeText(this, "No hay usuario autentificado.", Toast.LENGTH_SHORT).show();
            }
        });
    }
    public void mandarDatosAsalariado(FirebaseUser usuarioActual, String eleccion, String ingresoBruto, String edad, String personasACargo, Boolean vivienda){
        String tipoContrato = spinner_contrato.getSelectedItem().toString();
        boolean familiaNumerosa = familiaNumAsala.isChecked();
        String edadesHijos = etEdadHijosAsala.getText().toString();
        String gastosEscolares = gastosEscolaresAsala.getText().toString();

        if (usuarioActual != null) { //registro fue exitoso y el usuario está disponible.
            // Crear un HashMap para almacenar los datos del usuario
            HashMap<String, Object> datosUsuario = new HashMap<>();
            datosUsuario.put("eleccion", "Asalariado");
            datosUsuario.put("ingresoBruto", ingresoBruto);
            datosUsuario.put("edad", edad);
            datosUsuario.put("personasACargo", personasACargo);
            datosUsuario.put("vivienda", vivienda);

            datosUsuario.put("tipoContrato", tipoContrato);
            datosUsuario.put("familiaNumerosa", familiaNumerosa);
            datosUsuario.put("edadesHijos", edadesHijos);
            datosUsuario.put("gastosEscolares", gastosEscolares);

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
                    Intent intentAlPerfil = new Intent(AsalariadoPerfilar.this, MainActivity.class);
                    intentAlPerfil.putExtra("selected_tab", "perfil");
                    startActivity(intentAlPerfil);
                    finish();
                } else {
                    Toast.makeText(this, "Error al guardar datos en la base de datos.", Toast.LENGTH_SHORT).show();
                }
            });
        }else{
            Toast.makeText(this, "Error: Usuario no autenticado", Toast.LENGTH_SHORT).show();
        }
        Toast.makeText(this, "Usuario registrado exitosamente.", Toast.LENGTH_SHORT).show();
    };
}
