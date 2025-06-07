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

import com.example.aplicaciondeimpuestosdeltfg.Login;
import com.example.aplicaciondeimpuestosdeltfg.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class EmpresarioPerfilar extends AppCompatActivity {
    Spinner spinnerTipoempresa;
    EditText edit_ingreso, sueldoAdmin, empleadosNum, gastosDeduciblesEmp;
    Button btn_enviarEmpresario;
    private FirebaseAuth firebaseAuth;
    private FirebaseDatabase database;
    private DatabaseReference nodoUsuario;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_empresario_perfilar);

        spinnerTipoempresa = findViewById(R.id.spinnerTipoEmpresa);
        edit_ingreso = findViewById(R.id.etfacturaAnual);
        sueldoAdmin = findViewById(R.id.etsueldoAdmin);
        empleadosNum = findViewById(R.id.etnumeroEmpleados);
        gastosDeduciblesEmp = findViewById(R.id.etGastosDeduciblesEmpresario);
        btn_enviarEmpresario = findViewById(R.id.btEnviarCom);

        //Iniciamos Firebase
        firebaseAuth = FirebaseAuth.getInstance();
        database = FirebaseDatabase.getInstance("https://base-de-datos-del-tfg-1-default-rtdb.europe-west1.firebasedatabase.app/");
        nodoUsuario = database.getReference("Usuarios");

        List<String> spinnerValues = List.of("S.A", "S.L", "Otro");
        ArrayAdapter<String> arraySituacion = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, spinnerValues);
        spinnerTipoempresa.setAdapter(arraySituacion);
        spinnerTipoempresa.setSelection(0);

        //Recuperar datos del SharePreference
        SharedPreferences sharedPreferences = getSharedPreferences("impuestos", Context.MODE_PRIVATE);
        String eleccion = sharedPreferences.getString("situacion", "");
        String ingresoBruto = sharedPreferences.getString("ingresoBruto", "");
        String edad = sharedPreferences.getString("edad", "");
        String personasACargo = sharedPreferences.getString("personasACargo", "");
        Boolean vivienda = sharedPreferences.getBoolean("vivienda", false);

        btn_enviarEmpresario.setOnClickListener(v -> {
            FirebaseUser usuarioActual = firebaseAuth.getCurrentUser();
            if (usuarioActual != null) {
                mandarDatosEmpresario(usuarioActual, eleccion, ingresoBruto, edad, personasACargo, vivienda);
            }else{
                Toast.makeText(this, "No hay usuario autentificado.", Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void mandarDatosEmpresario(FirebaseUser usuarioActual, String eleccion, String ingresoBruto, String edad, String personasACargo, Boolean vivienda){
        String tipoEmpresa = spinnerTipoempresa.getSelectedItem().toString();
        String facturacionEmpresa = edit_ingreso.getText().toString();
        String sueldoJefe = sueldoAdmin.getText().toString();
        String numeroEmpleados = empleadosNum.getText().toString();
        String gastosDeduciblesEmpresa = gastosDeduciblesEmp.getText().toString();

        if (facturacionEmpresa.isEmpty() || sueldoJefe.isEmpty()|| numeroEmpleados.isEmpty()) {
            Toast.makeText(this, "Por favor, completa todos los campos", Toast.LENGTH_SHORT).show();
            return;
        }
        if (usuarioActual != null) { //registro fue exitoso y el usuario está disponible.
            // Crear un HashMap para almacenar los datos del usuario
            HashMap<String, Object> datosUsuario = new HashMap<>();
            datosUsuario.put("eleccion", "Empresario");
            datosUsuario.put("ingresoBruto", ingresoBruto);
            datosUsuario.put("edad", edad);
            datosUsuario.put("personasACargo", personasACargo);
            datosUsuario.put("vivienda", vivienda);
            datosUsuario.put("tipoContrato", tipoEmpresa);
            datosUsuario.put("sueldoAdministrador", sueldoJefe);
            datosUsuario.put("facturacionEmpresa", facturacionEmpresa);
            datosUsuario.put("empleados", numeroEmpleados);
            datosUsuario.put("gastosDeduciblesEmpresa", gastosDeduciblesEmpresa);

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
                    Intent intentAlLogin = new Intent(EmpresarioPerfilar.this, PerfilFragment.class);
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

