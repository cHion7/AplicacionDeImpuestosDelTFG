package com.example.aplicaciondeimpuestosdeltfg.vistas;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.RadioButton;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

import com.example.aplicaciondeimpuestosdeltfg.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;

public class JubiladoPerfilar extends AppCompatActivity {
    RadioButton radio_jubilado_si;
    EditText cobroPension, gastosMedicos;
    Button btn_enviarJubilado;
    ImageButton ibvolJubilado;
    private FirebaseAuth firebaseAuth;
    private FirebaseDatabase database;
    private DatabaseReference nodoUsuario;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_jubilado_perfilar);
        radio_jubilado_si = findViewById(R.id.rbVivienda2_sijubilado);
        cobroPension = findViewById(R.id.etcobroPension);
        gastosMedicos = findViewById(R.id.etgastosMedicos);
        ibvolJubilado = findViewById(R.id.ibvolverJubilado);
        btn_enviarJubilado = findViewById(R.id.btEnviarJubilado);

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
        ibvolJubilado.setOnClickListener(v ->{
            Intent volverAPregComunes = new Intent(JubiladoPerfilar.this, preguntasComunes.class);
            startActivity(volverAPregComunes);
        });

        //Botón enviar
        btn_enviarJubilado.setOnClickListener(v -> {
            FirebaseUser usuarioActual = firebaseAuth.getCurrentUser();
            if (usuarioActual != null) {
                mandarDatosJubilados(usuarioActual, ingresoBruto, edad, personasACargo, vivienda);
            }else{
                Toast.makeText(this, "No hay usuario autentificado.", Toast.LENGTH_SHORT).show();
            }
        });
    }
    public void mandarDatosJubilados(FirebaseUser usuarioActual, String ingresoBruto, String edad, String personasACargo, Boolean vivienda){
        Boolean segundaVivienda = radio_jubilado_si.isChecked();
        String cobroPensionario = cobroPension.getText().toString();
        String gastoMedico = gastosMedicos.getText().toString();

        if (gastoMedico.isEmpty() || cobroPensionario.isEmpty()) {
            Toast.makeText(this, "Por favor, completa todos los campos", Toast.LENGTH_SHORT).show();
            return;
        }
        if (usuarioActual != null) { //registro fue exitoso y el usuario está disponible.
            // Crear un HashMap para almacenar los datos del usuario
            HashMap<String, Object> datosUsuario = new HashMap<>();
            datosUsuario.put("eleccion", "Jubilado");
            datosUsuario.put("ingresoBruto", ingresoBruto);
            datosUsuario.put("edad", edad);
            datosUsuario.put("personasACargo", personasACargo);
            datosUsuario.put("vivienda", vivienda);

            datosUsuario.put("pensionAnual", cobroPensionario);
            datosUsuario.put("segundaVivienda", segundaVivienda);
            datosUsuario.put("gastosMedicos", gastoMedico);

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
                    Intent intentAlLogin = new Intent(JubiladoPerfilar.this, PerfilFragment.class);
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
    }
}